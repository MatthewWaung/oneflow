package com.oneflow.prm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oneflow.prm.entity.dao.customer.CustomerDO;
import com.oneflow.prm.entity.vo.request.customer.CustInfoReq;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface CustomerMapper extends BaseMapper<CustomerDO> {

    List<Map<String, Object>> findCustomerByName(@Param("name") String name);

    List<Map<String, Object>> findSalesperson(@Param("name") String name);

    List<Map<String, Object>> findContact(@Param("contact") String contact);

    List<CustomerDO> selectCustomer(@Param("req") CustInfoReq req, @Param("customerIds") List<Long> customerIdList, @Param("page")Integer page, @Param("pageSize")Integer pageSize);

    Long countList(@Param("req") CustInfoReq req, @Param("customerIdList") List<Long> customerIdList);

//    Long countList(@Param("req") CustInfoReq req);

    Integer modifyCustStatus1(@Param("id") Long id);

    Integer modifyCustStatus0(@Param("id") Long id);

    Integer modifyReceiverStatus1(@Param("id") Long id);

    Integer modifyReceiverStatus0(@Param("id") Long id);

    Integer modifyAddressStatus1(@Param("id") Long id);

    Integer modifyAddressStatus0(@Param("id") Long id);


    List<Map<String, String>> selectCustAndCountry();
}
