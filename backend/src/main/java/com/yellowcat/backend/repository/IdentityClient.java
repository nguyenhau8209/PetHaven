package com.yellowcat.backend.repository;

import com.yellowcat.backend.DTO.identity.TokenExchangeParam;
import com.yellowcat.backend.DTO.identity.TokenExchangeResponse;
import com.yellowcat.backend.DTO.identity.UserCreationParam;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface IdentityClient {
    TokenExchangeResponse exchangeToken(TokenExchangeParam param);
    ResponseEntity<?> createUser(String token, UserCreationParam param);
    // Lấy tất cả người dùng
    List<Map<String, Object>> getAllUsers(String token);

    // Lấy thông tin người dùng theo ID
    Map<String, Object> getUserById(String token, String userId);

    // Cập nhật thông tin người dùng
    ResponseEntity<?> updateUser(String token, String userId, UserCreationParam param);

    // Xóa người dùng theo ID
    ResponseEntity<?> deleteUser(String token, String userId);
}

