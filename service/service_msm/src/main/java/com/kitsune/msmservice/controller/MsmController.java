package com.kitsune.msmservice.controller;


import com.kitsune.commonutils.R;
import com.kitsune.msmservice.service.MsmService;
import com.kitsune.msmservice.utils.RandomUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
//@CrossOrigin
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //发送短信的方法
    @GetMapping("send/{phone}")
    public R sendMsm(@PathVariable String phone){
        //首先从redis获取验证码，如果能获取到直接返回
        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)) {
            return R.ok();
        }

        //如果redis获取不到，就发送验证码
        //生成一个随机值，传递给阿里云进行发送
        code = RandomUtil.getFourBitRandom();
        Map<String, Object> param = new HashMap<>();
        param.put("code", code);
        //调用service中发送短信的方法
//        boolean isSend = msmService.send(param, phone);
        boolean isSend = msmService.sendCode(param, phone);
        if(isSend){
            //如果发送成功，就把发送成功的验证码放到redis中并设置有效时间为五分钟
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return R.ok();
        }else {
            return R.error().message("短信发送失败");
        }

    }


}
