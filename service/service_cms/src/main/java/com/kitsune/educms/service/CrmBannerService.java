package com.kitsune.educms.service;

import com.kitsune.educms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author Kitsune
 * @since 2022-07-01
 */
public interface CrmBannerService extends IService<CrmBanner> {
    List<CrmBanner> selectList();
}
