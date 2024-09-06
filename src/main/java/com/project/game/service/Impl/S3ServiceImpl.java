package com.project.game.service.Impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.game.dto.response.ResponseDto;
import com.project.game.global.common.ResponseCode;
import com.project.game.global.handler.CustomException;
import com.project.game.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3ServiceImpl implements S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final List<String> allowedMimeTypes = Arrays.asList("image/jpg", "image/jpeg", "image/png");

    private final AmazonS3Client amazonS3Client;

    @Override
    public ResponseEntity uploadFile(MultipartFile file) throws IOException {

        //List<URL> list = new ArrayList<>();

        String contentType = file.getContentType();
        if (!allowedMimeTypes.contains(contentType))
            throw new CustomException(ResponseCode.FILE_REQUEST_FAIL);

        String uuid = UUID.randomUUID().toString();
        String fileUrl = "images/" + uuid;

        // 파일의 Content-Type 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileUrl, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);  // 퍼블릭 읽기 권한 부여

        amazonS3Client.putObject(putObjectRequest);

        URL url = amazonS3Client.getUrl(bucket, fileUrl);
        // list.add(url);

        return ResponseDto.success(url);
    }

    public boolean deleteFile(String imageUrl){

        try{
            String fileKey = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            log.info("이미지 url : " + imageUrl);
            log.info("파일경로 url : " + fileKey);
            amazonS3Client.deleteObject(bucket, "images/" + fileKey);

            return true;
        } catch (Exception e){
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
