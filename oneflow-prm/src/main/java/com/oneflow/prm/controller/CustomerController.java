package com.oneflow.prm.controller;

import com.oneflow.prm.core.common.PageResult;
import com.oneflow.prm.core.common.R;
import com.oneflow.prm.entity.vo.request.customer.CustInfoReq;
import com.oneflow.prm.entity.vo.request.customer.CustModifyReq;
import com.oneflow.prm.entity.vo.request.customer.CustStatusReq;
import com.oneflow.prm.entity.vo.request.customer.CustomerReq;
import com.oneflow.prm.entity.vo.response.customer.CustomerRes;
import com.oneflow.prm.service.ICustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(tags = "客户管理")
@RestController
@RequestMapping("/cust")
public class CustomerController {

    /**
     * swagger3访问地址：http://localhost:8010/swagger-ui/index.html
     */

    /**
     * 接口文档使用swagger3
     * Swagger 3.0配置整合使用教程：https://blog.csdn.net/weixin_44203158/article/details/109137799
     * 1、导入swagger3依赖
     * 2、启动类添加 @EnableOpenApi注解
     * 3、按需在yml文件中添加swagger配置，并添加配置类（会自动覆盖默认的配置），两者配合
     * 4、启动后，访问：http://localhost:8080/swagger-ui/index.html（主要swagger3和swagger2访问路径的区别）
     * 一般情况下，我们只有在开发环境下才会用到swagger，正式环境需要关闭swagger，一个是安全问题，还有一个是用了swagger会影响系统运行速度
     * 我们可以设置Docket对象的 enable属性来关闭swagger。
     *
     * 通过swagger3配置类设置 开关 &过滤 &分组
     * 通过 Docket.enable(true)设置是否打开swagger
     * 通过 paths(PathSelectors.ant("/**"))设置过滤条件
     * 通过 Docket createRestApi()复制多份设置分组
     *
     * swagger注解描述接口：
     * 在类上添加 @Api注解，在方法上添加 @ApiOperation注解
     * @ApiModle 用于响应类，表示返回数据的信息，一般配合post请求的 @RequestBody使用
     * @ApiModelProperty 用在属性上，描述响应类的属性
     */

    @Autowired
    private ICustomerService customerService;

    @ApiOperation(value = "添加客户信息")
    @PostMapping("/addCustomer")
    public R<Integer> addCustomer(@RequestBody CustomerReq customerReq){
        customerService.addCustomer(customerReq);
        //可以直接调用IService的saveOrUpdate方法，会根据主键先查询，如果存在就更新，不存在就插入
//        customerService.saveOrUpdate(CustomerConvert.INSTANCE.custReqToDO(customerReq));
        return R.ok();
    }

    @ApiOperation(value = "获取客户列表")
    @PostMapping("/getCustomer")
    public R<PageResult<CustomerRes>> getCustomer(@RequestBody CustInfoReq req){
        PageResult<CustomerRes> customerRes = customerService.getCustomer(req);
        return R.ok(customerRes);
    }

    //@PathVariable一般要和@RequestMapping(method = RequestMethod.GET)一起配合使用
    @ApiOperation(value = "根据id查询客户")
    @GetMapping("/getCustById")
    public R<CustomerRes> getCustById(@ApiParam(name = "id", value = "主键", required = true)
                                                  @RequestParam Long id){
        CustomerRes res = customerService.getCustById(id);
        return R.ok(res);
    }

    @ApiOperation(value = "搜索客户信息")
    @PostMapping("/findCustomer")
    public R<PageResult> findCustomer(@Valid @RequestBody CustInfoReq custInfoReq){
        PageResult<CustomerRes> result = customerService.findCustomer(custInfoReq);
        return R.ok(result);
    }

    @ApiOperation(value = "修改客户信息")
    @PutMapping("/updateCustomer")
    public R<Integer> updateCustomer(@Valid @RequestBody CustModifyReq custModifyReq){
        Integer res = customerService.updateCustomer(custModifyReq);
        return R.ok(res);
    }

    @ApiOperation(value = "启用/冻结客户信息")
    @PutMapping("/modifyStatus")
    public R<Integer> modifyStatus(@RequestBody CustStatusReq custStatusReq){
        Integer res = customerService.modifyStatus(custStatusReq);
        return R.ok(res);
    }

    @GetMapping("/exportCustomer")
    public R exportCustomer(HttpServletResponse response, CustInfoReq req) {
        return customerService.exportCustomer(response, req);
    }


}
