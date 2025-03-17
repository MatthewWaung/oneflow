package com.oneflow.prm.core.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;

public final class Assert {
    public Assert() {
    }

    public static void isTrue(boolean expression, String message, Object... params) {
        if (!expression) {
            throw ExceptionUtils.mpe(message, params);
        }
    }

    public static void isFalse(boolean expression, String message, Object... params) {
        isTrue(!expression, message, params);
    }

    public static void isNull(Object object, String message, Object... params) {
        isTrue(object == null, message, params);
    }

    public static void notNull(Object object, String message, Object... params) {
        isTrue(object != null, message, params);
    }

    public static void notEmpty(String value, String message, Object... params) {
        isTrue(StringUtils.isNotBlank(value), message, params);
    }

    public static void notEmpty(Collection<?> collection, String message, Object... params) {
        isTrue(CollectionUtils.isNotEmpty(collection), message, params);
    }

    public static void notEmpty(Map<?, ?> map, String message, Object... params) {
        isTrue(MapUtils.isNotEmpty(map), message, params);
    }

    public static void notEmpty(Object[] array, String message, Object... params) {
        isTrue(ArrayUtils.isNotEmpty(array), message, params);
    }
}
