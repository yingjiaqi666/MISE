package com.example.game.controller;

import com.example.game.dto.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    private final Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads");

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Map<String, String>>> upload(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", "文件为空"));
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", "仅支持图片文件上传"));
        }

        try {
            Files.createDirectories(uploadDir);
            String original = file.getOriginalFilename();
            String ext = "";
            if (original != null) {
                String filename = StringUtils.getFilename(original);
                String extCandidate = StringUtils.getFilenameExtension(filename);
                if (extCandidate != null && !extCandidate.isBlank()) {
                    ext = "." + extCandidate.toLowerCase();
                }
            }
            String storedName = UUID.randomUUID().toString().replaceAll("-", "") + ext;
            Path target = uploadDir.resolve(storedName);
            file.transferTo(target.toFile());

            String url = "/uploads/" + storedName;
            Map<String, String> data = new HashMap<>();
            data.put("url", url);
            data.put("filename", storedName);
            return ResponseEntity.ok(ApiResponse.success(data, "上传成功"));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("500", "文件保存失败"));
        }
    }
}
