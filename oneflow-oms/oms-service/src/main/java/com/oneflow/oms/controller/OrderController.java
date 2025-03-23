package com.oneflow.oms.controller;

import com.oneflow.oms.service.IOrderService;
import com.oneflow.oms.vo.request.order.OrderListReq;
import com.oneflow.oms.vo.response.order.OrderListRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Tag(name = "订单管理")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private IOrderService orderService;

    @Operation(summary = "订单列表")
    @PostMapping("/list")
    public List<OrderListRes> list(@RequestBody OrderListReq req) {
        // 这里要用PageResult返回，这里只是一个接口示意，使用了List返回
        return orderService.listOrder(req);
    }

}
