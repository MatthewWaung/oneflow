package com.oneflow.prm.entity.dao;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.minidev.json.annotate.JsonIgnore;

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 自动填充字段的基础实体类
 * @param <T>
 */
public abstract class BaseAutoFillModel<T extends BaseAutoFillModel<?>> extends Model<T> {
    private static final long serialVersionUID = -8404498533796107259L;

    /**
     * 租户人 id
     */
    @JsonIgnore
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NOT_NULL, value = "tenant_id", select = true)
    private Long tenantId;

    /**
     * 创建人 id
     */
    @JsonIgnore
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NOT_NULL, value = "create_by", select = true)
    private String createBy;

    /**
     * 创建时间——LocalDateTime用来替换Date类，Date如果不格式化，打印出的日期可读性很差，所以通常会使用SimpleDateFormat来实现格式化，但SDF最大的问题时线程不安全
     */
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NOT_NULL, value = "create_time", select = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Hongkong")
    private LocalDateTime createTime;

    /**
     * 更新人 id
     */
    @JsonIgnore
    @TableField(fill = FieldFill.UPDATE, updateStrategy = FieldStrategy.NOT_NULL, value = "update_by", select = true)
    private String updateBy;

    /**
     * 更新时间——LocalDateTime用来替换Date类，Date如果不格式化，打印出的日期可读性很差，所以通常会使用SimpleDateFormat来实现格式化，但SDF最大的问题时线程不安全
     */
    @JsonIgnore
    @TableField(fill = FieldFill.UPDATE, updateStrategy = FieldStrategy.NOT_NULL, value = "update_time", select = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Hongkong")
    private LocalDateTime updateTime;

    /**
     * 获取主键值
     * @return
     */
    public String getPkVal(){
        Serializable pkVal = pkVal();
        if (pkVal != null){
            return pkVal.toString();
        }
        return null;
    }

    /**
     * 自动填充属性
     * @return
     */
    protected Serializable pkVal(){
        TableInfo tableInfo = TableInfoHelper.getTableInfo(getClass());
        if (tableInfo != null){
            return (Serializable) ReflectionKit.getMethodValue(this, tableInfo.getKeyProperty());
        }
        return null;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @XmlTransient
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @XmlTransient
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "BaseAutoFillModel{" +
                "tenantId=" + tenantId +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                ", updateBy='" + updateBy + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
