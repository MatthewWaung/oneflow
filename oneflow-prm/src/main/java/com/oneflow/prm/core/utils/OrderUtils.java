package com.oneflow.prm.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderUtils {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss");

    private static final AtomicInteger atomicInteger = new AtomicInteger(10000);

    /**
     * 创建不连续的订单号
     * 数据中心编号
     *
     * @return 唯一的、不连续订单编号
     */
    public static synchronized String getOrderNoByUUID(){
        Integer uuidHashCode = UUID.randomUUID().toString().hashCode();
        if (uuidHashCode < 0){
            uuidHashCode = uuidHashCode * (-1);
        }
        String date = simpleDateFormat.format(new Date());
        return date + uuidHashCode;
    }

    /**
     * 获取同一秒生成的订单连续
     * <p></p>
     * 数据中心编号
     *
     * @return 同一秒内订单连续的编号
     */
    public static synchronized String getOrderNoByAtomic(){
        atomicInteger.getAndIncrement();
        int i = atomicInteger.get();
        String date = simpleDateFormat.format(new Date());
        return date + i;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
//            System.out.println("生成的连续订单号为：" + getOrderNoByAtomic());
//            System.out.println("生成不连续订单号为：" + getOrderNoByUUID());
        }
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 100; j++) {
//                        System.out.println("生成的连续订单号为：" + getOrderNoByAtomic());
                        String uuid = UUID.randomUUID().toString();
                        System.out.println("随机生成的uuid：" + uuid + ",对应的hashcode为：" + uuid.hashCode());
                    }
                }
            });
            threads[i].start();
        }
//        while (Thread.activeCount() > 1){   //保持线程活跃，上述代码执行完毕后不会停止程序
//            Thread.yield();
//        }
    }
}

