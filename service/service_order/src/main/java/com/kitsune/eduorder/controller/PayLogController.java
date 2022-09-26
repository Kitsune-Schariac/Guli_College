package com.kitsune.eduorder.controller;


import com.kitsune.commonutils.R;
import com.kitsune.eduorder.service.PayLogService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author Kitsune
 * @since 2022-09-21
 */
@RestController
@RequestMapping("/eduorder/pay-log")
@Slf4j
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    //生成微信支付二维码
    @ApiOperation(value = "生成微信支付二维码")
    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo) {
        //返回信息，包含二维码地址，还有其他需要的信息
        Map map = payLogService.createNative(orderNo);
        System.out.println("****返回二维码map集合" + map);
        return R.ok().data(map);

    }

    //查询订单支付状态
    @GetMapping("queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo) {
        Map<String, String> map = payLogService.queryPayStatus(orderNo);
        System.out.println("====查询订单状态map集合" + map);

        if(map == null ) {
            return R.error().message("支付出错");
        }
        //如果返回的map不为空，通过map获取订单状态
        if(map.get("trade_state").equals("SUCCESS")) {
            //添加记录到支付表，并且更新订单表的订单状态
            payLogService.updateOrdersStatus(map);
            return R.ok().message("支付成功");
        }
        return R.ok().code(25000).message("支付中");
    }

}

