package com.oneflow.prm.entity.vo.response.order;

import com.oneflow.prm.core.constant.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 贷款凭证导入返回值
 *
 * @author forest
 * @date 2024/07/03
 */
@Data
public class CreditItemVO {

    /**
     * 处理成功 1 处理失败 0
     * 默认处理成功
     */
    @Schema(description = "处理成功 1 处理失败 0")
    private Integer passFlag = Constants.YesOrNoFlag.Y.getCode();

    @Schema(description = "提示语")
    private String tip;

    @Schema(description = "校验不通过后，返回excel下载地址")
    private String errorExcelDownloadUrl;

    @Schema(description = "处理成功后的订单编号")
    private List<CreditOrderImportVO> orderImportSuccessList;

}
