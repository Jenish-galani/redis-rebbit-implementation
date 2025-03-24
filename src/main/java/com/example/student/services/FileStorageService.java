package com.example.student.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageService {

    private final Path fileStoragePath;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStoragePath = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStoragePath);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create upload directory!");
        }
    }

    public String storeFile(MultipartFile file) {
        try {
            Path targetLocation = fileStoragePath.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return file.getOriginalFilename();
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file!", ex);
        }
    }

    public Path loadFile(String fileName) {
        return fileStoragePath.resolve(fileName);
    }
}

