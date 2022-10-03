package com.kitsune.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kitsune.commonutils.JwtUtils;
import com.kitsune.commonutils.MD5;
import com.kitsune.educenter.entity.UcenterMember;
import com.kitsune.educenter.entity.vo.MemberInfo;
import com.kitsune.educenter.entity.vo.RegisterVo;
import com.kitsune.educenter.mapper.UcenterMemberMapper;
import com.kitsune.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kitsune.servicebase.exceptionhandler.GuliException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author Kitsune
 * @since 2022-07-05
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String login(UcenterMember member) {

        //获取用户的手机号和密码
        String mobile = member.getMobile();
        String password = MD5.encrypt(member.getPassword());

        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
            throw new GuliException(20001, "登录失败，非法的手机号或密码");
        }

        //判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        UcenterMember mobileMember = baseMapper.selectOne(wrapper);
        if(mobileMember == null){
            throw new GuliException(20001, "登陆失败，手机号或密码错误");
        }

        //判断密码
        if(!password.equals(mobileMember.getPassword())){
            throw new GuliException(20001, "登陆失败，手机号或密码错误");
        }

        //判断用户是否被禁用
        if(mobileMember.getIsDisabled()) {
            throw new GuliException(20001, "登录失败，您已被封禁");
        }

        //调用jwt工具类生成一个token
        String token = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());

        return token;
    }

    @Override
    public boolean register(RegisterVo registerVo) {

        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();

        //先进行非空判断
        if(StringUtils.isEmpty(code) || StringUtils.isEmpty(mobile) || StringUtils.isEmpty(nickname) || StringUtils.isEmpty(password)){
            throw new GuliException(20001, "致命的错误，注册失败");
        }

        //判断验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!redisCode.equals(code)){
            throw new GuliException(20001, "注册失败");
        }

        //判断手机号是否唯一
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count > 0){
            throw new GuliException(20001, "该用户已注册");
        }

        //添加用户信息
        UcenterMember member = new UcenterMember();
        member.setNickname(nickname);
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(password));
        member.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");
        member.setIsDisabled(false);
        baseMapper.insert(member);

        return true;
    }

    //获取首页的用户信息
    @Override
    public MemberInfo getInfo(String id) {

        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        UcenterMember member = baseMapper.selectOne(wrapper);
        MemberInfo memberInfo = new MemberInfo();
        BeanUtils.copyProperties(member, memberInfo);

        return memberInfo;
    }

    //根据openid查询用户信息
    @Override
    public UcenterMember getOpenIdMember(String openid) {

        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);

        UcenterMember member = baseMapper.selectOne(wrapper);

        return member;
    }

    //查询某一天注册人数
    @Override
    public Integer countRegisterDay(String day) {

        return baseMapper.countRegisterDay(day);

    }
}
