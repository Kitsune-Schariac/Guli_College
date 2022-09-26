package com.kitsune.eduservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kitsune.commonutils.R;
import com.kitsune.eduservice.entity.EduCourse;
import com.kitsune.eduservice.entity.vo.CourseInfoVo;
import com.kitsune.eduservice.entity.vo.CoursePublishVo;
import com.kitsune.eduservice.entity.vo.CourseQueryVo;
import com.kitsune.eduservice.service.EduCourseService;
import com.kitsune.servicebase.exceptionhandler.GuliException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
//@CrossOrigin
public class EduCourseController {
    private final static String COURSE_NORMAL = "Normal";
    private final static String COURSE_DRAFT = "Draft";

    @Autowired
    private EduCourseService eduCourseService;

    //获取所有课程
    @ApiOperation(value = "所有课程信息")
    @GetMapping("allCourse")
    public R allCourse(){
        List<EduCourse> list = eduCourseService.list(null);
        return R.ok().data("list", list);
    }

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
    @ApiOperation(value = "获取课程最终发布信息")
    @GetMapping("getPublishCourse/{id}")
    public R getPublishCourse(@PathVariable String id){
        CoursePublishVo publishCourse = eduCourseService.getPublishCourseInfo(id);
        return R.ok().data("publishCourse", publishCourse);
    }

    //课程最终发布
    @ApiOperation(value = "课程最终发布")
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id){

        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus(COURSE_NORMAL);
        boolean update = eduCourseService.updateById(eduCourse);
        if(update){
            return R.ok();
        }else {
            throw new GuliException(20001, "发布失败");
        }
    }

    //多条件分页查询课程信息
    @ApiOperation(value = "多条件分页查询课程信息")
    @PostMapping("coursePageQuery/{current}/{limit}")
    public R coursePageQuery(
                             @ApiParam(name = "current",value = "当前页", required = true)
                             @PathVariable long current,
                             @ApiParam(name = "limit",value = "页记录数", required = true)
                             @PathVariable long limit,
                             @ApiParam(name = "courseQuery",value = "查询对象", required = false)
                             @RequestBody CourseQueryVo courseQuery){
        Page<EduCourse> coursePage = eduCourseService.coursePageCondition(current, limit, courseQuery);
        long total = coursePage.getTotal();
        List<EduCourse> records = coursePage.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }

    //删除课程
    @ApiOperation(value = "删除课程信息(已停用)")
    @DeleteMapping("deleteByIdLogic/{id}")
    public R deleteLogic(@ApiParam(name = "id", value = "id", required = true)
                         @PathVariable String id){
        boolean remove = eduCourseService.removeById(id);
        if(remove){
            return R.ok();
        }else {
            throw new GuliException(20001, "删除失败");
        }
    }

    //删除课程·真
    @ApiOperation(value = "删除课程·真")
    @DeleteMapping("deleteCourse/{id}")
    public R deleteCourse(@ApiParam(name = "id", value = "id", required = true)
                          @PathVariable String id){

        eduCourseService.deleteCourse(id);
        return R.ok();
    }


}

