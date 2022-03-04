package com.kitsune.oss.controller;

import com.kitsune.commonutils.R;
import com.kitsune.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduoss/fileoss")
@CrossOrigin
public class OssController {

    @Autowired
    private OssService ossService;

    //上传头像的方法
    @PostMapping
    public R uploadOssFile(MultipartFile file){
        //获取上传文件， MultipartFile
        //返回上传的路径
        String url = ossService.uploadFileAvatar(file);


        return R.ok().data("url", url);
//        这里要把云端图片的url返回给前端，否则前端就没有图像的url，也无法将url再返回到后台数据库
//        return R.ok();
    }

}
