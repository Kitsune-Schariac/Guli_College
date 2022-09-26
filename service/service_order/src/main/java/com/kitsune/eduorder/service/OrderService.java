package com.kitsune.eduorder.service;

import com.kitsune.eduorder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author Kitsune
 * @since 2022-09-21
 */
public interface OrderService extends IService<Order> {

    //生成订单
    String createOrders(String courseId, String memberIdByJwtToken);
}
