package com.kitsune.eduorder.service.impl;

import com.kitsune.commonutils.ordervo.CourseWebVoOrder;
import com.kitsune.commonutils.ordervo.UcenterMemberOrder;
import com.kitsune.eduorder.client.EduClient;
import com.kitsune.eduorder.client.UcenterClient;
import com.kitsune.eduorder.entity.Order;
import com.kitsune.eduorder.mapper.OrderMapper;
import com.kitsune.eduorder.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kitsune.eduorder.utils.OrderNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author Kitsune
 * @since 2022-09-21
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduClient eduClient;
    @Autowired
    private UcenterClient ucenterClient;

    //生成订单
    @Override
    public String createOrders(String courseId, String memberIdByJwtToken) {

        //通过远程调用根据用户id获取用户信息
        UcenterMemberOrder userInfoOrder = ucenterClient.getUserInfoOrder(memberIdByJwtToken);

        //通过远程调用根据课程id获取课程信息
        CourseWebVoOrder courseInfoOrder = eduClient.getCourseInfoOrder(courseId);

        //创建order对象，向order对象里设置数据
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo()); //订单号
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfoOrder.getTitle());
        order.setCourseCover(courseInfoOrder.getCover());
        order.setTeacherName(courseInfoOrder.getTeacherName());
        order.setTotalFee(courseInfoOrder.getPrice());
        order.setMemberId(memberIdByJwtToken);
        order.setMobile(userInfoOrder.getMobile());
        order.setNickname(userInfoOrder.getNickname());
        order.setStatus(0);  //支付状态 0未支付 1已支付
        order.setPayType(1);  //支付类型 微信1

        baseMapper.insert(order);

        return order.getOrderNo();
    }
}
