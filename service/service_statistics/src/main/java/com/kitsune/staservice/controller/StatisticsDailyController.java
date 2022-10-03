package com.kitsune.staservice.controller;


import com.kitsune.commonutils.R;
import com.kitsune.staservice.client.UcenterClient;
import com.kitsune.staservice.entity.StatisticsDaily;
import com.kitsune.staservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author Kitsune
 * @since 2022-09-26
 */
@RestController
@RequestMapping("/staservice/statistics-daily")
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService staService;

    //统计某一天注册人数
    @PostMapping("registerCount/{day}")
    public R registerCount(@PathVariable String day) {
        staService.registerCount(day);

        return R.ok();
    }

    //图表显示，返回日期json数组，数量json数组
    @GetMapping("showData/{type}/{begin}/{end}")
    public R showData(@PathVariable String type,
                      @PathVariable String begin,
                      @PathVariable String end) {

        Map<String, Object> map = staService.getShowData(type, begin, end);

        return R.ok().data(map);

    }

}

