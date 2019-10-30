package com.example.pay.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.pay.acp.demo.DemoBase;
import com.example.pay.acp.sdk.AcpService;
import com.example.pay.acp.sdk.LogUtil;
import com.example.pay.acp.sdk.SDKConfig;
import com.example.pay.assembly.*;
import com.example.pay.bean.AdjunctAccount;
import com.example.pay.bean.StatusInformation;
import com.example.pay.bean.spdResult;
import com.example.pay.exception.MyException;
import com.example.pay.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @author: chenjie
 * @date: 2019/3/06 10:12
 * 统一支付平台接口 UPI
 *
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
 *                                神兽常保佑
 *                                代码无BUG!
 */
@RestController
//@EnableConfigurationProperties(MyException.class)
public class UnifyPayController {
    JSONObject json = new JSONObject();
    protected static Logger logger = LoggerFactory.getLogger(UnifyPayController.class);
    private static final String logExceptionFormat = "Capture Exception By GlobalExceptionHandler: Code: %s Detail: %s";
    /* @Autowired
    MyException myException;*/
    @RequestMapping("/hello")
    public String hello() throws Exception {
        logger.info("访问了helloController");
        int i = 0;
        //Exception异常会自动拦截，这里只是做个测试自定义异常
        logger.info("i=" + i + "--" + "自定义异常");
        throw new MyException(101, "Sam 错误");

    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void testException(@RequestParam(value = "role") Integer role) throws Exception {
        int i = role;
        if (i < 1) {
            throw new MyException();
        } else {
            throw new Exception();
        }
    }

    //生成8位随机数
    private static String getOrderId(){
        String orderId ;
        java.util.Random r=new java.util.Random();
        while(true){
            int i=r.nextInt(99999999);
            if(i<0)i=-i;
            orderId = String.valueOf(i);
            System.out.println("---生成随机数---"+orderId);
            if(orderId.length()<8){
                System.out.println("---位数不够8位---"+orderId);
                continue;
            }
            if(orderId.length()>=8){
                orderId = orderId.substring(0,8);
                System.out.println("---生成8位流水---"+orderId);
                break;
            }
        }
        return orderId;
    }

/*    @Resource
    protected HttpServletRequest request;
    @Resource
    protected HttpServletResponse resp;
    public void Form02_6_2_FrontConsume(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        //前台页面传过来的
        String merId = req.getParameter("merId");
        String txnAmt = req.getParameter("txnAmt");
        txnAmt=txnAmt.replace(".", "");
        System.out.println(txnAmt);
        Map<String, String> requestData = new HashMap<String, String>();

        *//***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***//*
        requestData.put("version", DemoBase.version);   			      //版本号，全渠道默认值
        requestData.put("encoding", DemoBase.encoding); 	      //字符集编码，可以使用UTF-8,GBK两种方式
        requestData.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
        requestData.put("txnType", "01");               			  //交易类型 ，01：消费
        requestData.put("txnSubType", "01");            			  //交易子类型， 01：自助消费
        requestData.put("bizType", "000202");           			  //业务类型 000202: B2B
        requestData.put("channelType", "07");           			  //渠道类型 固定07

        *//***商户接入参数***//*
        requestData.put("merId", merId);    	          			  //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
        requestData.put("accessType", "0");             			  //接入类型，0：直连商户
        requestData.put("orderId",DemoBase.getOrderId());             //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
        requestData.put("txnTime", DemoBase.getCurrentTime());        //订单发送时间，取系统时间，格式为yyyyMMddHHmmss，必须取当前时间，否则会报txnTime无效
        requestData.put("currencyCode", "156");         			  //交易币种（境内商户一般是156 人民币）
        requestData.put("txnAmt", txnAmt);             			      //交易金额，单位分，不要带小数点

        //前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
        //如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
        //异步通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
        requestData.put("frontUrl", DemoBase.frontUrl);

        //后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
        //后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
        //注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码
        //    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
        //    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
        requestData.put("backUrl", DemoBase.backUrl);

        //实现网银前置的方法：
        //上送issInsCode字段，该字段的值参考《平台接入接口规范-第5部分-附录》（全渠道平台银行名称-简码对照表）2）联系银联业务运营部门开通商户号的网银前置权限
        //requestData.put("issInsCode", "ABC");                 //发卡机构代码

        // 订单超时时间。
        // 超过此时间后，除网银交易外，其他交易银联系统会拒绝受理，提示超时。 跳转银行网银交易如果超时后交易成功，会自动退款，大约5个工作日金额返还到持卡人账户。
        // 此时间建议取支付时的北京时间加15分钟。
        // 超过超时时间调查询接口应答origRespCode不是A6或者00的就可以判断为失败。
        requestData.put("payTimeout", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date().getTime() + 15 * 60 * 1000));

        // 请求方保留域，
        // 透传字段，查询、通知、对账文件中均会原样出现，如有需要请启用并修改自己希望透传的数据。
        // 出现部分特殊字符时可能影响解析，请按下面建议的方式填写：
        // 1. 如果能确定内容不会出现&={}[]"'等符号时，可以直接填写数据，建议的方法如下。
//		requestData.put("reqReserved", "透传信息1|透传信息2|透传信息3");
        // 2. 内容可能出现&={}[]"'符号时：
        // 1) 如果需要对账文件里能显示，可将字符替换成全角＆＝｛｝【】“‘字符（自己写代码，此处不演示）；
        // 2) 如果对账文件没有显示要求，可做一下base64（如下）。
        //    注意控制数据长度，实际传输的数据长度不能超过1024位。
        //    查询、通知等接口解析时使用new String(Base64.decodeBase64(reqReserved), DemoBase.encoding);解base64后再对数据做后续解析。
//		requestData.put("reqReserved", Base64.encodeBase64String("任意格式的信息都可以".toString().getBytes(DemoBase.encoding)));

        *//**请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面**//*
        Map<String, String> reqData = AcpService.sign(requestData,DemoBase.encoding);  //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();  //获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
        String html = AcpService.createAutoFormHtml(requestFrontUrl,reqData,DemoBase.encoding);   //生成自动跳转的Html表单

        LogUtil.writeLog("打印请求HTML，此为请求报文，为联调排查问题的依据："+html);
        //将生成的html写到浏览器中完成自动跳转打开银联支付页面；这里调用signData之后，将html写到浏览器跳转到银联页面之前均不能对html中的表单项的名称和值进行修改，如果修改会导致验签不通过
        resp.getWriter().write(html);
    }


    @RequestMapping(value = "/frontRcvResponse", method = { RequestMethod.POST ,RequestMethod.GET })
    @ResponseBody
    protected void frontRcvResponse(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        LogUtil.writeLog("FrontRcvResponse前台接收报文返回开始");

        String encoding = req.getParameter(SDKConstants.param_encoding);
        LogUtil.writeLog("返回报文中encoding=[" + encoding + "]");
        String pageResult = "";
        if (DemoBase.encoding.equalsIgnoreCase(encoding)) {
            pageResult = "/utf8_result.jsp";
        } else {
            pageResult = "/gbk_result.jsp";
        }
        Map<String, String> respParam = getAllRequestParam(req);

        // 打印请求报文
        LogUtil.printRequestLog(respParam);

        Map<String, String> valideData = null;
        StringBuffer page = new StringBuffer();
        if (null != respParam && !respParam.isEmpty()) {
            Iterator<Map.Entry<String, String>> it = respParam.entrySet()
                    .iterator();
            valideData = new HashMap<String, String>(respParam.size());
            while (it.hasNext()) {
                Map.Entry<String, String> e = it.next();
                String key = (String) e.getKey();
                String value = (String) e.getValue();

                page.append("<tr><td width=\"30%\" align=\"right\">" + key
                        + "(" + key + ")</td><td>" + value + "</td></tr>");
                valideData.put(key, value);
            }
        }
        if (!AcpService.validate(valideData, encoding)) {
            page.append("<tr><td width=\"30%\" align=\"right\">验证签名结果</td><td>失败</td></tr>");
            LogUtil.writeLog("验证签名结果[失败].");
        } else {
            page.append("<tr><td width=\"30%\" align=\"right\">验证签名结果</td><td>成功</td></tr>");
            LogUtil.writeLog("验证签名结果[成功].");
            System.out.println(valideData.get("orderId")); //其他字段也可用类似方式获取
            String respCode = valideData.get("respCode");
            //判断respCode=00、A6后，对涉及资金类的交易，请再发起查询接口查询，确定交易成功后更新数据库。
        }
        req.setAttribute("result", page.toString());
        req.getRequestDispatcher(pageResult).forward(req, resp);

        LogUtil.writeLog("FrontRcvResponse前台接收报文返回结束");
    }

    *//**
     * 获取请求参数中所有的信息
     * 当商户上送frontUrl或backUrl地址中带有参数信息的时候，
     * 这种方式会将url地址中的参数读到map中，会导多出来这些信息从而致验签失败，这个时候可以自行修改过滤掉url中的参数或者使用getAllRequestParamStream方法。
     * @param request
     * @return
     *//*
    public static Map<String, String> getAllRequestParam(
            final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
                // 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
                if (res.get(en) == null || "".equals(res.get(en))) {
                    // System.out.println("======为空的字段名===="+en);
                    res.remove(en);
                }
            }
        }
        return res;
    }*/


    String errorInfo = "";
    @RequestMapping(value = "/UPI", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String unifyPay(HttpServletRequest request) throws Exception {
        String str = "";
        String upiParameterJson = "";
        StatusInformation information = new StatusInformation();
        //读取请求upi接口时传过来的参数
        InputStreamReader reader = new InputStreamReader(request.getInputStream(),"UTF-8");
        //设置数组的存储长度
        char[] buff = new char[204800];
        int length = 0;
        while ((length = reader.read(buff)) != -1) {
            upiParameterJson = new String(buff, 0, length);
            upiParameterJson = URLDecoder.decode(upiParameterJson, "UTF-8");
        }


        //将json字符串参数 转成 json对象
        JSONObject json = JSON.parseObject(upiParameterJson);
        //取json对象里面的具体参数值
        if (json == null || upiParameterJson.equals("")) {
            //自定义错误 "传入参数为空！"
            throw new MyException(20, "传入参数为空！");


        } else if (json.getString("bankCode") == null || json.getString("bankCode").equals("")) {
            //自定义错误 "未传入选择操作的银行！"
            throw new MyException(21, "未传入选择操作的银行！");

        }
        String Bankcode = json.getString("bankCode").toString();
        String Interfacename="";
        if (json.containsKey("interfaceName")||json.containsKey("interfaceName")) {
            if (json.getString("interfaceName") != null || !json.getString("interfaceName").equals("")) {
                Interfacename = json.getString("interfaceName").toString();
            }
        }
      //  String Key = json.getString("aesEncodeKey").toString();
        String SessionID = json.getString("sessionID").toString();
        if (json.containsKey("userName")||json.containsKey("payPwd")){
            //自定义错误 "没有传入用户名和密码！" (暂时不开放)
         //   throw new MyException(22, "没有传入用户名和密码！");
       /*     if(json.getString("userName") != null || !json.getString("userName").equals("")){
                String UsernName = json.getString("userName").toString();
                String PayPwd = json.getString("payPwd").toString();
            }else{
                //自定义错误 "传入用户名和密码为空值" (暂时不开放)
                //    throw new MyException(22, "传入用户名和密码为空值");
            }*/
        }

        String Flag = json.getString("flag").toString();
    //    String privateKeyStr = json.getString("key").toString();
        String paramStr = json.getString("param").toString();
        Map<String, File> fileMap = new HashMap<>();
        if (json.containsKey("fileMap")) {
            JSONObject jsonFile = (JSONObject) json.get("fileMap");
            if (jsonFile != null) {
                String textFile = jsonFile.getString("files");
                fileMap.put("textFile", new File(textFile));
            }
        }
        //生成秘钥对
    //    KeyPair keyPair = RSAUtil.getKeyPair();
        //  String privateKeyStr = RSAUtil.getPrivateKey(keyPair);//私钥
        //##############网络上传输的内容有Base64编码后的公钥 和 Base64编码后的公钥加密的内容     #################
        //===================服务端================
        //将Base64编码后的私钥转换成PrivateKey对象
    //    PrivateKey privateKey = RSAUtil.string2PrivateKey(privateKeyStr);
        //加密后的内容Base64解码
     //   byte[] base642Byte = RSAUtil.base642Byte(Key);
        //用私钥解密
     //   byte[] privateDecrypt = RSAUtil.privateDecrypt(base642Byte, privateKey);
        //解密后的明文
     //   String decryptStr = decrypt(paramStr, new String(privateDecrypt));
        JSONObject jsonobject = json.parseObject(paramStr);
        if ("ECITIC".equals(Bankcode) || Bankcode.contains("ECITIC")) {
            //将json格式数据转成xml格式
            logger.info("调用中信银行接口");
            String url = MySqlConfig.ecitic_url;
            Object ob = eciticPay(url, Interfacename, Flag,jsonobject);
            str = jsonobject.toJSONString(ob).toString();
        }else if("PAB".equals(Bankcode) || Bankcode.contains("PAB")){
           /* logger.info("调用平安银行接口");
            Object ob = pabPay(Interfacename, jsonobject, Flag);
            str = jsonobject.toJSONString(ob).toString();*/
        }else if("SPD".equals(Bankcode)|| Bankcode.contains("SPD")){
            logger.info("调用浦发银行接口");
            String url = MySqlConfig.spd_url;
            if (fileMap.size()>0) {
                Object ob = spdPay(url, Interfacename, jsonobject, Flag, fileMap);
                str = ob.toString();
            }else{
                Object ob = spdPay(url, Interfacename, jsonobject, Flag);
                str = new String(ob.toString().getBytes("GBK"),"GBK");
                System.out.println(str);
            }
        }else if("UNIONPAY".equals(Bankcode)|| Bankcode.contains("UNIONPAY")){
            logger.info("调用银联接口");
            Object ob = unionPay(Interfacename, jsonobject, Flag);
            str = ob.toString();
        }

        return str;
    }

    public Object unionPay(String interfacename, JSONObject jsonobject, String flag) throws  Exception{
        String html="";
        spdResult spdResult=new spdResult();
        if ("Form02_6_2_FrontConsume".equals(interfacename)||interfacename.contains("Form02_6_2_FrontConsume")) {

                //前台页面传过来的
                String merId = jsonobject.getString("merId");
                String txnAmt = jsonobject.getString("txnAmt");
                txnAmt = txnAmt.replace(".", "");
                System.out.println(txnAmt);
                Map<String, String> requestData = new HashMap<String, String>();

                /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
                requestData.put("version", DemoBase.version);                  //版本号，全渠道默认值
                requestData.put("encoding", DemoBase.encoding);          //字符集编码，可以使用UTF-8,GBK两种方式
                requestData.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
                requestData.put("txnType", "01");                          //交易类型 ，01：消费
                requestData.put("txnSubType", "01");                          //交易子类型， 01：自助消费
                requestData.put("bizType", "000202");                      //业务类型 000202: B2B
                requestData.put("channelType", "07");                      //渠道类型 固定07

                /***商户接入参数***/
                requestData.put("merId", merId);                              //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
                requestData.put("accessType", "0");                          //接入类型，0：直连商户
                requestData.put("orderId", DemoBase.getOrderId());             //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
                requestData.put("txnTime", DemoBase.getCurrentTime());        //订单发送时间，取系统时间，格式为yyyyMMddHHmmss，必须取当前时间，否则会报txnTime无效
                requestData.put("currencyCode", "156");                      //交易币种（境内商户一般是156 人民币）
                requestData.put("txnAmt", txnAmt);                              //交易金额，单位分，不要带小数点

                //前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
                //如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
                //异步通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
                requestData.put("frontUrl", DemoBase.frontUrl);

                //后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
                //后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
                //注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码
                //    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
                //    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
                requestData.put("backUrl", DemoBase.backUrl);

                //实现网银前置的方法：
                //上送issInsCode字段，该字段的值参考《平台接入接口规范-第5部分-附录》（全渠道平台银行名称-简码对照表）2）联系银联业务运营部门开通商户号的网银前置权限
                //requestData.put("issInsCode", "ABC");                 //发卡机构代码

                // 订单超时时间。
                // 超过此时间后，除网银交易外，其他交易银联系统会拒绝受理，提示超时。 跳转银行网银交易如果超时后交易成功，会自动退款，大约5个工作日金额返还到持卡人账户。
                // 此时间建议取支付时的北京时间加15分钟。
                // 超过超时时间调查询接口应答origRespCode不是A6或者00的就可以判断为失败。
                requestData.put("payTimeout", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date().getTime() + 15 * 60 * 1000));

                // 请求方保留域，
                // 透传字段，查询、通知、对账文件中均会原样出现，如有需要请启用并修改自己希望透传的数据。
                // 出现部分特殊字符时可能影响解析，请按下面建议的方式填写：
                // 1. 如果能确定内容不会出现&={}[]"'等符号时，可以直接填写数据，建议的方法如下。
//		requestData.put("reqReserved", "透传信息1|透传信息2|透传信息3");
                // 2. 内容可能出现&={}[]"'符号时：
                // 1) 如果需要对账文件里能显示，可将字符替换成全角＆＝｛｝【】“‘字符（自己写代码，此处不演示）；
                // 2) 如果对账文件没有显示要求，可做一下base64（如下）。
                //    注意控制数据长度，实际传输的数据长度不能超过1024位。
                //    查询、通知等接口解析时使用new String(Base64.decodeBase64(reqReserved), DemoBase.encoding);解base64后再对数据做后续解析。
//		requestData.put("reqReserved", Base64.encodeBase64String("任意格式的信息都可以".toString().getBytes(DemoBase.encoding)));

                /**请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面**/
                Map<String, String> reqData = AcpService.sign(requestData, DemoBase.encoding);  //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
                String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();  //获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
                html = AcpService.createAutoFormHtml(requestFrontUrl, reqData, DemoBase.encoding);   //生成自动跳转的Html表单
                spdResult.setHtml(html);
                LogUtil.writeLog("打印请求HTML，此为请求报文，为联调排查问题的依据：" + html);
                //将生成的html写到浏览器中完成自动跳转打开银联支付页面；这里调用signData之后，将html写到浏览器跳转到银联页面之前均不能对html中的表单项的名称和值进行修改，如果修改会导致验签不通过

             //   resp= resp.getWriter().write(html);
        } if ("Form02_6_4_Query".equals(interfacename)||interfacename.contains("Form02_6_4_Query")) {

            String merId = jsonobject.getString("merId");
            String orderId = jsonobject.getString("orderId");
            String txnTime = jsonobject.getString("txnTime");

            Map<String, String> data = new HashMap<String, String>();

            /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
            data.put("version", DemoBase.version);                 //版本号
            data.put("encoding", DemoBase.encoding);               //字符集编码 可以使用UTF-8,GBK两种方式
            data.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
            data.put("txnType", "00");                             //交易类型 00-默认
            data.put("txnSubType", "00");                          //交易子类型  默认00
            data.put("bizType", "000202");                         //业务类型

            /***商户接入参数***/
            data.put("merId", merId);                  			   //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
            data.put("accessType", "0");                           //接入类型，商户接入固定填0，不需修改

            /***要调通交易以下字段必须修改***/
            data.put("orderId", orderId);                 //****商户订单号，每次发交易测试需修改为被查询的交易的订单号
            data.put("txnTime", txnTime);                 //****订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间

            /**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/

            Map<String, String> reqData = AcpService.sign(data,DemoBase.encoding);			//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
            String url = SDKConfig.getConfig().getSingleQueryUrl();								//交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.singleQueryUrl
            Map<String, String> rspData = AcpService.post(reqData,url,DemoBase.encoding);  //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

            /**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
            //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
            if(!rspData.isEmpty()){
                if(AcpService.validate(rspData, DemoBase.encoding)){
                    LogUtil.writeLog("验证签名成功");
                    if(("00").equals(rspData.get("respCode"))){//如果查询交易成功
                        String origRespCode = rspData.get("origRespCode");
                        if(("00").equals(origRespCode)){
                            //交易成功，更新商户订单状态
                            //TODO
                        }else if(("03").equals(origRespCode)||
                                ("04").equals(origRespCode)||
                                ("05").equals(origRespCode)){
                            //订单处理中或交易状态未明，需稍后发起交易状态查询交易 【如果最终尚未确定交易是否成功请以对账文件为准】
                            //TODO
                        }else{
                            //其他应答码为交易失败
                            //TODO
                        }
                    }else if(("34").equals(rspData.get("respCode"))){
                        //订单不存在，可认为交易状态未明，需要稍后发起交易状态查询，或依据对账结果为准

                    }else{//查询交易本身失败，如应答码10/11检查查询报文是否正确
                        //TODO
                    }
                }else{
                    LogUtil.writeErrorLog("验证签名失败");
                    //TODO 检查验证签名失败的原因
                }
            }else{
                //未返回正确的http状态
                LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
            }

            String reqMessage = DemoBase.genHtmlResult(reqData);
            String rspMessage = DemoBase.genHtmlResult(rspData);
            html="交易状态查询交易</br>请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+"";
            spdResult.setHtml(html);
        }if ("Form02_6_3_Refund".equals(interfacename)||interfacename.contains("Form02_6_3_Refund")){
            String origQryId = jsonobject.getString("origQryId");
            String txnAmt = jsonobject.getString("txnAmt");

            Map<String, String> data = new HashMap<String, String>();

            /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
            data.put("version", DemoBase.version);               //版本号
            data.put("encoding", DemoBase.encoding);             //字符集编码 可以使用UTF-8,GBK两种方式
            data.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
            data.put("txnType", "04");                           //交易类型 04-退货
            data.put("txnSubType", "00");                        //交易子类型  默认00
            data.put("bizType", "000202");                       //业务类型
            data.put("channelType", "07");                       //渠道类型，07-PC，08-手机

            /***商户接入参数***/
            data.put("merId", "777290058110048");                //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
            data.put("accessType", "0");                         //接入类型，商户接入固定填0，不需修改
            data.put("orderId", DemoBase.getOrderId());          //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费
            data.put("txnTime", DemoBase.getCurrentTime());      //订单发送时间，格式为yyyyMMddHHmmss，必须取当前时间，否则会报txnTime无效
            data.put("currencyCode", "156");                     //交易币种（境内商户一般是156 人民币）
            data.put("txnAmt", txnAmt);                          //****退货金额，单位分，不要带小数点。退货金额小于等于原消费金额，当小于的时候可以多次退货至退货累计金额等于原消费金额
            data.put("backUrl", DemoBase.backUrl);               //后台通知地址，后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 退货交易 商户通知,其他说明同消费交易的后台通知

            /***要调通交易以下字段必须修改***/
            data.put("origQryId", origQryId);      //****原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取

            // 请求方保留域，
            // 透传字段，查询、通知、对账文件中均会原样出现，如有需要请启用并修改自己希望透传的数据。
            // 出现部分特殊字符时可能影响解析，请按下面建议的方式填写：
            // 1. 如果能确定内容不会出现&={}[]"'等符号时，可以直接填写数据，建议的方法如下。
//		data.put("reqReserved", "透传信息1|透传信息2|透传信息3");
            // 2. 内容可能出现&={}[]"'符号时：
            // 1) 如果需要对账文件里能显示，可将字符替换成全角＆＝｛｝【】“‘字符（自己写代码，此处不演示）；
            // 2) 如果对账文件没有显示要求，可做一下base64（如下）。
            //    注意控制数据长度，实际传输的数据长度不能超过1024位。
            //    查询、通知等接口解析时使用new String(Base64.decodeBase64(reqReserved), DemoBase.encoding);解base64后再对数据做后续解析。
//		data.put("reqReserved", Base64.encodeBase64String("任意格式的信息都可以".toString().getBytes(DemoBase.encoding)));

            /**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/

            Map<String, String> reqData  = AcpService.sign(data,DemoBase.encoding);//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
            String url = SDKConfig.getConfig().getBackRequestUrl();								//交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
            Map<String, String> rspData = AcpService.post(reqData, url,DemoBase.encoding);//这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

            /**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
            //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
            if(!rspData.isEmpty()){
                if(AcpService.validate(rspData, DemoBase.encoding)){
                    LogUtil.writeLog("验证签名成功");
                    String respCode = rspData.get("respCode") ;
                    if(("00").equals(respCode)){
                        //交易已受理(不代表交易已成功），等待接收后台通知更新订单状态,也可以主动发起 查询交易确定交易状态。
                        //TODO
                    }else if(("03").equals(respCode)||
                            ("04").equals(respCode)||
                            ("05").equals(respCode)){
                        //后续需发起交易状态查询交易确定交易状态
                        //TODO
                    }else{
                        //其他应答码为失败请排查原因
                        //TODO
                    }
                }else{
                    LogUtil.writeErrorLog("验证签名失败");
                    //TODO 检查验证签名失败的原因
                }
            }else{
                //未返回正确的http状态
                LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
            }
            String reqMessage = DemoBase.genHtmlResult(reqData);
            String rspMessage = DemoBase.genHtmlResult(rspData);
            html="请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+"";
            spdResult.setHtml(html);
        }if ("Form02_7_FileTransfer".equals(interfacename)|| interfacename.contains("Form02_7_FileTransfer")){


            String merId = jsonobject.getString("merId");
            String settleDate = jsonobject.getString("settleDate");

            Map<String, String> data = new HashMap<String, String>();

            /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
            data.put("version", DemoBase.version);               //版本号 全渠道默认值
            data.put("encoding", DemoBase.encoding);             //字符集编码 可以使用UTF-8,GBK两种方式
            data.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
            data.put("txnType", "76");                           //交易类型 76-对账文件下载
            data.put("txnSubType", "01");                        //交易子类型 01-对账文件下载
            data.put("bizType", "000000");                       //业务类型，固定

            /***商户接入参数***/
            data.put("accessType", "0");                         //接入类型，商户接入填0，不需修改
            data.put("merId", merId);                	         //商户代码，请替换正式商户号测试，如使用的是自助化平台注册的777开头的商户号，该商户号没有权限测文件下载接口的，请使用测试参数里写的文件下载的商户号和日期测。如需777商户号的真实交易的对账文件，请使用自助化平台下载文件。
            data.put("settleDate", settleDate);                  //清算日期，如果使用正式商户号测试则要修改成自己想要获取对账文件的日期， 测试环境如果使用700000000000001商户号则固定填写0119
            data.put("txnTime",DemoBase.getCurrentTime());       //订单发送时间，取系统时间，格式为yyyyMMddHHmmss，必须取当前时间，否则会报txnTime无效
            data.put("fileType", "00");                          //文件类型，一般商户填写00即可

            /**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/

            Map<String, String> reqData = AcpService.sign(data,DemoBase.encoding);				//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
            String url = SDKConfig.getConfig().getFileTransUrl();										//获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.fileTransUrl
            Map<String, String> rspData = AcpService.post(reqData, url,DemoBase.encoding);

            /**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/

            //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
            String fileContentDispaly = "";
            if(!rspData.isEmpty()){
                if(AcpService.validate(rspData, DemoBase.encoding)){
                    LogUtil.writeLog("验证签名成功");
                    String respCode = rspData.get("respCode");
                    if("00".equals(respCode)){
                        String outPutDirectory ="F:\\file";
                        // 交易成功，解析返回报文中的fileContent并落地
                        String zipFilePath = AcpService.deCodeFileContent(rspData,outPutDirectory,DemoBase.encoding);
                        //对落地的zip文件解压缩并解析
                        List<String> fileList = DemoBase.unzip(zipFilePath, outPutDirectory);
                        //解析ZM，ZME文件
                        fileContentDispaly ="<br>获取到商户对账文件，并落地到"+outPutDirectory+",并解压缩 <br>";
                        for(String file : fileList){
                            if(file.indexOf("ZM_")!=-1){
                                List<Map> ZmDataList = DemoBase.parseZMFile(file);
                                fileContentDispaly = fileContentDispaly+DemoBase.getFileContentTable(ZmDataList,file);
                            }else if(file.indexOf("ZME_")!=-1){
                                DemoBase.parseZMEFile(file);
                            }
                        }
                        //TODO
                    }else{
                        //其他应答码为失败请排查原因
                        //TODO
                    }
                }else{
                    LogUtil.writeErrorLog("验证签名失败");
                    //TODO 检查验证签名失败的原因
                }
            }else{
                //未返回正确的http状态
                LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
            }

            String reqMessage = DemoBase.genHtmlResult(reqData);
            String rspMessage = DemoBase.genHtmlResult(rspData);
            html="</br>请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+fileContentDispaly;
            spdResult.setHtml(html);
        }
        return  spdResult;
    }


    private Object eciticPay(String paramUrl, String interfacename, String flag,JSONObject jsonobject) throws Exception {
        String urlStr = "";
/*        String line = "";
        String jsonReturnParam = "";
        String listJsonStr = "";
        String paramStr="";*/
        //状态信息实体初始化
    //    StringBuffer resultSting = new StringBuffer();
        //拼接访问地址路径
        //查询主账户信息接口
        if ("DLBALQRY".equals(interfacename) || interfacename.contains("DLBALQRY")) {
            logger.info("访问主账户信息查询接口");
            urlStr = paramUrl + "/DLBALQRY";
            return DLBALQRY.escapeParamer(urlStr,jsonobject,interfacename,flag);
        }//附属用户普通转账接口
       /* if ("DLSUBTRN".equals(interfacename) || interfacename.contains("DLSUBTRN")) {
            logger.info("访问附属用户普通转账接口");
            urlStr = paramUrl + "/DLSUBTRN";
        }*/
        //附属用户强制转账接口
        if ("DLMDETRN ".equals(interfacename) || interfacename.contains("DLMDETRN")) {
            logger.info("访问附属用户强制转账接口");
            urlStr = paramUrl + "/DLMDETRN";
            return DLMDETRN.escapeParamer(urlStr,jsonobject,interfacename,flag,paramUrl);
        }
        //平台出金
        if ("DLFCSOUT".equals(interfacename) || interfacename.contains("DLFCSOUT")) {
            logger.info("访问平台出金接口");
            urlStr = paramUrl + "/DLFCSOUT";
            return DLFCSOUT.escapeParamer(urlStr,jsonobject,interfacename,flag,paramUrl);
        }
        //查询附属用户信息接口
        if ("DLSBALQR".equals(interfacename) || interfacename.contains("DLSBALQR")) {
            logger.info("访问附属用户信息查询接口");
            urlStr = paramUrl + "/DLSBALQR";
            return DLSBALQR.escapeParamer(urlStr,jsonobject,interfacename,flag);
        }
        //查询支付联行号信息接口
        if ("DLBNKCOD".equals(interfacename) || interfacename.contains("DLBNKCOD")) {
            logger.info("访问支付联行号信息查询接口");
            urlStr = paramUrl + "/DLBNKCOD";
            return DLBNKCOD.escapeParamer(urlStr,jsonobject,interfacename,flag);
        }
        //查询订单流水号信息接口
        if ("DLPGOSTT".equals(interfacename) || interfacename.contains("DLPGOSTT")) {
            logger.info("访问订单流水号信息查询接口");
            urlStr = paramUrl + "/DLPGOSTT";
            return DLPGOSTT.escapeParamer(urlStr,jsonobject,interfacename,flag);
        }//交易状态查询接口
        if ("DLCIDSTT".equals(interfacename) || interfacename.contains("DLCIDSTT")) {
            logger.info("访问交易状态查询接口");
            urlStr = paramUrl + "/DLCIDSTT";
            return DLCIDSTT.escapeParamer(urlStr,jsonobject,interfacename,flag);
        }
        //会员注册接口
        if("DLBREGSN".equals(interfacename)|| interfacename.contains("DLBREGSN")){
            logger.info("访问会员注册接口");
            urlStr= paramUrl + "/DLBREGSN";
            return DLBREGSN.escapeParamer(urlStr,jsonobject,interfacename,flag);
        }
        //在线销户接口
        if("DLOLCACC".equals(interfacename)|| interfacename.contains("DLOLCACC")){
            logger.info("在线销户接口");
            urlStr= paramUrl + "/DLOLCACC";
            return DLOLCACC.escapeParamer(urlStr,jsonobject,interfacename,flag);
        }
        //查询冻结编号接口
        if("DLFRNOQR".equals(interfacename)|| interfacename.contains("DLFRNOQR")){
            logger.info("查询冻结编号接口");
            urlStr= paramUrl + "/DLFRNOQR";
            return DLFRNOQR.escapeParamer(urlStr,jsonobject,interfacename,flag);
        }
        //非登录打印明细查询接口
        if("DLPTDTQY".equals(interfacename) || interfacename.contains("DLPTDTQY")){
            logger.info("非登录打印明细查询接口");
            urlStr= paramUrl + "/DLPTDTQY";
            return DLPTDTQY.escapeParamer(urlStr,jsonobject,interfacename,flag);
        }
        return null;
    }

    /*
     * 查询附属账户信息
     * */
    public static Object adjunctAccountQry(String paramUrl, String paramStr, String Flag) throws Exception {
        String urlStr = "";
        String line = "";
        String jsonReturnParam = "";
        String listJsonStr = "";
        AdjunctAccount information = new AdjunctAccount();
        StringBuffer resultSting = new StringBuffer();
        urlStr = paramUrl + "/DLSBALQR";
        URL url = new URL(urlStr);
        URLConnection con = url.openConnection();
        byte[] xmlData = paramStr.getBytes("GBK");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setUseCaches(false);
        con.setRequestProperty("Cache-Control", "no-cache");
        con.setRequestProperty("Content-Type", "text/xml");
        con.setRequestProperty("charset", "GBK");
        con.setRequestProperty("Content-length", String.valueOf(xmlData.length));
        OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream(), "GBK");
        logger.info(">>>>>>>>>>接口地址为urlStr=" + urlStr);
        logger.info(">>>>>>>>>>传入的报文xmlInfo=" + paramStr);
        System.out.println(">>>>>>>>>>接口地址为urlStr=" + urlStr);
        System.out.println(">>>>>>>>>>传入的报文xmlInfo=" + paramStr);
        out.write(paramStr);
        out.flush();
        out.close();
        //3.获取返回报文
        BufferedReader br = new BufferedReader(new InputStreamReader(
                con.getInputStream(), "GBK"));
        //对返回值报文进行打印
        for (line = br.readLine(); line != null; line = br.readLine()) {
            //对返回的报文进行结果判断<RspCode>0000</RspCode>
            logger.info(">>>>>>>>>>>>>>>>>>>返回的结果报文内容为:---------" + line);
            System.out.println(">>>>>>>>>>>>>>>>>>>返回的结果报文内容为:---------" + line);
            //对返回的报文进行拼接,然后返回给业务层,在业务层进行判断
            resultSting.append(line);
        }
        if (resultSting != null || resultSting.length() > 0) {
            JSONObject returnjson = JsonUtils.xmlToJson(resultSting.toString());
            if (returnjson.getJSONObject("stream").toString().contains("list")) {
                listJsonStr = returnjson.getJSONObject("stream").getJSONObject("list").getJSONObject("row").toString();
            } else {
                listJsonStr = returnjson.getJSONObject("stream").toString();
            }
            JSONObject listJson = JSON.parseObject(listJsonStr);
            listJson.put("platFormStatus", "AA");
            listJson.put("message", "请求成功");
            listJson.put("flag", Flag);
            information = JSONObject.parseObject(listJson.toJSONString(), AdjunctAccount.class);
            jsonReturnParam = listJson.toString();
            System.out.println(jsonReturnParam);
        }

        return information;

    }


    /**
     * 浦发银行接口
     * @param interfacename
     * @param jsonobject
     * @param flag
     * @return
     */
    private Object spdPay(String paramUrl,String interfacename, JSONObject jsonobject, String flag ) {
        String urlStr = "";
        if ("acctDtlInfoQry".equals(interfacename) || interfacename.contains("acctDtlInfoQry")) {
            logger.info("访问浦发银行对公账户信息查询接口");
            urlStr = paramUrl + "/acctDtlInfoQry";
            String jsonStr = JSON.toJSONString(jsonobject);
            return SPDBSignature.getSPDBSignature(urlStr, jsonStr, interfacename, flag);

        }if ("realTmSnglAgncPymt".equals(interfacename) || interfacename.contains("realTmSnglAgncPymt")) {
            logger.info("访问浦发银行单笔实时代收付交易接口");
            urlStr = paramUrl + "/realTmSnglAgncPymt";
            String jsonStr = JSON.toJSONString(jsonobject);
            return SPDBSignature.getSPDBSignature(urlStr, jsonStr, interfacename, flag);

        }if ("realTmSnglQry".equals(interfacename) || interfacename.contains("realTmSnglQry")) {
            logger.info("访问浦发银行实时单笔查询交易接口");
            urlStr = paramUrl + "/realTmSnglQry";
            String jsonStr = JSON.toJSONString(jsonobject);
            return SPDBSignature.getSPDBSignature(urlStr, jsonStr, interfacename, flag);

        }/*if ("btchAgncTranFileUpload".equals(interfacename) || interfacename.contains("btchAgncTranFileUpload")) {
            logger.info("访问浦发银行批量文件上传交易接口");
            urlStr = paramUrl + "/btchAgncTranFileUpload";
            String jsonStr = JSON.toJSONString(jsonobject);
            return SPDBSignature.getSPDBSignature(urlStr, jsonStr, interfacename, flag);

        }*/if ("btchAgncTranQry".equals(interfacename) || interfacename.contains("btchAgncTranQry")) {
            logger.info("访问浦发银行批量代收付交易查询接口");
            urlStr = paramUrl + "/btchAgncTranQry";
            String jsonStr = JSON.toJSONString(jsonobject);
            return SPDBSignature.getSPDBSignature(urlStr, jsonStr, interfacename, flag);
        }if ("btchAgncTranFileDownload".equals(interfacename) || interfacename.contains("btchAgncTranFileDownload")) {
            logger.info("访问浦发银行批量代收付API交易文件下载查询接口");
            urlStr = paramUrl + "/btchAgncTranFileDownload";
            String jsonStr = JSON.toJSONString(jsonobject);
            return SPDBSignature.getSPDBSignature(urlStr, jsonStr, interfacename, flag);
        }if("rcnclAplTran".equals(interfacename) || interfacename.contains("rcnclAplTran")){
            logger.info("访问浦发银行批量代收付API对账请求交易接口");
            urlStr = paramUrl + "/rcnclAplTran";
            String jsonStr = JSON.toJSONString(jsonobject);
            return SPDBSignature.getSPDBSignature(urlStr, jsonStr, interfacename, flag);
        }if ("rcnclRsltQryApl".equals(interfacename) || interfacename.contains("rcnclRsltQryApl")){
            logger.info("访问浦发银行批量代收付API对账结果查询下载请求交易接口");
            urlStr = paramUrl + "/rcnclRsltQryApl";
            String jsonStr = JSON.toJSONString(jsonobject);
            return SPDBSignature.getSPDBSignature(urlStr, jsonStr, interfacename, flag);
        }
            return null;
    }


    /**
     * 浦发银行批量文件上传交易接口
     * @param interfacename
     * @param jsonobject
     * @param flag
     * @return
     */
    private Object spdPay(String paramUrl,String interfacename, JSONObject jsonobject, String flag,Map<String, File>  fileMap) {
        String urlStr = "";
       if ("btchAgncTranFileUpload".equals(interfacename) || interfacename.contains("btchAgncTranFileUpload")) {
            logger.info("访问浦发银行批量文件上传交易接口");
            urlStr = paramUrl + "/btchAgncTranFileUpload";
            String jsonStr = JSON.toJSONString(jsonobject);
            return SPDBSignature.getSPDBSignature(urlStr, jsonStr, interfacename, flag, fileMap);

        }

        return null;
    }
}



