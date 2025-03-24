package com.oneflow.prm.entity.bo.activiti;

import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class ActivitiApproveBO {

    @NotBlank(message = "业务标识不能为空")
    @Schema(description = "流程业务标识，如：(type + : + businessKey + : + orderDO.getOrderNo())")
    private String businessKey;

    @NotBlank(message = "流程实例ID不能为空")
    @Schema(description = "流程实例ID")
    private String processInstanceId;

    @NotBlank(message = "任务ID不能为空")
    @Schema(description = "任务ID")
    private String taskId;

    /**
     * @see com.ecoflow.prm.core.enums.ActivitiMsgTypeEnum
     */
    @Schema(description = "流程任务类型")
    private String activitiMsgType;

    /**
     * @see com.ecoflow.prm.core.constant.TaskDefinitionKeyConstants
     */
    @NotBlank(message = "任务节点标识不能为空")
    @Schema(description = "任务节点标识")
    private String taskDefinitionKey;

    @NotBlank(message = "审批人不能为空")
    @Schema(description = "审批人（工号）")
    private String approver;

    @Schema(description = "审批时间")
    private LocalDateTime approveTime;

    @Schema(description = "审批意见")
    private String message;

    @Schema(description = "流程变量，")
    private JSONObject variables;

    //=======================================解析参数======================

    /**
     * 获取业务类型
     * @return {@link String }
     */
    public String getBusinessType() {
        return businessKey.split(":")[0];
    }

    /**
     * 获取业务ID
     * @return {@link String }
     */
    public String getBusinessId() {
        return businessKey.split(":")[1];
    }

    /**
     * 获取业务编号
     * @return {@link String }
     */
    public String getBusinessNo() {
        return businessKey.split(":")[2];
    }




}
