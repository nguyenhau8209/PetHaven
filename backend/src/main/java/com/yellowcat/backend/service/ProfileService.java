package com.yellowcat.backend.service;

import com.yellowcat.backend.DTO.identity.Credential;
import com.yellowcat.backend.DTO.identity.TokenExchangeParam;
import com.yellowcat.backend.DTO.identity.TokenExchangeResponse;
import com.yellowcat.backend.DTO.identity.UserCreationParam;
import com.yellowcat.backend.DTO.request.AccountRequest;
import com.yellowcat.backend.repository.IdentityClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileService {

    IdentityClient identityClient;

    @Value("${app.keycloak.admin.clientId}")
    @NonFinal
    String clientId;

    @Value("${app.keycloak.admin.clientSecret}")
    @NonFinal
    String clientSecret;

    /**
     * Helper method to get an access token
     */
    private TokenExchangeResponse getAccessToken() {
        log.info("Exchanging client credentials for token...");
        TokenExchangeParam param = TokenExchangeParam.builder()
                .grant_type("client_credentials")
                .client_id(clientId)
                .client_secret(clientSecret)
                .scope("openid")
                .build();

        return identityClient.exchangeToken(param);
    }

    /**
     * Create a new user in Keycloak
     */
    public String createAccount(AccountRequest request) {
        try {
            // Step 1: Exchange client credentials for access token
            TokenExchangeResponse token = getAccessToken();
            String accessToken = "Bearer " + token.getAccessToken();
            log.info("Successfully obtained token: {}", accessToken);

            // Step 2: Create a new user using the token and provided account request
            UserCreationParam newUser = UserCreationParam.builder()
                    .username(request.getUsername())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .enabled(true)
                    .emailVerified(false)
                    .credentials(List.of(Credential.builder()
                            .type("password")
                            .temporary(false)
                            .value(request.getPassword())
                            .build()))
                    .build();

            var creationResponse = identityClient.createUser(accessToken, newUser);
            log.info("Created user: {}", creationResponse);

            return "Created account successfully";
        } catch (Exception e) {
            log.error("Error creating account: {}", e.getMessage(), e);
            return "Error creating account";
        }
    }

    /**
     * Get all users in Keycloak
     */
    public List<Map<String, Object>> getAllUsers() {
        try {
            TokenExchangeResponse token = getAccessToken();
            String accessToken = "Bearer " + token.getAccessToken();
            // Giả sử phương thức này trả về List<Map<String, Object>>
            List<Map<String, Object>> users = (List<Map<String, Object>>) identityClient.getAllUsers(accessToken);
            log.info("Fetched all users: {}", users);
            return users;
        } catch (Exception e) {
            log.error("Error fetching users: {}", e.getMessage(), e);
            return Collections.emptyList(); // Trả về danh sách rỗng trong trường hợp lỗi
        }
    }


    /**
     * Get a user by ID
     */
    public Map<String, Object> getUserById(String userId) {
        try {
            TokenExchangeResponse token = getAccessToken();
            String accessToken = "Bearer " + token.getAccessToken();

            // Giả sử identityClient.getUserById trả về Map<String, Object>
            Map<String, Object> user = identityClient.getUserById(accessToken, userId);

            log.info("Fetched user by ID {}: {}", userId, user);
            return user;
        } catch (Exception e) {
            log.error("Error fetching user by ID: {}", e.getMessage(), e);
            return Collections.emptyMap(); // Trả về một Map rỗng trong trường hợp lỗi
        }
    }


    /**
     * Update a user in Keycloak
     */
    public String updateUser(String userId, AccountRequest request) {
        try {
            TokenExchangeResponse token = getAccessToken();
            String accessToken = "Bearer " + token.getAccessToken();
            UserCreationParam updatedUser = UserCreationParam.builder()
                    .username(request.getUsername())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .enabled(true)
                    .emailVerified(false)
                    .credentials(List.of(Credential.builder()
                            .type("password")
                            .temporary(false)
                            .value(request.getPassword())
                            .build()))
                    .build();

            var updateResponse = identityClient.updateUser(accessToken, userId, updatedUser);
            log.info("Updated user: {}", updateResponse);
            return "Updated user successfully";
        } catch (Exception e) {
            log.error("Error updating user: {}", e.getMessage(), e);
            return "Error updating user";
        }
    }

    /**
     * Delete a user in Keycloak
     */
    public String deleteUser(String userId) {
        try {
            TokenExchangeResponse token = getAccessToken();
            String accessToken = "Bearer " + token.getAccessToken();
            var deleteResponse = identityClient.deleteUser(accessToken, userId);
            log.info("Deleted user: {}", deleteResponse);
            return "Deleted user successfully";
        } catch (Exception e) {
            log.error("Error deleting user: {}", e.getMessage(), e);
            return "Error deleting user";
        }
    }
}
