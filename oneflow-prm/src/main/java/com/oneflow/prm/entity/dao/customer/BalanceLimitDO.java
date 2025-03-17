package com.oneflow.prm.entity.dao.customer;

import com.baomidou.mybatisplus.annotation.*;
import com.oneflow.prm.entity.dao.BaseAutoFillModel;
import lombok.Data;

@Data
@TableName("prm_customer_balance_limit")
public class BalanceLimitDO extends BaseAutoFillModel<BalanceLimitDO> {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 客户id
     */
    @TableField(value = "customer_id", updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long customerId;

    /**
     * 客户名称
     */
    @TableField(value = "customer_name", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String customerName;

    /**
     * 中信保客户编码
     */
    @TableField(value = "zxb_balance_code", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String zxbBalanceCode;

    /**
     * 中信保额度
     */
    @TableField(value = "zxb_balance_limit", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String zxbBalanceLimit;

    /**
     * 授信额度
     */
    @TableField(value = "trust_balance_limit", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String trustBalanceLimit;

    /**
     * 状态：0-启用，1-冻结
     */
    @TableField(value = "status", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String status;
}
