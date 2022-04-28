package com.kitsune.eduservice.service;

import com.kitsune.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kitsune.eduservice.entity.vo.CourseInfoVo;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Kitsune
 * @since 2022-03-08
 */
public interface EduCourseService extends IService<EduCourse> {

    //添加课程基本信息的方法
    void saveCourseInfo(CourseInfoVo courseInfoVo);
}
