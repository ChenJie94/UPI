package com.example.pay.bean.ecitic;

public class JsonRow {

	
	 private String accountNo;//主体账号
	 private String contactName;//联系人姓名 varchar(122) 
	 private String contactPhone;//联系电话 varchar(20)
	 private String mailAddress;//邮件地址 varchar(152)
	 private String clientID;//客户流水号
	 public void setAccountNo(String accountNo) {
         this.accountNo = accountNo;
     	}
     public String getAccountNo() {
         return accountNo;
     }
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getMailAddress() {
		return mailAddress;
	}
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
     
		
}
