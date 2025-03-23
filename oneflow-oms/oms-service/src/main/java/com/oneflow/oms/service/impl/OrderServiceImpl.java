package com.oneflow.oms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oneflow.oms.entity.order.OrderDO;
import com.oneflow.oms.mapper.OrderMapper;
import com.oneflow.oms.service.IOrderService;
import com.oneflow.oms.vo.request.order.OrderListReq;
import com.oneflow.oms.vo.response.order.OrderListRes;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderDO> implements IOrderService {


    @Override
    public List<OrderListRes> listOrder(OrderListReq req) {
        return Collections.emptyList();
    }


}
