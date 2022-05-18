package com.example.mountain.handler;

import com.example.mountain.dto.resp.MountainResp;
import com.example.mountain.service.MountainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MountainHandler {

    private final MountainService mountainService;

    // get mountain list By Page
    public Mono<ServerResponse> getMountainList(ServerRequest serverRequest){

        // district : 0 이면 전국 -> 전체 조회
        int district= Integer.parseInt(serverRequest.queryParam("district").get());
        
        String search = serverRequest.queryParam("search").get();
        int pageNum = Integer.parseInt(serverRequest.queryParam("pageNum").get());
        int pageSize = Integer.parseInt(serverRequest.queryParam("pageSize").get());
        boolean isAsc = Boolean.parseBoolean(serverRequest.queryParam("isAsc").get());
        String sortBy = serverRequest.queryParam("sortBy").get();


        Flux<MountainResp> mountainFlux = mountainService.getMountainListPage(district, pageNum, pageSize, sortBy, isAsc, search);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(mountainFlux, MountainResp.class)
                .onErrorResume(error -> ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> getMountainDetail(ServerRequest serverRequest) {

        Long mountainId = Long.parseLong(serverRequest.pathVariable("mountainId"));
        Mono<MountainResp> mountainRespMono = mountainService.getMountainDetail(mountainId);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(mountainRespMono, MountainResp.class)
                .onErrorResume(error -> ServerResponse.badRequest().build());
    }



}
