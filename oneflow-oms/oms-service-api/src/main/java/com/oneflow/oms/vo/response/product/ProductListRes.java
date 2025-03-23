package com.oneflow.oms.vo.response.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "产品列表响应参数")
public class ProductListRes {

    private Long id;

    @Schema(description = "销售组织编码")
    private String salesOrganizationCode;

    @Schema(description = "销售组织名称")
    private String salesOrganizationName;

    @Schema(description = "物料编码")
    private String materialCode;

    @Schema(description = "物料描述")
    private String materialDesc;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "物料类型")
    private String materialType;

    @Schema(description = "EAN")
    private String ean;

    @Schema(description = "UPC")
    private String upc;

    @Schema(description = "类型, 1-单品 2-组合品")
    private String type;

    @Schema(description = "状态, 0-启用 1-禁用")
    private String status;

    @Schema(description = "备注")
    private String remark;

}
