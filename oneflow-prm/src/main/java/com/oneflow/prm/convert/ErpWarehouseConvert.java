package com.oneflow.prm.convert;

import com.oneflow.prm.core.common.PageResult;
import com.oneflow.prm.entity.dao.warehouse.ErpWarehouseDO;
import com.oneflow.prm.entity.vo.request.warehouse.ErpWarehouseReq;
import com.oneflow.prm.entity.vo.response.warehouse.ErpWarehouseRes;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ErpWarehouseConvert {
    ErpWarehouseConvert INSTANCE = Mappers.getMapper(ErpWarehouseConvert.class);

    PageResult<ErpWarehouseRes> pageErpWarehouseDOToRes(PageResult<ErpWarehouseDO> pageResult);

    ErpWarehouseRes erpWarehouseDOToRes(ErpWarehouseDO erpWarehouseDO);

    ErpWarehouseDO erpWarehouseReqToDO(ErpWarehouseReq erpWarehouseReq);
}
