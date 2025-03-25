package com.oneflow.prm.core.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.oneflow.prm.core.enums.ExceptionEnums;
import com.oneflow.prm.core.enums.ModuleEnums;
import com.oneflow.prm.core.exception.CustomException;
import com.oneflow.prm.listener.ExcelImportListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

@Slf4j
public class ExcelUtil {


    /**
     * 解析第一个excel的数据
     * @param receiveReqClazz 定义的接收数据对象
     * @param file excel文件
     * @param removeLine 移除指定行数据,比如第一行是标题，第二行是写的注释，数据需要从第三行开始都，那就传1
     * @return {@link List }<{@link T }>
     */
    public static <T> List<T> parseFirstSheet(Class<T> receiveReqClazz, MultipartFile file, Integer removeLine) {
        // 校验Excel文件
        ExcelUtil.checkExcelFile(file);
        ExcelImportListener<T> excelImportListener = new ExcelImportListener<>();
        try (ExcelReader excelReader = EasyExcel.read(file.getInputStream(), receiveReqClazz, excelImportListener).build()) {
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
            return excelImportListener.getRemoveLineDataList(removeLine);
        } catch (Exception e) {
            log.error("解析excel文件失败", e);
            throw new CustomException(ModuleEnums.SYSTEM_MANAGEMENT, null, ExceptionEnums.EXCEL_PARSE_FAILED);
        }
    }


    /**
     * 校验上传的文件是否是Excel文件及是否是xls或xlsx格式
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static void checkExcelFile(MultipartFile file) {
        try {
            //校验文件名称
            String fileName = file.getOriginalFilename();
            if (fileName == null) {
                throw new CustomException("未获取到文件名");
            }
            //校验文件后缀名
            String fileSuffixName = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (fileSuffixName == null) {
                throw new CustomException("非法文件");
            }
            //校验文件后缀名是否为xls或者xlsx格式
            if (!"xls".equals(fileSuffixName) && !"xlsx".equals(fileSuffixName)) {
                throw new CustomException("文件格式错误，请上传xls或xlsx格式的Excel文件");
            }
            //根据Excel魔数判断该文件是否为Excel文件
            InputStream inputStream = file.getInputStream();
            InputStream fileMagics = FileMagic.prepareToCheckMagic(inputStream);
            FileMagic fileMagic = FileMagic.valueOf(fileMagics);

            //FileMagic.OLE2表示xls格式 FileMagic.OOXML表示xlsx格式
            if (!Objects.equals(fileMagic, FileMagic.OLE2) && !Objects.equals(fileMagic, FileMagic.OOXML)) {
                throw new CustomException("文件格式错误或文件解析失败(文件加密)");
            }
        } catch (CustomException e) {
            log.warn("Excel文件校验异常", e);
            throw e;
        } catch (Exception e) {
            log.info("Excel文件校验异常：", e);
            throw new CustomException("Excel文件校验异常");
        }
    }


    /**
     * 设置excel下载响应头属性
     */
    public static void setExcelRespProp(HttpServletResponse response, String rawFileName) {

        try {

            rawFileName = ensureCorrectExtension(rawFileName);
            // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode(rawFileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 确保 rawFileName 具有正确的扩展名
     *
     * @param rawFileName
     * @return
     */

    private static String ensureCorrectExtension(String rawFileName) {
        if (rawFileName == null || rawFileName.isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty.");
        }

        // 确保文件名具有至少一个预期的扩展名
        if (!rawFileName.endsWith(".xlsx") && !rawFileName.endsWith(".xls") && !rawFileName.endsWith(".csv")) {
            // 默认使用 .xlsx 作为扩展名
            return rawFileName + ".xlsx";
        }

        return rawFileName;
    }

    /** 
     * 获取字符中"-"的第一部分，如果数据为空则返回空，数据中不包含也返回空
     * @param str 字符串 
     * @return {@link String }
     */
    public static String getFirstWord(String str) {
        if (StrUtil.isBlank(str)) {
            return "";
        }
        if (str.contains(ExcelCellDataValidateUtil.HORIZONTAL_LINE)) {
            return str.split(ExcelCellDataValidateUtil.HORIZONTAL_LINE)[0];
        }
        return "";
    }

}
