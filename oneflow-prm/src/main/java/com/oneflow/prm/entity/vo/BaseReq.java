package com.oneflow.prm.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseReq implements Serializable {
    private static final long serialVersionUID = 6175314819548083441L;

    /**
     * 创建人ID
     */
    private String createBy;

    /**
     * 更新人ID
     */
    private String updateBy;

    /**
     * 创建开始时间 2022-03-10 16:12:48
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 创建结束时间 2022-03-10 16:12:48
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeEnd;

    /**
     * 更新时间 2022-03-10 16:12:48
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 更新结束时间 2022-03-10 16:12:48
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTimeEnd;

    /**
     * 页码
     */
    @DecimalMin(message = "page为数字，且起始页为1", value = "1")
    private int page = 1;

    /**
     * 分页大小
     */
    @DecimalMin(message = "pageSize为数字，且最小1", value = "1")
    @DecimalMax(message = "pageSize为数字，且最大200", value = "200")
    private int pageSize = 10;

    /**
     * 租户id
     */
    private String tenantId;

}
