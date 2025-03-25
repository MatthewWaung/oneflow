package com.oneflow.prm.listener;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.oneflow.prm.core.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ExcelImportListener:监听类，自定义
 *
 * @author : forest.liao
 */
@Slf4j
public class ExcelImportListener<T> extends AnalysisEventListener<T> {

    /**
     * 自定义用于暂时存储data。可以通过实例获取该值
     */
    private final List<T> dataList = new ArrayList<>();

    /**
     * 默认最大数量为50000
     */
    public static final int MAX_SIZE = 50000;

    /**
     * 实体序号字段
     */
    public static final String EXCEL_INDEX = "index";

    @Override
    public void invoke(T t, AnalysisContext context) {
        if (!checkNonEmptyRow(t)) {
            return;
        }
        // 如果t对象有index属性，则把currentRowNum设置进去
        setIndexIfNeed(t, context);
        // 存储数据到list
        addDataToList(t);
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 本监听器无需做业务处理
    }


    /**
     * 获取数据
     *
     * @return {@link List }<{@link T }>
     */
    public List<T> getDataList() {
        checkImportSize();
        return dataList;
    }

    // 检测导入数据数量
    private void checkImportSize() {
        if (dataList.size() > MAX_SIZE) {
            throw new CustomException("导入数据超过"+MAX_SIZE+"条");
        }
    }


    /**
     * 获取移除指定行后的数据
     *
     * @param removeLine 移除指定行数据,比如第一行是标题，第二行是写的注释，数据需要从第三行开始都，那就传1,不传直接返回
     * @return {@link List }<{@link T }>
     */
    public List<T> getRemoveLineDataList(Integer removeLine) {
        checkImportSize();
        if (ObjectUtil.isEmpty(removeLine)) {
            return getDataList();
        }
        if (dataList.size() > removeLine) {
            return dataList.subList(removeLine, dataList.size());
        } else {
            return Collections.emptyList();
        }
    }

    // 往对象设置index值
    private static <T> void setIndexIfNeed(T t, AnalysisContext context) {
        // 使用自定义方法来获取所有字段，包括继承的字段
        Arrays.stream(getAllFields(t.getClass()))
                .filter(p -> EXCEL_INDEX.equals(p.getName()))
                .findFirst()
                .ifPresent(field -> {
                    try {
                        field.setAccessible(true);
                        field.set(t, context.readRowHolder().getRowIndex()+1);
                    } catch (IllegalAccessException e) {
                        log.error("IllegalAccessException when setting index", e);
                        throw new RuntimeException("IllegalAccessException when setting index", e);
                    }
                });

    }

    // 递归方法用于获取一个类及其所有父类的所有字段
    private static Field[] getAllFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fieldList.toArray(new Field[0]);
    }


    // 获取对象的属性值
    private static Object getFieldValue(Object obj, String name) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getName().equalsIgnoreCase(name)) {
                    return field.get(obj);
                }
            }
            // 如果没有找到对应的字段，返回null
            return null;
        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException when accessing field: {}", name, e);
            throw new RuntimeException("IllegalAccessException when accessing field: " + name, e);
        }
    }


    /**
     * 获取带有ExcelProperty注解的属性名数组
     */
    private static String[] getFieldNames(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        List<String> fieldNameList = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelProperty.class)) {
                fieldNameList.add(field.getName());
            }
        }
        return fieldNameList.toArray(new String[0]);
    }

    /**
     * 判断空行并返回结果
     *
     * @param t 要检查的对象
     * @return 如果存在非空字段，则返回true；否则返回false
     */
    private boolean checkNonEmptyRow(T t) {
        String[] fieldName = getFieldNames(t);
        for (String string : fieldName) {
            Object fieldValue = getFieldValue(t, string);
            if (fieldValue instanceof String && StringUtils.isNotBlank((String) fieldValue)) {
                // 如果字符串类型的字段值非空，则无需继续检查其他字段
                return true;
            } else if (fieldValue != null) {
                // 对于非字符串类型，只要字段值非空，也认为该行有效
                // 如果其他类型的字段值非空，也无需继续检查其他字段
                return true;
            }
        }
        log.warn("该行被忽略,object={}", t);
        return false;
    }

    /**
     * 将数据添加到列表中
     *
     * @param t 要添加的对象
     */
    private void addDataToList(T t) {
        // 数据存储到list，供批量处理，或后续自己业务逻辑处理。
        dataList.add(t);
    }
}