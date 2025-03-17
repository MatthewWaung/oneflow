package com.oneflow.prm.entity.dao.order;

import com.baomidou.mybatisplus.annotation.*;
import com.oneflow.prm.entity.dao.BaseAutoFillModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@TableName("prm_order")
public class OrderDO extends BaseAutoFillModel<OrderDO> {

    private static final long serialVersionUID = -6016840684077541728L;
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号
     */
    @TableField(value = "order_no", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String orderNo;

    /**
     * 原始订单编号
     */
    @TableField(value = "original_order_no", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String originalOrderNo;

    /**
     * SAP订单号
     */
    @TableField(value = "sales_order_no", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String salesOrderNo;

    /**
     * 三易返回的订单编号
     */
    @TableField(value = "delivery_order_no", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String deliveryOrderNo;

    /**
     * 采购订单编号
     */
    @TableField(value = "purchase_order_no", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String purchaseOrderNo;

    /**
     * 流程状态
     */
    @TableField(value = "status", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String status;

    /**
     * 订单状态
     */
    @TableField(value = "order_status", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String orderStatus;

    /**
     * 物流状态
     */
    @TableField(value = "logistics_status", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String logisticsStatus;

    /**
     * 订单类型
     */
    @TableField(value = "type", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String type;

    /**
     * 订单来源
     */
    @TableField(value = "order_source", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String orderSource;

    /**
     * 成本中心id
     */
    @TableField(value = "cost_center_id", updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long costCenterId;

    /**
     * 成本中心
     */
    @TableField(value = "cost_center", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String costCenter;

    /**
     * 发货地id
     */
    @TableField(value = "start_point_id", updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long startPointId;

    /**
     * 发货地
     */
    @TableField(value = "start_point", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String startPoint;

    /**
     * 销售组织
     */
    @TableField(value = "sales_organization",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String salesOrganization;

    /**
     * 销售组织id
     */
    @TableField(value = "sales_organization_id",updateStrategy= FieldStrategy.NOT_EMPTY)
    private Long salesOrganizationId;

    /**
     * 销售大区
     */
    @TableField(value = "sales_area",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String salesArea;

    /**
     * 销售大区id
     */
    @TableField(value = "sales_area_id",updateStrategy= FieldStrategy.NOT_EMPTY)
    private Long salesAreaId;

    /**
     * 销售模式
     */
    @TableField(value = "sales_type",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String salesType;

    /**
     * 销售模式id
     */
    @TableField(value = "sales_type_id",updateStrategy= FieldStrategy.NOT_EMPTY)
    private Long salesTypeId;

    /**
     * 一级渠道
     */
    @TableField(value = "channel",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String channel;

    /**
     * 一级渠道id
     */
    @TableField(value = "channel_id",updateStrategy= FieldStrategy.NOT_EMPTY)
    private Long channelId;

    /**
     * 二级渠道
     */
    @TableField(value = "second_channel",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String secondChannel;

    /**
     * 二级渠道id
     */
    @TableField(value = "second_channel_id",updateStrategy= FieldStrategy.NOT_EMPTY)
    private Long secondChannelId;

    /**
     * 币别
     */
    @TableField(value = "currency",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String currency;

    /**
     * 币别id
     */
    @TableField(value = "currency_id",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String currencyId;

    /**
     * 付款条件
     */
    @TableField(value = "payment_terms",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String paymentTerms;

    /**
     * 付款条件id
     */
    @TableField(value = "payment_terms_id",updateStrategy= FieldStrategy.NOT_EMPTY)
    private Long paymentTermsId;

    /**
     * 国际贸易条款
     */
    @TableField(value = "international_trade_terms",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String internationalTradeTerms;

    /**
     * 国际贸易条款id
     */
    @TableField(value = "international_trade_terms_id",updateStrategy= FieldStrategy.NOT_EMPTY)
    private Long internationalTradeTermsId;

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
     * 收货人
     */
    @TableField(value = "receiver",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String receiver;

    /**
     * 联系方式
     */
    @TableField(value = "receiver_contact",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String receiverContact;

    /**
     * 收货人邮件地址
     */
    @TableField(value = "receiver_email",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String receiverEmail;

    /**
     * 国家
     */
    @TableField(value = "receiver_country",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String receiverCountry;

    /**
     * 国家id
     */
    @TableField(value = "receiver_country_id",updateStrategy= FieldStrategy.NOT_EMPTY)
    private Long receiverCountryId;

    /**
     * 省/州
     */
    @TableField(value = "receiver_province",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String receiverProvince;

    /**
     * 省/州id
     */
    @TableField(value = "receiver_province_id",updateStrategy= FieldStrategy.NOT_EMPTY)
    private Long receiverProvinceId;

    /**
     * 城市
     */
    @TableField(value = "receiver_city",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String receiverCity;

    /**
     * 城市id
     */
    @TableField(value = "receiver_city_id",updateStrategy= FieldStrategy.NOT_EMPTY)
    private Long receiverCityId;

    /**
     * 详细地址
     */
    @TableField(value = "receiver_detail_address",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String receiverDetailAddress;

    /**
     * 业务员工号
     */
    @TableField(value = "salesperson_no",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String salespersonNo;

    /**
     * 业务员名称
     */
    @TableField(value = "salesperson",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String salesperson;

    /**
     * 订单备注
     */
    @TableField(value = "order_note",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String orderNote;

    /**
     * 清关备注
     */
    @TableField(value = "clearance_note",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String clearanceNote;

    /**
     * 发货备注
     */
    @TableField(value = "delivery_note",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String deliveryNote;

    /**
     * 改单原因
     */
    @TableField(value = "order_change_reason",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String orderChangeReason;

    /**
     * 订单总金额
     */
    @TableField(value = "order_balance",updateStrategy= FieldStrategy.NOT_EMPTY)
    private BigDecimal orderBalance;

    /**
     * 发货平台id
     */
    @TableField(value = "delivery_platform_id",updateStrategy= FieldStrategy.NOT_EMPTY)
    private Long deliveryPlatformId;

    /**
     * 发货平台
     */
    @TableField(value = "delivery_platform",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String deliveryPlatform;

    /**
     * 交易路线id
     */
    @TableField(value = "trade_route_id",updateStrategy= FieldStrategy.NOT_EMPTY)
    private Long tradeRouteId;

    /**
     * 交易路线
     */
    @TableField(value = "trade_route",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String tradeRoute;

}
