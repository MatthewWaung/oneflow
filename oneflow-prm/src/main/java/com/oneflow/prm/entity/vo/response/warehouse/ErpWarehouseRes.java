package com.oneflow.prm.entity.vo.response.warehouse;

import com.oneflow.prm.entity.vo.BaseRes;
import lombok.Data;

@Data
public class ErpWarehouseRes extends BaseRes {
    private Long id;

    private String warehouseCode;

    private String warehouseName;

    private String deliveryPlatform;

    private String erpWarehouseCode;

    private String erpWarehouseName;

    private String status;
}
