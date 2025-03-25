package com.oneflow.prm.core.utils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};


    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 解析日期字符串，例如 "2023-12-31"，并返回 LocalDate 对象。
     *
     * @param dateStr 日期字符串
     * @return {@link LocalDate }
     */
    public static LocalDate parseEffectiveDate(String dateFormat, String dateStr) {
        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        try {
            // 尝试解析日期
            return LocalDate.parse(dateStr, formatter);
        } catch (Exception e) {
            // 如果解析失败（例如格式不正确），则返回null或者抛出异常
            // 这里返回null，您可以根据需要抛出异常
            return null;
        }
    }

    /**
     * 解析日期字符串，例如 "2023-12"，并返回 LocalDate 对象。
     * @param dateFormat 例如 yyyy-MM
     * @param dateStr 例如 2023-12
     * @return {@link LocalDate }
     */
    public static LocalDate parseEffectiveDateYearMonth(String dateFormat, String dateStr) {
        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        try {
            // 尝试解析日期
            // 由于LocalDate需要日，我们可以通过解析到YearMonth再转换到LocalDate（设置为月的第一天）
            YearMonth yearMonth = YearMonth.parse(dateStr, formatter);
            return yearMonth.atDay(1); // 返回该月的第一天作为LocalDate
        } catch (Exception e) {
            // 如果解析失败（例如格式不正确），则返回null或者抛出异常
            // 这里返回null，您可以根据需要抛出异常
            return null;
        }
    }

}
