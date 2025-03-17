package com.oneflow.prm.entity.dao.customer;

import com.baomidou.mybatisplus.annotation.*;
import com.oneflow.prm.entity.dao.BaseAutoFillModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Mybatis-Plus默认将pojo类名当做表名，如果类名和表名不一致，可以使用注解
 */
@Data
@Accessors(chain = true)
@TableName("prm_customer")
public class CustomerDO extends BaseAutoFillModel<CustomerDO> {
    private static final long serialVersionUID = 2392436786974860592L;

    /**
     * 主键
     * type = IdType.AUTO  表示自增
     * type = IdType.INPUT  表示自行输入
     * type = IdType.ID_WORKER   表示分布式全局唯一ID，长整数类型，默认类型
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 客户名称
     */
    @TableField(value = "customer_name", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String customerName;

    /**
     * 客户简称
     */
    @TableField(value = "customer_short_name", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String customerShortName;

    /**
     * 国家
     */
    @TableField(value = "country", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String country;

    /**
     * 国家id
     */
    @TableField(value = "country_id", updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long countryId;

    /**
     * 省/州
     */
    @TableField(value = "province", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String province;

    /**
     * 省/州id
     */
    @TableField(value = "province_id", updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long provinceId;

    /**
     * 城市
     */
    @TableField(value = "city", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String city;

    /**
     * 城市id
     */
    @TableField(value = "city_id", updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long cityId;

    /**
     * 详细地址
     */
    @TableField(value = "detail_address", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String detailAddress;

    /**
     * 客户电话
     */
    @TableField(value = "customer_phone", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String customerPhone;

    /**
     * 客户邮件地址
     */
    @TableField(value = "customer_email", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String customerEmail;

    /**
     * 客户联系人
     */
    @TableField(value = "customer_contact", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String customerContact;

    /**
     * 税号
     */
    @TableField(value = "tax_no", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String taxNo;

    /**
     * 银行国家
     */
    @TableField(value = "bank_country", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String bankCountry;

    /**
     * 银行代码
     */
    @TableField(value = "bank_code", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String bankCode;

    /**
     * 开户行名称
     */
    @TableField(value = "open_bank_name", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String openBankName;

    /**
     * 银行账号
     */
    @TableField(value = "bank_account", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String bankAccount;

    /**
     * 账户持有人
     */
    @TableField(value = "bank_account_name", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String bankAccountName;

    /**
     * 状态：0-启用，1-冻结
     */
    @TableField(value = "status", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String status;

    /**
     * 销售视图
     */
    @TableField(exist = false)
    private List<SalesViewDO> salesViewList;

    /**
     * 业务员
     */
    @TableField(exist = false)
    private List<SalespersonDO> salespersonList;

    /**
     * 收货人
     */
    @TableField(exist = false)
    private List<ReceiverDO> receiverList;

    /**
     * 收货地址
     */
    @TableField(exist = false)
    private List<AddressDO> addressList;

    /**
     * 信贷视图
     */
    @TableField(exist = false)
    private BalanceLimitDO balanceLimit;

    //插入数据时，自动填充时间，同时需要配置MetaObjectHandler定义填充的是什么时间
//    @TableField(fill = FieldFill.INSERT_UPDATE)
//    private String createTime;

    //修改数据时，自动填充时间，同时需要配置MetaObjectHandler定义填充的是什么时间
//    @TableField(fill = FieldFill.UPDATE)
//    private String updateTime;

}
