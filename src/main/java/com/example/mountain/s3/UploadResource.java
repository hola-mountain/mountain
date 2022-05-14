package com.example.mountain.s3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.SdkResponse;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author Philippe
 *
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class UploadResource {

    private final S3AsyncClient s3client;
    private final S3ClientConfigurarionProperties s3config;

    public UploadResource(S3AsyncClient s3client, S3ClientConfigurarionProperties s3config) {
        this.s3client = s3client;
        this.s3config = s3config;
    }

    private final Path basePath = Paths.get("./src/main/resources/upload/");

    /**
     *  filePartMono로 받아
     *  /src/main/resources/upload/{fp.filename()} 으로
     *  File을 생성한다.
     */
    @PostMapping("/upload")
    public Mono<ResponseEntity<UploadResult>> uploadHandler(@RequestPart("file") Mono<FilePart> filePartMono) {

        filePartMono.flatMap(
                 fp -> fp.transferTo(basePath.resolve(fp.filename())))
                .subscribe();

         filePartMono.doOnNext(fp-> uploadFiletoS3(fp.filename()))
                 .doOnNext(filePart -> filePart.delete());
        return null;
    }


    /**
     *  /src/main/resources/upload/ 에서 fileName으로 파일을 찾은 뒤
     *  파일을 Amazon S3에 올린다.
     */
    public Mono<ResponseEntity<UploadResult>> uploadFiletoS3(String fileName) {

        String fileKey = UUID.randomUUID().toString();
        Map<String, String> metadata = new HashMap<String, String>();

        //mediaType = MediaType.IMAGE_JPEG;
        System.out.println(fileName);
        File fi = new File("./src/main/resources/upload/"+ fileName);
        byte[] fileContent = null;
        try {
            fileContent = Files.readAllBytes(fi.toPath());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


        //log.info("[I95] uploadHandler: mediaType{}, length={}", mediaType, length);
        CompletableFuture<PutObjectResponse> future = s3client
                .putObject(PutObjectRequest.builder()
                                .bucket(s3config.getBucket())
                                .contentLength(Long.valueOf(fileContent.length))
                                .key(fileKey.toString()+".jpg")
                                .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE)
                                .metadata(metadata)
                                .build(),
                        AsyncRequestBody.fromPublisher(Flux.just(ByteBuffer.wrap(fileContent))));

        return Mono.fromFuture(future)
                .map((response) -> {
                    checkResult(response);
                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(new UploadResult(HttpStatus.CREATED, new String[] {fileKey}));
                });
    }

    /**
     * check result from an API call.
     * @param result Result from an API call
     */
    private static void checkResult(SdkResponse result) {
        if (result.sdkHttpResponse() == null || !result.sdkHttpResponse().isSuccessful()) {
            throw new UploadFailedException(result);
        }
    }
}