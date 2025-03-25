package com.oneflow.prm.service.component.credit;

import cn.hutool.core.util.StrUtil;
import com.oneflow.prm.core.constant.SysDictTypeConstants;
import com.oneflow.prm.entity.dao.sys.SysDictData;
import com.oneflow.prm.entity.vo.request.order.CreditOrderExcelReq;
import com.oneflow.prm.mapper.CommonsMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 返利原因
 *
 * @author forest
 * @date 2024/07/03
 */
@Component
public class ReturnReasonBizValidateHandler implements CreditOrderBizValidate {

    @Resource
    private CommonsMapper commonsMapper;

    @Override
    public void businessValidate(List<CreditOrderExcelReq> creditItemExcelReqList) {

        // 返利原因
        List<String> returnReasonList = commonsMapper.getByDictType(SysDictTypeConstants.RETURN_REASON)
                .stream()
                .map(SysDictData::getDictValue)
                .collect(Collectors.toList());

        for (CreditOrderExcelReq creditItemExcelReq : creditItemExcelReqList) {
            String returnReason = creditItemExcelReq.getReturnReason();
            if (StrUtil.isNotBlank(returnReason) && !returnReasonList.contains(returnReason)) {
                creditItemExcelReq.addErrors("返利原因不存在！");
            }
        }
    }
}
