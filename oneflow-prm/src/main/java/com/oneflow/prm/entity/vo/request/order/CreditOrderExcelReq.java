package com.oneflow.prm.entity.vo.request.order;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.oneflow.prm.core.utils.DateUtils;
import com.oneflow.prm.core.utils.ExcelCellDataValidateUtil;
import com.oneflow.prm.entity.dto.excel.ExcelCellValidateDTO;
import com.oneflow.prm.entity.vo.request.excel.ImportBaseReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreditOrderExcelReq extends ImportBaseReq {

    @ExcelProperty(value = "消息")
    private String reason;

    @ExcelProperty(value = "订单号")
    private String orderNo;

    @ExcelProperty(value = "Group")
    @NotBlank(message = "Group不能为空")
    private String group;

    @ExcelProperty(value = "需求部门")
    @NotBlank(message = "需求部门不能为空")
    private String deptName;

    @ExcelProperty(value = "具体类别")
    @NotBlank(message = "具体类别")
    private String specificCategoriesLabel;

    @NotBlank(message = "具体类别value")
    private String specificCategoriesValue;

    @ExcelProperty(value = "销售组织")
    @NotBlank(message = "销售组织不能为空")
    private String salesOrganization;

    @ExcelProperty(value = "客户编码")
    @NotBlank(message = "客户编码不能为空")
    private String customerCode;

    @ExcelProperty(value = "业务员")
    @NotBlank(message = "业务员不能为空")
    private String salespersonJobNo;

    @ExcelProperty(value = "币别")
    @NotBlank(message = "币别不能为空")
    private String currency;

    @ExcelProperty(value = "返利事前申请编码")
    private String returnBeforeApplyCode;

    @ExcelProperty(value = "CN归属期间(起始时间)")
    private String cnTimeStart;

    @ExcelProperty(value = "CN归属期间(结束时间)")
    private String cnTimeEnd;

    @ExcelProperty(value = "返利原因")
    private String returnReason;

    @ExcelProperty(value = "订单备注/领用原因")
    @Length(max = 1000, message = "订单备注/领用原因长度不能超过1000个字符")
    private String note;

    @ExcelProperty(value = "物料编码")
    @NotBlank(message = "物料编码不能为空")
    private String materialCode;

    @ExcelProperty(value = "单价")
    private String unitPrice;

    @ExcelProperty(value = "含税单价")
    private String unitPriceTax;

    @ExcelProperty(value = "数量")
    @NotBlank(message = "数量不能为空")
    @Positive(message = "数量必须为正数")
    private String qty;

    // 默认税率为0
    @ExcelProperty(value = "税率")
    @Min(value = 0, message = "税率必须在0-1之间")
    @Max(value = 1, message = "税率必须在0-1之间")
    private String rate="0";

    @ExcelProperty(value = "SN编码")
    @Length(max = 128, message = "SN编码长度不能超过128")
    private String serialNo;

    // ================================ 后台在校验的时候添加的参数================================

    @Schema(description = "销售组织id",hidden = true)
    private Long salesAreaId;

    //================================ 用于校验的业务对象 ====================================================
    @Schema(description = "具体类别",hidden = true)
    private Map<String,String> specificCategoriesMap;

    //================================ 用于分类校验的业务对象=====================================================
    @Schema(description = "具体类别列表",hidden = true)
    private Map<String, Set<String>> specificCategoriesSetByLabel;

    //=============================== 格式校验开始===================================

    // 时间格式校验
    public void setCnTimeStart(String cnTimeStart) {
        this.cnTimeStart = cnTimeStart;
        if (StrUtil.isBlank(cnTimeStart)) {
            return;
        }
        ExcelCellValidateDTO validateDateDTO = ExcelCellDataValidateUtil.isValidateDateDTO(DateUtils.YYYY_MM_DD, cnTimeStart);
        handleErrorInfo(validateDateDTO,"CN归属期间(起始时间)");
    }

    public void setCnTimeEnd(String cnTimeEnd) {
        if (StrUtil.isBlank(cnTimeEnd)) {
            return;
        }
        this.cnTimeEnd = cnTimeEnd;
        ExcelCellValidateDTO validateDateDTO = ExcelCellDataValidateUtil.isValidateDateDTO(DateUtils.YYYY_MM_DD, cnTimeEnd);
        handleErrorInfo(validateDateDTO,"CN归属期间(结束时间)");
    }

    // 金额指定长度
    private static final int scale = 2;

    // 金额格式校验
    public void setUnitPrice(String unitPrice) {
        if (StrUtil.isBlank(unitPrice)) {
            return;
        }
        this.unitPrice = unitPrice;
        ExcelCellValidateDTO validateMoneyDTO = ExcelCellDataValidateUtil.isSpecialLengthMoneyDTO(unitPrice, scale);
        handleErrorInfo(validateMoneyDTO,"单价");
    }

   public void setUnitPriceTax(String unitPriceTax) {
        if (StrUtil.isBlank(unitPriceTax)) {
            return;
        }
        this.unitPriceTax = unitPriceTax;
        ExcelCellValidateDTO validateMoneyDTO = ExcelCellDataValidateUtil.isSpecialLengthMoneyDTO(unitPriceTax, scale);
        handleErrorInfo(validateMoneyDTO,"含税单价");
   }



    public void setRate(String rate) {
        if (StrUtil.isBlank(rate)) {
            return;
        }
        this.rate = rate;
        ExcelCellValidateDTO validateMoneyDTO = ExcelCellDataValidateUtil.isSpecialLengthMoneyDTO(rate, scale);
        handleErrorInfo(validateMoneyDTO,"税率");
    }

    // 数量格式校验
    public void setQty(String qty) {
        if (StrUtil.isBlank(qty)) {
            return;
        }
        this.qty = qty;
        ExcelCellValidateDTO validateNumberDTO = ExcelCellDataValidateUtil.isPositiveNumberDTO(qty);
        handleErrorInfo(validateNumberDTO,"数量");
    }
    //=============================== 格式校验结束 ===================================


    //=============================== 条件关联项校验开始===================================
    //返利事前申请编码:当具体类别为达量返利、促销返利、毛利保护、降价库存补差时必填
    public void handReturnBeforeApplyCode(List<String> specificCategoriesLabelList){
        if (StrUtil.isNotBlank(this.specificCategoriesLabel) && specificCategoriesLabelList.contains(this.specificCategoriesLabel)) {
            if (StrUtil.isBlank(this.returnBeforeApplyCode)) {
                ExcelCellValidateDTO validateDTO = new ExcelCellValidateDTO();
                validateDTO.setPassValidate(false);
                validateDTO.setMessage("当具体类别为达量返利、促销返利、毛利保护、降价库存补差时必填");
                handleErrorInfo(validateDTO,"返利事前申请编码");
            }
        }
    }

    public static final String REBATES_CATEGORIES_DOWN = "rebates_categories_down";


    // CN归属期间(起始时间):
    // 当【具体类别】为达量返利、促销返利、毛利保护、降价库存补差时非必填，系统会根据返利事前申请编码自动带出；
    // 当【具体类别】= 营销支持-常规推广、营销支持-渠道进店、售后支持时必填
    public void handCnTimeStart(){
        //String other = specificCategoriesMap.get(OrderSpecificCategoriesConstants.SPECIFIC_CATEGORIES_FOUR);
        if (specificCategoriesSetByLabel.get(REBATES_CATEGORIES_DOWN).contains(this.specificCategoriesValue)) {
            // 当【具体类别】= 营销支持-常规推广、营销支持-渠道进店、售后支持时必填
            if (StrUtil.isNotBlank(this.specificCategoriesLabel) && StrUtil.isBlank(this.cnTimeStart)) {
                    ExcelCellValidateDTO validateDTO = new ExcelCellValidateDTO();
                    validateDTO.setPassValidate(false);
                    validateDTO.setMessage("当营销支持-常规推广、营销支持-渠道进店、售后支持时必填");
                    handleErrorInfo(validateDTO,"CN归属期间(起始时间)：");
            }
        }
    }

    // CN归属期间(结束时间):
    // 当【具体类别】为达量返利、促销返利、毛利保护、降价库存补差时非必填，系统会根据返利事前申请编码自动带出；
    // 当【具体类别】= 营销支持-常规推广、营销支持-渠道进店、售后支持时必填
    public void handCnTimeEnd(){
        //String other = specificCategoriesMap.get(OrderSpecificCategoriesConstants.SPECIFIC_CATEGORIES_FOUR);
        if (specificCategoriesSetByLabel.get(REBATES_CATEGORIES_DOWN).contains(this.specificCategoriesValue)) {
            // 当【具体类别】= 营销支持-常规推广、营销支持-渠道进店、售后支持时必填
            if (StrUtil.isNotBlank(this.specificCategoriesLabel) && StrUtil.isBlank(this.cnTimeEnd)) {
                    ExcelCellValidateDTO validateDTO = new ExcelCellValidateDTO();
                    validateDTO.setPassValidate(false);
                    validateDTO.setMessage(
                        "当营销支持-常规推广、营销支持-渠道进店、售后支持时必填");
                    handleErrorInfo(validateDTO, "CN归属期间(结束时间)：");
            }
        }
    }

    // CN归属期间(结束时间)，两个不为空，都能正常变成时间的情况下，判断结束时间大于等于开始时间
    public void handCnTimeStartEnd(){
        if (StrUtil.isNotBlank(this.cnTimeStart) && StrUtil.isNotBlank(this.cnTimeEnd)) {
            Date start = DateUtils.parseDate(this.cnTimeStart);
            Date end = DateUtils.parseDate(this.cnTimeEnd);
            if ( start!=null && end != null && end.getTime() < start.getTime()) {
                ExcelCellValidateDTO validateDTO = new ExcelCellValidateDTO();
                validateDTO.setPassValidate(false);
                validateDTO.setMessage("CN归属期间(结束时间)必须大于等于CN归属期间(起始时间)");
                handleErrorInfo(validateDTO,"");
            }
        }
    }

    // 单价/含税单价；单价和含税单价必填其中一个
    public void handUnitPrice(){
        if (StrUtil.isBlank(this.unitPrice) && StrUtil.isBlank(this.unitPriceTax)) {
            ExcelCellValidateDTO validateDTO = new ExcelCellValidateDTO();
            validateDTO.setPassValidate(false);
            validateDTO.setMessage("单价和含税单价必填其中一个");
            handleErrorInfo(validateDTO,"");
        }
    }
    
    //=============================== 条件关联项校验结束 ===================================

    // 1. 具体类别、销售组织、客户编码、业务员、币别、返利事前申请编码、返利销售额、CN归属期间(起始时间)、CN归属期间(结束时间)、返利原因、CI信息、订单备注/领用原因 去重拼接
    public String getKey() {
        return
                StrUtil.trimToEmpty(this.deptName) +
                StrUtil.trimToEmpty(this.specificCategoriesLabel) +
                StrUtil.trimToEmpty(this.salesOrganization) +
                StrUtil.trimToEmpty(this.customerCode) +
                StrUtil.trimToEmpty(this.salespersonJobNo) +
                StrUtil.trimToEmpty(this.currency) +
                StrUtil.trimToEmpty(this.returnBeforeApplyCode) +
                StrUtil.trimToEmpty(this.cnTimeStart) +
                StrUtil.trimToEmpty(this.cnTimeEnd) +
                StrUtil.trimToEmpty(this.returnReason) +
                StrUtil.trimToEmpty(this.note);
    }



    /**
     * 初始化校验
     *
     * @param specificCategoriesLabelList 具体类别
     * @param specificCategoriesMap 具体类别
     */
    public void initValidate(List<String> specificCategoriesLabelList, Map<String,String> specificCategoriesMap,Map<String, Set<String>> specificCategoriesSetByLabel) {
        this.specificCategoriesMap = specificCategoriesMap;
        this.specificCategoriesSetByLabel = specificCategoriesSetByLabel;
        initValidate(this);
        // 条件关联项校验
        handReturnBeforeApplyCode(specificCategoriesLabelList);
        //handReturnBalance();
        handCnTimeStart();
        handCnTimeEnd();
        handUnitPrice();
        handCnTimeStartEnd();

    }

}
