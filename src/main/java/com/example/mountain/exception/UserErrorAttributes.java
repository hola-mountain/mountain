package com.example.mountain.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Component
public class UserErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {

        Map<String, Object> map = super.getErrorAttributes(request, options);

        Throwable throwable = getError(request);
        if (throwable instanceof GlobalMountainException) {
            // 사용자 정의 에러일 경우, GlobalException을 통해 따로 처리된다.
            GlobalMountainException ex = (GlobalMountainException) getError(request);
            map.put("errorCode", ex.getStatus().value());
            map.remove("status");
            map.put("errorMessage", ex.getReason());
        }

        return map;
    }
}