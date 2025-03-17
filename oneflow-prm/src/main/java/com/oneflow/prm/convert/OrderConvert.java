package com.oneflow.prm.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderConvert {

    OrderConvert INSTANCE = Mappers.getMapper(OrderConvert.class);

}
