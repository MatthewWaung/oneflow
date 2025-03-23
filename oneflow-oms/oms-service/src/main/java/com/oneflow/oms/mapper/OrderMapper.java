package com.oneflow.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oneflow.oms.entity.order.OrderDO;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper extends BaseMapper<OrderDO> {
}
