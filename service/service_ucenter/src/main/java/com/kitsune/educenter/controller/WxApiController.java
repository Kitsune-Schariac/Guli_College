package com.kitsune.educenter.controller;

import com.google.gson.Gson;
import com.kitsune.commonutils.JwtUtils;
import com.kitsune.educenter.entity.UcenterMember;
import com.kitsune.educenter.service.UcenterMemberService;
import com.kitsune.educenter.utils.ConstantPropertiesUtil;
import com.kitsune.educenter.utils.HttpClientUtils;
import com.kitsune.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@Controller
//@CrossOrigin
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;

    /**
     * 个人理解
     * 首先是用户访问/api/ucenter/wx/login，此时服务器调用微信的API发送请求，通过appID和secret获得二维码
     * 用户扫描二维码之后就会在手机上看到微信登录的页面，当用户点击确认登录，微信的服务器就会向我们的业务服务器发起回调
     * 我们业务服务器设定的回调链接设定的是callback这个接口，所以我们需要开发的接口主要就是这个回调的callback接口
     *
     * 值得注意的一点是，用户在扫码登录的时候，除了一开始访问了服务器的接口，其实其余地方是没有和我们的服务器交互的，
     * 第一次访问服务器看到服务器跳转的微信二维码的页面，然后就是和微信进行交互，直到完成登录，微信回调告诉我们用户完成了登录
     *
     * 当然了，这个流程很特别的地方就在于这是尚硅谷为了让所有学习者都可以测试成功来设置的一个流程
     * 用户扫码登录之后的回调就是尚硅谷添加的一个特殊流程，是尚硅谷的服务器上做了一个程序，回调到我们本地的服务器
     * 如果是在公司的实际项目，那么我们只需要把回调要做的方法部署到服务器的callback接口上， 并不需要特定的8160端口
     *
     */

    /**
     * @param code
     * @param state
     * 通过code 和 state 两个属性，调用微信的API获取用户的信息
     */
    @GetMapping("callback")
    public String callback(String code, String state){
//        System.out.println("code: " + code);
//        System.out.println("state: " + state);
        //获取code
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        String accessTokenUrl = String.format(baseAccessTokenUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code);

        //拿着code请求微信的API，得到access_token 和 openid
        String result = null;
        try {
            result = HttpClientUtils.get(accessTokenUrl);
//            System.out.println("accessToken============= " + result);
        } catch (Exception e) {
            throw new GuliException(20001, "获取access_token失败");
        }

        //把获取到的json字符串解析成map对象，从中提取到access_Token和openid
        Gson gson = new Gson();
        HashMap mapAccessToken = gson.fromJson(result, HashMap.class);
        String access_token = (String)mapAccessToken.get("access_token");
        String openid = (String)mapAccessToken.get("openid");
//        System.out.println(access_token);
//        System.out.println(openid);


        UcenterMember member = memberService.getOpenIdMember(openid);
        if(member == null) {

            //用access_token和openid去换到用户信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
            String resultUserInfo = null;
            try {
                resultUserInfo = HttpClientUtils.get(userInfoUrl);
//                System.out.println(resultUserInfo);
            } catch (Exception e) {
                throw new GuliException(20001, "获取信息失败");
            }

            HashMap userInfo = gson.fromJson(resultUserInfo, HashMap.class);
            String nickname = (String) userInfo.get("nickname");
            String headimgurl = (String) userInfo.get("headimgurl");

            member = new UcenterMember();
            member.setAvatar(headimgurl);
            member.setNickname(nickname);
            member.setOpenid(openid);
            memberService.save(member);
        }

        //使用jwt根据member对象生成token
        System.out.println(member.getId());
        System.out.println(member.getNickname());
        String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());

        //最后返回首页，通过路径传递token
        return "redirect:http://localhost:3000?token=" + token;
    }

    //生成扫描二维码
    @GetMapping("login")
    public String genQrConnect(HttpSession session) {
        //微信开放凭条授权baseURL
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //回调地址
        String redirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL; //获取业务服务器重定向地址
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8"); //编码
        } catch (UnsupportedEncodingException e) {
            throw new GuliException(20001, e.getMessage());
        }

        // 防止csrf攻击（跨站请求伪造攻击）
//        String state = UUID.randomUUID().toString().replaceAll("-", "");//一般情况下会使用一个随机数
        //String state = "imhelen";//为了让大家能够使用我搭建的外网的微信回调跳转服务器，这里填写你在ngrok的前置域名
        //System.out.println("state = " + state);

        // 采用redis等进行缓存state 使用sessionId为key 30分钟后过期，可配置
        //键："wechar-open-state-" + httpServletRequest.getSession().getId()
        //值：satte
        //过期时间：30分钟

        //生成qrcodeUrl
        String url = String.format(
                baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl,
//                state
                "atguigu"
        );
        return "redirect:" + url;

    }

}
