package com.kitsune.eduservice.mapper;

import com.kitsune.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kitsune.eduservice.entity.frontVo.CourseWebVo;
import com.kitsune.eduservice.entity.vo.CoursePublishVo;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author Kitsune
 * @since 2022-03-08
 */
@Repository
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    public CoursePublishVo getPublishCourseInfo(String courseId);

    //根据课程id查询详情信息
    CourseWebVo getBaseCourseInfo(String courseId);

}
