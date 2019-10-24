package com.example.pay.bean;

import java.util.List;

/*
 * StatusInformation实体类
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
public class StatusInformation {

    private String platFormStatus;//平台状态
    private String message;//平台状态信息
    private String flag;//请求渠道标志
    private String status;//交易状态
    private String statusText;//交易状态信息
    private String clientID;//客户流水号
    private String freezeNo;//冻结编号
    private String verifyCode;//打印校验码 （3.42非登录打印明细查询使用）
    private String subAccNo;//付款账户号 （3.42非登录打印明细查询使用）
    private String accountNo;//收款账户号（3.42非登录打印明细查询使用）

    private List<Object> verifyCodeList;

    public String getPlatFormStatus() {
        return platFormStatus;
    }

    public void setPlatFormStatus(String platFormStatus) {
        this.platFormStatus = platFormStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getFreezeNo() {
        return freezeNo;
    }

    public void setFreezeNo(String freezeNo) {
        this.freezeNo = freezeNo;
    }

   /*    @Override
    public String toString() {
        return "StatusInformation{" +
                "platFormStatus='" + platFormStatus + '\'' +
                ", message='" + message + '\'' +
                ", flag='" + flag + '\'' +
                ", status='" + status + '\'' +
                ", statusText='" + statusText + '\'' +
                '}';
    }*/

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getSubAccNo() {
        return subAccNo;
    }

    public void setSubAccNo(String subAccNo) {
        this.subAccNo = subAccNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public List<Object> getVerifyCodeList() {
        return verifyCodeList;
    }

    public void setVerifyCodeList(List<Object> verifyCodeList) {
        this.verifyCodeList = verifyCodeList;
    }
}
