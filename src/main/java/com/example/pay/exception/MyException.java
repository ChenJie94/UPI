package com.example.pay.exception;

/*
 * @author: chenjie
 * @date: 2019/3/06 10:12
 * 统一支付平台接口 UPI
 *自定义错误类
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
/*@Configuration
@ConfigurationProperties(prefix = "exception", ignoreUnknownFields = false)
@PropertySource("classpath:config/myException.properties")*/
public class MyException extends RuntimeException  {

    private static final long serialVersionUID = 1L;

    private Integer code;//错误编号
    private String msg;//具体错误信息

    public MyException() {}

    public MyException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
