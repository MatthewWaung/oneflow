package com.oneflow.prm.service.component.activiti;


import com.oneflow.prm.core.enums.ActivitiTypeEnums;
import com.oneflow.prm.entity.bo.activiti.ActivitiApproveBO;

public interface ApprovalHandler {

    /**
     * 获取审批类型枚举
     *
     * @return
     */
    ActivitiTypeEnums.CatalogEnum getCatalogEnum();

    void disposeApproval(ActivitiApproveBO approveBO);

}
