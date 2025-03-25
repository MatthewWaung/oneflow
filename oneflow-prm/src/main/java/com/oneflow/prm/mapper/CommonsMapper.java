package com.oneflow.prm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oneflow.prm.entity.dao.commons.CommonsDO;
import com.oneflow.prm.entity.dao.sys.SysDictData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public interface CommonsMapper {

    List<SysDictData> getByDictType(String dictType);

    Set<String> getAllCurrency();

}
