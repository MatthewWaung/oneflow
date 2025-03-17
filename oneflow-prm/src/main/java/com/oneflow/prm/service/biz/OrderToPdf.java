package com.oneflow.prm.service.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oneflow.prm.core.pdf.Html2PdfUtil;
import com.oneflow.prm.core.utils.FileUtils;
import com.oneflow.prm.core.utils.SpringUtil;
import com.oneflow.prm.entity.dao.order.OrderDO;
import com.oneflow.prm.mapper.OrderMapper;
import com.oneflow.prm.service.ISysDictDataService;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用ftl模板应用itext的html2pdf导出pdf
 */
public class OrderToPdf {

    private final String fileDir = "/data/template/";
    private String orderId;

    /**
     * 参考：https://gitee.com/dlqx/springboot-code-book中的export-pdf工程、
     * 简书https://www.jianshu.com/p/07e28e2ec562
     *
     * 具体代码见OrderController的/exportDevicePdf
     */

    public OrderToPdf() {
    }

    public OrderToPdf(String id) {
        orderId = id;
    }

    public void createOrderPdf(OutputStream os) throws Exception {
        Map<String, Object> paramData = new HashMap<>();
        OrderDO orderDO = SpringUtil.getBean(OrderMapper.class).selectOne(new LambdaQueryWrapper<OrderDO>().eq(OrderDO::getId, orderId));
        Map orderMap = JSONObject.parseObject(JSONObject.toJSONString(orderDO), Map.class);
        paramData = orderMap;
        paramData.put("feeShareChannel", "");
        paramData.put("returnBalance", "");
        List<Map<String, String>> historyList = new ArrayList<>();
        paramData.put("historyList", historyList);

        // 通过字典配置获取模板文件url，然后下载到当前服务器指定目录，作为Html2PdfUtil获取模板文件的参数
        String fileUrl = SpringUtil.getBean(ISysDictDataService.class).selectDictLabel("template_file_path", "order_standard");
        String templateName = "order_standard.ftl";
        FileUtils.makeDir(new File(fileDir));
        FileUtils.fileUrl(fileUrl, templateName, fileDir);
        String templateContent = Html2PdfUtil.getTemplateContent(templateName, fileDir, paramData);
//        String templateContent = Html2PdfUtil.getTemplateContent("order_standard.ftl", paramData);
        Html2PdfUtil.html2Pdf(templateContent, os);
    }
}
