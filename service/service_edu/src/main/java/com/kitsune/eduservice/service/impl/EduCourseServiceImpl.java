package com.kitsune.eduservice.service.impl;

import ch.qos.logback.core.status.StatusUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kitsune.eduservice.entity.EduCourse;
import com.kitsune.eduservice.entity.EduCourseDescription;
import com.kitsune.eduservice.entity.EduTeacher;
import com.kitsune.eduservice.entity.frontVo.CourseFrontVo;
import com.kitsune.eduservice.entity.vo.CourseInfoVo;
import com.kitsune.eduservice.entity.vo.CoursePublishVo;
import com.kitsune.eduservice.entity.vo.CourseQueryVo;
import com.kitsune.eduservice.mapper.EduCourseDescriptionMapper;
import com.kitsune.eduservice.mapper.EduCourseMapper;
import com.kitsune.eduservice.service.EduChapterService;
import com.kitsune.eduservice.service.EduCourseDescriptionService;
import com.kitsune.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kitsune.eduservice.service.EduVideoService;
import com.kitsune.servicebase.exceptionhandler.GuliException;
import org.springframework.util.StringUtils;
import org.apache.tomcat.util.modeler.BaseModelMBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Kitsune
 * @since 2022-03-08
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    //这里注入description的service是因为在添加课程简介的时候要用到这个service，而在spring管理下的地方我们只需要注入就可以直接使用
    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;
    @Autowired
    private EduCourseMapper eduCourseMapper;
    @Autowired
    private EduVideoService eduVideoService;
    @Autowired
    private EduChapterService eduChapterService;

    //添加课程基本信息的方法
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {

        //1 向课程表添加课程基本信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);//直接使用BeanUtils类的方法
        int insert = baseMapper.insert(eduCourse);

        //insert返回值如果是0那么就是查询执行失败，这时就手动抛出异常
        if (insert == 0) {
            //添加失败
            throw new GuliException(20001, "添加课程失败");
        }

        //获取添加之后的课程id
        String cid = eduCourse.getId();

        //2 向课程简介表添加课程简介
        //new一个edu_course_description，封装数据，然后save
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setDescription(courseInfoVo.getDescription());//把简介信息封装
        //设置描述id，也就是课程id
        eduCourseDescription.setId(cid);//把取出的id封装进去
        eduCourseDescriptionService.save(eduCourseDescription);

        return cid;
    }

    @Override
    //根据课程id，查询课程信息和课程的描述信息，封装在一起之后返回
    public CourseInfoVo getCourseInfo(String courseId) {

        //1查询课程表
        EduCourse course = baseMapper.selectById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(course, courseInfoVo); //把课程信息封装到新创建的对象

        //查询简介表
        EduCourseDescription courseDescription = eduCourseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(courseDescription.getDescription());

        return courseInfoVo;
    }

    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {

        //修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int update = baseMapper.updateById(eduCourse);
        if(update == 0){
            throw new GuliException(20001, "修改课程信息失败");
        }else{
            //修改简介表
            EduCourseDescription eduCourseDescription = new EduCourseDescription();
            eduCourseDescription.setDescription(courseInfoVo.getDescription());
            eduCourseDescription.setId(courseInfoVo.getId());
            boolean update1 = eduCourseDescriptionService.updateById(eduCourseDescription);
            if(!update1){
                throw new GuliException(20001, "修改描述信息失败");
            }
        }

    }

    @Override
    public CoursePublishVo getPublishCourseInfo(String id) {
        CoursePublishVo publishCourseInfo = eduCourseMapper.getPublishCourseInfo(id);
        return publishCourseInfo;
    }

    @Override
    public Page<EduCourse> coursePageCondition(long current, long limit, CourseQueryVo courseQueryVo) {

        Page<EduCourse> page = new Page<>(current, limit);
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();

        //取出条件参数
        String status = courseQueryVo.getStatus();
        String subjectId = courseQueryVo.getSubjectId();
        String subjectParentId = courseQueryVo.getSubjectParentId();
        String title = courseQueryVo.getTitle();
        String teacherId = courseQueryVo.getTeacherId();

        //准备判断条件拼接
        if(!StringUtils.isEmpty(status)){
            wrapper.like("status", status);
        }
        if(!StringUtils.isEmpty(subjectId)){
            wrapper.like("subject_id", subjectId);
        }
        if(!StringUtils.isEmpty(subjectParentId)){
            wrapper.like("subject_parent_id", subjectParentId);
        }
        if(!StringUtils.isEmpty(title)){
            wrapper.like("title", title);
        }
        if(!StringUtils.isEmpty(teacherId)){
            wrapper.like("teacher_id", teacherId);
        }

        //调用父类方法实现分页
        super.page(page, wrapper);

        return page;
    }

    @Override
    public void deleteCourse(String id) {

        //删除小节
        eduVideoService.removeVideoByCourseId(id);

        //删除章节
        eduChapterService.removeChapterByCourseId(id);

        //删除描述
        eduCourseDescriptionService.removeById(id);

        //删除课程
        int res = baseMapper.deleteById(id);
        if(res == 0){
            throw new GuliException(20001, "删除失败");
        }

    }

    @Override
    public List<EduCourse> courseLimit() {

        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 8");
        List<EduCourse> list = baseMapper.selectList(wrapper);

        return list;
    }

    //条件查询带分页查询课程
    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseFrontVo courseFrontVo) {

        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        //判断条件是否为空 不为空就拼接
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())) { //一级分类
            wrapper.eq("subject_parent_id", courseFrontVo.getSubjectParentId());
        }
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectId())) { //二级分类
            wrapper.eq("subject_id", courseFrontVo.getSubjectId());
        }
        // TODO: 2022/7/20 将后面三个排序条件增加升序
        if(!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())) { //关注度
//            wrapper.orderByDesc("buy_count", courseFrontVo.getBuyCountSort());
            wrapper.orderByDesc("buy_count");

        }
        if(!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())) { //最新创建
//            wrapper.orderByDesc("gmt_create", courseFrontVo.getGmtCreateSort());
            wrapper.orderByDesc("gmt_create");

        }
        if(!StringUtils.isEmpty(courseFrontVo.getPriceSort())) { //价格
//            wrapper.orderByDesc("price", courseFrontVo.getPriceSort());
            wrapper.orderByDesc("price");

        }

        baseMapper.selectPage(pageCourse, wrapper);


        List<EduCourse> records = pageCourse.getRecords();
        long total = pageCourse.getTotal();
        long current = pageCourse.getCurrent();
        long size = pageCourse.getSize();
        long pages = pageCourse.getPages();
        boolean hasNext = pageCourse.hasNext();
        boolean hasPrevious = pageCourse.hasPrevious();


        //把分页数据取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("current", current);
        map.put("size", size);
        map.put("pages", pages);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        map.put("records", records);

        return map;
    }
}








