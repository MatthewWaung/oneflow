package com.oneflow.auth.controller;

import com.oneflow.auth.security.core.common.R;
import com.oneflow.auth.security.entity.User;
import com.oneflow.auth.security.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 账号密码登录，参考：https://www.cnblogs.com/zhouylove/p/16794889.html
 */
@RestController
public class LoginController {

    /**
     * SpringSecurity账号密码登录见：https://www.cnblogs.com/zhouylove/p/16794889.html
     */

    @Autowired
    private LoginService loginService;

    /**
     * 登录
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    public R login(@RequestBody User user) {
        return loginService.login(user);
    }

    /**
     * 退出
     *
     * @return
     */
    @GetMapping("/logout")
    public R logout() {
        return loginService.logout();
    }
}
