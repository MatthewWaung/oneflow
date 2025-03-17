package com.oneflow.prm.core.pdf;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.layout.font.FontProvider;
import com.oneflow.prm.core.utils.ApplicationContextUtil;
import com.oneflow.prm.service.impl.OrderServiceImpl;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class Html2PdfUtil {

    @Resource
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * @return
     * @throws Exception
     */
    public static String getTemplateDirectory() {
        ClassLoader classLoader = Html2PdfUtil.class.getClassLoader();
        URL resource = classLoader.getResource("templates");
        try {
            return Objects.requireNonNull(resource).toURI().getPath();
        } catch (URISyntaxException e) {
            log.error("获取模板文件夹失败：", e);
        }
        return null;
    }

    /**
     * 获取模板内容
     *
     * @param templateName 模板文件名
     * @param paramMap     模板参数
     * @return
     * @throws Exception
     */
    public static String getTemplateContent(String templateName, Map<String, Object> paramMap) throws Exception {
        Configuration config = ApplicationContextUtil.getBean(FreeMarkerConfigurer.class).getConfiguration();
        //注入的类在static中无法使用
//        Configuration config = freeMarkerConfigurer.getConfiguration();
        config.setDefaultEncoding("UTF-8");
//        config.setClassForTemplateLoading();
        Template template = config.getTemplate(templateName, "UTF-8");
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, paramMap);
    }

    /**
     * 获取模板内容
     *
     * @param templateName 模板文件名
     * @param fileDir 模板文件目录
     * @param paramMap     模板参数
     * @return
     * @throws Exception
     */
    public static String getTemplateContent(String templateName, String fileDir, Map<String, Object> paramMap) throws Exception {
        Configuration config = ApplicationContextUtil.getBean(FreeMarkerConfigurer.class).getConfiguration();
        //注入的类在static中无法使用
//        Configuration config = freeMarkerConfigurer.getConfiguration();
        config.setDefaultEncoding("UTF-8");
//        config.setClassForTemplateLoading(Html2PdfUtil.class, "/template/");
//        config.setDirectoryForTemplateLoading(new File("D:/template/"));
        config.setDirectoryForTemplateLoading(new File(fileDir));
        Template template = config.getTemplate(templateName, "UTF-8");
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, paramMap);
    }

    /**
     * 根据模板获得html
     */
    public static String getHtmlContent(Map data) {
        Writer out = new StringWriter();
        try {
            Configuration freemarkerCfg = ApplicationContextUtil.getBean(FreeMarkerConfigurer.class).getConfiguration();
            //注入的类在static中无法使用
//            Configuration freemarkerCfg = freeMarkerConfigurer.getConfiguration();
            //设置模板文件所在的目录，resources目录用setClassForTemplateLoading，任意目录用setDirectoryForTemplateLoading
            freemarkerCfg.setClassForTemplateLoading(OrderServiceImpl.class, "/template/");
//            freemarkerCfg.setDirectoryForTemplateLoading(new File("D:/Download/template/"));
            Template template = freemarkerCfg.getTemplate("device_template.ftl");
            // 合并数据模型与模板, 和上面getTemplateContent方法中processTemplateIntoString()方法相同
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

    /**
     * HTML 转 PDF
     *
     * @param content html内容
     * @param outPath 输出pdf路径
     * @return 是否创建成功
     */
    public static boolean html2Pdf(String content, String outPath) {
        try {
            ConverterProperties converterProperties = new ConverterProperties();
            converterProperties.setCharset("UTF-8");
            FontProvider fontProvider = new FontProvider();
            fontProvider.addSystemFonts();
            converterProperties.setFontProvider(fontProvider);
            HtmlConverter.convertToPdf(content, new FileOutputStream(outPath), converterProperties);
        } catch (Exception e) {
            log.error("生成模板内容失败：", e);
            return false;
        }
        return true;
    }

    /**
     * HTML 转 PDF
     *
     * @param content html内容
     * @return PDF字节数组
     */
    public static ByteArrayOutputStream html2Pdf(String content) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ConverterProperties converterProperties = new ConverterProperties();
            converterProperties.setCharset("UTF-8");
            FontProvider fontProvider = new FontProvider();
            fontProvider.addSystemFonts();
            converterProperties.setFontProvider(fontProvider);
            HtmlConverter.convertToPdf(content, outputStream, converterProperties);
        } catch (Exception e) {
            log.error("生成 PDF 失败：", e);
        }
        return outputStream;
    }

    /**
     * HTML 转 PDF
     *
     * @param content
     * @param outputStream
     */
    public static void html2Pdf(String content, OutputStream outputStream) {
        try {
            ConverterProperties converterProperties = new ConverterProperties();
            converterProperties.setCharset("UTF-8");
            FontProvider fontProvider = new FontProvider();
            String fontPath = Html2PdfUtil.class.getResource("/fonts/").getPath();
//            String fontPath = "usr/share/fonts/my_fonts/";
            //通过字体目录设置字体
            fontProvider.addDirectory(fontPath);
//            fontProvider.addSystemFonts();
            converterProperties.setFontProvider(fontProvider);
            HtmlConverter.convertToPdf(content, outputStream, converterProperties);
        } catch (Exception e) {
            log.error("生成 PDF 失败：", e);
        }
    }

}
