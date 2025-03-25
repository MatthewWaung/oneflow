package com.oneflow.prm.entity.dto.excel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Excel数据检测的结果值
 *
 * @author forest
 * @date 2024/06/13
 */

@Data
public class ExcelCellValidateDTO {

    @Schema(description = "校验结果 true: 校验通过 false: 校验不通过")
    private Boolean passValidate = true;

    @Schema(description = "校验结果信息")
    private String message;

    /**
     * 是否校验失败
     *
     * @return {@link Boolean }
     */
    public Boolean isFail() {
        return !passValidate;
    }
}
