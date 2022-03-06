package com.kitsune.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kitsune.eduservice.entity.EduSubject;
import com.kitsune.eduservice.entity.excel.SubjectData;
import com.kitsune.eduservice.service.EduSubjectService;
import com.kitsune.servicebase.exceptionhandler.GuliException;

public class subjectExcelListener extends AnalysisEventListener<SubjectData> {

    //因为这个subjectExcelListener不能交给spring进行管理，需要自己new，不能注入其他对象
    //不能实现数据库操作
    //所以就用有参构造来注入service

    public EduSubjectService subjectService;

    //无参构造
    public subjectExcelListener(){}

    //有参构造
    //因为这个监听器没有交给spring管理，所以我们的service无法通过AutoWrite注入
    //所以这里用传递参数的方式来手动管理注入service
    public subjectExcelListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Override
    public void invoke(SubjectData data, AnalysisContext context) {

        if(data == null) {
            //如果Excel中的数据为空，就主动抛出异常
            throw new GuliException(20001, "文件数据为空");
        }
        //一行一行读取，每次读取有两个值，第一个值是一级分类，第二个值是二级分类，把数据分别加入数据库

        //判断一级分类是否重复
        EduSubject existOneSubject = this.existOneSubject(data.getOneSubjectName(), subjectService);
        if(existOneSubject == null) { //没有相同一级分类， 进行添加
            existOneSubject = new EduSubject();
            existOneSubject.setParentId("0");
            existOneSubject.setTitle(data.getOneSubjectName()); //一级分类名称
            subjectService.save(existOneSubject);
        }

        //获取一级分类的id
        String pid = existOneSubject.getId();

        //添加耳机分类
        //判断二级分类是否重复
        EduSubject existTwoSubject = this.existtwoSubject(data.getTwoSubjectName(), subjectService, pid);
        if(existTwoSubject == null){
            existTwoSubject = new EduSubject();
            existTwoSubject.setParentId(pid);
            existTwoSubject.setTitle(data.getTwoSubjectName());
            subjectService.save(existTwoSubject);
        }


    }

    //判断一级分类不能重复添加
    //  因为以及分类代表的是表的列名，一个列名(一级分类)下有多个值(二级分类)
    //  所以一级分类在读取的时候遇到相同的值只用留下一个就够了
    //为了能使用service的方法同样把service通过传参的方式注入进来
    //需要传入的是一级分类的名称， service
    private EduSubject existOneSubject(String name, EduSubjectService eduSubjectService){

        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", "0");
        EduSubject oneSubject = eduSubjectService.getOne(wrapper);
        return oneSubject;

    }

    //判断二级分类不能重复添加
    //需要传入的是二级分类，service，二级分类的id值
    private EduSubject existtwoSubject(String name, EduSubjectService eduSubjectService, String pid){

        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", pid);
        EduSubject twoSubject = eduSubjectService.getOne(wrapper);
        return twoSubject;

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
