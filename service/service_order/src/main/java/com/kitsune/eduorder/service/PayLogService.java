package com.kitsune.eduorder.service;

import com.kitsune.eduorder.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author Kitsune
 * @since 2022-09-21
 */
public interface PayLogService extends IService<PayLog> {

    //生成微信支付二维码
    Map createNative(String orderNo);

    //根据订单号查询订单支付状态
    Map<String, String> queryPayStatus(String orderNo);

    //向支付表添加记录，并且更新订单状态
    void updateOrdersStatus(Map<String, String> map);
}
