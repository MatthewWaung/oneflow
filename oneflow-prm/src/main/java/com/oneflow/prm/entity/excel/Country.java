package com.oneflow.prm.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class Country {

    @ExcelProperty(value = "国家代码")
    private String countryCode;

    @ExcelProperty(value = "国家名称")
    private String countryName;

    @ExcelProperty(value = "国籍")
    private String nationality;

    @ExcelProperty(value = "语言代码")
    private String languageCode;
}
