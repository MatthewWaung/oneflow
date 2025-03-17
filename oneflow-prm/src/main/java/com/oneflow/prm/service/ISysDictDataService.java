package com.oneflow.prm.service;

public interface ISysDictDataService {

    /**
     * 根据字典类型和字典键值查询字典数据
     *
     * @param dictType
     * @param dictLabel
     * @return
     */
    String selectDictLabel(String dictType, String dictLabel);
}
