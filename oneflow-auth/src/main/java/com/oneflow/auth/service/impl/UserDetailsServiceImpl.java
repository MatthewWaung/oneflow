package com.oneflow.auth.service.impl;

import com.oneflow.auth.core.exception.CustomException;
import com.oneflow.auth.core.utils.StringUtils;
import com.oneflow.auth.entity.LoginUser;
import com.oneflow.auth.entity.SysUser;
import com.oneflow.auth.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ISysUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new CustomException("登录用户：" + username + " 不存在");
        } else if ("1".equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", username);
            throw new CustomException("对不起，您的账号：" + username + " 已被删除");
        } else if ("1".equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
            throw new CustomException("对不起，您的账号：" + username + " 已停用");
        }

        return createLoginUser(user);
    }

    private UserDetails createLoginUser(SysUser user) {
        return new LoginUser(user);
    }

}
