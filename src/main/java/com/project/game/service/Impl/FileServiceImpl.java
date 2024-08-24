package com.project.game.service.Impl;

import com.project.game.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Value("${file.path}") // 저장할 이미지들의 경로
    private String filePath;
    @Value("${file.url}")
    private String fileUrl;


    @Override
    public String upload(MultipartFile file) throws IOException {
        if(file.isEmpty()) return null;

        String originalFileName = file.getOriginalFilename();

        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        // navi.jpg : . 뒤에 있는 jpg를 extension에 넣을거임
        String uuid = UUID.randomUUID().toString(); // toString은 객체를 스트링타입으로 반환
        String saveFileName = originalFileName + "_" + uuid + extension;
        String savePath = filePath + saveFileName;
        // /Users/myungchul-yoon/fileUpload/sdfojosdf2-323r.jpg

        file.transferTo(new File(savePath));
            // file을 savePath경로로 옮기는것
        String url = fileUrl + saveFileName;

        return url;
    }

    @Override
    public Resource getImage(String fileName) throws MalformedURLException {

        Resource resource = new UrlResource("file:" + filePath + fileName);

        return resource;
    }
}
