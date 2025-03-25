package com.oneflow.prm.entity.dao.sys;

import lombok.Data;

@Data
public class SysDictData {

    /**
     * 父节点
     */
    private Long pid;

    /**
     * 字典编码
     */
    private Long dictCode;

    /**
     * 字典排序
     */
    private Long dictSort;

    /**
     * 字典标签
     */
    private String dictLabel;

    /**
     * 字典键值
     */
    private String dictValue;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 样式属性（其他样式扩展）
     */
    private String cssClass;


    /**
     * 状态（0正常 1停用）
     */
    private String status;

}
