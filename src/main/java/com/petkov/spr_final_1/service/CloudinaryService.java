package com.petkov.spr_final_1.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    String uploadImage(MultipartFile multipartFile) throws IOException;
}
