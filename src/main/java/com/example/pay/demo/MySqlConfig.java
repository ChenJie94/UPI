package com.example.pay.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/*
 * @author: chenjie
 * @date: 2019/3/06 10:12
 * 统一支付平台接口 UPI
 *sql配置
 *
 *         ┌─┐              ┌─┐
 *   ┌──┘  ┴───────┘  ┴──┐
 *   │                                  │
 *   │          ───                  │
 *   │     ─┬┘       └┬─          │
 *   │                                  │
 *   │           ─┴─                 │
 *   │                                  │
 *   └───┐                  ┌───┘
 *           │                  │
 *           │                  │
 *           │                  │
 *           │                  └──────────────┐
 *           │                                                │
 *           │                                                ├─┐
 *           │                                                ┌─┘
 *           │                                                │
 *           └─┐    ┐    ┌───────┬──┐    ┌──┘
 *               │  ─┤  ─┤              │  ─┤  ─┤
 *               └──┴──┘              └──┴──┘
 *                神兽保佑
 *               代码无BUG!
 */
@Component
//类名为mysql 实际操作的是SQLServer
public class MySqlConfig {

    // 驱动类
    public static String driver;
    @Value("${spring.datasource.driver-class-name}")
    public void setDriver(String $driver) {
        this.driver = $driver;
    }

    // url地址
    public static String url;
    @Value("${spring.datasource.url}")
    public void setUrl(String $url) {
        this.url = $url;
    }

    // 用户名
    public static String username;
    @Value("${spring.datasource.username}")
    public void setUsername(String $username) {
        this.username = $username;
    }

    // 密码
    public static String password;
    @Value("${spring.datasource.password}")
    public void setPassword(String $password) {
        this.password = $password;
    }


    public static String ecitic_url;
    @Value("${ecitic.request.url}")
    public void setEcitic_url(String $ecitic_url) {
        this.ecitic_url = $ecitic_url;
    }

    public static String spd_url;
    @Value("${spd.request.url}")
    public  void setSpd_url(String $spd_url) {
        this.spd_url = $spd_url;
    }
}
