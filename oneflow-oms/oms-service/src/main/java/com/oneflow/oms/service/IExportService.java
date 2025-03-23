package com.oneflow.oms.service;

import com.oneflow.oms.vo.request.order.OrderListReq;

import javax.servlet.http.HttpServletResponse;

public interface IExportService {

    /**
     * 导出订单列表
     *
     * @param req
     * @param response
     */
    void exportOrderList(OrderListReq req, HttpServletResponse response);

}
