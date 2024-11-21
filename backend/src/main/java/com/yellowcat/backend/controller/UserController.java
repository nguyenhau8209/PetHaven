package com.yellowcat.backend.controller;

import com.yellowcat.backend.DTO.request.AccountRequest;
import com.yellowcat.backend.DTO.response.ApiResponse;
import com.yellowcat.backend.model.Thucung;
import com.yellowcat.backend.service.KeycloakUserService;
import com.yellowcat.backend.service.ProfileService;
import com.yellowcat.backend.service.ThuCungService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {

    ProfileService profileService;

    @Autowired
    ThuCungService thuCungService;

    /**
     * API để lấy thông tin người dùng hiện tại (từ JWT token).
     * Trả về các thông tin về người dùng và thú cưng.
     */
    @GetMapping("/api/user")
    public Map<String, Object> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String idUser = authentication.getName();

        // Lấy JWT từ authentication để trích xuất thông tin
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        List<String> petHavenRoles = null;

        if (resourceAccess != null) {
            Map<String, Object> petHavenAccess = (Map<String, Object>) resourceAccess.get("PetHaven");
            if (petHavenAccess != null) {
                petHavenRoles = (List<String>) petHavenAccess.get("roles");
            }
        }

        String username = jwt.getClaimAsString("preferred_username");
        if (username == null) {
            username = jwt.getClaimAsString("email");
        }

        // Lấy danh sách thú cưng của người dùng
        List<Thucung> thucungList = thuCungService.findListThuCungByidChu(idUser);

        // Tạo response trả về
        Map<String, Object> response = new HashMap<>();
        response.put("roles", petHavenRoles);
        response.put("idUser", idUser);
        response.put("username", username);
        response.put("listThuCung", thucungList);

        return response;
    }

    /**
     * API đăng ký người dùng mới.
     * Nhận yêu cầu đăng ký và sử dụng ProfileService để tạo tài khoản.
     */
    @PreAuthorize("hasRole('admin')")
    @PostMapping("/register")
    public ApiResponse<Object> register(@RequestBody @Valid AccountRequest request) {
        // Gọi service ProfileService để tạo tài khoản
        String result = profileService.createAccount(request);

        // Trả về ApiResponse với kết quả
        return ApiResponse.builder()
                .result(result)  // Kết quả trả về từ ProfileService
                .build();
    }

    /**
     * API lấy tất cả người dùng.
     */
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/api/users")
    public ApiResponse<List<Map<String, Object>>> getAllUsers() {
        // Lấy tất cả người dùng từ service
        List<Map<String, Object>> users = profileService.getAllUsers();

        // Trả về dưới dạng ApiResponse
        return ApiResponse.<List<Map<String, Object>>>builder()
                .result(users)  // Kết quả trả về là một danh sách các Map
                .build();
    }


    /**
     * API lấy thông tin người dùng theo ID.
     */
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/api/users/{id}")
    public ApiResponse<Map<String, Object>> getUserById(@PathVariable("id") String userId) {
        Map<String, Object> user = profileService.getUserById(userId);
        return ApiResponse.<Map<String, Object>>builder()
                .result(user)
                .build();
    }

    /**
     * API cập nhật thông tin người dùng.
     */
    @PreAuthorize("hasRole('admin')")
    @PutMapping("/api/users/{id}")
    public ApiResponse<Object> updateUser(@PathVariable("id") String userId, @RequestBody @Valid AccountRequest request) {
        String result = profileService.updateUser(userId, request);
        return ApiResponse.builder()
                .result(result)
                .build();
    }

    /**
     * API xóa người dùng theo ID.
     */
    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/api/users/{id}")
    public ApiResponse<Object> deleteUser(@PathVariable("id") String userId) {
        String result = profileService.deleteUser(userId);
        return ApiResponse.builder()
                .result(result)
                .build();
    }

    @Autowired
    private KeycloakUserService keycloakUserService;

    @GetMapping("api/users/roles")
    public ResponseEntity<Map<String, List<String>>> getAllUsersWithRoles() {
        try {
            Map<String, List<String>> userRoles = keycloakUserService.getAllUsersRoles();
            return ResponseEntity.ok(userRoles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
