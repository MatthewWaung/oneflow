package com.oneflow.prm.core.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;

import java.lang.reflect.Field;

public class AopUtils {
    private static final Log logger = LogFactory.getLog(AopUtils.class);

    public AopUtils() {
    }

    public static <T> T getTargetObject(T proxy) {
        if (!ClassUtils.isProxy(proxy.getClass())) {
            return proxy;
        } else {
            try {
                if (org.springframework.aop.support.AopUtils.isJdkDynamicProxy(proxy)) {
                    return getJdkDynamicProxyTargetObject(proxy);
                } else if (org.springframework.aop.support.AopUtils.isCglibProxy(proxy)) {
                    return getCglibProxyTargetObject(proxy);
                } else {
                    logger.warn("Warn: The proxy object processing method is not supported.");
                    return proxy;
                }
            } catch (Exception var2) {
                throw ExceptionUtils.mpe("Error: Get proxy targetObject exception !  Cause:" + var2, new Object[0]);
            }
        }
    }

    private static <T> T getCglibProxyTargetObject(T proxy) throws Exception {
        Field cglibField = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        cglibField.setAccessible(true);
        Object dynamicAdvisedInterceptor = cglibField.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport)advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
        return (T) target;
    }

    private static <T> T getJdkDynamicProxyTargetObject(T proxy) throws Exception {
        Field jdkDynamicField = proxy.getClass().getSuperclass().getDeclaredField("jdkDynamicField");
        jdkDynamicField.setAccessible(true);
        AopProxy aopProxy = (AopProxy)jdkDynamicField.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport)advised.get(aopProxy)).getTargetSource().getTarget();
        return (T) target;
    }
}
