package com.kitsune.eduservice.controller;


import com.kitsune.commonutils.R;
import com.kitsune.eduservice.entity.EduChapter;
import com.kitsune.eduservice.entity.chapter.ChapterVo;
import com.kitsune.eduservice.service.EduChapterService;
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
@Api(tags = "课程章节信息")
@RestController
@RequestMapping("/eduservice/edu-chapter")
@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService chapterService;

    //课程大纲列表
    @GetMapping("/getChapterVideo/{courseId}")
    public R getChapterVideo(
            @ApiParam(name = "courseId", value = "课程id", required = false)
            @PathVariable String courseId){

        List<ChapterVo> list = chapterService.getChapterVideoByCourseId(courseId);
        return R.ok().data("list", list);

    }

    //添加章节
    @ApiOperation(value = "添加章节")
    @PostMapping("addChapter")
    public R addChapter(
            @ApiParam(name = "eduChapter", value = "章节对象", required = true)
            @RequestBody EduChapter eduChapter) {
        boolean save = chapterService.save(eduChapter);
        if(save){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //根据章节id查询
    @ApiOperation(value = "根据章节id查询")
    @GetMapping("getChapterById/{chapterId}")
    public R getChapterById(@ApiParam(name = "chapterId", value = "关键id", required = true)
                            @PathVariable String chapterId) {
        EduChapter chapter = chapterService.getById(chapterId);
        return R.ok().data("chapter", chapter);
    }

    //修改章节
    @ApiOperation(value = "修改章节")
    @PostMapping("updateChapter")
    public R updateChapter(
            @ApiParam(name = "eduChapter", value = "章节对象", required = true)
            @RequestBody EduChapter eduChapter) {
        boolean update = chapterService.updateById(eduChapter);
        if(update){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //删除章节
    @DeleteMapping("deleteChapter/{chapterId}")
    public R deleteChapter(
            @ApiParam(name = "chapterId", value = "id", required = true)
            @PathVariable String chapterId){

        boolean delete = chapterService.deleteChapter(chapterId);
        if(delete){
            return R.ok();
        }else {
            return R.error();
        }

    }

}

