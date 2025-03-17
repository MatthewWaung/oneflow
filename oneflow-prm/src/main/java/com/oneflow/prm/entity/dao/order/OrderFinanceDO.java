package com.oneflow.prm.entity.dao.order;

import com.baomidou.mybatisplus.annotation.*;
import com.oneflow.prm.entity.dao.BaseAutoFillModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("prm_order_finance")
public class OrderFinanceDO extends BaseAutoFillModel<OrderFinanceDO> {

    private static final long serialVersionUID = -8113560105014607214L;
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单id
     */
    @TableField(value = "order_id", updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long orderId;

    /**
     * 小计
     */
    @TableField(value = "subtotal", updateStrategy = FieldStrategy.NOT_EMPTY)
    private BigDecimal subtotal;

    /**
     * 整单折扣率
     */
    @TableField(value = "order_discount_rate", updateStrategy = FieldStrategy.NOT_EMPTY)
    private BigDecimal orderDiscountRate;

    /**
     * 总金额
     */
    @TableField(value = "total_balance", updateStrategy = FieldStrategy.NOT_EMPTY)
    private BigDecimal totalBalance;

    /**
     * 总税额
     */
    @TableField(value = "total_rate_balance", updateStrategy = FieldStrategy.NOT_EMPTY)
    private BigDecimal totalRateBalance;

    /**
     * 备注
     */
    @TableField(value = "note", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String note;

}
