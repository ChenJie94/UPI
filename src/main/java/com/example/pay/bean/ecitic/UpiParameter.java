package com.example.pay.bean.ecitic;


public class UpiParameter {
	
	private String bankCode;//银行标志
	private String interfaceName;//接口名称
	private String param;//参数
	private String key;//私钥
	private String aesEncodeKey;//加密后的内容Base64编码
	private String sessionID;
	private String userName;//用户名
	private String payPwd;//密码
	private String flag;//平台标志
	
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getInterfaceName() {
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPayPwd() {
		return payPwd;
	}
	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getAesEncodeKey() {
		return aesEncodeKey;
	}
	public void setAesEncodeKey(String aesEncodeKey) {
		this.aesEncodeKey = aesEncodeKey;
	}
	
	

}
