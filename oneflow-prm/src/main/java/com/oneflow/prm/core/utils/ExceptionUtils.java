package com.oneflow.prm.core.utils;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

public final class ExceptionUtils {
    private ExceptionUtils() {
    }

    public static MybatisPlusException mpe(String msg, Throwable t, Object... params) {
        return new MybatisPlusException(StringUtils.format(msg, params), t);
    }

    public static MybatisPlusException mpe(String msg, Object... params) {
        return new MybatisPlusException(StringUtils.format(msg, params));
    }

    public static MybatisPlusException mpe(Throwable t) {
        return new MybatisPlusException(t);
    }
}
