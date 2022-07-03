package com.kitsune.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kitsune.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kitsune.eduservice.entity.vo.CourseInfoVo;
import com.kitsune.eduservice.entity.vo.CoursePublishVo;
import com.kitsune.eduservice.entity.vo.CourseQueryVo;

import java.util.List;

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
    String saveCourseInfo(CourseInfoVo courseInfoVo);

    //获取课程信息
    CourseInfoVo getCourseInfo(String courseId);

    void updateCourseInfo(CourseInfoVo courseInfoVo);

    CoursePublishVo getPublishCourseInfo(String id);

    //多条件分页查询
    Page<EduCourse> coursePageCondition(long current, long limit, CourseQueryVo courseQueryVo);

    //删除课程
    void deleteCourse(String id);

    //获取前n个热门课程
    List<EduCourse> courseLimit();

}
