package com.oneflow.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oneflow.oms.entity.record.ModifyRecordDO;

public interface IModifyRecordService extends IService<ModifyRecordDO> {

    /**
     * 新增修改记录
     * @param modifyRecordDO
     */
    void insertModifyRecord(ModifyRecordDO modifyRecordDO);

}
