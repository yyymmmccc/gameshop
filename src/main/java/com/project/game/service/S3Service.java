package com.project.game.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3Service {

    ResponseEntity<?> uploadFile(MultipartFile files) throws IOException;

    ResponseEntity<?> deleteFile(List<String> imageUrlList);
}
