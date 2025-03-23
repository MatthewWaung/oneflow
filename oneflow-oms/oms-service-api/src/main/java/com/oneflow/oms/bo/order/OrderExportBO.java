package com.oneflow.oms.bo.order;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import lombok.Data;

@Data
@HeadStyle(fillBackgroundColor = 44)
@HeadFontStyle(fontHeightInPoints = 16)
public class OrderExportBO {

    @ColumnWidth(20)
    @ExcelProperty(value = "订单号")
    private String orderNo;

    @ColumnWidth(20)
    @ExcelProperty(value = "平台订单号")
    private String platformOrderNo;

    @ColumnWidth(20)
    @ExcelProperty(value = "订单来源")
    private String orderSource;

    @ColumnWidth(20)
    @ExcelProperty(value = "发货平台")
    private String deliveryPlatform;

    @ColumnWidth(20)
    @ExcelProperty(value = "SAP订单号")
    private String orderType;

    @ColumnWidth(20)
    @ExcelProperty(value = "订单状态")
    private String orderStatus;

    @ColumnWidth(20)
    @ExcelProperty(value = "店铺编码")
    private String shopName;

    @ColumnWidth(20)
    @ExcelProperty(value = "销售组织")
    private String salesOrganizationName;

    @ColumnWidth(20)
    @ExcelProperty(value = "销售大区")
    private String salesAreaName;

    @ColumnWidth(20)
    @ExcelProperty(value = "付款方式")
    private String paymentTermsName;

    @ColumnWidth(20)
    @ExcelProperty(value = "币种")
    private String currencyName;

    @ColumnWidth(20)
    @ExcelProperty(value = "备注")
    private String note;

}
