package com.oneflow.prm.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oneflow.prm.convert.CustInfoConvert;
import com.oneflow.prm.convert.CustomerConvert;
import com.oneflow.prm.core.common.PageResult;
import com.oneflow.prm.core.common.R;
import com.oneflow.prm.core.enums.StatusEnum;
import com.oneflow.prm.entity.dao.customer.*;
import com.oneflow.prm.entity.dto.CustExportDTO;
import com.oneflow.prm.entity.vo.request.customer.CustInfoReq;
import com.oneflow.prm.entity.vo.request.customer.CustModifyReq;
import com.oneflow.prm.entity.vo.request.customer.CustStatusReq;
import com.oneflow.prm.entity.vo.request.customer.CustomerReq;
import com.oneflow.prm.entity.vo.response.customer.CustomerRes;
import com.oneflow.prm.mapper.*;
import com.oneflow.prm.service.ICustomerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * service实现类 要继承mybatis-plus提供的通用service基类，并传入两个泛型：mapper和pojo
 */
@Slf4j
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, CustomerDO> implements ICustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ReceiverMapper receiverMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private SalesViewMapper salesViewMapper;

    @Autowired
    private SalespersonMapper salespersonMapper;

    @Autowired
    private BalanceLimitMapper balanceLimitMapper;


    @Override
    public PageResult<CustomerRes> getCustomer(CustInfoReq req) {
        PageResult<CustomerRes> pageResult = new PageResult<>();
        pageResult.setPage(req.getPage());
        pageResult.setPageSize(req.getPageSize());
        List<Long> customerIdList = new ArrayList<>();
        if (req.getCustomerId() != null && !req.getCustomerId().equals("")) {
            customerIdList.add(Long.valueOf(req.getCustomerId()));
        }

        //从副表中查询出customerId
        if (req.getSalesOrganizationId() != null && !req.getSalesOrganizationId().equals("")) {
            List<Long> customerIdByOrg = salesViewMapper.selectCustomerIdByOrg(req.getSalesOrganizationId());
            if (customerIdList.size() != 0 && customerIdList != null) {
                customerIdList.retainAll(customerIdByOrg);
//                customerIdList.stream().filter(item -> customerIdByOrg.contains(item)).collect(Collectors.toList());
            } else {
                customerIdList.addAll(customerIdByOrg);   //取customerId的交集
            }
        }
        if (req.getSalesTypeId() != null && !req.getSalesTypeId().equals("")) {
            List<Long> customerIdByType = salesViewMapper.selectCustomerIdByType(req.getSalesTypeId());
            if (customerIdList.size() != 0 && customerIdList != null) {
                customerIdList.retainAll(customerIdByType);
            } else {
                customerIdList.addAll(customerIdByType);
            }
        }
        if (req.getChannelId() != null && !req.getChannelId().equals("")) {
            List<Long> customerIdByChannel = salesViewMapper.selectCustomerIdByChannel(req.getChannelId());
            if (customerIdList.size() != 0 && customerIdList != null) {
                customerIdList.retainAll(customerIdByChannel);
            } else {
                customerIdList.addAll(customerIdByChannel);
            }
        }

        //通过customerId查出客户表的主体信息
        List<CustomerDO> customerList = customerMapper.selectCustomer(req, customerIdList, (req.getPage() - 1) * req.getPageSize(), req.getPageSize());
        Long count = customerMapper.countList(req, customerIdList);
        List<CustomerDO> customerResList = new ArrayList<>();
        if (customerList.size() > 0 && customerList != null) {
            //得到分页数据后的customerId，去查询副表信息
            List<Long> customerIds = customerList.stream().map(tmp -> tmp.getId()).collect(Collectors.toList());

            //一次性从副表中查出数据，然后再JVM中进行分类组装，使用stream流的方式使代码简洁
            List<SalesViewDO> salesViewList = salesViewMapper.selectList(new QueryWrapper<SalesViewDO>().in("customer_id", customerIds));
            Map<Long, List<SalesViewDO>> salesViewMap = salesViewList.stream().collect(Collectors.groupingBy(SalesViewDO::getCustomerId));  //以customerId进行分组
            List<SalespersonDO> salespersonList = salespersonMapper.selectList(new QueryWrapper<SalespersonDO>().in("customer_id", customerIds));
            Map<Long, List<SalespersonDO>> salespersonMap = salespersonList.stream().collect(Collectors.groupingBy(SalespersonDO::getCustomerId));
            List<ReceiverDO> receiverList = receiverMapper.selectList(new QueryWrapper<ReceiverDO>().in("customer_id", customerIds));
            Map<Long, List<ReceiverDO>> receiverMap = receiverList.stream().collect(Collectors.groupingBy(ReceiverDO::getCustomerId));
            List<AddressDO> addressList = addressMapper.selectList(new QueryWrapper<AddressDO>().in("customer_id", customerIds));
            Map<Long, List<AddressDO>> addressMap = addressList.stream().collect(Collectors.groupingBy(AddressDO::getCustomerId));
            List<BalanceLimitDO> balanceLimitList = balanceLimitMapper.selectList(new QueryWrapper<BalanceLimitDO>().in("customer_id", customerIds));
            Map<Long, BalanceLimitDO> balanceLimitMap = balanceLimitList.stream().collect(Collectors.toMap(BalanceLimitDO::getCustomerId, a -> a));
            //按照key值将信息赋值给相应对象
            for (CustomerDO customerDO : customerList) {
                Long customerId = customerDO.getId();
                customerDO.setSalesViewList(salesViewMap.get(customerId));
                customerDO.setSalespersonList(salespersonMap.get(customerId));
                customerDO.setReceiverList(receiverMap.get(customerId));
                customerDO.setAddressList(addressMap.get(customerId));
                customerDO.setBalanceLimit(balanceLimitMap.get(customerId));
            }


//            List<SalesViewDO> salesViewList = salesViewMapper.selectList(new QueryWrapper<SalesViewDO>().in("customer_id", customerIds));
//            Map<Long, List<SalesViewDO>> collect = salesViewList.stream().collect(Collectors.groupingBy(SalesViewDO::getCustomerId));

            //错误的写法，salesViewList、salespersonList等直接放进customerDO，并没有把对应的customerId对应起来，导致查询结果错误，往往因为数据库只造了一条数据而没有发现这个bug。
//            List<SalesViewDO> salesViewList = salesViewMapper.selectList(new QueryWrapper<SalesViewDO>().in("customer_id", customerIds));
//            List<SalespersonDO> salespersonList = salespersonMapper.selectList(new QueryWrapper<SalespersonDO>().in("customer_id", customerIds));
//            List<ReceiverDO> receiverList = receiverMapper.selectList(new QueryWrapper<ReceiverDO>().in("customer_id", customerIds));
//            List<AddressDO> addressList = addressMapper.selectList(new QueryWrapper<AddressDO>().in("customer_id", customerIds));
//            for (CustomerDO customerDO : customerList) {
//                customerDO.setSalesViewList(salesViewList);
//                customerDO.setSalespersonList(salespersonList);
//                customerDO.setReceiverList(receiverList);
//                customerDO.setAddressList(addressList);
//            }
            //通过这种循环，每页查10条，每次循环查5条，会大量占用数据库连接池线程，当有大并发的情况，会造成死锁
            //循环的方式会查询多次，效率不高
            /**
             for (Long customerId : customerIds) {
             CustomerDO customerDO = new CustomerDO();
             customerDO = customerMapper.selectById(customerId);
             List<SalesViewDO> salesViewList = salesViewMapper.selectList(new QueryWrapper<SalesViewDO>().eq("customer_id", customerId));
             List<SalespersonDO> salespersonList = salespersonMapper.selectList(new QueryWrapper<SalespersonDO>().eq("customer_id", customerId));
             List<ReceiverDO> receiverList = receiverMapper.selectList(new QueryWrapper<ReceiverDO>().eq("customer_id", customerId));
             List<AddressDO> addressList = addressMapper.selectList(new QueryWrapper<AddressDO>().eq("customer_id", customerId));
             customerDO.setSalesViewList(salesViewList);
             customerDO.setSalespersonList(salespersonList);
             customerDO.setReceiverList(receiverList);
             customerDO.setAddressList(addressList);
             customerResList.add(customerDO);
             }*/
        }

//        List<CustomerDO> customerList = customerMapper.selectCustomer(customerIdList, (req.getPage()-1) * req.getPageSize(), req.getPageSize());
        List<CustomerRes> customerRes = CustInfoConvert.INSTANCE.customerDOToList(customerResList);
        pageResult.setTotal(count);
        pageResult.setResult(customerRes);
        return pageResult;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateCustomer(CustModifyReq custModifyReq) {
        CustomerDO customerDO = CustInfoConvert.INSTANCE.custModifyToCustomer(custModifyReq);
        List<ReceiverDO> receiverList = customerDO.getReceiverList();
        List<AddressDO> addressList = customerDO.getAddressList();

        //修改customer表的业务员
        UpdateWrapper<CustomerDO> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.lambda().set(CustomerDO::getSalesperson, custModifyReq.getSalesperson())
//                              .eq(CustomerDO::getId, custModifyReq.getId());
        int result = customerMapper.update(null, updateWrapper);

        //修改收货人信息（先删后插）
        QueryWrapper<ReceiverDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ReceiverDO::getCustomerId, custModifyReq.getId());
        int delRes = receiverMapper.delete(queryWrapper);
        for (ReceiverDO receiverDO : receiverList) {
            receiverDO.setCustomerId(Long.valueOf(custModifyReq.getId()));
//            receiverDO.setCustomerName(custModifyReq.getCustomerName());
            receiverDO.setStatus(StatusEnum.ENABLE.getStatus());
            int insertRes = receiverMapper.insertReceiver(receiverDO);
        }

        //修改收货地址信息（先删后插）
        QueryWrapper<AddressDO> addressWrapper = new QueryWrapper<>();
        addressWrapper.lambda().eq(AddressDO::getCustomerId, custModifyReq.getId());
        addressMapper.delete(addressWrapper);
        for (AddressDO addressDO : addressList) {
            addressDO.setCustomerId(Long.valueOf(custModifyReq.getId()));
//            addressDO.setCustomerName(custModifyReq.getCustomerName());
            addressDO.setStatus(StatusEnum.ENABLE.getStatus());
            addressMapper.insertAddress(addressDO);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer modifyStatus(CustStatusReq custStatusReq) {
        Integer result = null;
        // 前端传来的状态是要修改的最终状态
        if (custStatusReq.getStatus().equals(StatusEnum.FREEZE.getStatus())) {
            result = customerMapper.modifyCustStatus1(custStatusReq.getId());
//            customerMapper.modifyReceiverStatus1(custStatusReq.getId());
//            customerMapper.modifyAddressStatus1(custStatusReq.getId());
        } else if (custStatusReq.getStatus().equals(StatusEnum.ENABLE.getStatus())) {
            result = customerMapper.modifyCustStatus0(custStatusReq.getId());
//            customerMapper.modifyReceiverStatus0(custStatusReq.getId());
//            customerMapper.modifyAddressStatus0(custStatusReq.getId());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> addCustomer(CustomerReq customerReq) {
        Map<String, String> returnMap = new HashMap<>();

        //参数校验：同一个国家客户名称不能相同
        List<Map<String, String>> custAndCountry = customerMapper.selectCustAndCountry();
        for (Map<String, String> stringMap : custAndCountry) {
            stringMap.get("customerName");
        }


        CustomerDO customerDO = CustInfoConvert.INSTANCE.customerReqToDO(customerReq);
        customerDO.setStatus(StatusEnum.ENABLE.getStatus());
        customerMapper.insert(customerDO);

        List<SalesViewDO> salesViewList = customerDO.getSalesViewList();
        List<SalespersonDO> salespersonList = customerDO.getSalespersonList();
        List<ReceiverDO> receiverList = customerDO.getReceiverList();
        List<AddressDO> addressList = customerDO.getAddressList();

        for (SalesViewDO salesViewDO : salesViewList) {
            salesViewDO.setCustomerId(customerDO.getId());
            salesViewDO.setCustomerName(customerDO.getCustomerName());
            salesViewDO.setStatus(StatusEnum.ENABLE.getStatus());
            salesViewMapper.insert(salesViewDO);
        }

        for (SalespersonDO salespersonDO : salespersonList) {
            salespersonDO.setCustomerId(customerDO.getId());
            salespersonDO.setCustomerName(customerDO.getCustomerName());
            salespersonDO.setStatus(StatusEnum.ENABLE.getStatus());
            salespersonMapper.insert(salespersonDO);
        }

        for (ReceiverDO receiverDO : receiverList) {
            receiverDO.setCustomerId(customerDO.getId());
            receiverDO.setCustomerName(customerDO.getCustomerName());
            receiverDO.setStatus(StatusEnum.ENABLE.getStatus());
            receiverMapper.insert(receiverDO);
        }

        for (AddressDO addressDO : addressList) {
            addressDO.setCustomerId(customerDO.getId());
            addressDO.setCustomerName(customerDO.getCustomerName());
            addressDO.setStatus(StatusEnum.ENABLE.getStatus());
            addressMapper.insert(addressDO);
        }

        returnMap.put("status", "true");
        returnMap.put("error", "");
        return returnMap;
    }

    //====================================================================================================================================
    //放在了公共controller
    @Override
    public List<Map<String, Object>> findCustomerByName(String name) {
        List<Map<String, Object>> customer = customerMapper.findCustomerByName(name);
        return customer;
    }

    @Override
    public List<Map<String, Object>> findSalesperson(String name) {
        return customerMapper.findSalesperson(name);
    }

    @Override
    public List<Map<String, Object>> findContact(String contact) {
        return customerMapper.findContact(contact);
    }


    //====================================================================================================================================
    public Integer addCustomer2(CustomerReq customerReq) {
        CustomerDO customerDO = CustomerConvert.INSTANCE.custReqToDO(customerReq);
        customerMapper.insert(customerDO);
        return 0;
    }

    @Override
    public PageResult<CustomerRes> getAllCustomer2(CustInfoReq req) {
        IPage<CustomerDO> page = new Page<>(req.getPage(), req.getPageSize());
        IPage<CustomerDO> pageResult = customerMapper.selectPage(page, new QueryWrapper<>());
        PageResult<CustomerDO> customers = new PageResult<>(pageResult);
        PageResult<CustomerRes> result = CustomerConvert.INSTANCE.customersDOToRes(customers);
        return result;
    }

    @Override
    public CustomerRes getCustById2(Long id) {
        CustomerDO customerDO = customerMapper.selectById(id);
        CustomerRes customerRes = CustomerConvert.INSTANCE.customerDOToRes(customerDO);
        return customerRes;
    }

    @Override
    public PageResult<CustomerRes> findCustomer2(CustInfoReq custInfoReq) {
        IPage<CustomerDO> page = new Page<>(custInfoReq.getPage(), custInfoReq.getPageSize());
        QueryWrapper<CustomerDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(custInfoReq.getName()), CustomerDO::getCustomerName, custInfoReq.getName());
//                .like(StringUtils.isNotBlank(custInfoReq.getSalesperson()), CustomerDO ::getSalesperson, custInfoReq.getSalesperson())
//                .like(StringUtils.isNotBlank(custInfoReq.getContact()), CustomerDO ::getContact, custInfoReq.getContact());
        IPage<CustomerDO> pageResult = customerMapper.selectPage(page, queryWrapper);
        PageResult<CustomerDO> customers = new PageResult<>(pageResult);
        PageResult<CustomerRes> result = CustomerConvert.INSTANCE.customersDOToRes(customers);
        return result;
    }

    @Override
    public Integer updateCustomer2(CustModifyReq custModifyReq) {
        CustomerDO customerDO = CustomerConvert.INSTANCE.custModifyToCustomer(custModifyReq);
        UpdateWrapper<CustomerDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(CustomerDO::getId, custModifyReq.getId());
        return customerMapper.update(customerDO, updateWrapper);
    }

    @Override
    public Integer modifyStatus2(CustStatusReq custStatusReq) {
        UpdateWrapper<CustomerDO> updateWrapper = new UpdateWrapper<>();
        if (custStatusReq.getStatus().equals("0")) {
            updateWrapper.lambda()
                    .set(CustomerDO::getStatus, 1)
                    .eq(CustomerDO::getId, custStatusReq.getId());
        } else if (custStatusReq.getStatus().equals("1")) {
            updateWrapper.lambda()
                    .set(CustomerDO::getStatus, 0)
                    .eq(CustomerDO::getId, custStatusReq.getId());
        }
        return customerMapper.update(null, updateWrapper);
    }


    @Override
    public PageResult<CustomerRes> getAllCustomer(CustInfoReq req) {
        return null;
    }

    @Override
    public CustomerRes getCustById(Long id) {
        return null;
    }

    @Override
    public PageResult<CustomerRes> findCustomer(CustInfoReq custInfoReq) {
        return null;
    }


    @Override
    public R exportCustomer(HttpServletResponse response, CustInfoReq req) {
        OutputStream outputStream = null;
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止浏览器端导出excel文件名中文乱码
            String fileName = URLEncoder.encode("客户信息", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            outputStream = response.getOutputStream();
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
            //必须放到循环外，否则会刷新流
            ExcelWriter excelWriter = EasyExcel.write(outputStream).build();

            List<Long> customerIdList = new ArrayList<>();
            Integer custCount = Math.toIntExact(customerMapper.countList(req, customerIdList));
            Integer pageSize = 1000;
            Integer writeCount = custCount % pageSize == 0 ? (custCount / pageSize) : (custCount / pageSize + 1);

            List<CustExportDTO> custExportList = new ArrayList<>();
            for (int i = 0; i < writeCount; i++) {
                //分批导出，查询数据库类似于分页查询
                List<CustomerDO> customerList = customerMapper.selectCustomer(req, customerIdList, i * pageSize, pageSize);
                for (CustomerDO customerDO : customerList) {
                    CustExportDTO custExportDTO = new CustExportDTO();
                    custExportDTO.setCustomerName(customerDO.getCustomerName());
                    custExportDTO.setCustomerPhone(customerDO.getCustomerPhone());
                    custExportDTO.setCustomerEmail(customerDO.getCustomerEmail());
                    custExportList.add(custExportDTO);
                }

                WriteSheet writeSheet = EasyExcel.writerSheet(0, "customer").head(CustExportDTO.class)
                        .registerWriteHandler(new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle))
                        .build();
                excelWriter.write(custExportList, writeSheet);
            }
            excelWriter.finish();
            outputStream.flush();

            //easyExcel导出有两种写法：上面是第一种写法，下面是另外一种写法：
            EasyExcel.write(response.getOutputStream(), CustExportDTO.class)
                    .registerWriteHandler(new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle))
                    .sheet("customer").doWrite(custExportList);
        } catch (IOException e) {
            log.info("IOException：", e);
        } catch (Exception e) {
            log.info("Exception：", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.info("IOException：", e);
                }
            }
        }
        return R.ok();
    }
}
