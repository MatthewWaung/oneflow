package com.oneflow.oms.entity.record;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 修改操作记录
 */
@Data
@TableName("oms_modify_record")
//public class ModifyRecordDO extends BaseEntity {
public class ModifyRecordDO {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 修改所属模块类型：0:order
     */
    @TableField(value = "module_type", updateStrategy = FieldStrategy.NOT_NULL)
    private Integer moduleType;

    /**
     * 业务操作类型:insert,modify
     */
    @TableField(value = "business_type", updateStrategy = FieldStrategy.NOT_NULL)
    private Integer businessType;

    /**
     * 旧数据
     */
    @TableField(value = "old_data", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String oldData;

    /**
     * 本次提交数据
     */
    @TableField(value = "post_data", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String postData;

    /**
     * 最新数据
     */
    @TableField(value = "new_data", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String newData;

    /**
     * 差异数据
     */
    @TableField(value = "change_data", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String changeData;

    /**
     * 修改描述
     */
    @TableField(value = "description", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String description;

    /**
     * 模块业务主键id
     */
    @TableField(value = "module_id", updateStrategy = FieldStrategy.NOT_NULL)
    private Long moduleId;

    /**
     * 数据类型 0:数据操作记录；1：状态操作记录
     */
    @TableField(value = "data_type", updateStrategy = FieldStrategy.NOT_NULL)
    private Integer dataType;

    /**
     * 修改者ip地址
     */
    @TableField(value = "ip", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String ip;

    /**
     * 操作状态 ：0：成功；1 失败
     */
    @TableField(value = "status", updateStrategy = FieldStrategy.NOT_NULL)
    private Integer status;

    /**
     * 失败原因
     */
    @TableField(value = "fail_cause", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String failCause;

    /**
     * traceId
     */
    @TableField(value = "trace_id", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String traceId;

    /**
     * 部门id
     */
    @TableField(value = "dept_id", updateStrategy = FieldStrategy.NOT_NULL)
    private Long deptId;

}
