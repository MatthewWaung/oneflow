package com.oneflow.oms.controller;

import com.oneflow.oms.service.IExportService;
import com.oneflow.oms.vo.request.order.OrderListReq;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Tag(name = "导出管理")
@Slf4j
@RestController
@RequestMapping("/export")
public class ExportController {

    @Resource
    private IExportService exportService;

    @PostMapping("/exportOrder")
    public void exportOrderList(@RequestBody OrderListReq req, HttpServletResponse response) {
        log.info("ExportOrderList start......");

        String fileName = "order_export_" + System.currentTimeMillis();

        // 记录导出记录，包括文件名，导出人，导出时间，导出状态，导出结果

        // 异步导出，这里使用同步示意
        exportService.exportOrderList(req, response);
    }

}
