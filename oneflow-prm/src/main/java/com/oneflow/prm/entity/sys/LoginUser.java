package com.oneflow.prm.entity.sys;

import com.oneflow.prm.entity.dao.sys.SysRole;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class LoginUser {

    private Long userId;

    private String userName;

    private String realName;

    private String email;

    private String phoneNumber;

    private String deptId;

    private String deptName;

    private List<SysRole> roles;
}
