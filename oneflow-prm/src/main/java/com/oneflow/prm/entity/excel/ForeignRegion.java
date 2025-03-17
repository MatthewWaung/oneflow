package com.oneflow.prm.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ForeignRegion {

    @ExcelProperty(value = "国家代码")
    private String countryCode;

    @ExcelProperty(value = "地区")
    private String region;

    @ExcelProperty(value = "描述")
    private String description;

    @ExcelProperty(value = "省税码")
    private String regionTaxCode;
}
