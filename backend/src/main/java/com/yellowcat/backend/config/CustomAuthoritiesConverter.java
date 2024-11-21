package com.yellowcat.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CustomAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    private final String REALM_ACCESS = "resource_access";
    private final String ROLE_PREFIX = "ROLE_";
    private final String ROLES = "roles";
    private final String PETHAVEN = "PetHaven";

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        // Lấy Map chứa các resource_access
        Map<String, Object> resourceAccessMap = source.getClaimAsMap(REALM_ACCESS);
        if (resourceAccessMap == null) {
            return Collections.emptyList();
        }

        // Lấy Map của PetHaven
        Object petHavenAccess = resourceAccessMap.get(PETHAVEN);
        if (!(petHavenAccess instanceof Map)) {
            return Collections.emptyList();
        }

        // Truy xuất danh sách roles từ PetHaven
        Map<String, Object> petHavenMap = (Map<String, Object>) petHavenAccess;
        Object rolesObj = petHavenMap.get(ROLES);
        if (!(rolesObj instanceof List<?>)) {
            return Collections.emptyList();
        }

        // Chuyển đổi danh sách roles thành GrantedAuthority
        List<String> roles = (List<String>) rolesObj;
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
                .collect(Collectors.toList());

    }

}
