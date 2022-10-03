package com.kitsune.educenter.mapper;

import com.kitsune.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author Kitsune
 * @since 2022-07-05
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    //查询某一天注册人数
    Integer countRegisterDay(String day);
}
