package com.oneflow.auth.core.constant;

/**
 * 通用信息常量
 */
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * UTF-8 字符集
     */
    public static final String GBK = "GBK";

    /**
     * RMI 远程方法调用
     */
    public static final String LOOKUP_RMI = "rmi:";


    /**
     * 成功标记
     */
    public static final Integer SUCCESS = 200;

    /**
     * 失败标记
     */
    public static final Integer FAIL = 600;

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 操作成功
     */
    public static final String OPERATION_SUCCESS = "success";

    /**
     * token前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * token信息
     */
    public static final String LOGIN_USER_KEY = "login_user_key";
    public static final String LOGIN_USER_ID = "user_id";
    public static final String LOGIN_USER_NAME = "user_name";
    public static final String LOGIN_USER_REAL_NAME = "user_real_name";
    public static final String LOGIN_USER_DEPT_ID = "user_dept_id";
    public static final String LOGIN_USER_DEPT_NAME = "user_dept_name";
    public static final String LOGIN_USER_ROLE = "user_role";



}
