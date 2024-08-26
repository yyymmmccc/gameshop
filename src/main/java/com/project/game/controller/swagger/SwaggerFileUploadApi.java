package com.project.game.controller.swagger;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;

@Tag(name = "파일 업로드", description = "파일 업로드 API")
public interface SwaggerFileUploadApi {

    @Operation(summary = "파일 업로드", description = "이미지 파일을 업로드하여 S3에 저장, jpg, png 등 이미지 파일만 저장 가능")
    @ApiResponse(responseCode = "200", description = "파일 업로드 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 파일 형식")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity uploadFile(@Parameter(description = "key 값 multipartFile / jpg, jpeg, png 만 가능")
            @RequestParam("files") MultipartFile[] files) throws IOException;
}
