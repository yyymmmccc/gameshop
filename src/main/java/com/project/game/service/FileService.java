package com.project.game.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface FileService {

    public String upload(MultipartFile file) throws IOException;

    public Resource getImage(String fileName) throws MalformedURLException;
}
