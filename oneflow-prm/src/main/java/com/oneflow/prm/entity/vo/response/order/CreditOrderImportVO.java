package com.oneflow.prm.entity.vo.response.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 贷项凭证导入成功的信息
 *
 * @author forest
 * @date 2024/07/08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditOrderImportVO {

    @Schema(description = "贷项凭证导入的分组号")
    private String group;

    @Schema(description = "贷项凭证导入的订单号")
    private String orderNo;

    @Schema(description = "订单住建")
    private Long orderId;

}
