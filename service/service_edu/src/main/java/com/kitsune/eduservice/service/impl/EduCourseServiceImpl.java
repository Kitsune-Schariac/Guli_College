package com.kitsune.eduservice.service.impl;

import com.kitsune.eduservice.entity.EduCourse;
import com.kitsune.eduservice.entity.EduCourseDescription;
import com.kitsune.eduservice.entity.vo.CourseInfoVo;
import com.kitsune.eduservice.mapper.EduCourseMapper;
import com.kitsune.eduservice.service.EduCourseDescriptionService;
import com.kitsune.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kitsune.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Kitsune
 * @since 2022-03-08
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    //这里注入description的service是因为在添加课程简介的时候要用到这个service，而在spring管理下的地方我们只需要注入就可以直接使用
    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;

    //添加课程基本信息的方法
    @Override
    public void saveCourseInfo(CourseInfoVo courseInfoVo) {

        //1 向课程表添加课程基本信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);//直接使用BeanUtils类的方法
        int insert = baseMapper.insert(eduCourse);

        //insert返回值如果是0那么就是查询执行失败，这时就手动抛出异常
        if (insert == 0) {
            //添加失败
            throw new GuliException(20001, "添加课程失败");
        }

        //获取添加之后的课程id
        String cid = eduCourse.getId();

        //2 向课程简介表添加课程简介
        //new一个edu_course_description，封装数据，然后save
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setDescription(courseInfoVo.getDescription());//把简介信息封装
        //设置描述id，也就是课程id
        eduCourseDescription.setId(cid);//把取出的id封装进去
        eduCourseDescriptionService.save(eduCourseDescription);

    }
}
