package com.kitsune.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kitsune.eduservice.entity.EduSubject;
import com.kitsune.eduservice.entity.excel.SubjectData;
import com.kitsune.eduservice.entity.subject.OneSubject;
import com.kitsune.eduservice.entity.subject.TwoSubject;
import com.kitsune.eduservice.listener.subjectExcelListener;
import com.kitsune.eduservice.mapper.EduSubjectMapper;
import com.kitsune.eduservice.service.EduSubjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author Kitsune
 * @since 2022-03-03
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

//    添加课程分类
    @Override
    public void saveSubject(MultipartFile file, EduSubjectService eduSubjectService) {

        try {
            //先得到文件的输入流
            InputStream in = file.getInputStream();
            //调用方法读取文件
            EasyExcel.read(in, SubjectData.class, new subjectExcelListener(eduSubjectService)).sheet().doRead();
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    //课程分类列表（树形）
    @Override
    public List<OneSubject> getOneTwoSubject() {

        //首先查询所有的一级分类 parent_id = 0
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id", 0);
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);//调用mybatis封装好的base mapper方法
//        List<EduSubject> oneSubjectList = this.list(wrapperOne);//效果相同

        //然后查询所有二级分类 parent_id != 0
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperTwo.ne("parent_id", 0);
        List<EduSubject> twoSubjectList = this.list(wrapperTwo);//调用继承自父类的方法，效果和上面查询一级的代码相同，因为是用来学习的项目，所以使用不同方法

        //创建一个list集合，用于存储最终封装的数据
        List<OneSubject> finalSubjectList = new ArrayList<>();

        //封装一级分类
        //封装的时候要把eduSubject变成FinalSubjectList
        //那么就是把一级分类的数据全部遍历，得到每个一级分类对象那个，获取每一个一级分类对象的值，然后封装到FinalSubjectList
        for (int i = 0; i < oneSubjectList.size(); i++) {
            //得到OneSubjectList中的每个eduSubject对象
            EduSubject eduSubject = oneSubjectList.get(i);

            //把eduSubject里面的值获取出来，然后放到OneSubject对象里面去
            OneSubject oneSubject = new OneSubject();

            //这是其中一种比较复杂的方法，一个个地获取值，然后封装，属性太多的话就会很麻烦
//            oneSubject.setTitle(eduSubject.getTitle());
//            oneSubject.setId(eduSubject.getId());

            //另一种方法利用spring的一个工具类
            BeanUtils.copyProperties(eduSubject, oneSubject);

            //把OneSubject放到FinalSubjectList
            finalSubjectList.add(oneSubject);

            //遍历查询所有的二级分类
            //创建一个list集合封装每个一级分类中的二级分类
            List<TwoSubject> twoFinalSubjectList = new ArrayList<>();
            //遍历二级分类list集合
            for (int m = 0; m < twoSubjectList.size(); m++) {
                //获取每个二级分类
                EduSubject eduSubject1 = twoSubjectList.get(m);
                //判断二级分类的parent_id和一级分类的id是否一样
                if (eduSubject1.getParentId().equals(eduSubject.getId())){
                    //把二级分类封装到最终对象
                    TwoSubject twoSubject = new TwoSubject();//new一个twoSubject用作容器
                    BeanUtils.copyProperties(eduSubject1, twoSubject);//把eduSubject放进twoSubject
                    twoFinalSubjectList.add(twoSubject);//把twoSubject放到集合中

                }
            }
            finalSubjectList.get(i).setChildren(twoFinalSubjectList);//把二级分类的集合放进finalSubjectList


        }


        return finalSubjectList;
    }
}
