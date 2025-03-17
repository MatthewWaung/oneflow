package com.oneflow.auth.service;

import com.oneflow.auth.security.entity.SysUser;

public interface ISysUserService {

    SysUser selectUserByUserName(String username);

}
