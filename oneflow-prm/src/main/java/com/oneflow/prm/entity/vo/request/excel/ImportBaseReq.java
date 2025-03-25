package com.oneflow.prm.entity.vo.request.excel;

import com.oneflow.prm.core.utils.ValidationUtils;
import com.oneflow.prm.entity.dto.excel.ExcelCellValidateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Excel的基本数据
 *
 * @author forest
 * @date 2024/06/24
 */
@Data
public class ImportBaseReq implements java.io.Serializable {

    @Schema(description = "序号:excel的序号")
    private Integer index;

    @Schema(description = "结果: true 通过 false -不通过")
    private Boolean validateResult = true;

    @Schema(description = "结果: 错误信息")
    private List<String> errors = new ArrayList<>();

    /** 设置错误信息 */
    public void addErrors(String message) {
        this.setValidateResult(false);
        this.getErrors().add(message);
    }

    // 处理错误信息
    protected void handleErrorInfo(ExcelCellValidateDTO excelCellValidateDTO, String tipWord) {
        if (excelCellValidateDTO.isFail()) {
            this.setValidateResult(false);
            this.getErrors().add(tipWord + excelCellValidateDTO.getMessage());
        }
    }

    /**
     * 校验数据：校验基本是否为空，长度是否过长等
     */
    protected void initValidate(Object validateObj) {
        /**
         * 这里主要获取javax.validation在属性上加的 @NotBlank、@NotEmpty等校验的结果集
         */

        // 校验数据是否为空
        Set<ConstraintViolation<Object>> violations = ValidationUtils.getValidator().validate(validateObj);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<Object> violation : violations) {
                this.setValidateResult(false);
                this.getErrors().add(violation.getMessage());
            }
        }
    }

}
