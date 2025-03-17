package com.oneflow.prm.advice.interceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * 线程缓存工具类
 *
 * 参考：https://blog.csdn.net/qq_43578385/article/details/111879678
 *
 * @author Boss
 */
public class ThreadLocalUtils {
    private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

    /**
     * 向ThreadLocal缓存值
     *
     * @param key   要缓存的KEY
     * @param value 要缓存的VALUE
     */
    public static void set(String key, Object value) {
        if (!isCaheIsNull()) {
            threadLocal.get().put(key, value);
        } else {
            Map<String, Object> vmap = new HashMap<>();
            vmap.put(key, value);
            threadLocal.set(vmap);
        }
    }

    /**
     * 从ThreadLocal里获取缓存的值
     *
     * @param key 要获取的数据的KEY
     * @return 要获取的值
     */
    public static Object getCache(String key) {
        Map<String, Object> map = threadLocal.get();
        if (isCaheIsNull()) {
            return null;
        }
        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            return null;
        }
    }

    /**
     * 根据KEY移除缓存里的数据
     *
     * @param key
     */
    public static void removeByKey(String key) {
        if (isCaheIsNull()) {
            return;
        } else {
            threadLocal.get().remove(key);
        }
    }

    /**
     * 移除当前线程缓存
     * 用于释放当前线程threadlocal资源
     */
    public static void remove() {
        threadLocal.remove();
    }

    private static boolean isCaheIsNull() {
        return threadLocal.get() == null;
    }
}

