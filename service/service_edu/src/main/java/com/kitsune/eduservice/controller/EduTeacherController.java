package com.kitsune.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kitsune.commonutils.R;
import com.kitsune.eduservice.entity.EduTeacher;
import com.kitsune.eduservice.entity.vo.eduTeacherVo;
import com.kitsune.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author Kitsune
 * @since 2021-12-22
 */
@Api(tags = "讲师管理")
@RestController
@RequestMapping("/eduservice/edu-teacher")
//@CrossOrigin
public class EduTeacherController {

    @Autowired
    private EduTeacherService eduTeacherService;

    /*//查询
    @ApiOperation(value = "查询")
    @GetMapping("/findAll")
    public List<EduTeacher> findTeacher(){
        List<EduTeacher> eduTeachers = eduTeacherService.list(null);
        return eduTeachers;
    }

    //删除
    @ApiOperation(value = "删除讲师")
    @DeleteMapping("/deleteTecher/{id}")
    public boolean deleteTecher(@ApiParam(name = "id", value = "讲师id", required = true)
                                @PathVariable String id) {
        return eduTeacherService.removeById(id);
    }*/




    //查询所有数据
    @ApiOperation(value = "查询所有的讲师")
    @GetMapping("/find")
    public R findTeachers() {
        List<EduTeacher> eduTeachers = eduTeacherService.list(null);
        return R.ok().data("items", eduTeachers);

        /*try {
            int i = 10/0;
        }catch(Exception e) {
            //执行自定义异常
            throw new GuliException(20001, "执行了自定义异常处理……");
        }*/

    }

    //删除
    @ApiOperation(value = "删除讲师")
    @DeleteMapping("/delete/{id}")
    public R deleteTeacher(@ApiParam(name = "id", value = "讲师id", required = true)
                           @PathVariable String id) {
        boolean deleted = eduTeacherService.removeById(id);
        if(deleted){
            return R.ok();
        }
        else {
            return R.error();
        }
    }

    //分页查询讲师
    //current  当前页
    //limit    每页记录数
    @ApiOperation(value = "分页查询讲师")
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageListTeacher(@PathVariable long current,
                             @PathVariable long limit){

        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);
        eduTeacherService.page(pageTeacher, null);
        long total = pageTeacher.getTotal();    //总记录数
        List<EduTeacher> records = pageTeacher.getRecords();  //数据list集合

        /*Map map = new HashMap();
        map.put("total", total);
        map.put("rows", records);
        return R.ok().data(map);*/

        return R.ok().data("total", total).data("rows", records);

    }

    //条件查询分页
    @ApiOperation(value = "条件查询分页")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) eduTeacherVo eduTeacherVo) {
        //创建一个page对象
        Page<EduTeacher> teacherPage = new Page<>(current, limit);

        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();

        //多条件组合
        String name = eduTeacherVo.getName();
        Integer level = eduTeacherVo.getLevel();
        String begin = eduTeacherVo.getBegin();
        String end = eduTeacherVo.getEnd();
        //判断条件值是否为空， 如果不为空就拼接条件
        if(!StringUtils.isEmpty(name)) {
            //构建条件
            wrapper.like("name", name);
        }
        if(!StringUtils.isEmpty(level)) {
            wrapper.eq("level", level);
        }
        if(!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if(!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }
//        排序
        wrapper.orderByDesc("gmt_create");


        //调用方法实现条件查询分页
        eduTeacherService.page(teacherPage, wrapper);
        long total = teacherPage.getTotal();//总记录数
        List<EduTeacher> list = teacherPage.getRecords();//数据list集合
        return R.ok().data("total", total).data("rows", list);
    }

    //添加讲师
    @ApiOperation(value = "添加讲师")
    @PostMapping("/addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean save = eduTeacherService.save(eduTeacher);
        if(save){
            return R.ok();
        }
        else {
            return R.error();
        }
    }

    //获取某个讲师
    @ApiOperation(value = "根据id查询讲师")
    @PostMapping("/getTeacher/{id}")
    public R getTeacher(@PathVariable String id) {
        EduTeacher eduTeacher = eduTeacherService.getById(id);
        return R.ok().data("teacher", eduTeacher);

    }

    //修改讲师
    @ApiOperation(value = "修改讲师")
    @PostMapping("/updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean flag = eduTeacherService.updateById(eduTeacher);
        if(flag){
            return R.ok();
        }
        else{
            return R.error();
        }
    }

}





















