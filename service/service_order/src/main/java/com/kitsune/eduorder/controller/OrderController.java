package com.kitsune.eduorder.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kitsune.commonutils.JwtUtils;
import com.kitsune.commonutils.R;
import com.kitsune.eduorder.entity.Order;
import com.kitsune.eduorder.service.OrderService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author Kitsune
 * @since 2022-09-21
 */
@RestController
@RequestMapping("/eduorder/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "生成订单的方法")
    @PostMapping("createOrder/{courseId}")
    public R saveOrder(
            @ApiParam(name = "courseId", value = "课程id", required = true)
            @PathVariable String courseId,
            HttpServletRequest request){

        String orderNo = orderService.createOrders(courseId, JwtUtils.getMemberIdByJwtToken(request));

        //返回订单号
        return R.ok().data("orderId", orderNo);
    }

    @ApiOperation(value = "根据订单id查询订单信息")
    @GetMapping("getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderId);

        Order one = orderService.getOne(wrapper);
        return R.ok().data("item", one);
    }

    //根据课程id和用户id查询订单表中订单装填
    @GetMapping("isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable String courseId,
                               @PathVariable String memberId) {

        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        wrapper.eq("member_id", memberId);
        wrapper.eq("status", 1);
        int count = orderService.count(wrapper);

        //大于零即为已经支付，反之为未购买
        return count > 0;

    }

}

