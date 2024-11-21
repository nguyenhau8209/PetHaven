package com.yellowcat.backend.controller;

import com.yellowcat.backend.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
public class ImageUploadController {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @Autowired
    private CloudinaryService cloudinaryService;

    // Hàm kiểm tra tên file hợp lệ
    public boolean isValidFileName(String fileName) {
        // Kiểm tra tên file không chứa các ký tự như "..", "/", "\", và chỉ chứa ký tự hợp lệ
        return fileName != null && !fileName.contains("..") && fileName.matches("[a-zA-Z0-9._-]+");
    }

    // Hàm kiểm tra định dạng file
    public boolean isValidFileType(String contentType) {
        return contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/jpg");
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile multipartFile) {
        try {
            String fileName = multipartFile.getOriginalFilename();
            String contentType = multipartFile.getContentType();
            long fileSize = multipartFile.getSize();

            // Kiểm tra tên file
            if (!isValidFileName(fileName)) {
                return ResponseEntity.badRequest().body("Invalid file name");
            }

            // Kiểm tra định dạng file
            if (!isValidFileType(contentType)) {
                return ResponseEntity.badRequest().body("Invalid file type. Only JPG, JPEG, and PNG are allowed.");
            }

            // Kiểm tra kích thước file
            if (fileSize > MAX_FILE_SIZE) {
                return ResponseEntity.badRequest().body("File size exceeds the limit of 5MB");
            }

            // Lưu file tạm thời
            File file = File.createTempFile("temp", fileName);
            multipartFile.transferTo(file);

            // Upload file lên Cloudinary
            Map uploadResult = cloudinaryService.uploadFile(file);

            return ResponseEntity.ok(uploadResult);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error uploading image: " + e.getMessage());
        }
    }
}
