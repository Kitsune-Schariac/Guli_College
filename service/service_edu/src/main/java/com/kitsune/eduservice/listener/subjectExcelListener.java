package com.kitsune.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.kitsune.eduservice.entity.excel.SubjectData;
import com.kitsune.eduservice.service.EduSubjectService;

public class subjectExcelListener extends AnalysisEventListener<SubjectData> {

    //因为这个subjectExcelListener不能交给spring进行管理，需要自己new，不能注入其他对象
    //不能实现数据库操作
    //所以就用有参构造来注入service

    public EduSubjectService subjectService;

    //无参构造
    public subjectExcelListener(){}

    //有参构造
    public subjectExcelListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Override
    public void invoke(SubjectData data, AnalysisContext context) {

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
