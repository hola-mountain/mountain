package com.example.mountain.s3;

import com.example.mountain.dto.resp.MountainResp;
import com.example.mountain.handler.MountainHandler;
import com.example.mountain.repository.RatingRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
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

@RestController
@RequestMapping("/mountain/api")
@Slf4j
@RequiredArgsConstructor
public class UploadResource {

    private final S3ClientConfigurarionProperties s3Property;
    private final S3AsyncClient s3client;
    private final S3ClientConfigurarionProperties s3config;
    private final RatingRepository ratingRepository;


    //private final Path basePath = Paths.get("/home/ec2-user/upload/");
    private final Path basePath = Paths.get("/home/ec2-user/upload/");



    /**
     *  filePartMono로 받아
     *  /src/main/resources/upload/{fp.filename()} 으로
     *  File을 생성한다.
     */
    @PostMapping("/upload/{ratingId}")
    public Mono<ResponseEntity<UploadResult>> uploadHandler(@RequestPart("file") Mono<FilePart> filePartMono, @PathVariable Long ratingId) {

        log.info("/upload/{}, ", ratingId);
        filePartMono.flatMap(
                 fp -> fp.transferTo(basePath.resolve(fp.filename())))
                .subscribe();

        return filePartMono.flatMap(fp-> uploadFiletoS3(fp.filename(), ratingId)).retry();
    }


    /**
     *  /src/main/resources/upload/ 에서 fileName으로 파일을 찾은 뒤
     *  파일을 Amazon S3에 올린다.
     */
    public Mono<ResponseEntity<UploadResult>> uploadFiletoS3(String fileName, Long ratingId) {

        String fileKey = UUID.randomUUID().toString()+".jpg";
        Map<String, String> metadata = new HashMap<String, String>();
        log.info("file dir : {}" ,"./" + fileName);
        //mediaType = MediaType.IMAGE_JPEG;
        File fi = new File("/home/ec2-user/upload/"+ fileName);
        byte[] fileContent = null;
        try {
            fileContent = Files.readAllBytes(fi.toPath());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        String buckDomain = s3Property.getEndpoint() + s3Property.getBucket() + "/";
        String img = buckDomain+fileKey;
        ratingRepository.findById(ratingId).flatMap( x-> {
            x.setThumbImg(img);
            return ratingRepository.save(x);
        }).subscribe();

        //log.info("[I95] uploadHandler: mediaType{}, length={}", mediaType, length);
        CompletableFuture<PutObjectResponse> future = s3client
                .putObject(PutObjectRequest.builder()
                                .bucket(s3config.getBucket())
                                .contentLength(Long.valueOf(fileContent.length))
                                .key(fileKey)
                                .contentType(MediaType.IMAGE_JPEG_VALUE)
                                .metadata(metadata)
                                .build(),
                        AsyncRequestBody.fromPublisher(Flux.just(ByteBuffer.wrap(fileContent))));
        
        // 업로드된 파일 삭제 
        fi.delete();

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