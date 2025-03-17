package com.oneflow.prm.entity.dao.sys;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode()
@TableName(value = "sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = -4941096466979547127L;
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @TableField(value = "user_name", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String userName;

    @TableField(value = "real_name", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String realName;

    @TableField(value = "email", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String email;

    @TableField(value = "phone_number", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String phoneNumber;

    @TableField(value = "dept_id", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String deptId;

    @TableField(exist = false)
    private List<SysRole> roles;

    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private String jobNum;
}
