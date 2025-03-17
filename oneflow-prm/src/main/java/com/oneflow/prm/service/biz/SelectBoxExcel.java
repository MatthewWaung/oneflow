package com.oneflow.prm.service.biz;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileOutputStream;

@Slf4j
public class SelectBoxExcel {

    /**
     * 实现导出带下拉选项框联动的excel模板
     */

    public static void main(String[] args) {
        Cascade1();
    }

    public static void Cascade1() {
        // 创建一个excel
        XSSFWorkbook book = new XSSFWorkbook();

        // 创建需要用户填写的sheet
        XSSFSheet sheetPro = book.createSheet("一件代发导入模板");

        Row row0 = sheetPro.createRow(0);

        Cell ztCell = row0.createCell(0);
        ztCell.setCellValue("订单类型");
        sheetPro = setCellStyle(sheetPro, book, ztCell, 0, "订单类型", true);

        Cell ztCell1 = row0.createCell(1);
        ztCell1.setCellValue("销售组织");
        sheetPro = setCellStyle(sheetPro, book, ztCell1, 1, "销售组织", true);

        Cell ztCell2 = row0.createCell(2);
        ztCell2.setCellValue("客户名称");
        sheetPro = setCellStyle(sheetPro, book, ztCell2, 2, "客户名称", true);

        row0.createCell(3).setCellValue("收货人");
        row0.createCell(4).setCellValue("联系方式");
        row0.createCell(5).setCellValue("收货人邮件地址");
        row0.createCell(6).setCellValue("收货国家");
        row0.createCell(7).setCellValue("城市");
        row0.createCell(8).setCellValue("邮编");

        Cell ztCell9 = row0.createCell(9);
        ztCell9.setCellValue("详细地址");
        sheetPro = setCellStyle(sheetPro, book, ztCell9, 9, "详细地址", true);

        row0.createCell(10).setCellValue("银行流水");
        row0.createCell(11).setCellValue("运输方式");
        row0.createCell(12).setCellValue("发货国家");
        row0.createCell(13).setCellValue("整单折扣率");
        row0.createCell(14).setCellValue("采购订单号");
        row0.createCell(15).setCellValue("发货备注");
        row0.createCell(16).setCellValue("清关备注");

        Cell ztCell17 = row0.createCell(17);
        ztCell17.setCellValue("物料编码");
        sheetPro = setCellStyle(sheetPro, book, ztCell17, 17, "物料编码", true);

        Cell ztCell118 = row0.createCell(18);
        ztCell118.setCellValue("单价");
        sheetPro = setCellStyle(sheetPro, book, ztCell118, 18, "单价", true);


        Cell ztCell119 = row0.createCell(19);
        ztCell119.setCellValue("数量");
        sheetPro = setCellStyle(sheetPro, book, ztCell119, 19, "数量", true);

        Cell ztCell120 = row0.createCell(20);
        ztCell120.setCellValue("税率");
        sheetPro = setCellStyle(sheetPro, book, ztCell120, 20, "税率", true);


        //得到第一级省名称，放在列表里
        String[] orderTypes = new String[]{"一件代发订单"};
        String[] saleOrgnations = new String[]{"正浩创新销售组织", "正浩智造销售组织", "日本正浩销售组织"};
        String[] customers = new String[]{"测试客户1", "测试客户2", "测试客户3"};

        //创建一个专门用来存放数据信息的隐藏sheet页 因此也不能在现实页之前创建，否则无法隐藏。
        Sheet hideSheet = book.createSheet("datasource");

        hideSheet = saveDataToHiSheet(hideSheet, 0, "订单类型", orderTypes);
        hideSheet = saveDataToHiSheet(hideSheet, 1, "销售组织类型", saleOrgnations);
        hideSheet = saveDataToHiSheet(hideSheet, 2, "客户名称", customers);

        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheetPro);
        sheetPro = check(sheetPro, dvHelper, orderTypes, 0, 0);
        sheetPro = check(sheetPro, dvHelper, saleOrgnations, 1, 1);
        sheetPro = check(sheetPro, dvHelper, customers, 2, 2);

        //对前200行设置有效性
        for (int i = 2; i < 200; i++) {
            setDataValidation("A", sheetPro, i, 1);
            setDataValidation("B", sheetPro, i, 2);
            setDataValidation("C", sheetPro, i, 3);
        }

        FileOutputStream os = null;
        try {
            os = new FileOutputStream("D:/" + System.currentTimeMillis() + ".xlsx");
            book.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
        }
    }


    public static XSSFSheet setCellStyle(XSSFSheet sheetPro, XSSFWorkbook book, Cell ztCell, Integer columnIndex, String name, boolean status) {
        XSSFCellStyle ztStyle = (XSSFCellStyle) book.createCellStyle();
        // 创建字体对象
        Font ztFont = book.createFont();
        if (status) {
            ztFont.setColor(Font.COLOR_RED);            // 将字体设置为“红色”
        }
        ztFont.setFontHeightInPoints((short) 13);    // 将字体大小设置为18px
        ztStyle.setFont(ztFont);                    // 将字体应用到样式上面
        ztCell.setCellStyle(ztStyle);
        ztStyle.setAlignment(HorizontalAlignment.CENTER);
        sheetPro.setColumnWidth(columnIndex, (name.getBytes().length) * 256);
        return sheetPro;
    }

    public static Sheet saveDataToHiSheet(Sheet hideSheet, Integer row, String name, String[] data) {
        //设置第几行,存销数据源
        Row dataRow = hideSheet.createRow(row);
        dataRow.createCell(0).setCellValue(name);
        for (int i = 0; i < data.length; i++) {
            Cell dataCell = dataRow.createCell(i + 1);
            dataCell.setCellValue(data[i]);
        }
        return hideSheet;
    }


    /**
     * 将数据放入到sheet 下拉中
     *
     * @param sheetPro
     * @param dvHelper
     * @param saleOrgnations
     * @param firstCol
     * @param lastCol
     * @return
     */
    public static XSSFSheet check(XSSFSheet sheetPro, XSSFDataValidationHelper dvHelper, String[] saleOrgnations, Integer firstCol, Integer lastCol) {
        DataValidationConstraint saleOrgTypeConstraint = dvHelper.createExplicitListConstraint(saleOrgnations);
        // 四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList saleOrgRangeAddressList = new CellRangeAddressList(1, 200, firstCol, lastCol);
        DataValidation saleDataValidation = dvHelper.createValidation(saleOrgTypeConstraint, saleOrgRangeAddressList);
        //验证
        saleDataValidation.createErrorBox("error", "请选择正确的数据");
        saleDataValidation.setShowErrorBox(true);
        saleDataValidation.setSuppressDropDownArrow(true);
        sheetPro.addValidationData(saleDataValidation);
        return sheetPro;
    }


    /**
     * 设置有效性
     *
     * @param offset 主影响单元格所在列，即此单元格由哪个单元格影响联动
     * @param sheet
     * @param rowNum 行数
     * @param colNum 列数
     */
    public static void setDataValidation(String offset, XSSFSheet sheet, int rowNum, int colNum) {
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
        DataValidation data_validation_list;
        data_validation_list = getDataValidationByFormula(
                "INDIRECT($" + offset + (rowNum) + ")", rowNum, colNum, dvHelper);
        sheet.addValidationData(data_validation_list);
    }

    /**
     * 加载下拉列表内容
     *
     * @param formulaString
     * @param naturalRowIndex
     * @param naturalColumnIndex
     * @param dvHelper
     * @return
     */
    private static DataValidation getDataValidationByFormula(
            String formulaString, int naturalRowIndex, int naturalColumnIndex, XSSFDataValidationHelper dvHelper) {
        // 加载下拉列表内容
        // 举例：若formulaString = "INDIRECT($A$2)" 表示规则数据会从名称管理器中获取key与单元格 A2 值相同的数据，
        //如果A2是江苏省，那么此处就是江苏省下的市信息。
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint(formulaString);
        // 设置数据有效性加载在哪个单元格上。
        // 四个参数分别是：起始行、终止行、起始列、终止列
        int firstRow = naturalRowIndex - 1;
        int lastRow = naturalRowIndex - 1;
        int firstCol = naturalColumnIndex - 1;
        int lastCol = naturalColumnIndex - 1;
        CellRangeAddressList regions = new CellRangeAddressList(firstRow,
                lastRow, firstCol, lastCol);
        // 数据有效性对象
        // 绑定
        XSSFDataValidation data_validation_list = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, regions);
        data_validation_list.setEmptyCellAllowed(false);
        if (data_validation_list instanceof XSSFDataValidation) {
            data_validation_list.setSuppressDropDownArrow(true);
            data_validation_list.setShowErrorBox(true);
        } else {
            data_validation_list.setSuppressDropDownArrow(false);
        }
        // 设置输入信息提示信息
        data_validation_list.createPromptBox("下拉选择提示", "请使用下拉方式选择合适的值！");
        // 设置输入错误提示信息
        //data_validation_list.createErrorBox("选择错误提示", "你输入的值未在备选列表中，请下拉选择合适的值！");
        return data_validation_list;
    }

    /**
     * 计算formula
     *
     * @param offset   偏移量，如果给0，表示从A列开始，1，就是从B列
     * @param rowId    第几行
     * @param colCount 一共多少列
     * @return 如果给入参 1,1,10. 表示从B1-K1。最终返回 $B$1:$K$1
     */
    public static String getRange(int offset, int rowId, int colCount) {
        char start = (char) ('A' + offset);
        if (colCount <= 25) {
            char end = (char) (start + colCount - 1);
            return "$" + start + "$" + rowId + ":$" + end + "$" + rowId;
        } else {
            char endPrefix = 'A';
            char endSuffix = 'A';
            if ((colCount - 25) / 26 == 0 || colCount == 51) {
                // 26-51之间，包括边界（仅两次字母表计算）
                if ((colCount - 25) % 26 == 0) {
                    // 边界值
                    endSuffix = (char) ('A' + 25);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                }
            } else {
                // 51以上
                if ((colCount - 25) % 26 == 0) {
                    endSuffix = (char) ('A' + 25);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26 - 1);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26);
                }
            }
            return "$" + start + "$" + rowId + ":$" + endPrefix + endSuffix + "$" + rowId;
        }
    }
}
