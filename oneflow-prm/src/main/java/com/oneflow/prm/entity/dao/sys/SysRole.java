package com.oneflow.prm.entity.dao.sys;

import lombok.Data;

@Data
public class SysRole {

    private Long roleId;

    private String roleName;

    /**
     * 数据范围（1-所有数据权限，2-自定义数据权限，3-本部门数据权限，4-本部门及以下权限）
     */
    private String dataScope;

    private String status;

    /**
     * 角色权限
     */
    private String roleKey;
}
