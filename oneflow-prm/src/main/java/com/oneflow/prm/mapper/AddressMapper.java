package com.oneflow.prm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oneflow.prm.entity.dao.customer.AddressDO;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressMapper extends BaseMapper<AddressDO> {
    void insertAddress(AddressDO addressDO);
}
