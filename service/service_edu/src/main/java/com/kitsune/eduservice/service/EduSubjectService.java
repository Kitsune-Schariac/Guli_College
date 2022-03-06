package com.kitsune.eduservice.service;

import com.kitsune.eduservice.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kitsune.eduservice.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author Kitsune
 * @since 2022-03-03
 */
public interface EduSubjectService extends IService<EduSubject> {

    //添加课程分类
    void saveSubject(MultipartFile file, EduSubjectService eduSubjectService);

    List<OneSubject> getOneTwoSubject();
}
