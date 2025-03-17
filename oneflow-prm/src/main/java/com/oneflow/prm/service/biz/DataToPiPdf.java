package com.oneflow.prm.service.biz;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.oneflow.prm.entity.dao.order.OrderDO;
import com.oneflow.prm.mapper.OrderMapper;
import com.oneflow.prm.mapper.OrderProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class DataToPiPdf {

    public static DataToPiPdf dataToPiPdf;
    private OrderDO orderData;

    /**
     * 此处注入的mapper直接使用会报空指针，因为使用本类DataToPiPdf采用的是new的方式
     * SpringBoot中自己new出来的对象不归spring 容器管。当然不给你自动注入容器中存在的bean了
     * 解决方案有两种：
     * 1、使用@PostConstruct
     * 2、使用SpringUtils.getBean()
     */

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderProductMapper orderProductMapper;

    public DataToPiPdf() {
    }

    public DataToPiPdf(OrderDO orderDO) {
        orderData = orderDO;
    }

    @PostConstruct
    public void init() {
        dataToPiPdf = this;
    }

    public void createPiPdf(OutputStream os) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, os);

        // 页面大小
        Rectangle rectangle = new Rectangle(PageSize.A4);
        // 页面背景颜色
        rectangle.setBackgroundColor(BaseColor.WHITE);
        document.setPageSize(rectangle);
        // 页边距 左，右，上，下
        document.setMargins(2, 2, 40, 40);
        document.open();

        BaseColor color = new BaseColor(221, 235, 247);
        //字体
        Font titleFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD);
        Font newRomanB = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD);
        Font newRoman = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8);
        Font calibri10 = new Font(Font.getFamily("Calibri"), 10);
        BaseFont bfSong = BaseFont.createFont("C:\\Windows\\Fonts\\simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//        BaseFont bfSong = BaseFont.createFont("/usr/share/fonts/my_fonts/simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Font song10B = new Font(bfSong, 10, Font.BOLD);
        Font song8 = new Font(bfSong, 8);

        String imagePath = dataToPiPdf.orderMapper.selectImagePath();
        Image image = Image.getInstance(imagePath);

        String enCountryName = dataToPiPdf.orderMapper.selectEnglishCountryName(orderData.getReceiverCountryId());
        String paymentTerms = dataToPiPdf.orderMapper.selectPaymentTerms(orderData.getPaymentTermsId());

        //表格
        PdfPTable table = new PdfPTable(26);
        table.setSpacingBefore(16f);
        table.setSplitLate(false);

        table.addCell(createCell(image, null, Element.ALIGN_LEFT, 4, 3));

        table.addCell(createCell("Customer:", newRomanB, Element.ALIGN_RIGHT, 4, color));
        table.addCell(createCell("", newRomanB, Element.ALIGN_LEFT, 10));
        table.addCell(createCell("Invoice No.:", newRomanB, Element.ALIGN_RIGHT, 4, color));
        table.addCell(createCell(orderData.getOrderNo(), newRomanB, Element.ALIGN_LEFT, 8));

        document.add(table);
        document.close();
    }


    /**参考：https://blog.csdn.net/weixin_37848710/article/details/89522862*/
    /**------------------------创建表格单元格的方法start----------------------------*/
    /**
     * 创建单元格(指定字体)
     * @param value
     * @param font
     * @return
     */
    public PdfPCell createCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }
    /**
     * 创建单元格（指定字体、水平..）
     * @param value
     * @param font
     * @param align
     * @return
     */
    public PdfPCell createCell(String value, Font font, int align) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }
    /**
     * 创建单元格（指定字体、水平居..、单元格跨x列合并）
     * @param value
     * @param font
     * @param align
     * @param colspan
     * @return
     */
    public PdfPCell createCell(String value, Font font, int align, int colspan) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }
    /**
     * 创建单元格（指定字体、水平居..、单元格跨x列合并、设置背景颜色）
     * @param value
     * @param font
     * @param align
     * @param colspan
     * @param color
     * @return
     */
    public PdfPCell createCell(String value, Font font, int align, int colspan, BaseColor color) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setBackgroundColor(color);
        cell.setColspan(colspan);
        cell.setPadding(2.0f);  //垂直居中对齐方法无效，使用padding设置对齐方式
        cell.setPaddingBottom(5.0f);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }
    /**
     * 创建单元格（插入图片、指定字体、水平居..、单元格跨x列合并）
     * @param image
     * @param font
     * @param align
     * @param colspan
     * @return
     */
    public PdfPCell createCell(Image image, Font font, int align, int colspan, int rowspan) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        cell.setFixedHeight(rowspan * 25f);
        cell.setBorder(0);
        cell.setImage(image);
        return cell;
    }
    /**
     * 创建单元格（指定字体、水平居..、单元格跨x列合并、设置单元格内边距）
     * @param value
     * @param font
     * @param align
     * @param colspan
     * @param borderFlag
     * @return
     */
    public PdfPCell createCell(String value, Font font, int align, int colspan, boolean borderFlag) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        cell.setPadding(3.0f);
        if (!borderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(15.0f);
            cell.setPaddingBottom(8.0f);
        } else if (borderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(0.0f);
            cell.setPaddingBottom(15.0f);
        }
        return cell;
    }
    /**
     * 创建单元格（指定字体、水平..、边框宽度：0表示无边框、内边距）
     * @param value
     * @param font
     * @param align
     * @param borderWidth
     * @param paddingSize
     * @param flag
     * @return
     */
    public PdfPCell createCell(String value, Font font, int align, float[] borderWidth, float[] paddingSize, boolean flag) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setPhrase(new Phrase(value, font));
        cell.setBorderWidthLeft(borderWidth[0]);
        cell.setBorderWidthRight(borderWidth[1]);
        cell.setBorderWidthTop(borderWidth[2]);
        cell.setBorderWidthBottom(borderWidth[3]);
        cell.setPaddingTop(paddingSize[0]);
        cell.setPaddingBottom(paddingSize[1]);
        if (flag) {
            cell.setColspan(2);
        }
        return cell;
    }
    /**------------------------创建表格单元格的方法end----------------------------*/

    /**--------------------------创建表格的方法start------------------- ---------*/
    /**
     * 创建默认列宽，指定列数、水平(居中、右、左)的表格
     * @param colNumber
     * @param align
     * @return
     */
    public PdfPTable createTable(int colNumber, int align) {
        PdfPTable table = new PdfPTable(colNumber);
        try {
            float maxWidth = 520;
            table.setTotalWidth(maxWidth);
            table.setLockedWidth(true);
            table.setHorizontalAlignment(align);
            table.getDefaultCell().setBorder(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }
    /**
     * 创建指定列宽、列数的表格
     * @param widths
     * @return
     */
    public PdfPTable createTable(float[] widths) {
        PdfPTable table = new PdfPTable(widths);
        try {
            float maxWidth = 520;
            table.setTotalWidth(maxWidth);
            table.setLockedWidth(true);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setBorder(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }
    /**
     * 创建空白的表格
     * @return
     */
    public PdfPTable createBlankTable() {
        PdfPTable table = new PdfPTable(1);
        table.getDefaultCell().setBorder(0);
        table.addCell(createCell("", new Font()));
        table.setSpacingAfter(20.0f);
        table.setSpacingBefore(20.0f);
        return table;
    }
    /**--------------------------创建表格的方法end------------------- ---------*/


}
