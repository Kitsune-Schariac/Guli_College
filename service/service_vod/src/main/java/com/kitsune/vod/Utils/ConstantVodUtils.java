package com.kitsune.vod.Utils;

import com.kitsune.commonutils.Key;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.lang.model.element.VariableElement;

@Component
public class ConstantVodUtils implements InitializingBean {

    Key key = new Key();

    @Value("${aliyun.oss.file.keyid}")
    private String keyId;

    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;

    //定义公开常量
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;

    @Override
    public void afterPropertiesSet() throws Exception {
        ACCESS_KEY_ID = key.simpleDecrypt(keyId);
        ACCESS_KEY_SECRET = key.simpleDecrypt(keySecret);
    }
}
