package com.yellowcat.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KeycloakUserService {

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${app.keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    @Value("${app.keycloak.admin.clientId}")
    private String clientId;

    @Cacheable("userRoles")
    public Map<String, List<String>> getAllUsersRoles() {
        Keycloak keycloak = null;
        try {
            keycloak = Keycloak.getInstance(
                    authServerUrl,
                    realm,
                    adminUsername,
                    adminPassword,
                    clientId
            );

            // Kiểm tra quyền truy cập
            RealmResource realmResource = keycloak.realm(realm);
            log.info("keycloak {}", keycloak);
            try {
                realmResource.toRepresentation();
            } catch (Exception e) {
                throw new SecurityException("Không có quyền truy cập realm: " + realm, e);
            }

            List<UserRepresentation> allUsers = getAllUsers(keycloak, realm);
            Map<String, List<String>> userRoles = new HashMap<>();

            for(UserRepresentation user : allUsers) {
                try {
                    List<String> roles = getUserRoles(keycloak, user.getId());
                    userRoles.put(user.getUsername(), roles);
                } catch (Exception e) {
                    log.error("Lỗi khi lấy roles cho user: " + user.getUsername(), e);
                }
            }

            return userRoles;

        } catch (Exception e) {
            log.error("Lỗi khi lấy thông tin users và roles", e);
            throw new RuntimeException("Không thể lấy thông tin users và roles", e);
        } finally {
            if (keycloak != null) {
                keycloak.close();
            }
        }
    }

    private List<UserRepresentation> getAllUsers(Keycloak keycloak, String realm) {
        List<UserRepresentation> allUsers = new ArrayList<>();
        int firstResult = 0;
        int maxResults = 100;
        List<UserRepresentation> users;

        do {
            users = keycloak.realm(realm)
                    .users()
                    .list(firstResult, maxResults);
            allUsers.addAll(users);
            firstResult += maxResults;
        } while (!users.isEmpty());

        return allUsers;
    }

    private List<String> getUserRoles(Keycloak keycloak, String userId) {
        try {
            List<RoleRepresentation> roles = keycloak
                    .realm(realm)
                    .users()
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .listAll();

            return roles.stream()
                    .map(RoleRepresentation::getName)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Lỗi khi lấy roles cho userId: " + userId, e);
            return Collections.emptyList();
        }
    }
}
