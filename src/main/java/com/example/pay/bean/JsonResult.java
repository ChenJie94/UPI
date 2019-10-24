package com.example.pay.bean;

import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Map;


/*
 * JsonResult实体类
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
public class JsonResult {

    private int code;   //返回码 非0即失败
    private String msg; //消息提示
    private Map<String, Object> data; //返回的数据

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }


    public JsonResult(){};

    public JsonResult(int code, String msg, Map<String, Object> data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static String success() {
        return success(new HashMap<>(0));
    }
    public static String success(Map<String, Object> data) {
        return JSON.toJSONString(new JsonResult(0, "解析成功", data));
    }

    public static String failed() {
        return failed("解析失败");
    }
    public static String failed(String msg) {
        return failed(-1, msg);
    }
    public static String failed(int code, String msg) {
        return JSON.toJSONString(new JsonResult(code, msg, new HashMap<>(0)));
    }

}
