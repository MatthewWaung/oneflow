package com.oneflow.prm.service.component.credit;

import cn.hutool.core.util.StrUtil;
import com.oneflow.prm.entity.vo.request.order.CreditOrderExcelReq;
import com.oneflow.prm.mapper.CommonsMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 贷项凭证导入币别是否存在
 * 导入的币别必须存在币别主数据。不存在，报错“币别不存在”
 *
 * @author forest
 * @date 2024/07/03
 */

@Component
public class CurrencyCreditItemBizValidateHandler implements CreditOrderBizValidate {

    @Resource
    private CommonsMapper commonsMapper;

    @Override
    public void businessValidate(List<CreditOrderExcelReq> creditItemExcelReqList) {

        Set<String> allCurrency = commonsMapper.getAllCurrency();
        for (CreditOrderExcelReq creditItemExcelReq : creditItemExcelReqList) {
            String currency = creditItemExcelReq.getCurrency();
            if (StrUtil.isNotBlank(currency) && !allCurrency.contains(currency)) {
                creditItemExcelReq.addErrors("币别不存在！");
            }
        }

    }
}
