package com.oneflow.auth.service.impl;

import com.oneflow.auth.core.common.R;
import com.oneflow.auth.core.exception.CustomException;
import com.oneflow.auth.core.utils.redis.RedisUtils;
import com.oneflow.auth.entity.LoginUser;
import com.oneflow.auth.entity.User;
import com.oneflow.auth.service.LoginService;
import com.oneflow.auth.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 账号密码登录，参考：https://www.cnblogs.com/zhouylove/p/16794889.html
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public R login(User user) {
        // 可以添加一些校验
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        // 使用authenticationManager.authenticate()传入要认证的对象信息,认证成功
        // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername()
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new CustomException("用户名或密码错误");
        }
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String token = tokenService.createToken(loginUser);
        return R.success(token);
    }

    @Override
    public R logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getUser().getUserId();
        redisUtils.del("login" + userId);
        return R.success("退出成功");
    }
}
