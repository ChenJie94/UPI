package com.example.pay.bean;

/**
 * ClassName: spdResult
 * Description:
 * date: 2019/8/8 17:42
 *
 * @author 陈杰
 * @version 1.0
 * @since JDK 1.8
 * .........┌─┐              ┌─┐
 * ...┌──┘  ┴───────┘  ┴──┐
 * ...│                                  │
 * ...│          ───                  │
 * ...│     ─┬┘       └┬─          │
 * ...│                                  │
 * ...│           ─┴─                 │
 * ...│                                  │
 * ...└───┐                  ┌───┘
 * ...........│                  │
 * ...........│                  │
 * ...........│                  │
 * ...........│                  └──────────────┐
 * ...........│                                                │
 * ...........│                                                ├─┐
 * ...........│                                                ┌─┘
 * ...........│                                                │
 * ...........└─┐    ┐    ┌───────┬──┐    ┌──┘
 * ...............│  ─┤  ─┤              │  ─┤  ─┤
 * ...............└──┴──┘              └──┴──┘
 * --------------------------------神兽保佑--------------------------------
 * --------------------------------代码无BUG!------------------------------
 */
public class spdResult {

    private String html;
    private String status;
    private String message;

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
