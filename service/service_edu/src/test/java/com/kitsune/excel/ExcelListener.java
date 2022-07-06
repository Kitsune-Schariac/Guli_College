package com.kitsune.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

public class ExcelListener extends AnalysisEventListener<DemoData> {

//    一行一行读取Excel内容
    @Override
    public void invoke(DemoData data, AnalysisContext context) {
        System.out.println("****" + data);
    }
//    data就是每行的内容

    //    读取表头内容
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头" + headMap);
    }
//headMap就是表头内容

    //    读取完成之后
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
