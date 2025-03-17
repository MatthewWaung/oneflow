package com.oneflow.prm.convert;

import com.oneflow.prm.entity.dao.customer.CustomerDO;
import com.oneflow.prm.entity.vo.request.customer.CustModifyReq;
import com.oneflow.prm.entity.vo.request.customer.CustomerReq;
import com.oneflow.prm.entity.vo.response.customer.CustomerRes;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CustInfoConvert {

    CustInfoConvert INSTANCE = Mappers.getMapper(CustInfoConvert.class);

    List<CustomerRes> customerDOToList(List<CustomerDO> customerResList);

    CustomerDO custModifyToCustomer(CustModifyReq custModifyReq);

    CustomerDO customerReqToDO(CustomerReq customerReq);
}
