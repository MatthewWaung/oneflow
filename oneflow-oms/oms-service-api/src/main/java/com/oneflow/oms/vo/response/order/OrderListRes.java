package com.oneflow.oms.vo.response.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "订单列表响应参数")
public class OrderListRes {

    private Long id;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "平台订单号")
    private String platformOrderNo;

    @Schema(description = "订单来源")
    private String orderSource;

    @Schema(description = "发货平台")
    private String deliveryPlatform;

    @Schema(description = "SAP订单号")
    private String sapOrderNo;

    @Schema(description = "订单类型")
    private String orderType;

    @Schema(description = "订单状态")
    private String orderStatus;

    @Schema(description = "店铺编码")
    private String shopCode;

    @Schema(description = "店铺名称")
    private String shopName;

    @Schema(description = "销售组织编码")
    private String salesOrganizationCode;

    @Schema(description = "销售组织名称")
    private String salesOrganizationName;

    @Schema(description = "销售区域编码")
    private String salesAreaCode;

    @Schema(description = "销售区域名称")
    private String salesAreaName;

    @Schema(description = "付款方式编码")
    private String paymentTermsCode;

    @Schema(description = "付款方式名称")
    private String paymentTermsName;

    @Schema(description = "币种编码")
    private String currencyCode;

    @Schema(description = "币种名称")
    private String currencyName;

    @Schema(description = "备注")
    private String note;

}
