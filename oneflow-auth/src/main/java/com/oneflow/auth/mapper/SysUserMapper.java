package com.oneflow.auth.mapper;

import com.oneflow.auth.security.entity.SysUser;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserMapper {

    /**
     * 通过用户名查找用户
     *
     * @param username
     * @return
     */
    SysUser selectUserByUserName(String username);
}
