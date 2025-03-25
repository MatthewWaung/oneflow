package com.oneflow.prm.core.constant;

public class RedisKeyConstants {
    public static final String BASE_KEY = "ecoflow:prm:";

    /************************消息中心****************************************/
    public static final String SYS_NOTICE = BASE_KEY + "notice:";
    public static final String SYS_NOTICE_UPDATE = SYS_NOTICE + "update:";
    public static final String SYS_NOTICE_MAINTENANCE = SYS_NOTICE + "maintenance:";

    /************************授权价****************************************/
    public static final String PRICE_LICESE = BASE_KEY + "price:license:";

    /************************贷项凭证导入****************************************/
    public static final String CREDIT_ITEM_IMPORT = BASE_KEY + "credit:item:import:";

    /************************一键代发导入****************************************/
    public static final String ONETIME_SEND_IMPORT = BASE_KEY + "onetime:send:import:";

    /************************订单管理****************************************/
    public static final String ORDER_MODIFY = BASE_KEY + "order:modify:";
    public static final String SAP_DELIVERY = BASE_KEY + "sap:delivery:";

    /************************客户管理****************************************/
    public static final String CUSTOMER_MANAGER_SALESPERSON_KEY = BASE_KEY + "customer:manager:salesperson:";

    /************************多维度数据参数保存****************************************/
    public static final String MULTI_DIMENSION = BASE_KEY + "multi:dimension:";

    /************************返利导入****************************************/
    public static final String REBATES_IMPORT = BASE_KEY + "rebates:import:";

    /************************返利预提****************************************/
    public static final String REBATES_CONFIRM = BASE_KEY + "rebates:confirm:";

    /************************返利修改****************************************/
    public static final String REBATES_MODIFY = BASE_KEY + "rebates:modify:";

    /*********************** 付款条件新增或修改****************************************/
    public static final String PAYMENT_CONDITION_UPDATE = BASE_KEY + "payment:condition:update:";

    /************************产品型号新增或修改****************************************/
    public static final String PRODUCT_MODEL_UPDATE = BASE_KEY + "product:model:update:";

    /************************pdt新增或修改****************************************/
    public static final String PDT_UPDATE = BASE_KEY + "pdt:update:";


    /************************订单标识更新****************************************/
    public static final String ORDER_IDENTIFY_UPDATE = BASE_KEY + "order:identify:update:";

    /************************sku映射导入****************************************/
    public static final String SKUMAP_IMPORT = BASE_KEY + "skumap:import:";

}
