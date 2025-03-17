package com.oneflow.prm.service.biz;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.oneflow.prm.core.exception.CustomException;
import com.oneflow.prm.entity.dao.order.OrderDO;
import com.oneflow.prm.entity.dto.ExcelProductDTO;
import com.oneflow.prm.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DataToCnExcel {

    public static DataToCnExcel dataToCnExcel;
    private OrderDO orderData;
    private OrderMapper orderMapper;

    public DataToCnExcel() {}

    public DataToCnExcel(OrderDO orderDO) {
        orderData = orderDO;
    }

    @PostConstruct
    public void init() {
        dataToCnExcel = this;
    }

    public void createCnExcel(OutputStream os) {
        try {
            String enCountryName = dataToCnExcel.orderMapper.selectEnglishCountryName(orderData.getReceiverCountryId());
            String paymentTerms = dataToCnExcel.orderMapper.selectPaymentTerms(orderData.getPaymentTermsId());

            //根据excel模板导出excel，字段值需和excel模板中的字段一一对应
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("country", enCountryName);
            paramsMap.put("paymentTerms", paymentTerms);

            List<ExcelProductDTO> excelVoList = new ArrayList<>();

            /**
             * 参考：https://blog.csdn.net/Matthew_99/article/details/130030262
             */
            int firstRow = 18;  //从第18行开始合并
            int lastRow = 18;
            int beginRow = 18;
            //单元格合并
            List<CellRangeAddress> cellRangeAddressList = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(excelVoList)) {
                if (excelVoList.size() > 1) {
                    for (int i = 0; i < excelVoList.size() - 1; i++) {
                        cellRangeAddressList.add(new CellRangeAddress(firstRow, lastRow, 1, 4));
                        cellRangeAddressList.add(new CellRangeAddress(firstRow, lastRow, 7, 8));
                        firstRow++;
                        lastRow++;
                    }
                }
            }

            //将easyExcel生成的文件保存在临时文件中待poi进一步做合并单元格
            File tmpFile = new File("D:\\tmp\\tmpFile.xlsx");
            OutputStream tmpOutputStream = Files.newOutputStream(tmpFile.toPath());
            //获取excel模板
            File file = new File("D:\\template\\template01.xlsx");
            InputStream inputStream = Files.newInputStream(file.toPath());

            //将easyExcel生成的文件保存在临时文件中待poi进一步做合并单元格
            //File tmpFile = new File("/tmp/" + "tmp_file.xlsx");
            //OutputStream tmpOutputStream = Files.newOutputStream(tmpFile.toPath());
            //获取excel模板
            //InputStream inputStream = new URL(filePath).openStream();

            ExcelWriter excelWriter = EasyExcel.write(tmpOutputStream).withTemplate(inputStream)
                    //      .registerWriteHandler(fillMergeStrategy)  //不采用合并策略
                    .build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();

            //参数集合，直接写入到Excel数据
            excelWriter.fill(paramsMap, writeSheet);
            //列表数据
            excelWriter.fill(excelVoList, fillConfig, writeSheet);
            excelWriter.finish();

            //使用poi合并单元格，使用registerWriteHandler合并单元格会与fill方法中创建单元格后校验合并单元格冲突而引发报错
            InputStream in = Files.newInputStream(tmpFile.toPath());
            XSSFWorkbook workbook = new XSSFWorkbook(in);
            XSSFSheet sheet = workbook.getSheetAt(0);
            if (CollectionUtils.isNotEmpty(cellRangeAddressList)) {
                for (CellRangeAddress cellAddresses : cellRangeAddressList) {
                    //合并单元格
                    sheet.addMergedRegion(cellAddresses);
                    //设置单元格样式，解决合并单元格后边框缺失问题
                    setRegionStyle(sheet, cellAddresses, setDefaultStyle(workbook));
                }
            }
            workbook.write(os);
            os.flush();
            os.close();
        } catch (IOException e) {
            log.error("Cn文件导出excel错误：", e);
            throw new CustomException("Cn文件导出excel错误：" + e.getMessage());
        }
    }

    //使用poi设置合并单元格后的样式
    public void setRegionStyle(XSSFSheet sheet, CellRangeAddress region, XSSFCellStyle xssfCellStyle) {
        for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (null == row) {
                row = sheet.createRow(i);
            }
            for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                XSSFCell cell = row.getCell(j);
                if (null == cell) {
                    cell = row.createCell(j);
                }
                cell.setCellStyle(xssfCellStyle);
            }
        }
    }

    public XSSFCellStyle setDefaultStyle(XSSFWorkbook workbook) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        // 边框
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        // 居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 字体
        XSSFFont font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 10);
        cellStyle.setFont(font);
        return cellStyle;
    }

}
