package com.oneflow.oms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oneflow.oms.entity.record.ModifyRecordDO;
import com.oneflow.oms.mapper.ModifyRecordMapper;
import com.oneflow.oms.service.IModifyRecordService;
import org.springframework.stereotype.Service;

@Service
public class ModifyRecordServiceImpl extends ServiceImpl<ModifyRecordMapper, ModifyRecordDO> implements IModifyRecordService {

    @Override
    public void insertModifyRecord(ModifyRecordDO modifyRecordDO) {
        save(modifyRecordDO);
    }

}
