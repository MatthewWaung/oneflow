package com.oneflow.prm.entity.dao.order;

import com.baomidou.mybatisplus.annotation.*;
import com.oneflow.prm.entity.dao.BaseAutoFillModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("prm_order_product")
public class OrderProductDO extends BaseAutoFillModel<OrderProductDO> {

    private static final long serialVersionUID = -2893490207620736053L;
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
     * 仓库id
     */
    @TableField(value = "warehouse_id", updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long warehouseId;

    /**
     * 仓库
     */
    @TableField(value = "warehouse", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String warehouse;

    /**
     * 物料编码
     */
    @TableField(value = "material_code", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String materialCode;

    /**
     * 物料类型
     */
    @TableField(value = "material_type", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String materialType;

    /**
     * item
     */
    @TableField(value = "item", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String item;

    /**
     * ean
     */
    @TableField(value = "ean", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String ean;

    /**
     * sku
     */
    @TableField(value = "sku", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String sku;

    /**
     * 单价
     */
    @TableField(value = "unit_price", updateStrategy = FieldStrategy.NOT_EMPTY)
    private BigDecimal unitPrice;

    /**
     * 含税单价 = 单价 *（1-整单折扣率）（1+税率）
     */
    @TableField(value = "tax_unit_price", updateStrategy = FieldStrategy.NOT_EMPTY)
    private BigDecimal taxUnitPrice;

    /**
     * 数量
     */
    @TableField(value = "qty", updateStrategy = FieldStrategy.NOT_EMPTY)
    private BigDecimal qty;

    /**
     * 税率
     */
    @TableField(value = "rate", updateStrategy = FieldStrategy.NOT_EMPTY)
    private BigDecimal rate;

    /**
     * 金额 = 单价*数量
     */
    @TableField(value = "balance", updateStrategy = FieldStrategy.NOT_EMPTY)
    private BigDecimal balance;

    /**
     * 含税金额 = 含税单价*数量
     */
    @TableField(value = "tax_balance", updateStrategy = FieldStrategy.NOT_EMPTY)
    private BigDecimal taxBalance;

    /**
     * 税额 = 税率*单价*数量* (1-整单折扣率)
     */
    @TableField(value = "rate_balance", updateStrategy = FieldStrategy.NOT_EMPTY)
    private BigDecimal rateBalance;

    /**
     * 应付金额 = 含税单价*数量
     */
    @TableField(value = "ap_balance", updateStrategy = FieldStrategy.NOT_EMPTY)
    private BigDecimal apBalance;

    /**
     * 发货数量
     */
    @TableField(value = "delivery_qty", updateStrategy = FieldStrategy.NOT_EMPTY)
    private BigDecimal deliveryQty;

    /**
     * 物料状态（1全部发货，2部分发货）
     */
    @TableField(value = "logistics_status", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String logisticsStatus;

    /**
     * 行号
     */
    @TableField(value = "line_no", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String lineNo;

    /**
     * 修改标识
     */
    @TableField(value = "update_flag", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String updateFlag;

}
