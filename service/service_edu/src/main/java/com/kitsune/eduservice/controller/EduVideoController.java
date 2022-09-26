package com.kitsune.eduservice.controller;


import com.kitsune.commonutils.R;
import com.kitsune.eduservice.client.VodClient;
import com.kitsune.eduservice.entity.EduVideo;
import com.kitsune.eduservice.service.EduVideoService;
import com.kitsune.servicebase.exceptionhandler.GuliException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author Kitsune
 * @since 2022-03-08
 */
@RestController
@RequestMapping("/eduservice/edu-video")
//@CrossOrigin
public class EduVideoController {

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private VodClient vodClient;

    //添加小节
    @ApiOperation(value = "添加小节")
    @PostMapping("addVideo")
    public R addVideo(@ApiParam(name = "eduVideo", value = "小节信息", required = true)
                      @RequestBody EduVideo eduVideo){
        boolean save = eduVideoService.save(eduVideo);
        if(save){
            return R.ok();
        }else{
            throw new GuliException(20001, "添加小节失败");
        }
    }

    //删除小节
    // TODO: 2022/6/27 需要将这个方法封装到service
    @ApiOperation(value = "删除小节")
    @DeleteMapping("deleteVideo/{id}")
    public R deleteVideo(@PathVariable String id){
        //根据小节id查询到视频的id，然后删除视频
        EduVideo video = eduVideoService.getById(id);
        String sourceId = video.getVideoSourceId();
        //当视频id存在，就删除这个视频
        if(!StringUtils.isEmpty(sourceId)){
            vodClient.removeAliyunVideo(sourceId);
        }

        boolean remove = eduVideoService.removeById(id);
        if(remove){
            return R.ok();
        }else {
            throw new GuliException(20001, "添加删除小节失败");
        }
    }

    //修改小节
    @ApiOperation(value = "修改小节")
    @PostMapping("updateVideo")
    public  R updateVideo(@RequestBody EduVideo eduVideo){
        boolean update = eduVideoService.updateById(eduVideo);
        if(update){
            return R.ok();
        }else{
            throw new GuliException(20001, "修改小节信息失败");
        }
    }

    //根据小节id查询小节信息
    @ApiOperation(value = "根据小节id查询")
    @GetMapping("getVideo/{id}")
    public R getVideoById(@PathVariable String id){
        EduVideo video = eduVideoService.getById(id);
        return R.ok().data("video", video);
    }
}

