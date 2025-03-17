package com.oneflow.prm.entity.vo.request.customer;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerReq implements Serializable {
    private static final long serialVersionUID = -5456887162371645026L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 销售组织
     */
    private String salesOrg;
}
