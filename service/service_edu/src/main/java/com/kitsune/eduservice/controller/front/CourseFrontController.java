package com.kitsune.eduservice.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kitsune.commonutils.R;
import com.kitsune.eduservice.entity.EduCourse;
import com.kitsune.eduservice.entity.chapter.ChapterVo;
import com.kitsune.eduservice.entity.frontVo.CourseFrontVo;
import com.kitsune.eduservice.service.EduChapterService;
import com.kitsune.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@Api(tags = "课程前端接口")
@RequestMapping("eduservice/coursefront")
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    //条件查询分页查询课程
    @ApiOperation(value = "条件查询分页查询课程")
    @PostMapping("getFrontCourseList/{page}/{limit}")
    public R getFrontCourseList (@PathVariable long page, @PathVariable long limit,
                                 @RequestBody(required = false) CourseFrontVo courseFrontvo){

        Page<EduCourse> pageCourse = new Page<>(page, limit);
        Map<String, Object> map = courseService.getCourseFrontList(pageCourse, courseFrontvo);

        return R.ok().data(map);
    }

    //获取课程详情信息
    @ApiOperation(value = "获取课程详情信息")
    @GetMapping("getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable String courseId){

        //通过课程id查询其下的章节和小节
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoByCourseId(courseId);

        //查询课程的详细信息


        return null;
    }

}
