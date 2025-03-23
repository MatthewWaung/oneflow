package com.oneflow.auth.controller;

import com.oneflow.auth.core.common.R;
import com.oneflow.auth.core.exception.CustomException;
import com.oneflow.auth.core.utils.StringUtils;
import com.oneflow.auth.core.utils.redis.RedisUtils;
import com.oneflow.auth.entity.LoginUser;
import com.oneflow.auth.entity.SsoAuthVO;
import com.oneflow.auth.mapper.Oauth2ClientMapper;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/oauth2")
public class AccessTokenController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private Oauth2ClientMapper oauth2ClientMapper;

    /**
     * AbstractEndpoint有两个子类：
     * TokenEndpoint（实现/oauth/token接口）、
     * AuthorizationEndpoint（实现/oauth/authorize接口）
     */
    @Autowired
    private TokenEndpoint tokenEndpoint;

    @PostMapping("/token")
    public R accessToken(SsoAuthVO authVO, Principal principal) throws HttpRequestMethodNotSupportedException {
        if (StringUtils.isEmpty(authVO.getClient_id()) || StringUtils.isEmpty(authVO.getRedirect_uri())
                || StringUtils.isEmpty(authVO.getCode()) || StringUtils.isEmpty(authVO.getClient_secret())) {
            throw new CustomException("client_id、client_secret、code和redirect_uri不能为空");
        }
        if (!checkClientParams(authVO.getClient_id(), authVO.getClient_secret())) {
            throw new CustomException("客户端密钥错误");
        }
        String codeKey = "auth:code:" + authVO.getCode();
        if (redisUtils.get(codeKey)) {
            JSONObject userObj = redisUtils.get(codeKey);
//            String code = UUID.randomUUID().toString().replace("-", "");
            // 第三方登录需要生成一个token，单点的情况下不需要
            String token = RandomStringUtils.randomAlphanumeric(32);
            redisUtils.set("auth:access_token:" + token, userObj, 2, TimeUnit.MINUTES);

            // 如何使用客户端信息获取用户的信息从而生成jwtToken？
            /**
             * 获取授权码code的时候，oauth2会维护一个code->OAuth2Authentication的ConcurrentHashMap，此时就已完成了
             * code与用户信息的绑定，OAuth2Authentication中的userAuthentication的principal就用用户名等信息。
             * 在AuthorizationCodeTokenGranter类的getOAuth2Authentication()方法中
             * this.authorizationCodeServices.consumeAuthorizationCode(authorizationCode)可以看出。
             */

            // 判断当前SpringSecurity登录的用户有没有OAuth2登录，没有就进行OAuth2登录
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof OAuth2Authentication) {
                OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
                Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
                LoginUser loginUser = (LoginUser) userAuthentication.getPrincipal();
                // 然后生成OAuth2AccessToken返回
                OAuth2AccessToken oAuth2AccessToken;
            } else {
                LoginUser loginUser = (LoginUser) authentication.getPrincipal();
                Map<String, String> params = new HashMap<>();
                params.put("username", loginUser.getUsername());
                params.put("password", loginUser.getPassword());
                params.put("grant_type", "password");
                params.put("client_id", "test-client");
                params.put("client_secret", "123456");
                // 使用密码模式登录
                OAuth2AccessToken auth2AccessToken = tokenEndpoint.postAccessToken(principal, params).getBody();
                return R.success(auth2AccessToken);
            }

        } else {
            throw new CustomException("授权码无效");
        }
        return R.success();
    }

    private boolean checkClientParams(String client_id, String client_secret) {
        ClientDetails client = oauth2ClientMapper.selectClientById(client_id);
        if (client != null && client.getClientSecret().equals(client_secret)) {
            return true;
        }
        return false;
    }

    /**
     * 未开启表单验证.formLogin()情况下的自定义 /oauth2/token 接口
     *
     * @param principal
     * @param params
     * @return
     * @throws HttpRequestMethodNotSupportedException
     */
    @PostMapping("/auth/token")
    public R postAccessToken(Principal principal, @RequestParam Map<String, String> params) throws HttpRequestMethodNotSupportedException {
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, params).getBody();
        return R.success(oAuth2AccessToken);
    }

}
