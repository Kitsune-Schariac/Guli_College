package com.kitsune.educenter.controller;


import com.kitsune.commonutils.JwtUtils;
import com.kitsune.commonutils.R;
import com.kitsune.educenter.entity.UcenterMember;
import com.kitsune.educenter.entity.vo.MemberInfo;
import com.kitsune.educenter.entity.vo.RegisterVo;
import com.kitsune.educenter.service.UcenterMemberService;
import com.kitsune.servicebase.exceptionhandler.GuliException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author Kitsune
 * @since 2022-07-05
 */
@RestController
@RequestMapping("/educenter/member")
@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService ucenterMemberService;

    //登录
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember member){
        //调用service方法
        String token = ucenterMemberService.login(member);

        return R.ok().data("token", token);
    }

    //注册
    @ApiOperation(value = "注册用户")
    @PostMapping("register")
    public R registerUser(@RequestBody RegisterVo registerVo){
        boolean register = ucenterMemberService.register(registerVo);
        if(!register){
            return R.error().message("注册失败");
        }
        return R.ok();
    }

    //根据token获取用户信息
    @ApiOperation(value = "根据token获取用户信息")
    @GetMapping("auth/getLoginInfo")
    public R getLoginInfo(HttpServletRequest request) {

        try {
            String memberId = JwtUtils.getMemberIdByJwtToken(request);
            MemberInfo loginInfo = new MemberInfo();
            if(memberId != null && memberId != ""){
                loginInfo = ucenterMemberService.getInfo(memberId);
            }
            return R.ok().data("info", loginInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001, "error");
        }
    }


}

