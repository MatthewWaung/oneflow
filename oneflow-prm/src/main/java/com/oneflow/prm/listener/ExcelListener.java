package com.oneflow.prm.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * ExcelListener:监听类，自定义
 */
public class ExcelListener<T> extends AnalysisEventListener<T> {

    /**
     * 自定义用于暂时存储data。可以通过实例获取该值
     */
    private List<T> data = new ArrayList<>();

    /**
     * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
     */
    @Override
    public void invoke(T t, AnalysisContext context) {
        //数据存储到list，供批量处理，或后续自己业务逻辑处理。
        data.add(t);
        //根据业务自行 do something
        doSomething();

        /*
        如数据过大，可以进行定量分批处理
        if(data.size()<=100){
            data.add(object);
        }else {
            doSomething();
            data = new ArrayList<Object>();
        }
         */

    }

    /**
     * 根据业务自行实现该方法
     */
    private void doSomething() {
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        /*
            data.clear();
            解析结束销毁不用的资源
         */
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
