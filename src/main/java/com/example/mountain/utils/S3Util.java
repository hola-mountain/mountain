package com.example.mountain.utils;


//import java.io.InputStream;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import lombok.Getter;
//import lombok.extern.slf4j.Slf4j;
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
//import software.amazon.awssdk.core.ResponseInputStream;
//import software.amazon.awssdk.core.sync.RequestBody;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.GetObjectRequest;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import software.amazon.awssdk.services.s3.model.PutObjectResponse;

//@Service
//@Getter
//@Slf4j
public class S3Util {

//    private S3Client s3Client;
//
//    @Autowired
//    private S3Properties s3Properties;
//
//
//
//    @PostConstruct
//    public void initialize() {
//        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(s3Properties.getAccessKey(),
//                s3Properties.getSecretKey());
//        s3Client = S3Client
//                .builder()
//                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
//                .region(Region.of(s3Properties.getRegion()))
//                .build();
//
//    }
//
//
//
//    public boolean putObject(String fileName, byte[] fileData) {
//        PutObjectRequest putObjectRequest = PutObjectRequest
//                .builder()
//                .bucket(s3Properties.getBucket())
//                .key(fileName)
//                .build();
//        PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileData));
//        return response
//                .sdkHttpResponse()
//                .statusCode() == 200;
//    }
//
//
//
//    public InputStream getObjectAsInputStream(String name) {
//        GetObjectRequest getObjectRequest = GetObjectRequest
//                .builder()
//                .bucket(s3Properties.getBucket())
//                .key(name)
//                .build();
//        ResponseInputStream inputStream = s3Client.getObject(getObjectRequest);
//        return inputStream;
//    }
//
//
//
//    public byte[] getObjectAsByteArray(String name) {
//        GetObjectRequest getObjectRequest = GetObjectRequest
//                .builder()
//                .bucket(s3Properties.getBucket())
//                .key(name)
//                .build();
//        return s3Client
//                .getObjectAsBytes(getObjectRequest)
//                .asByteArray();
//    }
}
