package com.example.storage.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.example.storage.constants.AppConstants.*;

@Service
public class MultipartService {

    private final String IMAGE_FOLDER = "Preview";
    @Value("${video.home}")
    private String videoHome;

    public void transferFile(MultipartFile file) throws IOException {
        String path = videoHome + File.separator;
        file.transferTo(new File(path + file.getOriginalFilename()));

    }

    public ResponseEntity<byte[]> getImageBytes(String name, String type) {
        String fullPath = videoHome + File.separator + IMAGE_FOLDER + File.separator + name + "." + type;
        File file = new File(fullPath);
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(fullPath));
            return ResponseEntity.status(HttpStatus.OK)
                    .header(CONTENT_TYPE, IMAGE_CONTENT + type)
                    .body(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }


    }
}
