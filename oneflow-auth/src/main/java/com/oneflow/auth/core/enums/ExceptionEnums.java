package com.oneflow.auth.core.enums;

/**
 * 异常code
 *
 * @author Dunvida
 * @date 2022年03月10日 14:34
 */
public enum ExceptionEnums {
    /********************************1.系统操作*****************************************/
    SYSTEM_OPERATE_SUCCESS("100001", "操作成功"),
    SYSTEM_OPERATE_ERROR("100002", "操作失败"),
    SYSTEM_ERROR("100003", "系统异常，请稍后重试"),
    THE_SAME_DATA_EXISTS("100004", "存在相同数据，请核对后添加！"),
    ILLEGAL_ARGUMENT("100005", "参数不合法！"),
    REQUEST_METHOD_NOT_MATCH("100006", "请求方法不匹配！"),
    PATH_NOT_FOUND("100007", "路径未找到！"),
    DATABASE_OPERATION_EXCEPTION("100008", "数据库操作异常！"),
    CONNECTION_ERROR("100009", "网络连接失败！"),
    RUNTIME_ERROR("100010", "内部异常"),
    INTERNAL_ERROR("100011", "内部异常"),

    /********************************2.授权、用户*****************************************/
    AUTHORIZATION_RULE_FAILS("200001", "授权规则不通过,请稍后再试!"),
    NO_ACCESS_PERMISSION("200002", "无访问权限,请联系管理员授予权限"),
    INVALID_TOKEN("200003", "访问令牌不合法"),
    ACCESS_DENIED("200004", "没有权限访问该资源"),
    CLIENT_AUTHENTICATION_FAILED("200005", "客户端认证失败"),
    USERNAME_OR_PASSWORD_ERROR("200006", "用户名或密码错误"),
    UNSUPPORTED_GRANT_TYPE("200007", "不支持的认证模式"),
    REPEAT_MOBILE("200008", "已存在此手机号"),
    REPEAT_EMAIL("200009", "已存在此邮箱地址"),
    NO_AUTHORITY("200010", "您无权限执行此次删除操作"),


    /********************************3.系统管理*****************************************/
    PARAMETERS_EMPTY_EXCEPTION("900001", "参数为空异常");

    private final String code;

    private final String message;

    ExceptionEnums(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
