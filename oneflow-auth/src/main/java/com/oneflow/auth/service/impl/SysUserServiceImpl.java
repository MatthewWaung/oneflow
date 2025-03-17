package com.oneflow.auth.service.impl;

import com.oneflow.auth.entity.SysUser;
import com.oneflow.auth.mapper.SysUserMapper;
import com.oneflow.auth.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements ISysUserService {

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public SysUser selectUserByUserName(String username) {
//        return userMapper.selectUserByUserName(username);
        SysUser sysUser = new SysUser();
        sysUser.setUserId(101L);
        sysUser.setUserName("admin");
        sysUser.setPassword("$2a$10$yJvpy40eVzf5G3ELMDRUKecI9oehDS3UDLdrJgW8sWf7bY/AXIH7G");
        sysUser.setDelFlag("0");
        sysUser.setStatus("0");
        return sysUser;
    }
}
