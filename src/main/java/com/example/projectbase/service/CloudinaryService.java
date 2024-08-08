package com.example.projectbase.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CloudinaryService {
    String uploadImage(MultipartFile file);
    List<String> uploadImages(MultipartFile[] files);
}
