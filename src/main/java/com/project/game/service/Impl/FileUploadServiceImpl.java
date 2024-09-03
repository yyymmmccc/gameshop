package com.project.game.service.Impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.game.global.common.ResponseCode;
import com.project.game.dto.response.ResponseDto;
import com.project.game.global.handler.CustomException;
import com.project.game.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final List<String> allowedMimeTypes = Arrays.asList("image/jpeg", "image/jpg", "image/png");
    private final AmazonS3Client amazonS3Client;

    @Override
    public ResponseEntity uploadFile(MultipartFile[] files) throws IOException {

        List<URL> list = new ArrayList<>();

        for (MultipartFile file : files) {
            String contentType = file.getContentType();
            if (!allowedMimeTypes.contains(contentType)) {
                throw new CustomException(ResponseCode.FILE_REQUEST_FAIL);
            }
            String fileName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String fileUrl = "images/" + uuid + "_" + fileName;

            // 파일의 Content-Type 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileUrl, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);  // 퍼블릭 읽기 권한 부여

            amazonS3Client.putObject(putObjectRequest);

            URL url = amazonS3Client.getUrl(bucket, fileUrl);
            list.add(url);
        }
        return ResponseDto.success(list);
    }
}
