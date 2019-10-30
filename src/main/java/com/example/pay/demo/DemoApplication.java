package com.example.pay.demo;

import com.example.pay.acp.sdk.SDKConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        SDKConfig.getConfig().loadPropertiesFromSrc();// 从classpath加载acp_sdk.properties文件
    }
}