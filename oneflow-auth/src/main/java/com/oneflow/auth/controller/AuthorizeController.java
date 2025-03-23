package com.oneflow.auth.controller;

import com.oneflow.auth.core.exception.CustomException;
import com.oneflow.auth.core.utils.StringUtils;
import com.oneflow.auth.core.utils.redis.RedisUtils;
import com.oneflow.auth.entity.SsoAuthVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/oauth2")
public class AuthorizeController {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 重写/oauth2/authorize接口
     *
     * @param authVO
     * @param view
     * @return
     */
    @GetMapping("/authorize")
    public ModelAndView authorize(SsoAuthVO authVO, ModelAndView view) {
        if (StringUtils.isEmpty(authVO.getClient_id()) || StringUtils.isEmpty(authVO.getRedirect_uri())) {
            throw new CustomException("client_id和redirect_uri不能为空");
        }
        // 随机生成固定长度字符串作为code
//        String code = UUID.randomUUID().toString().replace("-", "");
        String code = RandomStringUtils.randomAlphanumeric(16);
        String redirectUri = "redirect:" + authVO.getRedirect_uri() + "?code=" + code;
        redisUtils.set("auth:code:" + code, code, 1, TimeUnit.MINUTES);
        view.setViewName(redirectUri);
        return view;
    }
}
