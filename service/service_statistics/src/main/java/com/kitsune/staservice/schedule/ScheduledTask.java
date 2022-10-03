package com.kitsune.staservice.schedule;

import com.kitsune.staservice.service.StatisticsDailyService;
import com.kitsune.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService staService;

//    //0/5 * * * * ?表示每隔五秒执行一次
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void task1(){
//        System.out.println("task1 execute.........");
//    }

    //每天凌晨一点，把前一天的数据进行数据查询添加
    @Scheduled(cron = "0 0 1 * * ?")
    public void getYesterdayData(){
        staService.registerCount(DateUtil.formatDate(DateUtil.addDays(new Date(), -1)));
    }

}
