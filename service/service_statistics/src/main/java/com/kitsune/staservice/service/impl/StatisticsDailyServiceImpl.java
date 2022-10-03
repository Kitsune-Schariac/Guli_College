package com.kitsune.staservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kitsune.commonutils.R;
import com.kitsune.staservice.client.UcenterClient;
import com.kitsune.staservice.entity.StatisticsDaily;
import com.kitsune.staservice.mapper.StatisticsDailyMapper;
import com.kitsune.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author Kitsune
 * @since 2022-09-26
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;

    //统计某一天注册人数
    @Override
    public void registerCount(String day) {

        //在添加之前先删除表相同日期的数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated", day);
        baseMapper.delete(wrapper);

        R registerR = ucenterClient.countRegister(day);
        Integer countRegister = (Integer) registerR.getData().get("count");

        //把获取到的数据添加到数据库的分析表里面
        StatisticsDaily sta = new StatisticsDaily();
        sta.setRegisterNum(countRegister);
        sta.setDateCalculated(day);

        sta.setCourseNum(RandomUtils.nextInt(100, 200));
        sta.setLoginNum(RandomUtils.nextInt(100, 200));
        sta.setVideoViewNum(RandomUtils.nextInt(100, 200));

        // TODO: 2022/9/26 这里的登录和观看之类的数据需要获取真实数据
        baseMapper.insert(sta);


    }

    //图表显示，返回日期json数组，数量json数组
    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {

        //根据条件查询对应数据
        QueryWrapper<StatisticsDaily> staWrapper = new QueryWrapper<>();
        staWrapper.between("date_calculated", begin, end);
        staWrapper.select("date_calculated", type);
        List<StatisticsDaily> list = baseMapper.selectList(staWrapper);

        //封装日期和日期的对应数量
        //创建好日期和数量的list集合
        List<String> dateList = new ArrayList<>();
        List<Integer> dataList = new ArrayList<>();

        //开始遍历拿到的list
        for(int i = 0 ; i < list.size() ; i++) {
            StatisticsDaily daily = list.get(i);
            //把日期放到日期集合中
            String date = daily.getDateCalculated();
            dateList.add(date);

            //把数据数量放入list
            switch (type) {
                case "register_num":
                    dataList.add(daily.getRegisterNum());
                    break;
                case "login_num":
                    dataList.add(daily.getLoginNum());
                    break;
                case "video_view_num":
                    dataList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    dataList.add(daily.getCourseNum());
                    break;
            }

        }

        //把封装好的数据放入map
        Map<String, Object> map = new HashMap<>();
        map.put("date_calculatedList", dateList);
        map.put("numDataList", dataList);

        return map;
    }
}
