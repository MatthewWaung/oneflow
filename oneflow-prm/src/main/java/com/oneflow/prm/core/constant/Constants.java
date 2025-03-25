package com.oneflow.prm.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用信息常量
 */
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

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
     * 操作成功
     */
    public static final String OPERATION_SUCCESS = "success";

    /**
     * contentType
     */
    public static final String CONTENT_TYPE_NAME = "Content-Type";

    /**
     * JSON内容类型
     */
    public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

    /**
     * 表单内容类型
     */
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded;charset=UTF-8";

    /**
     * token前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

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

    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 部门数据权限
     */
    public static final String DATA_SCOPE_DEPT = "3";

    /**
     * 部门及以下数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "5";

    /**
     * 标记 1-是，0-否
     */
    @AllArgsConstructor
    @Getter
    public enum YesOrNoFlag {
        Y(1, "是"),
        N(0, "否");
        private final Integer code;
        private final String desc;
    }

}
