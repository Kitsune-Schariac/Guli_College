package com.kitsune.eduservice.service;

import com.kitsune.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author Kitsune
 * @since 2021-12-22
 */
public interface EduTeacherService extends IService<EduTeacher> {

    //前四个讲师信息
    List<EduTeacher> teacherLimit();

}
