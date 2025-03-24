package com.oneflow.prm.core.enums;

import com.oneflow.prm.core.utils.StringUtils;
import com.oneflow.prm.entity.dao.customer.CustomerDO;
import com.oneflow.prm.entity.dao.order.OrderDO;

/**
 * @Author: Dunvida
 * @Date: 2022/3/28 19:55
 */
public enum ActivitiTypeEnums {

    STANDARD_ORDER_REVIEW("order_standard","order_standard","标准订单审核"),
    STANDARD_ORDER_HOME_STORAGE_REVIEW("order_standard_h", "order_standard_h", "家储-标准订单审核"),
    FREE_SAMPLES_ORDER_REVIEW("order_free","order_free","免费样品订单审核"),
    FREE_SAMPLES_ORDER_HOME_STORAGE_REVIEW("order_free_h", "order_free_h", "家储-免费样品订单审核"),
    A_UNDERTAKES_TO_ORDER_REVIEW("order_undertakes","order_undertakes","一件代发订单审核"),
    A_UNDERTAKES_TO_ORDER_HOME_STORAGE_REVIEW("order_undertakes_h", "order_undertakes_h", "家储-一件代发订单审核"),
    CREDIT_VOUCHERS_ORDER_REVIEW("order_credit","order_credit","贷项凭证订单审核"),
    CREDIT_VOUCHERS_ORDER_HOME_STORAGE_REVIEW("order_credit_h", "order_credit_h", "家储-贷项凭证订单审核"),
    DEBIT_VOUCHER_ORDER_REVIEW("order_debit","order_debit","借项凭证订单审核"),
    DEBIT_VOUCHER_ORDER_HOME_STORAGE_REVIEW("order_debit_h", "order_debit_h", "家储-借项凭证订单审核"),

    STANDARD_ORDER_REVIEW_MODIFY("order_standard_modify","order_standard_modify","标准订单修改审核"),
    STANDARD_ORDER_REVIEW_HOME_STORAGE_MODIFY("order_standard_modify_h", "order_standard_modify_h", "家储-标准订单修改审核"),
    FREE_SAMPLES_ORDER_REVIEW_MODIFY("order_free_modify","order_free_modify","免费样品订单修改审核"),
    FREE_SAMPLES_ORDER_REVIEW_HOME_STORAGE_MODIFY("order_free_modify_h", "order_free_modify_h", "家储-免费样品订单修改审核"),
    A_UNDERTAKES_TO_ORDER_REVIEW_MODIFY("order_undertakes_modify","order_undertakes_modify","一件代发订单修改审核"),
    A_UNDERTAKES_TO_ORDER_REVIEW_HOME_STORAGE_MODIFY("order_undertakes_modify_h", "order_undertakes_modify_h", "家储-一件代发订单修改审核"),
    CREDIT_VOUCHERS_ORDER_REVIEW_MODIFY("order_credit_modify","order_credit_modify","贷项凭证订单修改审核"),
    CREDIT_VOUCHERS_ORDER_REVIEW_HOME_STORAGE_MODIFY("order_credit_modify_h", "order_credit_modify_h", "家储-贷项凭证订单修改审核"),
    DEBIT_VOUCHER_ORDER_REVIEW_MODIFY("order_debit_modify","order_debit_modify","借项凭证订单修改审核"),
    DEBIT_VOUCHER_ORDER_REVIEW_HOME_STORAGE_MODIFY("order_debit_modify_h", "order_debit_modify_h", "家储-借项凭证订单修改审核"),
    CUSTOMER_INFO_ADD("customer_create_flow","customer_add","客户信息新增审核"),
    CUSTOMER_INFO_MODIFY("customer_modify_flow","customer_modify","客户信息修改审核"),
    PRICE_LICENSE_ADD("price_l_add","price_l_add","授权价-新品定价"),
    PRICE_LICENSE_MODIFY("price_l_modify","price_l_modify","授权价-老品调价"),
    REBATES_MANAGE("rebates_manage","rebates_manage","事前返利申请流程"),

    ;
    /**
     * 流程类型
     **/
    private final String type;

    /**
     * 流程code
     */
    private final String code;

    /**
     * 描述
     **/
    private final String description;

    ActivitiTypeEnums(String type, String code, String description) {
        this.type = type;
        this.code = code;
        this.description = description;
    }

    public String getType() {
        return type;
    }
    public String getCode() {
        return code;
    }
    public String getDescription() {
        return description;
    }


    public static ActivitiTypeEnums getByType(String type) {
        for (ActivitiTypeEnums typeEnums : values()) {
            if (typeEnums.getType().equals(type)) {
                return typeEnums;
            }
        }
        return null;
    }



//    /**
//     * 通过订单的类型、信息获取ActivitiType对应type
//     * @param type
//     * @param salesOrderNo
//     * @return
//     */
//    public static ActivitiTypeEnums getActivityTypeByOrderInfo(String type, String salesOrderNo) {
//
//        if (StringUtils.isEmpty(salesOrderNo)) {
//            if (OrderTypeConstants.STANDARD.equals(type)) {
//                return ActivitiTypeEnums.STANDARD_ORDER_REVIEW;
//            } else if (OrderTypeConstants.FREE.equals(type)) {
//                return ActivitiTypeEnums.FREE_SAMPLES_ORDER_REVIEW;
//            } else if (OrderTypeConstants.UNDERTAKES.equals(type)) {
//                return ActivitiTypeEnums.A_UNDERTAKES_TO_ORDER_REVIEW;
//            } else if (OrderTypeConstants.CREDIT.equals(type)) {
//                return ActivitiTypeEnums.CREDIT_VOUCHERS_ORDER_REVIEW;
//            } else if (OrderTypeConstants.DEBIT.equals(type)) {
//                return ActivitiTypeEnums.DEBIT_VOUCHER_ORDER_REVIEW;
//            }
//        } else {
//            if (OrderTypeConstants.STANDARD.equals(type)) {
//                return ActivitiTypeEnums.STANDARD_ORDER_REVIEW_MODIFY;
//            } else if (OrderTypeConstants.FREE.equals(type)) {
//                return ActivitiTypeEnums.FREE_SAMPLES_ORDER_REVIEW_MODIFY;
//            } else if (OrderTypeConstants.UNDERTAKES.equals(type)) {
//                return ActivitiTypeEnums.A_UNDERTAKES_TO_ORDER_REVIEW_MODIFY;
//            } else if (OrderTypeConstants.CREDIT.equals(type)) {
//                return ActivitiTypeEnums.CREDIT_VOUCHERS_ORDER_REVIEW_MODIFY;
//            } else if (OrderTypeConstants.DEBIT.equals(type)) {
//                return ActivitiTypeEnums.DEBIT_VOUCHER_ORDER_REVIEW_MODIFY;
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 通过订单的类型、信息获取ActivitiType对应type
//     * @param type
//     * @param salesOrderNo
//     * @return
//     */
//    public static ActivitiTypeEnums getActivityTypeByOrderInfo(String type, String salesOrderNo, String deliveryPlatform) {
//
//        if (StringUtils.isEmpty(salesOrderNo) && StringUtils.isEmpty(deliveryPlatform)) {
//            if (OrderTypeConstants.STANDARD.equals(type)) {
//                return ActivitiTypeEnums.STANDARD_ORDER_REVIEW;
//            } else if (OrderTypeConstants.FREE.equals(type)) {
//                return ActivitiTypeEnums.FREE_SAMPLES_ORDER_REVIEW;
//            } else if (OrderTypeConstants.UNDERTAKES.equals(type)) {
//                return ActivitiTypeEnums.A_UNDERTAKES_TO_ORDER_REVIEW;
//            } else if (OrderTypeConstants.CREDIT.equals(type)) {
//                return ActivitiTypeEnums.CREDIT_VOUCHERS_ORDER_REVIEW;
//            } else if (OrderTypeConstants.DEBIT.equals(type)) {
//                return ActivitiTypeEnums.DEBIT_VOUCHER_ORDER_REVIEW;
//            }
//        } else {
//            if (OrderTypeConstants.STANDARD.equals(type)) {
//                return ActivitiTypeEnums.STANDARD_ORDER_REVIEW_MODIFY;
//            } else if (OrderTypeConstants.FREE.equals(type)) {
//                return ActivitiTypeEnums.FREE_SAMPLES_ORDER_REVIEW_MODIFY;
//            } else if (OrderTypeConstants.UNDERTAKES.equals(type)) {
//                return ActivitiTypeEnums.A_UNDERTAKES_TO_ORDER_REVIEW_MODIFY;
//            } else if (OrderTypeConstants.CREDIT.equals(type)) {
//                return ActivitiTypeEnums.CREDIT_VOUCHERS_ORDER_REVIEW_MODIFY;
//            } else if (OrderTypeConstants.DEBIT.equals(type)) {
//                return ActivitiTypeEnums.DEBIT_VOUCHER_ORDER_REVIEW_MODIFY;
//            }
//        }
//        return null;
//    }
//
//
//    /**
//     * 通过订单信息获取ActivitiType对应type
//     * @param orderDO
//     * @return
//     */
//    public static ActivitiTypeEnums getActivityTypeByOrderInfo(OrderDO orderDO) {
//        if (null == orderDO) {
//            return null;
//        }
//        if (PlatformConstants.OMS.equals(orderDO.getDeliveryPlatform())) {
//            return ActivitiTypeEnums.getActivityTypeByOrderInfo(orderDO.getType(), orderDO.getSalesOrderNo(), orderDO.getDeliveryPlatform());
//        } else {
//            return ActivitiTypeEnums.getActivityTypeByOrderInfo(orderDO.getType(), orderDO.getSalesOrderNo());
//        }
//    }
//
//    /**
//     * 根据客户信息获取ActivitiType对应type
//     * @param customerDO
//     * @return
//     */
//    public static ActivitiTypeEnums getActivityTypeByCustInfo(CustomerDO customerDO) {
//        if (null == customerDO) {
//            return null;
//        }
//
//        if (StringUtils.isNotEmpty(customerDO.getCustomerCode())) {
//            return ActivitiTypeEnums.CUSTOMER_INFO_MODIFY;
//        } else {
//            return ActivitiTypeEnums.CUSTOMER_INFO_ADD;
//        }
//    }
//
//
//    /**
//     * 获取类型
//     *
//     * @param activitiTypeEnums
//     * @return
//     */
//
//    public static String getFiledTypeByCustInfo(ActivitiTypeEnums activitiTypeEnums) {
//        if (null == activitiTypeEnums) {
//            return null;
//        }
//
//        if (activitiTypeEnums.getCode().contains("order")) {
//            return FieldConstants.TableType.ORDER.getType();
//        } else if ((activitiTypeEnums.getCode().contains("customer")) ){
//            return FieldConstants.TableType.CUSTOMER.getType();
//        }else if ((activitiTypeEnums.getCode().contains("price")) ){
//            return FieldConstants.TableType.PRICE_LICENSE.getType();
//        }else if ((activitiTypeEnums.getCode().contains("rebates_manage")) ){
//            return FieldConstants.TableType.REBATES_MANAGE.getType();
//        }
//
//        return null;
//    }
//
//
//    /**
//     * 通过价格类型获取ActivitiType
//     *
//     * @param priceType
//     * @return
//     */
//    public static ActivitiTypeEnums getActivitiTypeByPriceType(Integer priceType) {
//        if (null == priceType) {
//            return null;
//        }
//
//        if (PriceTypeEnum.NEW.getCode().equals(priceType)) {
//            return ActivitiTypeEnums.PRICE_LICENSE_ADD;
//        } else if (PriceTypeEnum.OLD.getCode().equals(priceType)) {
//            return ActivitiTypeEnums.PRICE_LICENSE_MODIFY;
//        }
//
//        return null;
//    }


    public enum CatalogEnum {
        ORDER("order", "订单"),
        CUSTOMER("customer", "客户"),
        PRICE("price", "价格"),
        REBATES("rebates", "返利"),
        ;

        private final String type;
        private final String desc;

        CatalogEnum(String type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        public String getType() {
            return type;
        }

        public String getDesc() {
            return desc;
        }

        /**
         * 通过activitiType获取CatalogEnum
         *
         * @param activitiType
         * @return
         */
        public static CatalogEnum getCatalogByActivitiType(String activitiType) {
            for (CatalogEnum catalogEnum : CatalogEnum.values()) {
                if (activitiType.startsWith(catalogEnum.getType())) {
                    return catalogEnum;
                }
            }
            return null;
        }
    }

}
