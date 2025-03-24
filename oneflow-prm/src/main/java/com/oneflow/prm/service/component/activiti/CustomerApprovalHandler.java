package com.oneflow.prm.service.component.activiti;

import com.oneflow.prm.core.enums.ActivitiTypeEnums;
import com.oneflow.prm.entity.bo.activiti.ActivitiApproveBO;
import org.springframework.stereotype.Component;

/**
 * 客户审批处理
 */
@Component
public class CustomerApprovalHandler implements ApprovalHandler {

    @Override
    public ActivitiTypeEnums.CatalogEnum getCatalogEnum() {
        return ActivitiTypeEnums.CatalogEnum.CUSTOMER;
    }

    @Override
    public void disposeApproval(ActivitiApproveBO approveBO) {
        // 处理客户流程相关逻辑
    }

}
