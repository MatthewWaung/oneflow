package com.oneflow.prm.entity.vo.request.customer;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustStatusReq implements Serializable {
    private static final long serialVersionUID = 2844490012894354068L;

    private Long id;

    private String status;
}
