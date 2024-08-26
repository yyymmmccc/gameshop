package com.project.game.controller;

import com.project.game.controller.swagger.SwaggerFileUploadApi;
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

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class FileUploadController implements SwaggerFileUploadApi {

    private final FileUploadService fileUploadService;
    @PostMapping
    public ResponseEntity uploadFile(@RequestParam("files") MultipartFile[] files) throws IOException {

        return fileUploadService.uploadFile(files);
    }
}