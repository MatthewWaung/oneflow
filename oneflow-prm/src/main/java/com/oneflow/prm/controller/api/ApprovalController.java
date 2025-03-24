package com.oneflow.prm.controller.api;

import com.oneflow.prm.core.common.R;
import com.oneflow.prm.core.exception.CustomException;
import com.oneflow.prm.entity.bo.activiti.ActivitiApproveBO;
import com.oneflow.prm.service.IApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Tag(name = "审批数据处理接口，调用可能来自其它服务，如审批流服务或消息队列消费者服务")
@RestController
@RequestMapping("/privateApi/approval")
public class ApprovalController {

    @Resource
    private IApprovalService approvalService;

    @Operation(summary = "处理流程审批数据")
    @PostMapping("/handleActiviti")
    public R<Void> handleActiviti(@RequestBody ActivitiApproveBO approveBO) {
        try {
            approvalService.handleActiviti(approveBO);
            return R.ok();
        } catch (CustomException ce) {
            return R.fail(ce.getDefaultMessage());
        } catch (Exception e) {
            log.info("handleActiviti error: ", e);
            return R.fail(e.getMessage());
        }
    }


}
