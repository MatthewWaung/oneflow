package com.oneflow.prm.convert;

import com.oneflow.prm.core.common.PageResult;
import com.oneflow.prm.entity.dao.customer.CustomerDO;
import com.oneflow.prm.entity.vo.request.customer.CustModifyReq;
import com.oneflow.prm.entity.vo.request.customer.CustomerReq;
import com.oneflow.prm.entity.vo.response.customer.CustomerRes;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 使用mapstruct定义了接口，需要将项目编译compile在target中生成接口对应的实现类，否则就会报错
 */
@Mapper
public interface CustomerConvert {

    CustomerConvert INSTANCE = Mappers.getMapper(CustomerConvert.class);

    PageResult<CustomerRes> customersDOToRes(PageResult<CustomerDO> pageResult);

    CustomerRes customerDOToRes(CustomerDO customerDO);

    CustomerDO custReqToDO(CustomerReq customerReq);

    CustomerDO custModifyToCustomer(CustModifyReq custModifyReq);
}
