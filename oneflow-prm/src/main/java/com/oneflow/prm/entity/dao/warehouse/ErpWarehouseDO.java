package com.oneflow.prm.entity.dao.warehouse;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.oneflow.prm.entity.dao.BaseAutoFillModel;
import lombok.Data;

@Data
@TableName("prm_erp_warehouse")
public class ErpWarehouseDO extends BaseAutoFillModel<ErpWarehouseDO> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "warehouse_code")
    private String warehouseCode;

    @TableField(value = "warehouse_name")
    private String warehouseName;

    @TableField(value = "warehouse_name")
    private String deliveryPlatform;

    @TableField(value = "erp_warehouse_code")
    private String erpWarehouseCode;

    @TableField(value = "erp_warehouse_name")
    private String erpWarehouseName;

    @TableField(value = "status")
    private String status;
}
