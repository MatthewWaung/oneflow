package com.oneflow.oms.entity.product;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("oms_product")
public class ProductDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "销售组织编码")
    @TableField(value = "sales_organization_code", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String salesOrganizationCode;

    @Schema(description = "销售组织名称")
    @TableField(value = "sales_organization_name", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String salesOrganizationName;

    @Schema(description = "物料编码")
    @TableField(value = "material_code", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String materialCode;

    @Schema(description = "物料描述")
    @TableField(value = "material_desc", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String materialDesc;

    @Schema(description = "单位")
    @TableField(value = "unit", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String unit;

    @Schema(description = "物料类型")
    @TableField(value = "material_type", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String materialType;

    @Schema(description = "EAN")
    @TableField(value = "ean", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String ean;

    @Schema(description = "UPC")
    @TableField(value = "upc", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String upc;

    @Schema(description = "类型, 1-单品 2-组合品")
    @TableField(value = "type", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String type;

    @Schema(description = "状态, 0-启用 1-禁用")
    @TableField(value = "status", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String status;

    @Schema(description = "备注")
    @TableField(value = "remark", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String remark;

}
