package com.oneflow.prm.core.utils;

import cn.hutool.http.HttpUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Threads {

    public static void main(String[] args) {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            final int index = i;
            String result = HttpUtil.get("http://localhost:8100/ecoflow/prm/cust/getAllCustomer?page=1&pageSize=5");
            cachedThreadPool.execute(() -> System.out.println(result));
        }
    }

    public static void printException(Runnable r, Throwable t) {

    }
}
