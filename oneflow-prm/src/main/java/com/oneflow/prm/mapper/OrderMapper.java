package com.oneflow.prm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oneflow.prm.entity.dao.order.OrderDO;
import org.springframework.stereotype.Component;

@Component
public interface OrderMapper extends BaseMapper<OrderDO> {

    String selectImagePath();

    String selectEnglishCountryName(Long receiverCountryId);

    String selectPaymentTerms(Long paymentTermsId);
}
