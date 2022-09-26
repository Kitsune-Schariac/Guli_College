package com.kitsune.eduservice.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kitsune.commonutils.JwtUtils;
import com.kitsune.commonutils.R;
import com.kitsune.commonutils.ordervo.CourseWebVoOrder;
import com.kitsune.eduservice.client.OrderClient;
import com.kitsune.eduservice.entity.EduCourse;
import com.kitsune.eduservice.entity.chapter.ChapterVo;
import com.kitsune.eduservice.entity.frontVo.CourseFrontVo;
import com.kitsune.eduservice.entity.frontVo.CourseWebVo;
import com.kitsune.eduservice.service.EduChapterService;
import com.kitsune.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
//@CrossOrigin
@Api(tags = "课程前端接口")
@RequestMapping("eduservice/coursefront")
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrderClient orderClient;

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
    public R getFrontCourseInfo(@PathVariable String courseId, HttpServletRequest request){

        //通过课程id查询其下的章节和小节
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoByCourseId(courseId);

        //根据id，查询课程的详细信息
        CourseWebVo baseCourseInfo = courseService.getBaseCourseInfo(courseId);

        //根据课程id和用户id查询课程是否已经支付过
        boolean buyCourse = orderClient.isBuyCourse(courseId, JwtUtils.getMemberIdByJwtToken(request));
        // TODO: 2022/9/24 当用户未登录时，应该无法使用购买选项

        return R.ok().data("courseWebVo", baseCourseInfo).data("chapterVideoList", chapterVideoList).data("isbuy", buyCourse);
    }

    @ApiOperation(value = "根据课程id查询课程信息")
    @PostMapping("getCourseInfoOrder/{id}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable String id) {

        CourseWebVo baseCourseInfo = courseService.getBaseCourseInfo(id); //拿到课程信息
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder(); //新建一个对象准备获取信息
        BeanUtils.copyProperties(baseCourseInfo, courseWebVoOrder);

        return courseWebVoOrder;

    }

}
