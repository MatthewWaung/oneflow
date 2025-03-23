package com.oneflow.auth.service;

import com.oneflow.auth.entity.SysUser;

public interface ISysUserService {

    SysUser selectUserByUserName(String username);

}
