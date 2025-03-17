package com.oneflow.prm.entity.vo.response.customer;

import com.oneflow.prm.entity.vo.BaseRes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "客户信息返回响应数据")
public class CustomerRes extends BaseRes {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    private Long id;

    /**
     * 销售组织
     */
    @ApiModelProperty(value = "销售组织")
    private String salesOrg;

}
