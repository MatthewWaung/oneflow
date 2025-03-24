package com.oneflow.prm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.oneflow.prm.core.enums.ActivitiTypeEnums;
import com.oneflow.prm.core.exception.CustomException;
import com.oneflow.prm.entity.bo.activiti.ActivitiApproveBO;
import com.oneflow.prm.service.IApprovalService;
import com.oneflow.prm.service.component.activiti.ApprovalFactory;
import com.oneflow.prm.service.component.activiti.ApprovalHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
@Service
public class ApprovalServiceImpl implements IApprovalService {

    /**
     * 使用工厂模式和策略模式，ApprovalServiceImpl 类实现了高内聚低耦合的设计，提高了代码的可维护性和可扩展性。具体好处包括：
     *  + 解耦：客户端代码不需要知道具体的实现类，只需要知道接口。
     *  + 扩展性：新增审批处理器和策略时，只需添加新的类和逻辑，无需修改现有代码。
     *  + 灵活性：不同的审批逻辑可以独立变化，互不影响。
     *  + 复用性：不同的审批逻辑可以被多个客户端复用。
     */

    @Resource
    private ApprovalFactory approvalFactory;    // 使用工厂模式获取具体审批处理器

    @Override
    public void handleActiviti(ActivitiApproveBO approveBO) {
        log.info("IApprovalService handleActiviti activitiApproveBO: {}", JSONObject.toJSONString(approveBO));

        // 校验参数
        checkParams(approveBO);

        // 获取流程类型枚举
        ActivitiTypeEnums.CatalogEnum catalogEnum = ActivitiTypeEnums.CatalogEnum.getCatalogByActivitiType(getActivitiType(approveBO.getBusinessKey()));

        /**
         * ApprovalHandler 是一个策略接口，定义了 disposeApproval 方法。
         * 不同的审批处理器实现了 ApprovalHandler 接口，提供了不同的审批逻辑。
         * ApprovalFactory 根据不同的 catalogEnum 返回不同的 ApprovalHandler 实例。
         */

        // 获取具体流程类别审批处理器
        ApprovalHandler approvalHandler = approvalFactory.getApprovalHandler(catalogEnum);
        if (Objects.nonNull(approvalHandler)) {
            approvalHandler.disposeApproval(approveBO);
        }

    }

    private void checkParams(ActivitiApproveBO approveBO) {
        String businessKey = approveBO.getBusinessKey();
        if (StringUtils.isEmpty(businessKey)) {
            throw new CustomException("businessKey不能为空");
        }

        // 校验businessKey参数
        String[] businessKeyArr = approveBO.getBusinessKey().split(":");
        if (businessKeyArr.length < 2) {
            throw new CustomException("businessKey格式错误");
        }
        if (!StringUtils.isNumeric(businessKeyArr[1])) {
            throw new CustomException("businessId格式错误");
        }
    }

    private String getActivitiType(String businessKey) {
        return businessKey.split(":")[0];
    }

}
