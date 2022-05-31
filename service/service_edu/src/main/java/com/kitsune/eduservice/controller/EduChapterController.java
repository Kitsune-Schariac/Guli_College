package com.kitsune.eduservice.controller;


import com.kitsune.commonutils.R;
import com.kitsune.eduservice.entity.chapter.ChapterVo;
import com.kitsune.eduservice.service.EduChapterService;
import com.kitsune.eduservice.service.EduSubjectService;
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
@RestController
@RequestMapping("/eduservice/edu-chapter")
@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService chapterService;

    //课程大纲列表
    @GetMapping("/getChapterVideo{courseId}")
    public R getChapterVideo(
            @ApiParam(name = "courseId", value = "课程id", required = false)
            @PathVariable String courseId){

        // TODO: 2022/5/16 需要完成service中对于查询的方法
        List<ChapterVo> list = chapterService.getChapterVideoByCourseId(courseId);
        return R.ok().data("list", list);

    }

}

