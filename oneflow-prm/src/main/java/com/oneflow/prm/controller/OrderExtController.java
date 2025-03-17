package com.oneflow.prm.controller;

import com.oneflow.prm.core.common.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orderExt")
public class OrderExtController{

    @GetMapping("/getOrders")
    public R getOrders() {
        return R.ok();
    }
}
