package com.oneflow.prm.entity.dao.customer;

import com.baomidou.mybatisplus.annotation.*;
import com.oneflow.prm.entity.dao.BaseAutoFillModel;
import lombok.Data;

@Data
@TableName("prm_customer_sales_view")
public class SalesViewDO extends BaseAutoFillModel<SalesViewDO> {
    private static final long serialVersionUID = -6313379997697109200L;
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
     * 客户性质名称
     */
    @TableField(value = "nature",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String nature;

    /**
     * 客户性质id
     */
    @TableField(value = "nature_id",updateStrategy= FieldStrategy.NOT_EMPTY)
    private Long natureId;

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
     * 状态：0-启用，1-冻结
     */
    @TableField(value = "status",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String status;

}
