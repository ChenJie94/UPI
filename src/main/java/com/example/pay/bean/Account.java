package com.example.pay.bean;

    /*
    * Account实体类
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
public class Account {
    private String platFormStatus;//平台状态
    private String message;//平台状态消息
    private String flag;//请求渠道标志
    private String balance;//余额
    private String usableBalance;//可用余额
    private String accountName;//账户名称
    private String accountNo;//账户账号
    private String openBankName;//开户行名称
    private String statusText;//银行返回状态信息
    private String status;//银行返回状态

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getOpenBankName() {
        return openBankName;
    }

    public void setOpenBankName(String openBankName) {
        this.openBankName = openBankName;
    }

    public String getUsableBalance() {
        return usableBalance;
    }

    public void setUsableBalance(String usableBalance) {
        this.usableBalance = usableBalance;
    }


    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

}
