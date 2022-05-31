package com.kitsune.eduservice.service;

import com.kitsune.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kitsune.eduservice.entity.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Kitsune
 * @since 2022-03-08
 */
public interface EduChapterService extends IService<EduChapter> {

    public List<ChapterVo> getChapterVideoByCourseId(String courseId);

}
