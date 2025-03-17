package com.oneflow.prm.service.impl;

import com.oneflow.prm.core.common.R;
import com.oneflow.prm.core.exception.CustomException;
import com.oneflow.prm.core.pdf.Html2PdfUtil;
import com.oneflow.prm.entity.dao.order.OrderDO;
import com.oneflow.prm.mapper.OrderMapper;
import com.oneflow.prm.service.IOrderService;
import com.oneflow.prm.service.biz.DataToCnExcel;
import com.oneflow.prm.service.biz.DataToPiPdf;
import com.oneflow.prm.service.biz.OrderToPdf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {

    private OrderMapper orderMapper;

    private Map<String, OrderDO> orderDOMap = new HashMap<>();
    {
        orderDOMap.put("20230420B0000001", new OrderDO());
        orderDOMap.put("20230420B0000002", new OrderDO());
    }


    @Override
    public R exportPiFile(HttpServletResponse response, String id, String fileType) {
        try {
            String fileName = "sales_pi_" + System.currentTimeMillis();
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/pdf");
            OutputStream os = response.getOutputStream();

            OrderDO orderDO = orderMapper.selectById(id);
            new DataToPiPdf(orderDO).createPiPdf(os);
        } catch (Exception e) {
            log.info("导出pi文件异常：", e);
            throw new CustomException("导出pi文件异常");
        }
        return R.ok();
    }

    @Override
    public R exportCnFile(HttpServletResponse response, String id, String fileType) {
        try {
            String fileName = "cn_" + System.currentTimeMillis();
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");  //支持.xlsx格式
            OutputStream os = response.getOutputStream();

            OrderDO orderDO = orderMapper.selectById(id);
            new DataToCnExcel(orderDO).createCnExcel(os);
        } catch (Exception e) {
            log.info("导出cn文件异常：", e);
            throw new CustomException("导出cn文件异常");
        }
        return R.ok();
    }

    @Override
    public R exportDevicePdf(HttpServletResponse response) {
        try {
            HashMap<String, Object> mapData = new HashMap<>();
//            mapData.put("topDeviceList", topoDeviceService.findByList(queryDto));
            List<Map<String, String>> deviceList = new ArrayList<>();
            Map<String, String> map1 = new HashMap<>();
            map1.put("deviceName", "无线终端");
            map1.put("mark", "1");
            map1.put("port", "2010");
            map1.put("ip", "192.168.0.2");
            map1.put("unit", "2");
            Map<String, String> map2 = new HashMap<>();
            map2.put("deviceName", "网络交换机");
            map2.put("mark", "2");
            map2.put("port", "2011");
            map2.put("ip", "192.168.0.3");
            map2.put("unit", "2");
            deviceList.add(map1);
            deviceList.add(map2);

            mapData.put("topDeviceList", deviceList);

//            String templateContent = HtmlUtils.getTemplateContent("/template/device_template.ftl", mapData);
            String templateContent = Html2PdfUtil.getHtmlContent(mapData);
            OutputStream os = response.getOutputStream();
            Html2PdfUtil.html2Pdf(templateContent, os);

            //分页其他操作见http://www.manongjc.com/detail/60-ivxytbnnfzgfxgf.html

        } catch (Exception e) {
            log.error("error occurs when downloading file", e);
            throw new CustomException("导出pdf异常：" + e.getMessage());
        }
        return R.ok();
    }

    @Override
    public R exportOrderPdf(String id, HttpServletResponse response) {
        try {
            String fileName = "order_" + System.currentTimeMillis();
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/pdf");
            OutputStream os = response.getOutputStream();
            new OrderToPdf(id).createOrderPdf(os);
        } catch (Exception e) {
            log.info("导出订单pdf文件异常：", e);
            throw new CustomException("导出订单pdf文件异常");
        }
        return R.ok();
    }




    /**
     * 模拟查询订单
     *
     * @param orderNo
     * @return
     */
    public OrderDO getByOrderNo(String orderNo) {
        return orderDOMap.get(orderNo);
    }

}
