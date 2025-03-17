package com.oneflow.prm.core.pdf;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * 参照StringBuilder的append方法，实现对addCell的链式调用
 */
public class PdfTable extends PdfPTable {

    public static PdfPTable table;

    public PdfTable(int numColumns) {
        PdfPTable table = new PdfTable(numColumns);
        this.table = table;
    }

    public PdfTable appendCell(PdfPCell cell) {
        table.addCell(cell);
        return this;
    }

    public PdfPTable getPdfPTable() {
        return table;
    }
}
