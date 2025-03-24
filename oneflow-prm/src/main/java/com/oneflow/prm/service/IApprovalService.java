package com.oneflow.prm.service;

import com.oneflow.prm.entity.bo.activiti.ActivitiApproveBO;

public interface IApprovalService {

    /**
     * 处理流程数据
     *
     * @param approveBO
     */
    void handleActiviti(ActivitiApproveBO approveBO);

}
