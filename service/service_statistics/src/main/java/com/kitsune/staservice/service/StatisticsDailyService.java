package com.kitsune.staservice.service;

import com.kitsune.staservice.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author Kitsune
 * @since 2022-09-26
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    //统计某一天注册人数
    void registerCount(String day);

    //图表显示，返回日期json数组，数量json数组
    Map<String, Object> getShowData(String type, String begin, String end);
}
