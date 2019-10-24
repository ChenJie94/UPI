package com.example.pay.bean.ecitic;

public class Stream {
	    private String action;//接口名称
	    private String userName;//登录名  默认为xinhuawang
	    private String subAccNo;//附属账号    ,也是会员注册接口的附属账号尾号字段
	    private String clientID;//客户流水号
	    private String payAccNo;//付款账号
	    private String tranAmt;//交易金额
	    private String memo;//备注信息
	    private String recvAccNo;//收款账号
	    private String recvAccNm;//收款公司名称
	    private String accountNo;//主体账户
	    private String tranType;//转账类型varchar(2) ，BF：转账；BG：解冻；BH：解冻支付；BR：冻结；BS：支付冻结
	    private String freezeNo;//冻结编号varchar(22)，转账类型为“解冻”或“解冻支付”时，必输
	    private String ofreezeamt;//原冻结金额decimal(15,2) ，可空，进行续冻结时必输
	    private String tranFlag;//转账时效标识char (1)，0：异步交易；1：同步交易
	    private String type;//原请求代码，可空
	    private String provinceName;//省名称 不要填“省”
	    private String cityName;//市名称 不要填“市”
	    private String bankName;//银银行名称
	    private String tgfi;//银行支付联行号
	    private String tgname;//开户行名称
	    private String sameBank;//银行标志,中信标识char(1) 0：本行 1： 他行
	    private String payType;//支付方式varchar(2)，2H：大额；2B：小额；2E：网银跨行支付，可空
	    private String recvBankNm;//收款账户开户行名
	    private String preFlg;//预约标志
	    private String preDate;//预约标志
	    private String preTime;//预约标志
	    private JsonList list;//内嵌JsonList
	    
	    //2019/06/26新增实体字段
	    private String mainAccNo;//会员注册时主体账户的对应KEY
	    private String appFlag;//应用系统char(1)， 2：B2B电子商务；3：投标保证金
	    private String accGenType;//附属账户生成方式char(1) ，0：自动输入 ；1：手动生成
	    private String subAccNm;//附属账户名称 varchar(122)，可空，appFlag为2时必输，appFlag为3时可空，若不为空则其值必须为客户名称
	    private String accType;//附属账户类型 char(2)，03：一般交易账号；04：保证金账号；11：招投标保证金
	    private String calInterestFlag;//是否计算利息标志 char(1)， 0：不计息；1：不分段计息；2：分段计息；当appFlag为3时，是否计算利息标志必须为0
	    private String interestRate;//默认计息利率 decimal(9.7)，calInterestFlag为 0时，可空；appFlag为3时，必须为0
	    private String overFlag;//是否允许透支char(1)，0：不允许；1：限额透支；2：全额透支 ；appFlag为3时，必须为0
	    private String overAmt;//透支额度decimal(15.2)，当overFlag为 0时，可空；appFlag为3时，必须为空
	    private String overRate;//透支利率decimal(9.7)，当overFlag为 0时，可空；appFlag为3时，必须为空
	    private String autoAssignInterestFlag;//自动分配利息标示char(1)，0：否；1：是；appFlag为3时，必须为0
	    private String autoAssignTranFeeFlag;//自动分配转账手续费标char(1)，0：否；1：是；appFlag为3时，必须为0
	    private String feeType;//手续费收取方式 char(1)，0：不收取；1：实时收取；2：月末累计；appFlag为3时，必须为0
	    private String realNameParm;//实名制更换char(1) ，0：账户名与账号全不换；1：账户名与账号全换；2：换账户名；3：换账号；appFlag为3时，必须为0
	    private String subAccPrintParm;//附属账户凭证打印更换 char(1)，0：全部显示；1：显示附属账户名和账号；2：显示实体账户名和账号；3：显示附属账户名和实体账号；4：显示实体账户名和附属账号；appFlag为3时，必须为0
	    private String mngNode;//会员确认中心char(6)
	    private String vtlCustNm;//虚拟客户名称 varchar(122)
	    private String legalPersonNm;//法人名称 varchar(122)
	    private String custCertType;//客户证件类型 varchar(1)
	    private String custCertNo;//客户证件号码 varchar(30)
	    private String branch;//所属机构 char(3)
	    private String commAddress;//通讯地址 varchar(152)



		private String mctNo;//商户编号varchar(8)
		private String mctJnlNo;//商户流水号varchar(20)

		private String recvTgfi;//收款账户银行联行号

		private String startDate;//起始日期
		private String endDate;//结算日期
		private Integer startRecord;//起始记录号
		private Integer pageNumber;//请求记录数

	    public void setAction(String action) {
	         this.action = action;
	     }
	     public String getAction() {
	         return action;
	     }

	    public void setUserName(String userName) {
	         this.userName = userName;
	     }
	     public String getUserName() {
	         return userName;
	     }

	    public void setList(JsonList list) {
	         this.list = list;
	     }
	     public JsonList getList() {
	         return list;
	     }
		public String getSubAccNo() {
			return subAccNo;
		}
		public void setSubAccNo(String subAccNo) {
			this.subAccNo = subAccNo;
		}
		public String getClientID() {
			return clientID;
		}
		public void setClientID(String clientID) {
			this.clientID = clientID;
		}
		public String getPayAccNo() {
			return payAccNo;
		}
		public void setPayAccNo(String payAccNo) {
			this.payAccNo = payAccNo;
		}
		public String getTranAmt() {
			return tranAmt;
		}
		public void setTranAmt(String tranAmt) {
			this.tranAmt = tranAmt;
		}
		public String getMemo() {
			return memo;
		}
		public void setMemo(String memo) {
			this.memo = memo;
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
		public String getAccountNo() {
			return accountNo;
		}
		public void setAccountNo(String accountNo) {
			this.accountNo = accountNo;
		}
		public String getTranType() {
			return tranType;
		}
		public void setTranType(String tranType) {
			this.tranType = tranType;
		}
		public String getFreezeNo() {
			return freezeNo;
		}
		public void setFreezeNo(String freezeNo) {
			this.freezeNo = freezeNo;
		}
		public String getOfreezeamt() {
			return ofreezeamt;
		}
		public void setOfreezeamt(String ofreezeamt) {
			this.ofreezeamt = ofreezeamt;
		}
		public String getTranFlag() {
			return tranFlag;
		}
		public void setTranFlag(String tranFlag) {
			this.tranFlag = tranFlag;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getProvinceName() {
			return provinceName;
		}
		public void setProvinceName(String provinceName) {
			this.provinceName = provinceName;
		}
		public String getCityName() {
			return cityName;
		}
		public void setCityName(String cityName) {
			this.cityName = cityName;
		}
		public String getBankName() {
			return bankName;
		}
		public void setBankName(String bankName) {
			this.bankName = bankName;
		}
		public String getTgfi() {
			return tgfi;
		}
		public void setTgfi(String tgfi) {
			this.tgfi = tgfi;
		}
		public String getTgname() {
			return tgname;
		}
		public void setTgname(String tgname) {
			this.tgname = tgname;
		}
		public String getSameBank() {
			return sameBank;
		}
		public void setSameBank(String sameBank) {
			this.sameBank = sameBank;
		}
		public String getPayType() {
			return payType;
		}
		public void setPayType(String payType) {
			this.payType = payType;
		}
		public String getRecvBankNm() {
			return recvBankNm;
		}
		public void setRecvBankNm(String recvBankNm) {
			this.recvBankNm = recvBankNm;
		}
		public String getPreFlg() {
			return preFlg;
		}
		public void setPreFlg(String preFlg) {
			this.preFlg = preFlg;
		}
		public String getPreDate() {
			return preDate;
		}
		public void setPreDate(String preDate) {
			this.preDate = preDate;
		}
		public String getPreTime() {
			return preTime;
		}
		public void setPreTime(String preTime) {
			this.preTime = preTime;
		}
		public String getMainAccNo() {
			return mainAccNo;
		}
		public void setMainAccNo(String mainAccNo) {
			this.mainAccNo = mainAccNo;
		}
		public String getAppFlag() {
			return appFlag;
		}
		public void setAppFlag(String appFlag) {
			this.appFlag = appFlag;
		}
		public String getAccGenType() {
			return accGenType;
		}
		public void setAccGenType(String accGenType) {
			this.accGenType = accGenType;
		}
		public String getSubAccNm() {
			return subAccNm;
		}
		public void setSubAccNm(String subAccNm) {
			this.subAccNm = subAccNm;
		}
		public String getAccType() {
			return accType;
		}
		public void setAccType(String accType) {
			this.accType = accType;
		}
		public String getCalInterestFlag() {
			return calInterestFlag;
		}
		public void setCalInterestFlag(String calInterestFlag) {
			this.calInterestFlag = calInterestFlag;
		}
		public String getInterestRate() {
			return interestRate;
		}
		public void setInterestRate(String interestRate) {
			this.interestRate = interestRate;
		}
		public String getOverFlag() {
			return overFlag;
		}
		public void setOverFlag(String overFlag) {
			this.overFlag = overFlag;
		}
		public String getOverAmt() {
			return overAmt;
		}
		public void setOverAmt(String overAmt) {
			this.overAmt = overAmt;
		}
		public String getOverRate() {
			return overRate;
		}
		public void setOverRate(String overRate) {
			this.overRate = overRate;
		}
		public String getAutoAssignInterestFlag() {
			return autoAssignInterestFlag;
		}
		public void setAutoAssignInterestFlag(String autoAssignInterestFlag) {
			this.autoAssignInterestFlag = autoAssignInterestFlag;
		}
		public String getAutoAssignTranFeeFlag() {
			return autoAssignTranFeeFlag;
		}
		public void setAutoAssignTranFeeFlag(String autoAssignTranFeeFlag) {
			this.autoAssignTranFeeFlag = autoAssignTranFeeFlag;
		}
		public String getFeeType() {
			return feeType;
		}
		public void setFeeType(String feeType) {
			this.feeType = feeType;
		}
		public String getRealNameParm() {
			return realNameParm;
		}
		public void setRealNameParm(String realNameParm) {
			this.realNameParm = realNameParm;
		}
		public String getSubAccPrintParm() {
			return subAccPrintParm;
		}
		public void setSubAccPrintParm(String subAccPrintParm) {
			this.subAccPrintParm = subAccPrintParm;
		}
		public String getMngNode() {
			return mngNode;
		}
		public void setMngNode(String mngNode) {
			this.mngNode = mngNode;
		}
		public String getVtlCustNm() {
			return vtlCustNm;
		}
		public void setVtlCustNm(String vtlCustNm) {
			this.vtlCustNm = vtlCustNm;
		}
		public String getLegalPersonNm() {
			return legalPersonNm;
		}
		public void setLegalPersonNm(String legalPersonNm) {
			this.legalPersonNm = legalPersonNm;
		}
		public String getCustCertType() {
			return custCertType;
		}
		public void setCustCertType(String custCertType) {
			this.custCertType = custCertType;
		}
		public String getCustCertNo() {
			return custCertNo;
		}
		public void setCustCertNo(String custCertNo) {
			this.custCertNo = custCertNo;
		}
		public String getBranch() {
			return branch;
		}
		public void setBranch(String branch) {
			this.branch = branch;
		}
		public String getCommAddress() {
			return commAddress;
		}
		public void setCommAddress(String commAddress) {
			this.commAddress = commAddress;
		}

		public String getMctNo() {
			return mctNo;
		}

		public void setMctNo(String mctNo) {
			this.mctNo = mctNo;
		}

		public String getMctJnlNo() {
			return mctJnlNo;
		}

		public void setMctJnlNo(String mctJnlNo) {
			this.mctJnlNo = mctJnlNo;
		}

		public String getRecvTgfi() {
		return recvTgfi;
	}

		public void setRecvTgfi(String recvTgfi) {
		this.recvTgfi = recvTgfi;
	}

		public String getStartDate() {
			return startDate;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		public String getEndDate() {
			return endDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		public Integer getStartRecord() {
			return startRecord;
		}

		public void setStartRecord(Integer startRecord) {
			this.startRecord = startRecord;
		}

		public Integer getPageNumber() {
			return pageNumber;
		}

		public void setPageNumber(Integer pageNumber) {
			this.pageNumber = pageNumber;
		}
}
