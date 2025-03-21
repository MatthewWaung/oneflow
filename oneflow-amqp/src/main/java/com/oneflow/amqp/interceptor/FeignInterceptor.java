package com.oneflow.amqp.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FeignInterceptor implements RequestInterceptor {

    @Value("${oms.token}")
    private String token;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 设置请求头的token
        requestTemplate.header("Authorization", "Bearer " + token);
        // 设置traceId
        requestTemplate.header("traceId", MDC.get("traceId"));
    }

}
