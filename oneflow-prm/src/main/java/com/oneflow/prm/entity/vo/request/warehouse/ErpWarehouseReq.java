package com.oneflow.prm.entity.vo.request.warehouse;

import com.oneflow.prm.entity.vo.BaseReq;
import lombok.Data;

@Data
public class ErpWarehouseReq extends BaseReq {

    private String id;

    private String warehouseCode;

    private String warehouseName;

    private String deliveryPlatform;

    private String erpWarehouseCode;

    private String erpWarehouseName;

    private String status;

    /**
     * 平台列表
     */
    private String[] platforms;
}
