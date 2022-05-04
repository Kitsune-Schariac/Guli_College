package com.kitsune.test;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Scanner;

public class demo {

//    @Autowired
//    StringEncryptor

    @Test
    public void main() throws Exception {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "oss-cn-chengdu.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5tNuiVwP1udjxZthahvc";
        String accessKeySecret = "j8tMJbX6UdDfnVudZjc2DU4J5dilfS";
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "private-kitsune";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // ossClient.listObjects返回ObjectListing实例，包含此次listObject请求的返回结果。
            ObjectListing objectListing = ossClient.listObjects(bucketName);
            // objectListing.getObjectSummaries获取所有文件的描述信息。
            for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                System.out.println(" - " + objectSummary.getKey() + "  " +
                        "(size = " + objectSummary.getSize() + ")");
            }
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @Test
    public void run(){
//        Scanner input = new Scanner(System.in);
//        System.out.println("please input");
        //获取输入
//        String publicStr = input.nextLine();
        String publicStr = "wdnmdsb";
        System.out.println(publicStr);
        char[] keyC = publicStr.toCharArray();

        for(int i = 0 ; i < keyC.length ; i++) {
            keyC[i] = (char)(keyC[i] ^ 50);
        }
        System.out.println(new String(keyC));
    }

    @Test
    public void crypt(){

        String publicStr = "AfiY2X5yO3yqezl49lWP2Na1mfr75h";
        System.out.println(publicStr);
        char[] keyC = publicStr.toCharArray();

        for (int i = 0 ; i < keyC.length ; i ++){
            if((keyC[i] == 'Z') || (keyC[i] == 'z')){
                keyC[i] = (char)(keyC[i] - 25);
            }else if(keyC[i] == '9'){
                keyC[i] = (char)(keyC[i] - 9);
            }
            else {
                keyC[i] = (char) (keyC[i] + 1);
            }

        }
        System.out.println(keyC);

    }

}