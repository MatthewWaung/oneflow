package com.oneflow.oms.core.annotation;

import com.oneflow.oms.enums.DataType;
import com.oneflow.oms.enums.ModuleType;
import com.oneflow.oms.enums.BusinessType;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ModifyRecord {

    /**
     * 模块
     */
    public ModuleType moduleType();

    /**
     * 操作方法-新增、修改
     */
    public BusinessType businessType();

    /**
     * IService
     */
    public Class iService();

    /**
     * 方法名
     */
    public String methodName();

    /**
     * 数据类型
     */
    public DataType dataType();

}
