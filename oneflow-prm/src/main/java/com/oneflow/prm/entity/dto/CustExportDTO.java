package com.oneflow.prm.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

@Data
public class CustExportDTO {

    @ExcelProperty(value = "客户名称")
    @ColumnWidth(20)
    private String customerName;

    @ExcelProperty(value = "客户电话")
    private String customerPhone;

    @ExcelProperty(value = "客户Email")
    private String customerEmail;
}
