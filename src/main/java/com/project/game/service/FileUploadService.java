package com.project.game.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface FileUploadService {

    ResponseEntity<String> uploadFile(MultipartFile file);
}
