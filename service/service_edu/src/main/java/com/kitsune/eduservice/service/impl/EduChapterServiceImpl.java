package com.kitsune.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kitsune.eduservice.entity.EduChapter;
import com.kitsune.eduservice.entity.EduVideo;
import com.kitsune.eduservice.entity.chapter.ChapterVo;
import com.kitsune.eduservice.entity.chapter.VideoVo;
import com.kitsune.eduservice.mapper.EduChapterMapper;
import com.kitsune.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kitsune.eduservice.service.EduCourseService;
import com.kitsune.eduservice.service.EduVideoService;
import com.kitsune.servicebase.exceptionhandler.GuliException;
import com.sun.xml.bind.v2.TODO;
import org.ehcache.Cache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Kitsune
 * @since 2022-03-08
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;

    //课程大纲列表，根据课程id进行查询
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {

        //1 根据课程id查询课程里面所有的章节
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id", courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);

        //2 根据课程id查询课程里面所有的小节
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id", courseId);
        List<EduVideo> eduVideos = eduVideoService.list(wrapperVideo);

        List<ChapterVo> finalChapterVo = new ArrayList<>();

        //3 遍历查询章节list集合进行封装
        for(int i = 0 ; i < eduChapterList.size() ; i++){
            //TODO 2022年5月29日18:57:58
            //new一个chapter对象，从集合获取对象
            EduChapter eduChapter = eduChapterList.get(i);

            //new一个ChapterVo，把list中获取到的对象放进去
            ChapterVo chapterVo = new ChapterVo();
            //利用spring的工具类
            BeanUtils.copyProperties(eduChapter, chapterVo);
            //把接收到的对象放入最终的list
            finalChapterVo.add(chapterVo);

            //4 遍历查询小节list集合， 进行封装
            List<VideoVo> finalVideoList = new ArrayList<>();
            for(int m = 0 ; m < eduVideos.size() ; m++){
                EduVideo eduVideo = eduVideos.get(m);
                //根据courseId判断课程归属是否一致
                if(eduVideo.getChapterId().equals(chapterVo.getId())){
                    //如果相等就把这个小节存到一个finalVideoList中
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo, videoVo);
                    finalVideoList.add(videoVo);
                }
            }
            //把最终的小节集合封装到章节对象中
            finalChapterVo.get(i).setChildren(finalVideoList);

        }


        return finalChapterVo;
    }

    @Override
    public boolean deleteChapter(String chapterId) {

        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id", chapterId);
        int count = eduVideoService.count(wrapper);
        if(count > 0) {
            throw new GuliException(20001, "仍有小节未删除，无法删除章节");
        }else{
            int delete = baseMapper.deleteById(chapterId);
            return delete > 0;
        }

    }
}
