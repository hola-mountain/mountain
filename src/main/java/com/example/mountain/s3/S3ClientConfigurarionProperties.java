package com.example.mountain.s3;


import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import software.amazon.awssdk.regions.Region;

//cloud:
//    aws:
//        credentials :
//            accessKey : AKIA4JLK22UMGNRY5U7T
//            secretKey : 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
    //    stack:
    //        auto: false
    //    s3:
    //      bucket: holamountain
    //    region:
    //        static: ap-northeast-2


@ConfigurationProperties
@Data
public class S3ClientConfigurarionProperties {

    @Value("${cloud.aws.region.static}")
    private Region region;

    @Value("${cloud.aws.credentials.accesskey}")
    private     String accessKeyId;
    @Value("${cloud.aws.credentials.secretkey}")
    private String secretAccessKey;

    // Bucket name we'll be using as our backend storage
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // AWS S3 requires that file parts must have at least 5MB, except
    // for the last part. This may change for other S3-compatible services, so let't
    // define a configuration property for that
    private int multipartMinPartSize = 100;

}