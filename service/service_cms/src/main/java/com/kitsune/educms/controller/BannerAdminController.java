package com.kitsune.educms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kitsune.commonutils.R;
import com.kitsune.educms.entity.CrmBanner;
import com.kitsune.educms.service.CrmBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author Kitsune
 * @since 2022-07-01
 */
@Api(tags = "后台banner管理")
@RestController
@RequestMapping("/educms/banneradmin")
@CrossOrigin
public class BannerAdminController {

    @Autowired
    private CrmBannerService bannerService;

    //分页查询banner
    @ApiOperation(value = "分页查询banner")
    @GetMapping("pageBanner/{page}/{limit}")
    public R pageBanner (@ApiParam(name = "page", value = "当前页", required = true)
                         @PathVariable long page,
                         @ApiParam(name = "limit", value = "页记录数", required = true)
                         @PathVariable long limit) {

        Page<CrmBanner> bannerPage = new Page<>();
        bannerPage.setCurrent(page);
        bannerPage.setSize(limit);

        IPage<CrmBanner> info = bannerService.page(bannerPage, null);

        return R.ok().data("total", info.getTotal()).data("records", info.getRecords());
    }

    @ApiOperation(value = "添加banner")
    @PostMapping("addBanner")
    public R save(@RequestBody CrmBanner banner){
        bannerService.save(banner);
        return R.ok();
    }

    @ApiOperation(value = "修改banner")
    @PostMapping("updateBanner")
    public R update(@RequestBody CrmBanner banner){
        bannerService.updateById(banner);
        return R.ok();
    }

    @ApiOperation(value = "删除banner")
    @DeleteMapping("deleteBanner/{id}")
    public R update(@PathVariable String id){
        bannerService.removeById(id);
        return R.ok();
    }

}

