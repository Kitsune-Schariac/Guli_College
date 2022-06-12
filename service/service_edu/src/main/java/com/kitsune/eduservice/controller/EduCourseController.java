package com.kitsune.eduservice.controller;


import com.kitsune.commonutils.R;
import com.kitsune.eduservice.entity.EduCourse;
import com.kitsune.eduservice.entity.vo.CourseInfoVo;
import com.kitsune.eduservice.entity.vo.CoursePublishVo;
import com.kitsune.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Kitsune
 * @since 2022-03-08
 */
@Api(tags = "课程信息管理")
@RestController
@RequestMapping("/eduservice/edu-course")
@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService eduCourseService;


    //添加课程基本信息的方法
    @ApiOperation(value = "添加课程信息")
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        //返回添加之后课程id， 为了后面添加大纲使用
        // TODO: 2022/4/14
//        String id = eduCourseService
        String id = eduCourseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId", id);
    }

    //根据课程id查询课程信息
    @ApiOperation(value = "根据课程id查询课程信息")
    @GetMapping("CourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId){

        CourseInfoVo courseInfo = eduCourseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo", courseInfo);
    }

    //修改课程信息
    @ApiOperation(value = "修改课程信息")
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){

        eduCourseService.updateCourseInfo(courseInfoVo);

        return R.ok();
    }

    //获取课程最终发布信息
    @ApiOperation(value = "课程最终发布信息")
    @GetMapping("getPublishCourse/{id}")
    public R getPublishCourse(@PathVariable String id){
        CoursePublishVo publishCourse = eduCourseService.getPublishCourseInfo(id);
        return R.ok().data("publishCourse", publishCourse);
    }


}

