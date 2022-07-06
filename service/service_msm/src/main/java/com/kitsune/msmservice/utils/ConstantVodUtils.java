package com.kitsune.msmservice.utils;

import com.kitsune.commonutils.Key;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class ConstantVodUtils implements InitializingBean {


    @Value("${aliyun.vod.file.keyid}")
    private String keyId;

    @Value("${aliyun.vod.file.keysecret}")
    private String keySecret;

    //定义公开常量
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
//    public static String TEST = "fuck";
//    public static String TEST2;

    Key key = new Key();

    @Override
    public void afterPropertiesSet() throws Exception {
        ACCESS_KEY_ID = key.simpleDecrypt(keyId);
//        ACCESS_KEY_ID = key.simpleDecrypt("MUBJ6uIoqyNUBEr64ajWacDF");
        ACCESS_KEY_SECRET = key.simpleDecrypt(keySecret);
//        ACCESS_KEY_SECRET = key.simpleDecrypt("BgjZ3Y6zP4zrfam50mXQ3Ob2ngs86i");
//        TEST2 = key.simpleDecrypt("fuck");
    }
}
