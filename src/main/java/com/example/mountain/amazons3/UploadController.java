package com.example.mountain.amazons3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@Slf4j
public class UploadController {

    private final S3Uploader s3Uploader;

    @PostMapping(value = "/upload/{ratingId}")
    public String upload(@RequestParam("file") MultipartFile file, @PathVariable Long ratingId) throws IOException {
        System.out.println("fileName : {}" + file.getName());

        s3Uploader.upload(file, "static", ratingId);
        return "test";
    }
}