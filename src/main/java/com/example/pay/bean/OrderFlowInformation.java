package com.example.pay.bean;

/*
 * OrderFlowInformation实体类
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
public class OrderFlowInformation {
    private String platFormStatus;//平台状态
    private String flag;//请求渠道标志
    private String message;//平台状态返回信息
    private String status_stream;//交易状态
    private String statusText_stream;//交易状态信息
    private String mctJnlNo;//商户流水号
    private String stt;//状态标志
    private String status;//状态代码
    private String statusText;//交易状态信息
    private String freezeNo;//冻结编号
    private String tranAmt;//交易金额
    private String cryCode;//货币代码
    private String payAccNo;//付款账号
    private String payAccNm;//付款账户名称
    private String recvAccNo;//收款账号
    private String recvAccNm;//收款账户名称

    public String getPlatFormStatus() {
        return platFormStatus;
    }

    public void setPlatFormStatus(String platFormStatus) {
        this.platFormStatus = platFormStatus;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
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

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getMctJnlNo() {
        return mctJnlNo;
    }

    public void setMctJnlNo(String mctJnlNo) {
        this.mctJnlNo = mctJnlNo;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }


    public String getFreezeNo() {
        return freezeNo;
    }

    public void setFreezeNo(String freezeNo) {
        this.freezeNo = freezeNo;
    }

    public String getTranAmt() {
        return tranAmt;
    }

    public void setTranAmt(String tranAmt) {
        this.tranAmt = tranAmt;
    }

    public String getCryCode() {
        return cryCode;
    }

    public void setCryCode(String cryCode) {
        this.cryCode = cryCode;
    }

    public String getPayAccNo() {
        return payAccNo;
    }

    public void setPayAccNo(String payAccNo) {
        this.payAccNo = payAccNo;
    }

    public String getPayAccNm() {
        return payAccNm;
    }

    public void setPayAccNm(String payAccNm) {
        this.payAccNm = payAccNm;
    }

    public String getRecvAccNo() {
        return recvAccNo;
    }

    public void setRecvAccNo(String recvAccNo) {
        this.recvAccNo = recvAccNo;
    }

    public String getRecvAccNm() {
        return recvAccNm;
    }

    public void setRecvAccNm(String recvAccNm) {
        this.recvAccNm = recvAccNm;
    }

    public String getStatus_stream() {
        return status_stream;
    }

    public void setStatus_stream(String status_stream) {
        this.status_stream = status_stream;
    }

    public String getStatusText_stream() {
        return statusText_stream;
    }

    public void setStatusText_stream(String statusText_stream) {
        this.statusText_stream = statusText_stream;
    }
}
