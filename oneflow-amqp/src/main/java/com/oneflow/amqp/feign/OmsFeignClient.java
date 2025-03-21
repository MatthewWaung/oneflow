package com.oneflow.amqp.feign;

import com.oneflow.amqp.interceptor.FeignInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "oneflow-oms", configuration = FeignInterceptor.class)
public interface OmsFeignClient {

    @PostMapping(path = "/oms/handleOrders", produces = "application/json")
    R handleOrders(@RequestBody Long orderId);

}
