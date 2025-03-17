package com.oneflow.prm.entity.vo.request.customer;

import com.oneflow.prm.entity.vo.BaseReq;
import lombok.Data;

@Data
public class CustInfoReq extends BaseReq {

    private String customerId;

    private String customerName;

    /**
     * 销售组织id
     */
    private String salesOrganizationId;

    /**
     * 销售类型id
     */
    private String salesTypeId;

    /**
     * 销售渠道id
     */
    private String channelId;


    //真实业务中不存在这三个参数
    private String name;
    private String salesperson;
    private String contact;

}
