package com.oneflow.prm.controller;

import com.oneflow.prm.core.common.PageResult;
import com.oneflow.prm.core.common.R;
import com.oneflow.prm.entity.vo.request.warehouse.ErpWarehouseReq;
import com.oneflow.prm.entity.vo.response.warehouse.ErpWarehouseRes;
import com.oneflow.prm.service.IErpWarehouseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/erpWarehouse")
public class ErpWarehouseController {

    @Resource
    IErpWarehouseService erpWarehouseService;

    @GetMapping("/getErpWarehouse")
    public R<PageResult<ErpWarehouseRes>> getErpWarehouse(@RequestBody ErpWarehouseReq erpWarehouseReq) {
        PageResult<ErpWarehouseRes> pageResult = erpWarehouseService.getErpWarehouse(erpWarehouseReq);
        return R.ok(pageResult);
    }

    @GetMapping("getErpWarehouseById")
    public R<ErpWarehouseRes> getErpWarehouseById(@RequestParam("id") String id) {
        ErpWarehouseRes erpWarehouseRes = erpWarehouseService.getErpWarehouseById(id);
        return R.ok(erpWarehouseRes);
    }

    @PostMapping("/addErpWarehouse")
    public R addErpWarehouse(@RequestBody ErpWarehouseReq erpWarehouseReq) {
        return erpWarehouseService.addErpWarehouse(erpWarehouseReq);
    }

    @PostMapping("/updateErpWarehouse")
    public R updateErpWarehouse(@RequestBody ErpWarehouseReq erpWarehouseReq) {
        return erpWarehouseService.updateErpWarehouse(erpWarehouseReq);
    }

    @GetMapping("deleteById")
    public R deleteErpWarehouseById(@RequestParam("id") String id) {
        return R.ok(erpWarehouseService.removeById(id));
    }

}
