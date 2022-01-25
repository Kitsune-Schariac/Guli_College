package com.kitsune.eduservice.controller;


import com.kitsune.commonutils.R;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eduService/user")
@Api(tags = "登录")
@CrossOrigin  //跨域访问
public class EduLoginController {

    //login
    @PostMapping("login")
    public R login(){



        return R.ok().data("token", "admin");
    }

    //info
    @GetMapping("info")
    public R info(){

        return R.ok().data("roles", "[admin]").data("name", "admin").data("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }

}