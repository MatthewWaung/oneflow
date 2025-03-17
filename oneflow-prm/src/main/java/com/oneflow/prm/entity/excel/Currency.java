package com.oneflow.prm.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class Currency {

    @ExcelProperty(value = "货币")
    private String currency;

    @ExcelProperty(value = "ISO代码")
    private String currencyName;

    @ExcelProperty(value = "短文本")
    private String isoCode;

    @ExcelProperty(value = "长文本")
    private String currencyDesc;

}
