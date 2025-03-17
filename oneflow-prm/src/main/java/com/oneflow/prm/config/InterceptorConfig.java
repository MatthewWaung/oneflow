package com.oneflow.prm.config;

import com.oneflow.prm.advice.interceptor.ContextInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 配置拦截条件
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    // 参数为我们自定义类，实现了HandlerInterceptor接口重写了三个方
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ContextInterceptor())
                .addPathPatterns("/**")  //拦截所有的路径
                .excludePathPatterns("/login");  //放行login路径下的url
    }

}
