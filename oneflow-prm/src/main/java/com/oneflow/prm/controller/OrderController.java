package com.oneflow.prm.controller;

import com.oneflow.prm.core.common.R;
import com.oneflow.prm.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    IOrderService orderService;

    /**
     * 使用itext5导出pdf文件
     *
     * @param response
     * @param id
     * @param fileType
     * @return
     */
    @GetMapping("/exportPi")
    public R exportPiFile(HttpServletResponse response, @RequestParam("id") String id, @RequestParam("fileType") String fileType) {
        return orderService.exportPiFile(response, id, fileType);
    }

    /**
     * 使用easyExcel导出带有合并单元格的xlsx文件
     *
     * @param response
     * @param id
     * @param fileType
     * @return
     */
    @GetMapping("/exportCn")
    public R exportCnFile(HttpServletResponse response, @RequestParam("id") String id, @RequestParam("fileType") String fileType) {
        return orderService.exportCnFile(response, id, fileType);
    }

    /**
     * 使用ftl模板导出pdf示例
     *
     * @param response
     * @return
     */
    @GetMapping("/exportDevicePdf")
    public R exportDevicePdf(HttpServletResponse response) {
        return orderService.exportDevicePdf(response);
    }

    /**
     * 使用ftl模板应用itext的html2pdf导出pdf
     *
     * @param id
     * @param response
     * @return
     */
    @GetMapping("/exportOrderPdf")
    public R exportOrderPdf(String id, HttpServletResponse response) {
        return orderService.exportOrderPdf(id, response);
    }

}
