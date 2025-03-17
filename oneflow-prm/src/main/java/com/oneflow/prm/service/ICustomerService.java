package com.oneflow.prm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oneflow.prm.core.common.PageResult;
import com.oneflow.prm.core.common.R;
import com.oneflow.prm.entity.dao.customer.CustomerDO;
import com.oneflow.prm.entity.vo.request.customer.CustInfoReq;
import com.oneflow.prm.entity.vo.request.customer.CustModifyReq;
import com.oneflow.prm.entity.vo.request.customer.CustStatusReq;
import com.oneflow.prm.entity.vo.request.customer.CustomerReq;
import com.oneflow.prm.entity.vo.response.customer.CustomerRes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface ICustomerService extends IService<CustomerDO> {

    Map<String, String> addCustomer(CustomerReq customerReq);

    PageResult<CustomerRes> getAllCustomer(CustInfoReq req);

    CustomerRes getCustById(Long id);

    PageResult<CustomerRes> findCustomer(CustInfoReq custInfoReq);

    PageResult<CustomerRes> getCustomer(CustInfoReq req);

    List<Map<String, Object>> findCustomerByName(String name);

    List<Map<String, Object>> findSalesperson(String name);

    List<Map<String, Object>> findContact(String contact);

    Integer updateCustomer(CustModifyReq custModifyReq);

    Integer modifyStatus(CustStatusReq custStatusReq);

    PageResult<CustomerRes> getAllCustomer2(CustInfoReq req);

    CustomerRes getCustById2(Long id);

    PageResult<CustomerRes> findCustomer2(CustInfoReq custInfoReq);

    Integer updateCustomer2(CustModifyReq custModifyReq);

    Integer modifyStatus2(CustStatusReq custStatusReq);

    R exportCustomer(HttpServletResponse response, CustInfoReq req);
}
