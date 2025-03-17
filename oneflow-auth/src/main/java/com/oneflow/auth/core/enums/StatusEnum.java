package com.oneflow.auth.core.enums;

import lombok.Getter;

@Getter
public enum StatusEnum {
    ENABLE("0", "启用"),
    FREEZE("1", "禁用");


    private final String status;
    private final String desc;

    StatusEnum(String status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
