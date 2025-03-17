package com.oneflow.prm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oneflow.prm.entity.dao.customer.ReceiverDO;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiverMapper extends BaseMapper<ReceiverDO> {
    int insertReceiver(ReceiverDO receiverDO);
}
