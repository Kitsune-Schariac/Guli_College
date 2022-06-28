package com.kitsune.vod.controller;

import com.kitsune.vod.service.VodService;
import com.kitsune.commonutils.R;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
@CrossOrigin
public class VodController {
    @Autowired
    private VodService vodService;

    //上传视频到阿里云
    @ApiOperation(value = "上传视频到阿里云")
    @PostMapping("uploadAliyunVideo")
    public R uploadAliVideo(MultipartFile file){
        String videoId = vodService.uploadVideoAly(file);

        return R.ok().data("videoId", videoId);
    }

    //根据视频id删除视频
    @ApiOperation(value = "删除视频")
    @DeleteMapping("removeAliyunVideo/{id}")
    public R removeAliyunVideo(
            @ApiParam(name = "id", value = "视频id", required = true)
            @PathVariable String id){

        vodService.removeAliyunVideo(id);

        return R.ok();
    }

    //删除多个视频的方法
    @ApiOperation(value = "删除多个视频")
    @DeleteMapping("delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList){

        vodService.removeMoreAliyunVideo(videoIdList);
        return R.ok();
    }

}
