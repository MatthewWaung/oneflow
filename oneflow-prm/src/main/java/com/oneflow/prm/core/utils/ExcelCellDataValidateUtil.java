package com.oneflow.prm.core.utils;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.oneflow.prm.entity.dto.excel.ExcelCellValidateDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Excel常用校验工具类
 *
 * @author forest
 * @date 2024/06/12
 */
public class ExcelCellDataValidateUtil {

    private ExcelCellDataValidateUtil() {
    }


    /**
     * 横线
     */
    public static final String HORIZONTAL_LINE = "-";

    /**
     * 校验日期格式
     *
     * @param dateFormat 格式 yyyy-MM-dd
     * @param dateStr    日期字符串
     * @return true: 日期格式正确 false: 日期格式错误
     */
    public static boolean isValidateDate(String dateFormat, String dateStr) {
        LocalDate localDate = DateUtils.parseEffectiveDate(dateFormat, dateStr);
        return localDate != null;
    }

    /**
     * 返回值为 ExcelCellValidateDTO passValidate为校验结果，message为校验失败的原因
     *
     * @param dateFormat 格式 例如：yyyy-MM-dd
     * @param dateStr    日期字符串
     */
    public static ExcelCellValidateDTO isValidateDateDTO(String dateFormat, String dateStr) {
        ExcelCellValidateDTO excelCellValidateDTO = new ExcelCellValidateDTO();
        LocalDate localDate = DateUtils.parseEffectiveDate(dateFormat, dateStr);
        if (localDate == null) {
            excelCellValidateDTO.setPassValidate(false);
            excelCellValidateDTO.setMessage(dateStr + "格式错误，正确格式为：" + dateFormat);
        } else {
            excelCellValidateDTO.setMessage(dateStr + "格式正确");
        }
        return excelCellValidateDTO;
    }

    /**
     * 校验必须是大于0的整数
     *
     * @param numberStr 数字字符串
     * @return true: 数字格式正确 false: 数字格式错误
     */
    public static boolean isPositiveNumber(String numberStr) {
        boolean isLong = NumberUtil.isLong(numberStr);
        if (!isLong) {
            return false;
        }
        // 大于0的整数
        return Long.parseLong(numberStr) > 0;
    }

    /**
     * 返回值为 ExcelCellValidateDTO passValidate为校验结果，message为校验失败的原因
     *
     * @param numberStr 数字字符串
     */
    public static ExcelCellValidateDTO isPositiveNumberDTO(String numberStr) {
        ExcelCellValidateDTO excelCellValidateDTO = new ExcelCellValidateDTO();
        boolean isLong = NumberUtil.isLong(numberStr);
        if (!isLong) {
            excelCellValidateDTO.setPassValidate(false);
            excelCellValidateDTO.setMessage("数字格式错误");
        } else {
            if (Long.parseLong(numberStr) <= 0) {
                excelCellValidateDTO.setPassValidate(false);
                excelCellValidateDTO.setMessage("数字必须大于0");
            } else {
                excelCellValidateDTO.setMessage("数字格式正确");
            }
        }
        return excelCellValidateDTO;
    }

    /**
     * 指定长度金额校验，指定小数位数,多了小数位数也会校验失败
     *
     * @param moneyStr 金额字符串
     * @param scale    小数位数
     */
    public static boolean isSpecialLengthMoney(String moneyStr, int scale) {
        boolean isNumber = NumberUtil.isNumber(moneyStr);
        if (!isNumber) {
            return false;
        }
        // 采用正则表达式校验
        String regex = "^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0," + scale + "})?$";
        return moneyStr.matches(regex);
    }

    /**
     * 指定长度金额校验，指定小数位，多了小数位数也会校验失败
     * 返回值为 ExcelCellValidateDTO passValidate为校验结果，message为校验失败的原因
     *
     * @param moneyStr 金额字符串
     * @param scale    小数位数
     */
    public static ExcelCellValidateDTO isSpecialLengthMoneyDTO(String moneyStr, int scale) {
        ExcelCellValidateDTO excelCellValidateDTO = new ExcelCellValidateDTO();
        boolean isNumber = NumberUtil.isNumber(moneyStr);
        if (!isNumber) {
            excelCellValidateDTO.setPassValidate(false);
            excelCellValidateDTO.setMessage("金额格式错误");
        } else {
            // 采用正则表达式校验
            String regex = "^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0," + scale + "})?$";
            if (moneyStr.matches(regex)) {
                excelCellValidateDTO.setMessage("金额格式正确");
            } else {
                excelCellValidateDTO.setPassValidate(false);
                excelCellValidateDTO.setMessage("金额格式错误，不能为负数同时小数位数最多" + scale + "位");
            }
        }
        return excelCellValidateDTO;
    }

    /**
     * 校验字符串：包含指定分割符号，分隔符两边的数据不能为空，分割后只能是两个组，多了也有问题，分割符号只能有一个 例如 xpss-新品上市
     *
     * @param str         字符串
     * @param splitSymbol 指定分割符号
     * @return boolean
     */
    public static boolean isContainSplitSymbol(String str, String splitSymbol) {
        boolean result = false;
        if (StrUtil.isBlank(str) || StrUtil.isBlank(splitSymbol)) {
            return result;
        }
        // 判断是否包含分割符号
        if (!str.contains(splitSymbol)) {
            return result;
        }
        // 判断分割符号只能有一个
        if (str.indexOf(splitSymbol) != str.lastIndexOf(splitSymbol)) {
            return result;
        }
        // 拆分
        String[] split = str.split(splitSymbol);
        if (split.length != 2) {
            return result;
        }
        // 判断两边的数据不能为空
        if (StrUtil.isBlank(split[0]) || StrUtil.isBlank(split[1])) {
            return result;
        }

        result = true;
        return result;
    }

    /**
     * 校验字符串：包含指定分割符号，分隔符两边的数据不能为空，分割后只能是两个组，多了也有问题，分割符号只能有一个 例如 xpss-新品上市
     * 返回值为 ExcelCellValidateDTO passValidate为校验结果，message为校验失败的原因
     *
     * @param str         字符串
     * @param splitSymbol 指定分割符号
     */
    public static ExcelCellValidateDTO isContainSplitSymbolDTO(String str, String splitSymbol) {
        ExcelCellValidateDTO excelCellValidateDTO = new ExcelCellValidateDTO();
        boolean isContainSplitSymbol = isContainSplitSymbol(str, splitSymbol);
        if (!isContainSplitSymbol) {
            excelCellValidateDTO.setPassValidate(false);
            excelCellValidateDTO.setMessage("字符串格式错误，正确格式为：xxx" + splitSymbol + "xxx");
        } else {
            excelCellValidateDTO.setMessage("字符串格式正确");
        }
        return excelCellValidateDTO;
    }


    /**
     * 校验字符串：包含特定分隔符，分隔后第一部分不能为空
     */
    public static boolean isContainSplitSymbol(String str) {
        boolean result = false;
        if (StrUtil.isBlank(str)) {
            return result;
        }
        if (!str.contains(HORIZONTAL_LINE)) {
            return result;
        }

        String[] split = str.split(HORIZONTAL_LINE);
        if (StrUtil.isBlank(split[0])) {
            return result;
        }
        result = true;
        return result;
    }

    /**
     * @param str 字符串
     * @return {@link ExcelCellValidateDTO }
     */
    public static ExcelCellValidateDTO isContainSplitSymbolDTO(String str) {
        ExcelCellValidateDTO excelCellValidateDTO = new ExcelCellValidateDTO();
        boolean isContainSplitSymbol = isContainSplitSymbol(str);
        if (!isContainSplitSymbol) {
            excelCellValidateDTO.setPassValidate(false);
            excelCellValidateDTO.setMessage("字符串格式错误，正确格式为：xxx" + HORIZONTAL_LINE + "xxx");
        } else {
            excelCellValidateDTO.setMessage("字符串格式正确");
        }
        return excelCellValidateDTO;
    }

    /**
     * 校验金额是否为整数 入参类型为BigDecimal
     *
     * @param amount 金额
     */
    public static boolean isInteger(BigDecimal amount) {
        if (amount == null) {
            return false;
        }
        return amount.stripTrailingZeros().scale() <= 0;
    }


    /**
     * 校验是否为合法季度 2024Q1 2024Q2 请用正则 开头必须是2
     *
     * @param quarterReport 季度
     * @return {@link ExcelCellValidateDTO }
     */
    public static ExcelCellValidateDTO isValidateQuarterDTO(String quarterReport) {
        ExcelCellValidateDTO excelCellValidateDTO = new ExcelCellValidateDTO();
        if (StrUtil.isBlank(quarterReport)) {
            excelCellValidateDTO.setPassValidate(false);
            excelCellValidateDTO.setMessage("提报季度不能为空");
            return excelCellValidateDTO;
        }
        if (!quarterReport.matches("^2\\d{3}Q[1-4]$")) {
            excelCellValidateDTO.setPassValidate(false);
            excelCellValidateDTO.setMessage("提报季度格式错误，正确格式为：2024Q1");
            return excelCellValidateDTO;
        }
        return excelCellValidateDTO;
    }


    public static void main(String[] args) {

//        String moneyStr = "12555555555443";
//        int scale = 2;
//
//        boolean specialLengthMoney = isSpecialLengthMoney(moneyStr, scale);
//        System.out.println("specialLengthMoney = " + specialLengthMoney);

//        boolean containSplitSymbol = isContainSplitSymbol("-3", "-");
//        System.out.println("containSplitSymbol = " + containSplitSymbol);

//        ExcelCellValidateDTO positiveNumberDTO = isPositiveNumberDTO("100");
//        System.out.println("positiveNumberDTO = " + positiveNumberDTO);

    }


}
