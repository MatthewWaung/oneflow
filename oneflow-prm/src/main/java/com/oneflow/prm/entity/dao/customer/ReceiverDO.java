package com.oneflow.prm.entity.dao.customer;

import com.baomidou.mybatisplus.annotation.*;
import com.oneflow.prm.entity.dao.BaseAutoFillModel;
import lombok.Data;

@Data
@TableName("prm_customer_receiver")
public class ReceiverDO extends BaseAutoFillModel<ReceiverDO> {

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
     * 收货人
     */
    @TableField(value = "receiver",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String receiver;

    /**
     * 联系方式
     */
    @TableField(value = "contact_way",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String contactWay;

    /**
     * 收货人邮件地址
     */
    @TableField(value = "email_address",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String emailAddress;

    /**
     * 状态：0-启用，1-冻结
     */
    @TableField(value = "status",updateStrategy= FieldStrategy.NOT_EMPTY)
    private String status;

}
