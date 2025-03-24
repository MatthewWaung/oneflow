package com.oneflow.oms.core.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页体
 */
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = -8602688343677539706L;

    /**
     * 页号
     */
    private long page = 1;

    /**
     * 分页大小
     */
    private long pageSize = 10;

    /**
     * 总条数
     */
    private long total;

    private List<T> result = Collections.emptyList();

    public PageResult(IPage<T> iPage){
        this.page = iPage.getCurrent();
        this.pageSize = iPage.getPages();
        this.total = iPage.getTotal();
        this.result = iPage.getRecords();
    }

}
