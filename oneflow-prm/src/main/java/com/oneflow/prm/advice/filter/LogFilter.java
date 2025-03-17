package com.oneflow.prm.advice.filter;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: Dunvida
 * @Date: 2022/4/14 16:57
 */
@Slf4j
@Order(1)
@WebFilter(urlPatterns = "/*", filterName = "traceIdFilter")
public class LogFilter implements Filter {
    public final static String MDC_TRACE_ID = "traceId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String traceId = httpRequest.getHeader(MDC_TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            traceId = IdUtil.fastSimpleUUID();
        }
        MDC.put(MDC_TRACE_ID, traceId);
        //ThreadLocalUtils.setTraceId(traceId);
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader(MDC_TRACE_ID, traceId);
        chain.doFilter(request, response);
    }

}
