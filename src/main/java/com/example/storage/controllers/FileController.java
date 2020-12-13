package com.example.storage.controllers;

import com.example.storage.services.MultipartService;
import com.example.storage.services.VideoStreamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
public class FileController {

   private final VideoStreamService videoStreamService;
   private final MultipartService multipartService;

    public FileController(VideoStreamService videoStreamService, MultipartService multipartService) {
        this.videoStreamService = videoStreamService;
        this.multipartService = multipartService;
    }

    @GetMapping("/stream/{fileName}/{fileType}")
    public Mono<ResponseEntity<byte[]>> streamVideo(@RequestHeader(value = "Range", required = false) String httpRangeList,
                                                    @PathVariable("fileType") String fileType,
                                                    @PathVariable("fileName") String fileName) {
        return Mono.just(videoStreamService.prepareContent(fileName, fileType, httpRangeList));
    }

    @PostMapping("/uploadVideo")
    public ResponseEntity<?> uploadVideo(@RequestParam MultipartFile file) {
        try {
            multipartService.transferFile(file);
            return new ResponseEntity(HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getImage/{fileName}/{fileType}")
    public ResponseEntity<byte[]> getImage(@PathVariable("fileType") String fileType,
                           @PathVariable("fileName") String fileName) {
        return multipartService.getImageBytes(fileName, fileType);
    }
}
