package com.kitsune.staservice.client;

import com.kitsune.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-ucenter")
public interface UcenterClient {

    //查询某一天的注册人数
    @GetMapping("/educenter/member/countRegister/{day}")
    public R countRegister(@PathVariable("day") String day);

}
