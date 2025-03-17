package com.oneflow.auth.core.enums;

/**
 * @author Dunvida
 * @date 2022年03月10日 15:11
 */
public enum ModuleEnums {

    SYSTEM_MANAGEMENT("1","系统管理"),
    CONFIGURATION_CENTRAL("2","配置中心"),
    GOODS_MANAGEMENT("3","商品管理"),
    ORDER_MANAGEMENT("4","订单管理"),
    CUSTOMER_MANAGEMENT("5","客户管理"),
    PROCESS_MANAGEMENT("6","流程管理");

    /**
     * 模块id
     **/
    private final String moduleCode;
    /**
     * 模块类别
     **/
    private final String type;

    ModuleEnums(String moduleCode, String type) {
        this.moduleCode = moduleCode;
        this.type = type;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public String getType() {
        return type;
    }
}
