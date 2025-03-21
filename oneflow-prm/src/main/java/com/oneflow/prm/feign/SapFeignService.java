package com.oneflow.prm.feign;

import com.oneflow.prm.entity.vo.request.sap.SapRequestVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(
    name = "oneflow-bpm-sap",
    contextId = "sap-common-feign"
)
public interface SapFeignService {
    String api = "/api/sap/";

    @RequestMapping(
        value = {"/api/sap/v1/request"},
        method = {RequestMethod.POST}
    )
    String requestSap(@RequestBody SapRequestVo var1) throws Exception;
}
