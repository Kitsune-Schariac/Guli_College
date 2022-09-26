package com.kitsune.eduservice.controller;


import com.kitsune.commonutils.R;
import com.kitsune.eduservice.entity.subject.OneSubject;
import com.kitsune.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author Kitsune
 * @since 2022-03-03
 */
@RestController
@RequestMapping("/eduservice/edu-subject")
//@CrossOrigin
@Api(tags = "课程分类管理")
public class EduSubjectController {

    @Autowired
    private EduSubjectService eduSubjectService;

//    添加课程分类
//    策略是把文件上传，但是并不是以服务器为目标，而是直接读取上传的Excel文件获取数据
//    获取到上传过来的文件，把文件内容读取出来
//
    @ApiOperation(value = "添加课程分类")
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file){
//        上传过来Excel文件
        eduSubjectService.saveSubject(file, eduSubjectService);

        return R.ok();
    }

    @ApiOperation(value = "获取所有课程分类")
    @GetMapping("getAllSubject")
    public R getAllSubject(){

        List<OneSubject> subject = eduSubjectService.getOneTwoSubject();

        return R.ok().data("list", subject);
    }


}

