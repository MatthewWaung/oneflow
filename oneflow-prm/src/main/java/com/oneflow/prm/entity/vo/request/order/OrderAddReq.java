package com.oneflow.prm.entity.vo.request.order;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OrderAddReq {

    /**
     * 保存类型
     * 1 保存为草稿
     * 2 保存并提交
     */
    @NotBlank(message = "{field:order:saveType}：{}")
    private String saveType = "1";

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 原始订单号
     */
    private String originalOrderNo;


    /**
     * 1.多地址收货 , 0 仅一个地址
     */
    private String multipleAddress;


    /**
     * 成本中心(免费样品订单才有这个字段)
     */
    private String costCenter;

    /**
     * 成本中心id(免费样品订单才有这个字段)
     */
    private Long costCenterId;

    /**
     * 订单类型
     */
    @NotBlank(message = "{field:order:type}：{}")
    private String type;


    /**
     * 订单来源
     */
    private String orderSource;


    /**
     * 发货地(贷项凭证订单和借项凭证订单不需要)
     */
    private String startPoint;

    /**
     * 发货地id(贷项凭证订单和借项凭证订单不需要)
     */
    private Long startPointId;

    /**
     * 销售组织id
     */
    @NotNull(message = "{field:order:OrgId}：{}")
    private Long salesOrganizationId;

    /**
     * 销售组织
     */
    //@NotBlank(message = "销售组织不能为空")
    private String salesOrganization;

    /**
     * 销售组织编码
     */
    private String salesOrganizationCode;


    /**
     * 销售大区 id
     */
    private Long salesAreaId;

    /**
     * 销售大区名称
     */
    private String salesArea;

}
