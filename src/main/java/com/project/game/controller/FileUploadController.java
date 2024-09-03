package com.project.game.controller;

import com.project.game.controller.swagger.SwaggerFileUploadApi;
import com.project.game.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Slf4j
public class FileUploadController implements SwaggerFileUploadApi {

    private final S3Service s3Service;
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        return s3Service.uploadFile(file);
    }
}