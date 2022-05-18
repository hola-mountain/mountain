package com.example.mountain.handler;

import com.example.mountain.dto.resp.FavoriteMountainResp;
import com.example.mountain.dto.resp.RatingMountainResp;
import com.example.mountain.s3.S3ClientConfigurarionProperties;
import com.example.mountain.s3.S3Config;
import com.example.mountain.dto.req.RatingRecommendReq;
import com.example.mountain.dto.req.RatingRequest;
import com.example.mountain.dto.resp.RatingResp;
import com.example.mountain.entity.RatingEntity;
import com.example.mountain.service.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class RatingHandler {

    private final RatingService ratingService;
    private final S3Config s3client;
    private final S3ClientConfigurarionProperties s3config;
    //private final S3Util s3Util;

    public Mono<ServerResponse> getReviewInMountainDetail(ServerRequest serverRequest){

        int pageNum = Integer.parseInt(serverRequest.queryParam("pageNum").get());
        int pageSize = Integer.parseInt(serverRequest.queryParam("pageSize").get());
        Long mountainId = Long.parseLong(serverRequest.pathVariable("mountainId"));

        Flux<RatingResp> result = ratingService.getRatingListPage(mountainId,pageNum,pageSize);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result, RatingResp.class)
                .onErrorResume(error -> ServerResponse.badRequest().build());
    }

//    public Mono<ServerResponse> createReviewTemp(ServerRequest serverRequest){
//        serverRequest.body(BodyExtractors.toMultipartData())
//                .flatMap(p->{
//                    Map<String, Part> map = p.toSingleValueMap();
//                    map.keySet().stream().forEach(c->log.info("key:{}",c));
//                    return null;
//                });
//
//    }
//    private RatingEntity formDataToRatingEntity(Mono<MultiValueMap<String, String>> formData) {
//
//        RatingEntity ratingEntity = new RatingEntity();
//
//        formData.subscribe(formDatamap -> {
//            employee.setName(formDatamap.get().get(0));
//            employee.setDateOfBirth(formDatamap.getFirst(DATE_OF_BIRTH));
//            employee.setGender(formDatamap.getFirst(GENDER));
//            employee.setAddressLine1(formDatamap.getFirst(ADDRESS_LINE_1));
//            employee.setAddressLine2(formDatamap.getFirst(ADDRESS_LINE_2));
//            employee.setCountry(formDatamap.getFirst(COUNTRY));
//            employee.setState(formDatamap.getFirst(STATE));
//            employee.setCity(formDatamap.getFirst(CITY));
//            employee.setZipCode(formDatamap.getFirst(ZIP_CODE));
//            employee.setMobile(formDatamap.getFirst(MOBILE));
//            employee.setEmail(formDatamap.getFirst(EMAIL));
//            employee.setSkills(formDatamap.get(SKILLS));
//            employee.setWebsite(formDatamap.getFirst(WEBSITE));
//            employee.setBiography(formDatamap.getFirst(BIOGRAPHY));
//        });
//
//        return employee;
//    }


    public Mono<ServerResponse> createReview(ServerRequest serverRequest){

        Long mountainId = Long.parseLong(serverRequest.pathVariable("mountainId"));

        Mono<RatingEntity> ratingMono = serverRequest.bodyToMono(RatingRequest.class)
                .onErrorResume(throwable -> {
                    return Mono.error(new RuntimeException(throwable));
                })

                .flatMap( r-> {
                    return ratingService.createRating(RatingEntity.builder()
                                         .title(r.getTitle())
                                         .comment(r.getComment())
                                         .star(r.getStar())
                                         .mountainId(mountainId)
                                         .userId(r.getUserId())
                                         .ratingNum(0L)
                                         .nickname(r.getNickname()).build());
                });

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ratingMono, RatingEntity.class)
                .onErrorResume(error -> ServerResponse.badRequest().build());
    }


    public Mono<ServerResponse> modifyReview(ServerRequest serverRequest){

        Long mountainId = Long.parseLong(serverRequest.pathVariable("mountainId"));
        Long ratingId = Long.parseLong(serverRequest.pathVariable("ratingId"));

        Mono<RatingEntity> ratingMono = serverRequest.bodyToMono(RatingRequest.class)
                .onErrorResume(throwable -> {
                    return Mono.error(new RuntimeException(throwable));
                }).flatMap( r-> {
                    return ratingService.updateRating(r, ratingId);
                });

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ratingMono, RatingResp.class)
                .onErrorResume(error -> ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> removeReview(ServerRequest serverRequest){

        Long mountainId = Long.parseLong(serverRequest.pathVariable("mountainId"));
        Long ratingId = Long.parseLong(serverRequest.pathVariable("ratingId"));
        Mono<RatingRequest> req = serverRequest.bodyToMono(RatingRequest.class);
        Mono<Void> result = Mono.zip(req, Mono.just(ratingId)).flatMap(
                x-> ratingService.deleteRating(x.getT2().longValue(), x.getT1().getUserId())
        );

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ratingId, String.class)
                .onErrorResume(error -> ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> recommendReview(ServerRequest serverRequest){

        Long ratingId = Long.parseLong(serverRequest.pathVariable("ratingId"));
        Mono<Integer> ratingMono = serverRequest.bodyToMono(RatingRecommendReq.class)
                .flatMap(r-> ratingService.toggleRecommend(ratingId, r.getUserId(), r.getAdd()));

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ratingMono, RatingResp.class)
                .onErrorResume(error -> ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> getMyPageReview(ServerRequest serverRequest){

        Long userId= Long.parseLong(serverRequest.queryParam("userId").get());

        Flux<RatingMountainResp> result = ratingService.getMyPageReviewMountain(userId);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result, RatingMountainResp.class)
                .onErrorResume(error -> ServerResponse.badRequest().build());
    }

}
