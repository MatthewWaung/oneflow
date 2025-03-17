package com.oneflow.prm.service.biz;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用ftl模板应用itext的xmlworker导出pdf
 */
public class OrderToPdfXml {

    /**
     * 使用pdf模板导出表格无法实现动态插入新行，使用ftl或html模板可以解决这个问题，而且html模板很容易修改样式
     *
     * 参考：https://gitee.com/wuliaohaige/pdf-kit/tree/master
     */

    private static final String DEST = "D:/Download/order_file.pdf";
    private static final String HTML = "order_template.ftl";
    private static final String FONT = "C:\\Windows\\Fonts\\simsun.ttc,0";
    private static Configuration freemarkerCfg = null;

    static {
        freemarkerCfg = new Configuration();
        //freemarker的模板目录
        // 这里的路径是存放模板所在的文件夹
//            freemarkerCfg.setDirectoryForTemplateLoading(new File("D:/Download/template/"));
        freemarkerCfg.setClassForTemplateLoading(OrderToPdfXml.class,"/template/");
    }

    public static void main(String[] args) throws IOException, DocumentException {
        Map<String, Object> data = new HashMap<>();
        data.put("orderNo", "B20230412000001");
        data.put("originalOrderNo", "B20230412000001");
        data.put("orderType", "标准订单");
        data.put("salesOrganization", "欧洲销售组织");
        String note = "订单备注：\n" +
                "第二行：\n" +
                "\n" +
                "第三行：\n" +
                "\n";
        data.put("note", note);
        String content = OrderToPdfXml.freeMarkerRender(data, HTML);
        OrderToPdfXml.createPdf(content, DEST);
    }

    public static void createPdf(String content, String dest) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        // step 3
        document.open();
        // step 4
        XMLWorkerFontProvider fontImp = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
        fontImp.register(FONT);    //可传字体所在的文件目录
        XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                new ByteArrayInputStream(content.getBytes()), null,
                StandardCharsets.UTF_8, fontImp);
        // step 5
        document.close();
    }

    /**
     * freemarker渲染html
     */
    public static String freeMarkerRender(Map<String, Object> data, String htmlTmp) {
        Writer out = new StringWriter();
        try {
            // 获取模板,并设置编码方式
            Template template = freemarkerCfg.getTemplate(htmlTmp);
            template.setEncoding("UTF-8");
            // 合并数据模型与模板
            template.process(data, out); //将合并后的数据和模板写入到流中，这里使用的字符流
            out.flush();
            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

}
