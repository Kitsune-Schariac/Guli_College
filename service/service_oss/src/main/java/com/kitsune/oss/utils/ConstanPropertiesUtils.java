package com.kitsune.oss.utils;


import com.kitsune.commonutils.Key;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//当项目已启动，spring接口，spring加载之后，执行接口中的一个方法
@Component
public class ConstanPropertiesUtils implements InitializingBean {

    //读取配置文件内容
    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.file.keyid}")
    private String keyId;

    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;

    @Value("${aliyun.oss.file.bucketname}")
    private String bucketName;

    //定义公开常量
    public static String END_POINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String ACCESS_NAME;

    Key key = new Key();

    @Override
    public void afterPropertiesSet() throws Exception {

        END_POINT = endpoint;
        ACCESS_KEY_ID = key.simpleDecrypt(keyId);
        ACCESS_KEY_SECRET = key.simpleDecrypt(keySecret);
        ACCESS_NAME = bucketName;

    }
}
