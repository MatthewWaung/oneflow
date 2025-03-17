package com.oneflow.prm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oneflow.prm.convert.ErpWarehouseConvert;
import com.oneflow.prm.core.common.PageResult;
import com.oneflow.prm.core.common.R;
import com.oneflow.prm.core.exception.CustomException;
import com.oneflow.prm.entity.dao.warehouse.ErpWarehouseDO;
import com.oneflow.prm.entity.vo.request.warehouse.ErpWarehouseReq;
import com.oneflow.prm.entity.vo.response.warehouse.ErpWarehouseRes;
import com.oneflow.prm.mapper.ErpWarehouseMapper;
import com.oneflow.prm.service.IErpWarehouseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ErpWarehouseServiceImpl extends ServiceImpl<ErpWarehouseMapper, ErpWarehouseDO> implements IErpWarehouseService {

    @Resource
    private ErpWarehouseMapper erpWarehouseMapper;

    @Override
    public PageResult<ErpWarehouseRes> getErpWarehouse(ErpWarehouseReq erpReq) {
        IPage<ErpWarehouseDO> page = new Page<>(erpReq.getPage(), erpReq.getPageSize());
        LambdaQueryWrapper<ErpWarehouseDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(erpReq.getDeliveryPlatform()), ErpWarehouseDO::getDeliveryPlatform, erpReq.getDeliveryPlatform())
                .eq(StringUtils.isNotBlank(erpReq.getErpWarehouseCode()), ErpWarehouseDO::getErpWarehouseCode, erpReq.getWarehouseCode())
                .eq(StringUtils.isNotBlank(erpReq.getErpWarehouseName()), ErpWarehouseDO::getErpWarehouseName, erpReq.getErpWarehouseName())
                .eq(StringUtils.isNotBlank(erpReq.getStatus()), ErpWarehouseDO::getStatus, erpReq.getStatus())
                .orderByDesc(ErpWarehouseDO::getCreateTime);
        IPage<ErpWarehouseDO> warehouseDOIPage = erpWarehouseMapper.selectPage(page, queryWrapper);
        PageResult<ErpWarehouseDO> pageResult = new PageResult<>(warehouseDOIPage);
        return ErpWarehouseConvert.INSTANCE.pageErpWarehouseDOToRes(pageResult);
    }

    @Override
    public ErpWarehouseRes getErpWarehouseById(String id) {
        ErpWarehouseDO erpWarehouseDO = erpWarehouseMapper.selectById(id);
        return ErpWarehouseConvert.INSTANCE.erpWarehouseDOToRes(erpWarehouseDO);
    }

    @Override
    public R addErpWarehouse(ErpWarehouseReq erpWarehouseReq) {
        String[] platforms = erpWarehouseReq.getPlatforms();
        for (String platform : platforms) {
            ErpWarehouseDO erpWarehouseDO = erpWarehouseMapper.selectOne(new LambdaQueryWrapper<ErpWarehouseDO>().eq(ErpWarehouseDO::getDeliveryPlatform, platform)
                    .eq(ErpWarehouseDO::getErpWarehouseName, erpWarehouseReq.getErpWarehouseCode()));
            if (null != erpWarehouseDO) {
                throw new CustomException(erpWarehouseDO.getDeliveryPlatform() + "+" + erpWarehouseDO.getErpWarehouseName() + "已存在！");
            } else {
                ErpWarehouseDO erpWarehouse = ErpWarehouseConvert.INSTANCE.erpWarehouseReqToDO(erpWarehouseReq);
                erpWarehouseDO.setDeliveryPlatform(platform);
                erpWarehouseMapper.insert(erpWarehouse);
            }
        }
        return R.ok();
    }

    @Override
    public R updateErpWarehouse(ErpWarehouseReq erpWarehouseReq) {
        ErpWarehouseDO erpDO = erpWarehouseMapper.selectOne(new LambdaQueryWrapper<ErpWarehouseDO>()
                .eq(ErpWarehouseDO::getDeliveryPlatform, erpWarehouseReq.getDeliveryPlatform())
                .eq(ErpWarehouseDO::getErpWarehouseName, erpWarehouseReq.getErpWarehouseCode()));
        if (null != erpDO && !StringUtils.equals(String.valueOf(erpDO.getId()), erpWarehouseReq.getId())) {
            ErpWarehouseDO erpWarehouseDO = ErpWarehouseConvert.INSTANCE.erpWarehouseReqToDO(erpWarehouseReq);
            erpWarehouseMapper.updateById(erpWarehouseDO);
        }
        return R.ok();
    }
}
