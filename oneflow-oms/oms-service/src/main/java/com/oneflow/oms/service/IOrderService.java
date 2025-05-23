package com.oneflow.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oneflow.oms.entity.order.OrderDO;
import com.oneflow.oms.vo.request.order.OrderListReq;
import com.oneflow.oms.vo.request.order.OrderModifyReq;
import com.oneflow.oms.vo.response.order.OrderListRes;

import java.util.List;

public interface IOrderService extends IService<OrderDO> {

    /**
     * 订单列表
     *
     * @param req
     * @return
     */
    List<OrderListRes> listOrder(OrderListReq req);

    /**
     * 修改订单
     * @param req
     */
    void modifyOrder(OrderModifyReq req);

    /**
     * 获取订单详情
     * @param id
     * @return
     */
    OrderDO getOrderDOById(Long id);

}
