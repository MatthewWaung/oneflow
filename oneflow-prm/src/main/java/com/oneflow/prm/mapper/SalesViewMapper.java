package com.oneflow.prm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oneflow.prm.entity.dao.customer.SalesViewDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesViewMapper extends BaseMapper<SalesViewDO> {

    List<Long> selectCustomerIdByType(String salesTypeId);

    List<Long> selectCustomerIdByChannel(String channelId);

    List<Long> selectCustomerIdByOrg(String salesOrganizationId);
}
