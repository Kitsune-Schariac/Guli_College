package com.kitsune.educms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kitsune.educms.entity.CrmBanner;
import com.kitsune.educms.mapper.CrmBannerMapper;
import com.kitsune.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author Kitsune
 * @since 2022-07-01
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    @Override
    @Cacheable(value = "banner", key = "'selectIndexList'")
    public List<CrmBanner> selectList() {

//        QueryWrapper<CrmBanner> wrapper = new QueryWrapper<>();
//        wrapper.orderByDesc("id");
//        wrapper.last("limit 2");

        List<CrmBanner> banners = baseMapper.selectList(null);

        return banners;
    }
}
