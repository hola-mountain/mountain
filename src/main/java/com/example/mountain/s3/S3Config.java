package com.example.mountain.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.utils.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

@Configuration
@EnableConfigurationProperties(S3ClientConfigurarionProperties.class)
public class S3Config {


    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;


    @Bean
    public S3AsyncClient s3client(S3ClientConfigurarionProperties properties, AwsCredentialsProvider credentialsProvider) throws URISyntaxException {

        SdkAsyncHttpClient httpClient = NettyNioAsyncHttpClient.builder()
                .writeTimeout(Duration.ZERO)
                .maxConcurrency(64)
                .build();

        S3Configuration serviceConfiguration = S3Configuration.builder()
                .checksumValidationEnabled(false)
                .chunkedEncodingEnabled(true)
                .build();

        S3AsyncClientBuilder b = S3AsyncClient.builder()
                .httpClient(httpClient)
                .region(properties.getRegion())
                .credentialsProvider(credentialsProvider)
                .serviceConfiguration(serviceConfiguration);

        b = b.endpointOverride(new URI("https://hola-mountain.s3.ap-northeast-2.amazonaws.com/"));

        return b.build();
    }

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {

        if (StringUtils.isBlank(accessKey)) {
            // Return default provider
            return DefaultCredentialsProvider.create();
        }
        else {
            // Return custom credentials provider
            return () -> {
                AwsCredentials creds = AwsBasicCredentials.create(accessKey, secretKey);
                return creds;
            };
        }
    }
}