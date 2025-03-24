package com.oneflow.prm.service.component.activiti;

import com.oneflow.prm.core.enums.ActivitiTypeEnums;
import com.oneflow.prm.entity.bo.activiti.ActivitiApproveBO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 订单审批处理
 */
@Component
public class OrderApprovalHandler implements ApprovalHandler {

//    @Resource
//    private OrderIdentifySaveService orderIdentifyService;

    @Override
    public ActivitiTypeEnums.CatalogEnum getCatalogEnum() {
        return ActivitiTypeEnums.CatalogEnum.ORDER;
    }

    @Override
    public void disposeApproval(ActivitiApproveBO approveBO) {

        // 处理订单流程相关的逻辑
//        orderIdentifyService.saveOrderIdentifyFromActiviti(approveBO);
    }

}
