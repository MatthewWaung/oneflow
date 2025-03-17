package com.oneflow.auth.core.exception;

import com.oneflow.auth.security.core.enums.ExceptionEnums;
import com.oneflow.auth.security.core.enums.ModuleEnums;

/**
 * 业务异常
 *
 * @author jiangwei
 */
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 所属模块
     */
    private String module;

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误码对应的参数
     */
    private Object[] args;

    /**
     * 错误消息
     */
    private String defaultMessage;

    public CustomException(String module, String code, Object[] args, String defaultMessage) {
        this.module = module;
        this.code = code;
        this.args = args;
        this.defaultMessage = defaultMessage;
    }

    public CustomException(String module, String code, Object[] args) {
        this(module, code, args, null);
    }

    public CustomException(String module, String defaultMessage) {
        this(module, null, null, defaultMessage);
    }

    public CustomException(String code, Object[] args) {
        this(null, code, args, null);
    }

    public CustomException(String defaultMessage) {
        this(null, null, null, defaultMessage);
    }

    public CustomException(ModuleEnums moduleEnums, Object[] args, ExceptionEnums exceptionEnums) {
        this.module = moduleEnums.getType();
        this.args = args;
        this.code = exceptionEnums.getCode();
        this.defaultMessage = exceptionEnums.getMessage();
    }

    public String getModule() {
        return module;
    }

    public String getCode() {
        return code;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
