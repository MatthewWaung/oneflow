package com.oneflow.prm.advice.interceptor;

import com.oneflow.prm.entity.sys.LoginUser;

/**
 * 实现将Token解析出来的用户信息放入到ThreadLocal中，进行变量在不同类中的传递
 *
 * 参考ThreadLocalUtils
 */
public class LoginUserContext {

    private static ThreadLocal<LoginUser> threadLocal = new ThreadLocal<>();

    /**
     * ThreadLocal常用的三个方法：set、get、remove
     */

    public static void setThreadLocal(LoginUser loginUser) {
        if (null == threadLocal.get()) {
            threadLocal.set(loginUser);
        }
    }

    public static Object get() {
        LoginUser loginUser = threadLocal.get();
        // 参考SecurityContextHolder中的ThreadLocal
        if (null == loginUser) {
            loginUser = new LoginUser();
            threadLocal.set(loginUser);
        }
        return loginUser;
    }

    public static void remove() {
        threadLocal.remove();
    }

}
