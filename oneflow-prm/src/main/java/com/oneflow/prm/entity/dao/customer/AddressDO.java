package com.oneflow.prm.entity.dao.customer;

import com.baomidou.mybatisplus.annotation.*;
import com.oneflow.prm.entity.dao.BaseAutoFillModel;
import lombok.Data;

@Data
@TableName("prm_customer_address")
public class AddressDO extends BaseAutoFillModel<AddressDO> {

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
     * 国家
     */
    @TableField(value = "country",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String country;

    /**
     * 国家id
     */
    @TableField(value = "country_id",updateStrategy= FieldStrategy.NOT_EMPTY)
    private Long countryId;

    /**
     * 省/州
     */
    @TableField(value = "province",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String province;

    /**
     * 省/州id
     */
    @TableField(value = "province_id",updateStrategy= FieldStrategy.NOT_EMPTY)
    private Long provinceId;

    /**
     * 城市
     */
    @TableField(value = "city",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String city;

    /**
     * 城市id
     */
    @TableField(value = "city_id",updateStrategy= FieldStrategy.NOT_EMPTY)
    private Long cityId;

    /**
     * 详细地址
     */
    @TableField(value = "detail_address",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String detailAddress;

    /**
     * 状态：0-启用，1-冻结
     */
    @TableField(value = "status",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String status;

}
