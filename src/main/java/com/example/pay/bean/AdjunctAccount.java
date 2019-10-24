package com.example.pay.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/*
 * AdjunctAccount实体类
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
public class AdjunctAccount {

    private String platFormStatus;//平台状态
    private String message;//平台状态信息
    private String flag;//请求渠道标志
    private String status;//交易状态
    private String statusText;//交易状态信息
    private String subAccNo;//附属账户
    @JSONField(name="SUBACCNM")
    private String SUBACCNM;//附属账户名称
    @JSONField(name="TZAMT")
    private BigDecimal TZAMT;//透支额度
    @JSONField(name="XSACVL")
    private BigDecimal XSACVL;//实体账户可用资金（主账户资金）
    @JSONField(name="KYAMT")
    private BigDecimal KYAMT;//可用余额
    @JSONField(name="SJAMT")
    private BigDecimal SJAMT;//实际金额
    @JSONField(name="DJAMT")
    private BigDecimal DJAMT;//冻结金额

    private String hostNo;//客户号

    private String mainAccNo;//主体账户



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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
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

    public String getSubAccNo() {
        return subAccNo;
    }

    public void setSubAccNo(String subAccNo) {
        this.subAccNo = subAccNo;
    }
    @JsonProperty("SUBACCNM")
    public String getSUBACCNM() {
        return SUBACCNM;
    }
    public void setSUBACCNM(String SUBACCNM) {
        this.SUBACCNM = SUBACCNM;
    }
    @JsonProperty("ZAMT")
    public BigDecimal getTZAMT() {
        return TZAMT;
    }

    public void setTZAMT(BigDecimal TZAMT) {
        this.TZAMT = TZAMT;
    }
    @JsonProperty("XSACVL")
    public BigDecimal getXSACVL() {
        return XSACVL;
    }

    public void setXSACVL(BigDecimal XSACVL) {
        this.XSACVL = XSACVL;
    }
    @JsonProperty("KYAMT")
    public BigDecimal getKYAMT() {
        return KYAMT;
    }

    public void setKYAMT(BigDecimal KYAMT) {
        this.KYAMT = KYAMT;
    }
    @JsonProperty("SJAMT")
    public BigDecimal getSJAMT() {
        return SJAMT;
    }

    public void setSJAMT(BigDecimal SJAMT) {
        this.SJAMT = SJAMT;
    }
    @JsonProperty("DJAMT")
    public BigDecimal getDJAMT() {
        return DJAMT;
    }

    public void setDJAMT(BigDecimal DJAMT) {
        this.DJAMT = DJAMT;
    }

    public String getHostNo() {
        return hostNo;
    }

    public void setHostNo(String hostNo) {
        this.hostNo = hostNo;
    }

    public String getMainAccNo() {
        return mainAccNo;
    }

    public void setMainAccNo(String mainAccNo) {
        this.mainAccNo = mainAccNo;
    }
}
