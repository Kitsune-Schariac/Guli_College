package com.kitsune.eduorder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.gson.Gson;
import com.kitsune.eduorder.entity.Order;
import com.kitsune.eduorder.entity.PayLog;
import com.kitsune.eduorder.mapper.PayLogMapper;
import com.kitsune.eduorder.service.OrderService;
import com.kitsune.eduorder.service.PayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kitsune.eduorder.utils.HttpClient;
import com.kitsune.servicebase.exceptionhandler.GuliException;
import io.swagger.models.Xml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author Kitsune
 * @since 2022-09-21
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;

    //生成微信支付二维码
    @Override
    public Map createNative(String orderNo) {

        try {

            //1 根据订单号查询订单信息
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no", orderNo);
            Order order = orderService.getOne(wrapper);

            //2使用map设置二维码需要的参数
            Map m = new HashMap();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            m.put("body", order.getCourseTitle()); //课程标题
            m.put("out_trade_no", orderNo);  //订单号
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");
            m.put("spbill_create_ip", "127.0.0.1");
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");
            m.put("trade_type", "NATIVE");

            //3 发送httpclient请求，传递xml格式参数，微信支付提供的固定的地址
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //设置xml格式的参数
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            //执行请求发送
            client.post();

            //4得到发送请求返回结果
            //返回的内容使用xml格式返回
            String content = client.getContent();

            //把xml格式转换成map集合，把map集合返回
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);

            //最终数据封装
            Map map = new HashMap<>();
            map.put("out_trade_no", orderNo);
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            map.put("result_code", resultMap.get("result_code"));  //返回二维码操作状态码
            map.put("code_url", resultMap.get("code_url"));  //二维码地址

            return map;

        }catch (Exception e) {
            throw new GuliException(20001, "失败");
        }

    }

    //根据订单号查询订单支付状态
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {

        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            //2、发送httpclient
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();

            //得到请求返回的内容
            String content = client.getContent();
            Map<String, String> map = WXPayUtil.xmlToMap(content);

            return map;

        } catch (Exception e) {
            return null;
        }

    }

    //向支付表添加记录，并且更新订单状态
    @Override
    public void updateOrdersStatus(Map<String, String> map) {

        //从map获取订单号
        String orderNo = map.get("out_trade_no");
        //根据订单id查询订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        Order order = orderService.getOne(wrapper);

        //更新订单表的订单状态
        if(order.getStatus().intValue() == 1) {
            return;
        }
        order.setStatus(1); //1代表已支付
        orderService.updateById(order);

        //向支付表添加支付记录
        PayLog payLog=new PayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(JSONObject.toJSONString(map));
        baseMapper.insert(payLog);//插入到支付日志表

    }
}
