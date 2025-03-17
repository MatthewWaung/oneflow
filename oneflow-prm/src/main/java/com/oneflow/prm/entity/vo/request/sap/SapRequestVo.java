package com.oneflow.prm.entity.vo.request.sap;

import lombok.Data;

import java.util.Map;

@Data
public class SapRequestVo {

    private String url;

    private Map<String, Object> params;

    private Map<String, String> heads;

    private String reqXml;
}
