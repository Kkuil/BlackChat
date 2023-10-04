package com.kkuil.blackchat.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author Kkuil
 * @Date 2023/10/4 11:02
 * @Description aliyun
 */
@Service
@Slf4j
public class AliyunOssService {

    /**
     * 终端地址
     */
    private final static String ENDPOINT = "https://oss-cn-beijing.aliyuncs.com";

    /**
     * 密钥ID
     */
    private final static String ACCESS_KEY_ID = System.getenv("OSS_ACCESS_KEY_ID");

    /**
     * 密钥
     */
    private final static String ACCESS_KEY_SECRET = System.getenv("OSS_ACCESS_KEY_SECRET");

    /**
     * 桶名
     */
    private final static String BUCKET_NAME = "black-chat";

    /**
     * 阿里云OSS SDK
     *
     * @param path 路径
     * @param file 文件对象
     * @return 访问地址
     */
    public String upload(String path, MultipartFile file, Long maxFileSize) {

        OSS client = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);

        try {
            // 判空
            if (file == null) {
                throw new IllegalArgumentException("至少上传一个文件");
            }
            // 判断文件大小
            if (file.getSize() > maxFileSize) {
                throw new IllegalArgumentException("文件大小不能超过" + maxFileSize);
            }

            // 上传文件到aliyunoss
            File fileObj = convert(file);

            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, path, fileObj);

            // 设置该属性可以返回response。如果不设置，则返回的response为空。
            putObjectRequest.setProcess("true");

            // 上传文件
            PutObjectResult result = client.putObject(putObjectRequest);

            if (result == null) {
                return "";
            }

            // 拼接图片地址
            String url = "https://" + BUCKET_NAME + "." + ENDPOINT.split("//")[1] + "/" + path;

            log.info("url: {}", url);

            return url;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, " + "but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + oe.getErrorMessage());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered " + "a serious internal problem while trying to communicate with OSS, " + "such as not being able to access the network.");
            System.out.println("Error Message: " + ce.getMessage());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            // 释放资源
            client.shutdown();
        }
        return "";
    }

    public static File convert(MultipartFile multipartFile) throws IOException {
        // 获取MultipartFile对象的字节数组
        byte[] bytes = multipartFile.getBytes();

        // 创建临时文件
        File tempFile = File.createTempFile("temp", null);

        // 将字节数组写入临时文件
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(bytes);
        }

        return tempFile;
    }
}
