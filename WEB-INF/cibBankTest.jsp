<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- <%@ page import="cn.com.aperfect.scm.controller.*"%> --%>
<%-- <%@ page import="com.cib.epay.sdk.*"%>
<%@ page import="com.cib.epay.sdk.util.*"%> --%>
<%@ page import="com.example.pay.cib.*"%>

<!DOCTYPE html>
<html>
  <head>
    <title>收付直通车示例页面</title>
    <style type="text/css">
    body{font-family:Microsoft YaHei,Tahoma,Helvetica,Arial,sans-serif;}
    .header{border-bottom:3px solid #28529A;}
    .fix{margin:0 auto;width:790px;}
    .header .inner{color:#004392;font-size:18px;height:50px;line-height:40px;position:relative;}
    .fixtop{padding:15px 0;}
    input[type=submit]{border:2px solid #28529A;background-color:white;margin:10px 0;font-size:14px;padding:5px;}
    input[type=text]{border:1px solid grey;font-size:16px;}
    </style>
  </head>
  
  <body>
   	<h1>这是兴业银行支付测试页面</h1>
   	<div class="header"><h2 class="inner fix">兴业银行 收付直通车 跳转接口示例</h2></div>
  	<div class="fixtop fix">
  	<h4>！！ 请务必先参看com.cib.epay.sdk.Example类和Readme.txt中的说明。</h4>
  	<hr />
  	
     <p><b>Ex.1-1</b> 快捷支付认证：</p>
    <form name="epauth" action="epay_redirect.jsp" method="get">
    <input type="text" name="trac_no" value="<%="SDK"+1234567890%>" width="32" maxlength="32" placeholder="商户系统跟踪号" />
    <input type="text" name="acct_type" value="0" style="width:100px" maxlength="32" placeholder="账号类型" />
    <input type="text" name="bank_no" value="105100000017" width="32" maxlength="12" placeholder="人行联网行号" />
    <input type="text" name="card_no" value="6222801234567888953" width="32" maxlength="20" placeholder="账号" />
    <input type="hidden" name="redirect_type" value="ep_auth" /><br />
    <input type="submit" value="点此跳转至快捷认证" />
    </form>
    <hr />
    
    <p><b>Ex.1-9</b> 无绑定账户的快捷支付接口：</p>
    <form name="epauthpay" action="epay_redirect.jsp" method="get">
    <input type="text" name="order_no" value="<%="SDK"+1234567890%>" width="32" maxlength="32" placeholder="商户订单号" />
    <input type="text" name="order_amount" value="1.00" style="width:100px" maxlength="32" placeholder="订单金额" />
    <input type="text" name="order_title" value="SDK测试订单" width="32" maxlength="12" placeholder="订单标题" />
    <input type="text" name="order_desc" value="欢迎使用收付直通车" width="32" maxlength="32" placeholder="订单描述" />
    <input type="hidden" name="redirect_type" value="ep_authpay" /><br />
    <input type="submit" value="点此跳转至无签约快捷支付" />
    </form>
    <hr />
    
    <p><b>Ex.2-1</b> 网关支付：</p>
    <form name="gppay" action="/cibBankPayment" method="post">
    <input type="text" name="order_no" value="<%="SDK"+1234567890%>" width="32" maxlength="32" placeholder="商户订单号" />
    <input type="text" name="order_amount" value="1.00" style="width:100px" maxlength="32" placeholder="订单金额" />
    <input type="text" name="order_title" value="SDK测试订单" width="32" maxlength="12" placeholder="订单标题" />
    <input type="text" name="order_desc" value="欢迎使用收付直通车" width="32" maxlength="32" placeholder="订单描述" />
    <input type="hidden" name="redirect_type" value="gp_pay1" /><br />
    <input type="submit" value="点此跳转至网关支付" />
    </form>
    <hr />
    
     <p><b>Ex.2-1</b> 对账文件下载接口（不生成文件）：</p>
    <form name="check" action="/cibBankAccountChecking" method="post">
    <input type="text" name="order_type" value="3" style="width:100px" maxlength="32" placeholder="订单类型" />
    <input type="text" name="order_date" value="20200108" width="32" maxlength="12" placeholder="订单日期" />
    <input type="hidden" name="redirect_type" value="check" /><br />
    <input type="submit" value="点此跳转至对账文件下载接口" />
    </form>
    <hr />
    
       <p><b>Ex.2-1</b> 测试支付完成通知回调：</p>
    <form name="return" action="http://183.251.50.201:8090/cibBankReturnInfo" method="post">
    <input type="text" name="order_type" value="3" style="width:100px" maxlength="32" placeholder="订单类型" />
    <input type="text" name="order_date" value="20200108" width="32" maxlength="12" placeholder="订单日期" />
    <input type="text" name="order_mac" value="YfFK/hfqFryTliHFqxSdMaK7t2CtMWyRJ8ZgPBDOTlGCnhAz4/I5GyJbPFGtfhOFzx3Lyxy4U0ZA+vbSPE7W5t5JX+o6bbTwk8kBn7TBO7EsCdoydpkr8Y8JPKUY9gm820zfhPaCIl7EI2p/px3czfz+fdjfNACpNnkAJD8EfHCSYZpR1mocoLtISQNDnnNs7L/+4i2NTHynym1YFFlhmtrddovMy37SgZEKKgaCHpJ9WJ5DxQ0cqUps5Un6RDH7bkuWZXTutpqFreNnPs9sj7j0E3JUa5d+3tZhCtMqwuZCBVrpsU0EwTDnnMIPYyeX72xXzBudXXJIVbr8xiIL6Q==" width="32" maxlength="12" placeholder="mac校验数据" />
    <input type="hidden" name="redirect_type" value="return" /><br />
    <input type="submit" value="点此跳转至通知回调接口" />
    </form>
    <hr />
    
	<p><b>Ex.1-18</b> 托收认证：</p>
    <form name="epauth" action="epay_redirect.jsp" method="get">
    <input type="text" name="trac_no" value="<%="TN"+1234567890%>" width="32" maxlength="32" placeholder="商户系统跟踪号" />
    <input type="text" name="acct_type" value="0" style="width:100px" maxlength="32" placeholder="账号类型" />
    <input type="text" name="bank_no" value="309391000011" width="32" maxlength="12" placeholder="人行联网行号" />
    <input type="text" name="card_no" value="622908121000127512" width="32" maxlength="20" placeholder="账号" />
    <input type="hidden" name="redirect_type" value="entrust_auth" /><br />
    <input type="submit" value="点此跳转至快捷认证" />
    </form> 
    </div>
   	
  </body>
</html>
