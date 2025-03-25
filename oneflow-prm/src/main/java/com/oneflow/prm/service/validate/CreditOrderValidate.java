package com.oneflow.prm.service.validate;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.oneflow.prm.core.constant.SysDictTypeConstants;
import com.oneflow.prm.core.exception.CustomException;
import com.oneflow.prm.entity.dao.sys.SysDictData;
import com.oneflow.prm.entity.vo.request.order.CreditOrderExcelReq;
import com.oneflow.prm.mapper.CommonsMapper;
import com.oneflow.prm.service.component.credit.CreditOrderBizValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 贷项凭证校验
 *
 * @author forest
 * @date 2024/07/03
 */
@Component
public class CreditOrderValidate {

    private static final Logger log = LoggerFactory.getLogger(CreditOrderValidate.class);

    /**
     * @Resource 注解：
     * @Resource 是 Java 的标准注解，用于依赖注入。它可以注入单个 Bean 或者一个 Bean 的集合。
     * 如果有多个类实现了同一个接口（例如 CreditOrderBizValidate），Spring 会将这些类的实例收集到一个 List 中。
     */
    @Resource
    private List<CreditOrderBizValidate> creditItemBizValidateList;

    @Resource
    private CommonsMapper commonsMapper;

    /**
     * 贷项凭证校验
     *
     * @param CreditOrderExcelReqList 解析后的excel数据
     */
    public void validate(List<CreditOrderExcelReq> CreditOrderExcelReqList) {

        // 0.填充事前返利编码value值
        Map<String, String> specificCategoriesMap = getSpecificCategoriesMap();
        for (CreditOrderExcelReq CreditOrderExcelReq : CreditOrderExcelReqList) {
            CreditOrderExcelReq.setSpecificCategoriesValue(specificCategoriesMap.get(CreditOrderExcelReq.getSpecificCategoriesLabel()));
        }

        // 1. 基本数据校验
        baseDataValidate(CreditOrderExcelReqList);

        // 2.业务校验
        businessValidate(CreditOrderExcelReqList);
    }

    //具体类别查询
    private Map<String, String> getSpecificCategoriesMap() {
        return commonsMapper.getByDictType(SysDictTypeConstants.SPECIFIC_CATEGORIES)
            .stream()
            .collect(Collectors.toMap(SysDictData::getDictLabel, SysDictData::getDictValue));
    }

    // 业务校验
    private void businessValidate(List<CreditOrderExcelReq> CreditOrderExcelReqList) {

        /**
         *  Spring 框架通过依赖注入（DI）机制将所有实现了某个接口的类自动注入到一个 List 中。
         *  因此，creditItemBizValidateList 是一个包含所有 CreditOrderBizValidate 接口实现类的列表。
         *
         *  接口多实现类的使用方式一：遍历循环所有实现这个接口的实现类
         *  接口多实现类的使用方式二：使用策略模式，每个实现类中定义一个标记，如一个枚举类，通过枚举类来区分执行具体哪一个实现类，见com.oneflow.prm.service.component.activiti.ApprovalHandler的用法
         */

        creditItemBizValidateList.forEach(creditItemBizValidate -> creditItemBizValidate.businessValidate(CreditOrderExcelReqList));
    }

    // 基本数据校验
    private void baseDataValidate(List<CreditOrderExcelReq> CreditOrderExcelReqList) {

        // 1. 数据非空和超长校验
        filterAndcheckDataSize(CreditOrderExcelReqList);

        // 2.group数据行为空
        checkGroupData(CreditOrderExcelReqList);

        // 3.基本数据校验
        checkBaseDate(CreditOrderExcelReqList);

        // 4.group订单头校验
        groupOrderHeaderValidate(CreditOrderExcelReqList);

    }

    // 基本数据校验
    private void checkBaseDate(List<CreditOrderExcelReq> CreditOrderExcelReqList) {
//        // 查询具体类别 key-value
//        Map<String, String> specificCateGories = commonsMapper.getDictType(SysDictTypeConstants.SPECIFIC_CATEGORIES)
//                .stream()
//                .collect(Collectors.toMap(SysDictDataRes::getDictValue, SysDictDataRes::getDictLabel));
//
//        // 查询具体类别必填的类别
//        List<String> returnBeforeApplyCodeConditionList = commonsMapper.getDictType(SysDictTypeConstants.RETURN_BEFORE_APPLY_CODE_CONDITION)
//                .stream()
//                .map(SysDictDataRes::getDictLabel)
//                .collect(Collectors.toList());
//
//        //查询所有的具体类别分类条件
//        Map<String, Set<String>> specificCategoriesSetByLabel = commonsMapper.getDictType(
//                SysDictTypeConstants.RETURN_SPECIFIC_CATEGORIES_SET)
//            .stream()
//            .collect(Collectors.toMap(SysDictDataRes::getDictLabel, res -> Arrays.stream(res.getDictValue().split(","))
//                .collect(Collectors.toSet())));
//
//        // 遍历所有数据进行校验
//        CreditOrderExcelReqList.forEach(CreditOrderExcelReq -> {
//            CreditOrderExcelReq.initValidate(returnBeforeApplyCodeConditionList,specificCateGories,specificCategoriesSetByLabel);
//        });
    }

    // group订单头校验
    private void groupOrderHeaderValidate(List<CreditOrderExcelReq> CreditOrderExcelReqList) {
        Map<String, List<CreditOrderExcelReq>> map = CreditOrderExcelReqList
                .stream()
                .collect(Collectors.groupingBy(CreditOrderExcelReq::getGroup));
        map.forEach((group, mapList) -> {
            int size = mapList
                    .stream()
                    .map(CreditOrderExcelReq::getKey)
                    .collect(Collectors.toSet())
                    .size();
            if (size > 1) {
                mapList.forEach(p -> {
                    p.setValidateResult(false);
                    p.getErrors().add("Group相同的行，【具体类别】到【订单备注/领用原因】列字段必须一致！");
                });
            }

        });


    }

    // group数据行全为空
    private void checkGroupData(List<CreditOrderExcelReq> CreditOrderExcelReqList) {
        // 过滤group数据行为空的数据
        List<CreditOrderExcelReq> groupIsBlankList = CreditOrderExcelReqList
                .stream()
                .filter(p -> StrUtil.isBlank(p.getGroup()))
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(groupIsBlankList)) {
            Set<Integer> indexLinde = groupIsBlankList.stream().map(CreditOrderExcelReq::getIndex).collect(Collectors.toSet());
            // indexLine 转成 1,2,3,4,5
            String indexLindeStr = StrUtil.join(",", indexLinde);
            throw new CustomException("第【" + indexLindeStr + "】行group不能为空");
        }

    }

    // 过滤orderNo不为空的数据，数据非空和超长校验
    private void filterAndcheckDataSize(List<CreditOrderExcelReq> CreditOrderExcelReqList) {

        // 获取orderNo不等于空的group
        Set<String> filterGroup = CreditOrderExcelReqList
                .stream()
                .filter(p -> StrUtil.isNotBlank(p.getOrderNo()))
                .map(CreditOrderExcelReq::getGroup)
                .collect(Collectors.toSet());


        // 过滤orderNo不为空的数据,有orderNo的数据，后续也不用处理,通过迭代器处理
        CreditOrderExcelReqList.removeIf(CreditOrderExcelReq -> filterGroup.contains(CreditOrderExcelReq.getGroup()));

        // 控制最大导入数据量
        int maxsize = 200;
        if (CollectionUtil.isEmpty(CreditOrderExcelReqList)) {
            throw new CustomException("可处理数据为空");
        }
        if (CreditOrderExcelReqList.size()>maxsize) {
            throw new CustomException("导入不能超过"+maxsize+"行");
        }
    }

}
