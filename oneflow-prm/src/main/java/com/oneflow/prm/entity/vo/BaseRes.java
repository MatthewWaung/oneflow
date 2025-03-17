package com.oneflow.prm.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础返回实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseRes implements Serializable {
    private static final long serialVersionUID = 1552248435532034553L;

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
     * 更新时间 2022-03-10 16:12:48
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 租户id
     */
    private String tenantId;
}
