package com.oneflow.prm.core.handler;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Dunvida
 * @Date: 2022/3/28 15:36
 */
public class FeignHandler implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //添加token
        requestTemplate.header("Authorization", request.getHeader("Authorization"));
        String traceId = MDC.get("traceId");
        requestTemplate.header("traceId", traceId);
    }
}
