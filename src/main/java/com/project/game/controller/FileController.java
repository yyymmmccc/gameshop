package com.project.game.controller;

import com.project.game.service.FileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Tag(name = "이미지, 파일 업로드", description = "이미지, 파일 업로드 할 수 있는 API")
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {

        return fileService.upload(file);
    }

    @GetMapping(value="{fileName}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public Resource getImage(@PathVariable("fileName") String fileName) throws MalformedURLException {

        return fileService.getImage(fileName);
    }
}
