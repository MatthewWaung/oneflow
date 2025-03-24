package com.oneflow.oms.vo.request.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "订单修改请求参数")
public class OrderModifyReq {

    @Schema(description = "订单ID")
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
    private String salesAreaCode;

}
