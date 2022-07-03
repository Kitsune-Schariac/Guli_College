package com.kitsune.eduservice.controller.front;

import com.kitsune.commonutils.R;
import com.kitsune.eduservice.entity.EduCourse;
import com.kitsune.eduservice.entity.EduTeacher;
import com.kitsune.eduservice.service.EduCourseService;
import com.kitsune.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author kitsune
 * @data 2022年7月1日
 */
@Api(tags = "首页的课程和名师信息")
@RestController
@RequestMapping("/eduservice/indexfront")
@CrossOrigin
public class IndexFrontController {

    @Autowired
    private EduCourseService eduCourseService;

    @Autowired
    private EduTeacherService eduTeacherService;

    //获取前八个课程和前四个讲师
    @ApiOperation(value = "获取前八个课程和前四个讲师")
    @GetMapping("courseAndTeacher")
    public R courseAndTeacher(){
        //调用两个service封装的方法
        List<EduCourse> eduCourses = eduCourseService.courseLimit();
        List<EduTeacher> teachers = eduTeacherService.teacherLimit();

        return R.ok().data("eduCourses", eduCourses).data("teachers", teachers);
    }

}
