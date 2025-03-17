package com.oneflow.prm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oneflow.prm.core.common.PageResult;
import com.oneflow.prm.core.common.R;
import com.oneflow.prm.entity.dao.warehouse.ErpWarehouseDO;
import com.oneflow.prm.entity.vo.request.warehouse.ErpWarehouseReq;
import com.oneflow.prm.entity.vo.response.warehouse.ErpWarehouseRes;

public interface IErpWarehouseService extends IService<ErpWarehouseDO> {

    /**
     * 查询列表
     * @param erpWarehouseReq
     * @return
     */
    PageResult<ErpWarehouseRes> getErpWarehouse(ErpWarehouseReq erpWarehouseReq);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    ErpWarehouseRes getErpWarehouseById(String id);

    /**
     * 新增
     * @param erpWarehouseReq
     * @return
     */
    R addErpWarehouse(ErpWarehouseReq erpWarehouseReq);

    /**
     * 修改
     * @param erpWarehouseReq
     * @return
     */
    R updateErpWarehouse(ErpWarehouseReq erpWarehouseReq);
}
