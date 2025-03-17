package com.oneflow.prm.service.biz;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.oneflow.prm.core.pdf.PdfTable;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * 使用itexpdf代码实现pdf导出，版本为iText5
 */
public class OrderToPdfFile {

    BaseFont bfSong = BaseFont.createFont("C:\\Windows\\Fonts\\simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
    //    BaseFont bfSong = BaseFont.createFont("/usr/share/fonts/my_fonts/simsun.ttc,0", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
    Font mySong8 = new Font(bfSong, 8);

    public OrderToPdfFile() throws DocumentException, IOException {
    }

    public static void main(String[] args) throws DocumentException, IOException {
        OrderToPdfFile orderToPdf = new OrderToPdfFile();
        orderToPdf.createPdfFile();
    }


    public void createPdfFile() throws IOException, DocumentException {

        File file = new File("D:\\Download\\order202304.pdf");
//        file.getParentFile().mkdirs();
//        OutputStream outputStream = new FileOutputStream(file);
        OutputStream os = Files.newOutputStream(file.toPath());

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

        //参照StringBuilder的append方法，实现对addCell的链式调用
        PdfTable table = new PdfTable(20);
        table.appendCell(createCell("订单编号", 3)).appendCell(createCell("SIT20230412B0000004", 7)).appendCell(createCell("原始订单编号", 3)).appendCell(createCell("SIT20230412B0000004", 7))
                .appendCell(createCell("订单类型", 3)).appendCell(createCell("标准订单", 7)).appendCell(createCell("销售组织", 3)).appendCell(createCell("欧洲正浩销售组织", 7));

//        PdfPTable table = new PdfPTable(20);
//        table.setSpacingBefore(16f);
//        table.setSplitLate(false);
//
//        table.addCell(createCell("订单编号", 3));
//        table.addCell(createCell("SIT20230412B0000004", 7));
//        table.addCell(createCell("原始订单编号", 3));
//        table.addCell(createCell("SIT20230412B0000004", 7));

//        PdfPTable table1 = PdfTable.table;
        PdfPTable pdfPTable = table.getPdfPTable();
        document.add(pdfPTable);
        document.close();
    }

    public PdfPCell createCell(String value, int colspan) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);     //垂直居中
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);   //水平居中
        cell.setColspan(colspan);
        cell.setPaddingTop(2.0f);
        cell.setPaddingBottom(5.0f);
        cell.setPhrase(new Phrase(value, mySong8));
        return cell;
    }

    public PdfPCell createCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);     //垂直居中
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);   //水平居中
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }

}
