package com.oneflow.prm.feign;

import com.oneflow.prm.entity.vo.request.sap.SapRequestVo;

public interface SapRequestService {

    public String requestSap(SapRequestVo reqVo);
}
