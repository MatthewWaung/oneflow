package com.oneflow.prm.advice.aspect;

import com.oneflow.prm.core.common.R;
import com.oneflow.prm.core.utils.TokenUtil;
import com.oneflow.prm.entity.dao.sys.SysUser;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Aspect
@Component
@Order(-5)
public class WebLogAspect {

    static ThreadLocal<Long> startTime = new ThreadLocal<>();
    private Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut("execution(* com.oneflow.prm.controller.*.*(..))")
    public void webLog() {

    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(attributes)) {
            HttpServletRequest request = attributes.getRequest();
            SysUser loginUser = TokenUtil.getLoginUser(request);
            if (Objects.nonNull(loginUser)) {
                logger.info("▉▉▉▉▉▉▉▉START▉▉▉▉▉▉▉▉");
                logger.info("▊▊▊▊▊▊▊▊START-{}▊▊▊▊▊▊▊▊", String.format("%s[%s]", loginUser.getUserName(), loginUser.getRealName()));
            } else {
                logger.info("▊▊▊▊▊▊▊▊START▊▊▊▊▊▊▊▊");
            }
            logger.info("URI={}, Method={}", request.getRequestURI(), request.getMethod());
            if (request.getMethod().equalsIgnoreCase("get")) {
                Map<String, String[]> parameterMap = request.getParameterMap();
                parameterMap.forEach((key, value) -> {
                    logger.info(key + ":" + Stream.of(value).collect(Collectors.joining(",", "", "")));
                });
            } else {
                logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
            }
        }
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfter(R ret) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(attributes)) {
            HttpServletRequest request = attributes.getRequest();
            SysUser loginUser = TokenUtil.getLoginUser(request);
            long time = new Date().getTime() - startTime.get();
            logger.info("耗时: {}ms, RETURN={}", time, ret.toString());
            if (Objects.nonNull(loginUser)) {
                logger.info("▊▊▊▊▊▊▊▊END-{}▊▊▊▊▊▊▊▊", String.format("%s[%s]", loginUser.getUserName(), loginUser.getRealName()));
            } else {
                logger.info("▊▊▊▊▊▊▊▊END▊▊▊▊▊▊▊▊");
            }
        }
    }

}
