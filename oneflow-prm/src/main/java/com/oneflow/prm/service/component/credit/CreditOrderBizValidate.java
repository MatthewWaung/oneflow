package com.oneflow.prm.service.component.credit;

import com.oneflow.prm.entity.vo.request.order.CreditOrderExcelReq;

import java.util.List;

/**
 * 贷项凭证业务处理
 *
 * @author forest
 * @date 2024/07/03
 */
public interface CreditOrderBizValidate {


    /**
     *  业务数据校验 ：业务数据有关联关系，所以把相关的放在一起，不同类执行顺序处理
     *
     * @param creditItemExcelReqList 贷项凭证导入数据
     */
    void businessValidate(List<CreditOrderExcelReq> creditItemExcelReqList);

}
