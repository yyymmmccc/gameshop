package com.project.game.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.project.game.common.ResponseCode;
import com.project.game.handler.CustomException;
import com.project.game.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        return fileUploadService.uploadFile(file);
    }
}