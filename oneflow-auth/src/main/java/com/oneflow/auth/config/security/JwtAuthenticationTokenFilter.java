package com.oneflow.auth.config.security;

import com.oneflow.auth.security.core.utils.StringUtils;
import com.oneflow.auth.security.entity.LoginUser;
import com.oneflow.auth.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token过滤器，验证token有效性
 * 有了token之后，我们要把过滤器放在过滤器链中，用于解析token，因为我们没有session，所以我们每次去辨别这是哪个用户的请求的时候，都是根据请求中的token来解析出来当前是哪个用户。
 * 所以我们需要一个过滤器去拦截所有请求，前文我们也说过，这个过滤器我们会放在绿色部分用来替代
 * UsernamePasswordAuthenticationFilter
 * ————————————————
 * 版权声明：本文为CSDN博主「程序员柒柒」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/CXY_QIQI/article/details/124770755
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 根据请求头token信息获取LoginUser
        LoginUser loginUser = tokenService.getLoginUser(request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNull(authentication)) {
            // 校验token信息
            tokenService.verifyToken(loginUser);
            // 组装authentication对象，构造参数是Principal Credentials 与 Authorities
            // 后面的拦截器里面会用到 grantedAuthorities 方法
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // 将authentication信息放入到上下文对象中
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}
