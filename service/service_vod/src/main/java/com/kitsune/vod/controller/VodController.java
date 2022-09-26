package com.kitsune.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.kitsune.servicebase.exceptionhandler.GuliException;
import com.kitsune.vod.service.VodService;
import com.kitsune.commonutils.R;
import com.kitsune.vod.utils.ConstantVodUtils;
import com.kitsune.vod.utils.InitVodClient;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
//@CrossOrigin
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

    /**
     * 根据视频id获取凭证的方法
     * @param id 视频id
     * @return 播放凭证
     * @since 2022年9月21日
     */
    @ApiOperation(value = "根据视频id获取凭证")
    @GetMapping("getPlayAuth/{id}")
    public R getPlayAuth(@PathVariable String id) {
        try {
            //创建初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建获取凭证的request和response
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
            //向request设置视频id
            request.setVideoId(id);
            //调用方法得到凭证
            response = client.getAcsResponse(request);

            return R.ok().data("playAuth", response.getPlayAuth());

        }catch (Exception e) {
            throw new GuliException(20001, "视频凭证获取失败");
        }

    }

}
