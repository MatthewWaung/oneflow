package com.oneflow.prm.entity.dao.customer;

import com.baomidou.mybatisplus.annotation.*;
import com.oneflow.prm.entity.dao.BaseAutoFillModel;
import lombok.Data;

@Data
@TableName("prm_customer_salesperson")
public class SalespersonDO extends BaseAutoFillModel<SalespersonDO> {

    /**
     *  主键
     */
    @TableId(value = "id",type = IdType.INPUT)
    private Long id;

    /**
     * 客户id
     */
    @TableField(value = "customer_id",updateStrategy= FieldStrategy.NOT_EMPTY)
    private Long customerId;

    /**
     * 客户名称
     */
    @TableField(value = "customer_name",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String customerName;

    /**
     * 业务员工号(数据从sys_user中来)
     */
    @TableField(value = "salesperson_no",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String salespersonNo;

    /**
     * 业务员名称
     */
    @TableField(value = "salesperson",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String salesperson;

    /**
     * 业务员所在部门
     */
    @TableField(value = "salesperson_dept",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String salespersonDept;

    /**
     * 状态：0-启用，1-冻结
     */
    @TableField(value = "status",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String status;


}
