package com.kitsune.eduservice.client;

import com.kitsune.commonutils.R;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-vod")
@Component
public interface VodClient {

    //定义调用的方法路径
    //根据视频id删除视频
    @ApiOperation(value = "删除视频")
    @DeleteMapping("/eduvod/video/removeAliyunVideo/{id}")
    public R removeAliyunVideo(
            @ApiParam(name = "id", value = "视频id", required = true)
            @PathVariable("id") String id);

}
