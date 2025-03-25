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
 * 具体类别校验:
 * 导入的具体类别必须存在具体类别主数据中，不存在，导入时报错“具体类别不存在！”
 *
 * @author forest
 * @date 2024/07/03
 */
@Component
public class CategoriesCreditItemBizValidateHandler implements CreditOrderBizValidate {

    @Resource
    private CommonsMapper commonsMapper;

    @Override
    public void businessValidate(List<CreditOrderExcelReq> creditItemExcelReqList) {

        // 具体类别校验
        List<String> categoriesList = commonsMapper.getByDictType(SysDictTypeConstants.SPECIFIC_CATEGORIES)
                .stream()
                .map(SysDictData::getDictLabel)
                .collect(Collectors.toList());

        for (CreditOrderExcelReq creditItemExcelReq : creditItemExcelReqList) {
            String specificCategoriesLabel = creditItemExcelReq.getSpecificCategoriesLabel();
            if (StrUtil.isNotBlank(specificCategoriesLabel) && !categoriesList.contains(specificCategoriesLabel)) {
                creditItemExcelReq.addErrors("具体类别不存在！");
            }
        }
    }
}
