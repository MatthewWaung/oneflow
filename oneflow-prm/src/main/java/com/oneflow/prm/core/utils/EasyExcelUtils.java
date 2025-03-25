package com.oneflow.prm.core.utils;

import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.oneflow.prm.core.common.R;
import com.oneflow.prm.core.enums.ExceptionEnums;
import com.oneflow.prm.core.exception.CustomException;
import com.oneflow.prm.entity.vo.request.excel.ImportBaseReq;
import com.oneflow.prm.feign.FileFeignClient;
import com.oneflow.prm.service.ISysDictDataService;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author forest
 * @date 2024年6月29日
 */
@Slf4j
public class EasyExcelUtils {
    private EasyExcelUtils() {
    }

    /** 标题行:消息 */
    public static final String TITLE_MESSAGE = "消息";

    @SneakyThrows(Exception.class)
    public static void commonExport(String templatePath, Map<String, Object> dataMap, String fileName, HttpServletResponse response, ExcelTypeEnum type) {
        //处理中文乱码
        fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        // 判断excel版本
        if (type == ExcelTypeEnum.XLS) {
            response.setContentType("application/vnd.ms-excel");
        } else {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        }
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename*=" + fileName);
        InputStream in = EasyExcelUtils.class.getClassLoader().getResourceAsStream(templatePath);
        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).excelType(type).withTemplate(in).build()) {
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            excelWriter.fill(dataMap.get("list"), writeSheet);
            excelWriter.fill(dataMap, writeSheet);
        } catch (IOException e) {
            log.error("获取文件流失败", e);
//            throw new BizException("文件下载失败。");
        }
    }

    /**
     * 通用导出
     *
     * @param templatePath 模板地址
     * @param dataMap     数据
     * @param fileName   文件名
     */

    public static void commonExport(String templatePath, Map<String, Object> dataMap, String fileName, HttpServletResponse response) {
        commonExport(templatePath, dataMap, fileName, response, ExcelTypeEnum.XLSX);
    }

    /**
     * 设置响应头
     *
     * @param response 响应
     * @param fileName 文件名
     */
    public static void setExcelResponse(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName + ".xlsx", "UTF-8"));
    }

    /**
     * 获取或创建标题行
     *
     * @param sheet    表格
     * @param titleRow 标题行
     * @param title    标题
     * @return int
     */
    public static int findOrCreateColumn(Sheet sheet, Row titleRow, String title) {
        Integer i = getColumnIndex(titleRow, title);
        if (i != null) {
            return i;
        }

        // 如果列不存在，则创建它（这里只是简单地假设我们将在新的一列中创建它）
        // 注意：这只是一个简单的示例，你可能需要根据你的具体需求来调整创建新列的方式
        sheet.autoSizeColumn(sheet.getRow(0).getLastCellNum()); // 自动调整前一列的宽度（如果需要）
        Row newTitleRow = sheet.getRow(0); // 获取或创建标题行
        if (newTitleRow == null) {
            newTitleRow = sheet.createRow(0);
        }
        Cell newCell = newTitleRow.createCell(newTitleRow.getLastCellNum()); // 在新位置创建单元格
        newCell.setCellValue(title); // 设置单元格值
        return newTitleRow.getLastCellNum()-1; // 返回新列的索引
    }

    /**
     * 获取标题行中列的索引
     * @param titleRow 标题行
     * @param title 标题
     * @return {@link Integer }
     */
    public static Integer getColumnIndex(Row titleRow, String title) {
        for (int i = 0; i < titleRow.getLastCellNum(); i++) {
            Cell cell = titleRow.getCell(i);
            if (cell != null && cell.getStringCellValue().equals(title)) {
                return i;
            }
        }
        return null;
    }

    /**
     * 设置单元格的值
     * @param row 表格行
     * @param index 索引
     * @param value 内容
     */
    public static void setCellValue(Row row, int index, String value) {
        Cell cell = row.getCell(index);
        if (cell == null) {
            row.createCell(index).setCellValue(value);
        } else {
            cell.setCellValue(value);
        }
    }

    /**
     * 设置水平样式策略
     *
     * @return {@link HorizontalCellStyleStrategy }
     */
    public static HorizontalCellStyleStrategy setHorizontalCellStyleStrategy() {
        //头策略使用默认 设置字体大小
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 11);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 设置背景颜色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        //内容样式策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 字体策略
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 11);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }


    /**
     * 导出单个sheet
     *
     * @param response 响应
     * @param dataList 数据
     * @param clazz    类
     * @param fileName 文件名
     */
    public static <T> void exportSigletonSheet(HttpServletResponse response, List<T> dataList, Class<T> clazz, String fileName) {
        exportSigletonSheet(response, dataList, clazz, fileName, null);
    }

    /**
     * 导出单个sheet，不需要排除字段
     *
     * @param response                响应
     * @param dataList                数据
     * @param clazz                   类
     * @param fileName                文件名
     * @param excludeColumnFiledNames 需要排除的字段
     */
    public static <T> void exportSigletonSheet(HttpServletResponse response, List<T> dataList, Class<T> clazz, String fileName, Set<String> excludeColumnFiledNames) {
        try (OutputStream outputStream = response.getOutputStream()) {
            EasyExcelUtils.setExcelResponse(response, fileName);
            ExcelWriter excelWriter = EasyExcel.write(outputStream).build();

            WriteSheet writeSheet = EasyExcel.writerSheet(0, "Sheet1")
                    .head(clazz)
                    .excludeColumnFieldNames(Optional.ofNullable(excludeColumnFiledNames).orElseGet(HashSet::new))
                    .registerWriteHandler(setHorizontalCellStyleStrategy())
                    .build();

            excelWriter.write(dataList, writeSheet);
            excelWriter.finish();
            outputStream.flush();
        } catch (Exception e) {
            log.info("数据导出excel失败：", e);
            throw new CustomException(ExceptionEnums.EXPORT_EXCEL_ERROR.getMessage());
        }
    }

    /**
     * 导出多个sheet
     * @param response       响应
     * @param multiSheetList 多个sheet
     * @param fileName       文件名
     */
    public static void exportMultiSheet(HttpServletResponse response, List<MultiSheet> multiSheetList, String fileName) {
        try (OutputStream outputStream = response.getOutputStream()) {
            EasyExcelUtils.setExcelResponse(response, fileName);
            ExcelWriter excelWriter = EasyExcel.write(outputStream).build();
            for (MultiSheet multiSheet : multiSheetList) {
                WriteSheet writeSheet = EasyExcel.writerSheet()
                        .head(multiSheet.getClazz())
                        .excludeColumnFieldNames(Optional.ofNullable(multiSheet.getExcludeColumnFiledNames()).orElseGet(HashSet::new))
                        .registerWriteHandler(setHorizontalCellStyleStrategy())
                        .sheetName(multiSheet.getSheetName())
                        .build();
                excelWriter.write(multiSheet.getDataList(), writeSheet);
            }
            excelWriter.finish();
            outputStream.flush();
        } catch (Exception e) {
            log.info("数据导出excel失败：", e);
            throw new CustomException(ExceptionEnums.EXPORT_EXCEL_ERROR.getMessage());
        }
    }

    @Data
    public static class MultiSheet {
        private String sheetName;
        private List<?> dataList;
        private Class<?> clazz;
        private Set<String> excludeColumnFiledNames;

        public MultiSheet(String sheetName, List<?> dataList, Class<?> clazz, Set<String> excludeColumnFiledNames) {
            this.sheetName = sheetName;
            this.dataList = dataList;
            this.clazz = clazz;
            this.excludeColumnFiledNames = excludeColumnFiledNames;
        }
    }

    /**
     * 对象转字符串
     *
     * @param obj {@link Object}
     * @return {@link String }
     */
    public static String objToStr(Object obj) {
        return Optional.ofNullable(obj).map(Object::toString).orElse(null);
    }

/**
     * 对象转字符串
     *
     * @param numbers {@link Object}
     * @return {@link String }
     */
    public static String getStrLine(Set<Integer> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (int number : numbers) {
            if (!isFirst) {
                sb.append("，"); // 在每个元素之间添加逗号
            }
            sb.append("第").append(number).append("行");
            isFirst = false; // 标记已经不是第一个元素了
        }

        return sb.toString();
    }

    /**
     * 通用导出
     *
     * @param templatePath 模板地址
     * @param dataMap     数据
     * @param outPath     输出地址
     */

    public static void commonExportToFilePath(String templatePath, Map<String, Object> dataMap, String outPath) {
        InputStream in = null;
        try {
            in = new FileInputStream(templatePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (ExcelWriter excelWriter = EasyExcel.write(new File(outPath)).withTemplate(in).build()) {
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            excelWriter.fill(dataMap.get("list"), writeSheet);
            excelWriter.fill(dataMap, writeSheet);
        }
    }


    /**
     * 将包含错误信息的请求对象列表导出到 Excel 文件
     *
     * @param reqList 请求对象列表
     * @param file    上传的 Excel 文件
     * @param <T>     请求对象类型，必须继承自 ImportBaseReq
     */
    public static <T extends ImportBaseReq> String extractAndWriteToExcel(List<T> reqList, String fileName, MultipartFile file) {
        File tempFile = FileUtils.createTempFile(fileName, ".xlsx", true);

        try (InputStream inputStream = file.getInputStream();
             FileOutputStream fos = new FileOutputStream(tempFile)) {

            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Row titleRow = sheet.getRow(0);

            // 找到或创建“错误信息”列
            int messageCol = EasyExcelUtils.findOrCreateColumn(sheet, titleRow, EasyExcelUtils.TITLE_MESSAGE);

            Map<Integer, T> reqMap = reqList.stream()
                    .collect(Collectors.toMap(ImportBaseReq::getIndex, Function.identity(), (oldValue, newValue) -> newValue));

            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                T req = reqMap.get(i + 1);
                if (Objects.isNull(req)) {
                    continue;
                }

                // 将错误消息放入指定列单元格中
                if (CollectionUtils.isNotEmpty(req.getErrors())) {
                    List<String> errors = req.getErrors().stream().filter(Objects::nonNull).collect(Collectors.toList());
                    // 写入Excel的时候，需要把错误信息换行
                    String errorMsg = errors.stream().distinct().collect(Collectors.joining("；"));
                    EasyExcelUtils.setCellValue(row, messageCol, errorMsg);
                }
            }

            workbook.write(fos);

            String projectUrl = SpringUtil.getBean(ISysDictDataService.class).selectDictLabel("project_url", "project_url");
            R<String> r = SpringUtil.getBean(FileFeignClient.class).upload(FileUtils.fileToMultipartFile(tempFile), fileName);
            return Optional.ofNullable(r.getData()).map(data -> data.replace(projectUrl, "")).orElse(null);
        } catch (IOException e) {
            log.error("处理返回数据失败", e);
            throw new RuntimeException(e.getMessage());
        }
    }


}
