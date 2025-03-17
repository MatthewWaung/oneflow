package com.oneflow.auth.entity;

import lombok.Data;

@Data
public class SsoAuthVO {

    /**
     * 客户端ID
     */
    private String client_id;

    /**
     * 客户端秘钥
     */
    private String client_secret;

    /**
     * 授权码
     */
    private String code;

    /**
     * 重定向URI
     */
    private String redirect_uri;
}
