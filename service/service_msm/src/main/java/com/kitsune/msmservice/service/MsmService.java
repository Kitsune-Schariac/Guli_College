package com.kitsune.msmservice.service;

import java.util.Map;

public interface MsmService {

    //发送短信的方法
    boolean send(Map<String, Object> param, String phone);

    //发送短信的方法（阿里云市场购买的临时信息服务）
    boolean sendCode(Map<String, Object> param, String phone);

}
