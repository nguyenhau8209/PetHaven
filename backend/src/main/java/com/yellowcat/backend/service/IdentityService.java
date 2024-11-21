package com.yellowcat.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yellowcat.backend.DTO.identity.TokenExchangeParam;
import com.yellowcat.backend.DTO.identity.TokenExchangeResponse;
import com.yellowcat.backend.DTO.identity.UserCreationParam;
import com.yellowcat.backend.repository.IdentityClient;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdentityService implements IdentityClient {
    @Value("${app.keycloak.serverUrl}")
    @NonFinal
    String serverUrl;

    private final WebClient webClient;

    private String buildUrl(String... paths) {
        return UriComponentsBuilder.fromHttpUrl(serverUrl)
                .pathSegment(paths)
                .build()
                .toUriString();
    }

    @Override
    public TokenExchangeResponse exchangeToken(TokenExchangeParam param) {
        String url = buildUrl("realms", "spring", "protocol", "openid-connect", "token");

        // Mã hóa body thành application/x-www-form-urlencoded
        String formBody = UriComponentsBuilder.newInstance()
                .queryParam("grant_type", param.getGrant_type())
                .queryParam("client_id", param.getClient_id())
                .queryParam("client_secret", param.getClient_secret())
                .queryParam("scope", param.getScope())
                .build()
                .toUriString()
                .substring(1); // Bỏ dấu `?` ở đầu

        return webClient.post()
                .uri(url)
                .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .bodyValue(formBody)
                .retrieve()
                .bodyToMono(TokenExchangeResponse.class)
                .block();
    }

    @Override
    public ResponseEntity<?> createUser(String token, UserCreationParam param) {
        String url = buildUrl("admin", "realms", "spring", "users");

        return webClient.post()
                .uri(url)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(param)
                .retrieve()
                .toEntity(String.class)
                .block();
    }

    private Map<String, Object> parseToken(String token) {
        try {
            // Kiểm tra và xử lý token
            if (token.startsWith("Bearer ")) {
                token = token.substring(7); // Bỏ "Bearer " ở đầu
            }

            String[] chunks = token.split("\\.");
            if (chunks.length != 3) { // JWT token phải có 3 phần
                log.error("Invalid token format");
                return Collections.emptyMap();
            }

            Base64.Decoder decoder = Base64.getUrlDecoder();
            String payload = new String(decoder.decode(chunks[1]));

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            log.error("Error parsing token: {}", e.getMessage(), e);
            return Collections.emptyMap();
        }
    }

    @Override
    public List<Map<String, Object>> getAllUsers(String token) {
        try {
            String url = buildUrl("admin", "realms", "spring", "users");

            ResponseEntity<String> response = webClient.get()
                    .uri(url)
                    .header("Authorization", token) // Đảm bảo gửi token dạng "Bearer ..."
                    .retrieve()
                    .toEntity(String.class)
                    .block();

            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> users = objectMapper.readValue(
                    response.getBody(),
                    new TypeReference<List<Map<String, Object>>>() {}
            );

            // Thêm roles vào thông tin user
            for (Map<String, Object> user : users) {
                // Lấy roles từ token của admin (token hiện tại)
                Map<String, Object> tokenData = parseToken(token);
                log.info("tokenData: {}", tokenData);
                Map<String, Object> resourceAccess = (Map<String, Object>) tokenData.get("resource_access");

                if (resourceAccess != null && resourceAccess.containsKey("PetHaven")) {
                    Map<String, Object> petHavenAccess = (Map<String, Object>) resourceAccess.get("PetHaven");
                    List<String> roles = (List<String>) petHavenAccess.get("roles");
                    log.info("roles: {}", roles);
                    user.put("roles", roles != null ? roles : Collections.emptyList());
                } else {
                    user.put("roles", Collections.emptyList());
                }
            }

            return users;
        } catch (Exception e) {
            log.error("Error fetching users with roles: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public Map<String, Object> getUserById(String token, String userId) {
        try {
            // Xây dựng URL API
            String url = buildUrl("admin", "realms", "spring", "users", userId);

            // Gọi API và nhận về JSON String
            ResponseEntity<String> response = webClient.get()
                    .uri(url)
                    .header("Authorization", token)
                    .retrieve()
                    .toEntity(String.class)
                    .block();

            log.info("Fetched user JSON: {}", response.getBody());

            // Chuyển đổi JSON thành Map<String, Object>
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            log.error("Error fetching user by ID: {}", e.getMessage(), e);
            return Collections.emptyMap(); // Trả về Map rỗng nếu có lỗi
        }
    }


    @Override
    public ResponseEntity<?> updateUser(String token, String userId, UserCreationParam param) {
        String url = buildUrl("admin", "realms", "spring", "users", userId);

        return webClient.put()
                .uri(url)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(param)
                .retrieve()
                .toEntity(String.class)
                .block();
    }

    @Override
    public ResponseEntity<?> deleteUser(String token, String userId) {
        String url = buildUrl("admin", "realms", "spring", "users", userId);

        return webClient.delete()
                .uri(url)
                .header("Authorization", token)
                .retrieve()
                .toEntity(String.class)
                .block();
    }
}
