package com.oneflow.oms.entity.order;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("oms_order")
@Schema(description = "订单表")
//public class OrderDO extends BaseEntity {
public class OrderDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "订单号")
    @TableField(value = "order_no", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String orderNo;

    @Schema(description = "平台订单号")
    @TableField(value = "platform_order_no", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String platformOrderNo;

    @Schema(description = "订单来源")
    @TableField(value = "order_source", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String orderSource;

    @Schema(description = "发货平台")
    @TableField(value = "delivery_platform", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String deliveryPlatform;

    @Schema(description = "SAP订单号")
    @TableField(value = "sap_order_no", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String sapOrderNo;

    @Schema(description = "订单类型")
    @TableField(value = "order_type", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String orderType;

    @Schema(description = "订单状态")
    @TableField(value = "order_status", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String orderStatus;

    @Schema(description = "店铺编码")
    @TableField(value = "shop_code", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String shopCode;

    @Schema(description = "店铺名称")
    @TableField(value = "shop_name", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String shopName;

    @Schema(description = "销售组织编码")
    @TableField(value = "sales_organization_code", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String salesOrganizationCode;

    @Schema(description = "销售组织名称")
    @TableField(value = "sales_organization_name", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String salesOrganizationName;

    @Schema(description = "销售区域编码")
    @TableField(value = "sales_area_code", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String salesAreaCode;

    @Schema(description = "销售区域名称")
    @TableField(value = "sales_area_name", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String salesAreaName;

    @Schema(description = "付款方式编码")
    @TableField(value = "payment_terms_code", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String paymentTermsCode;

    @Schema(description = "付款方式名称")
    @TableField(value = "payment_terms_name", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String paymentTermsName;

    @Schema(description = "币种编码")
    @TableField(value = "currency_code", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String currencyCode;

    @Schema(description = "币种名称")
    @TableField(value = "currency_name", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String currencyName;

    @Schema(description = "备注")
    @TableField(value = "note", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String note;

}
