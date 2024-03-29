package com.kitsune.educenter.service;

import com.kitsune.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kitsune.educenter.entity.vo.MemberInfo;
import com.kitsune.educenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author Kitsune
 * @since 2022-07-05
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    //登录的方法
    String login(UcenterMember member);

    //注册的方法
    boolean register(RegisterVo registerVo);

    //根据id获取用户信息
    MemberInfo getInfo(String id);

    //根据openid查询用户信息
    UcenterMember getOpenIdMember(String openid);

    //查询某一天注册人数
    Integer countRegisterDay(String day);

}
