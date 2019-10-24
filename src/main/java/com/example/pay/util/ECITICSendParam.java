package com.example.pay.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.pay.bean.Account;
import com.example.pay.bean.AdjunctAccount;
import com.example.pay.bean.PaymentlLinkInformation;
import com.example.pay.bean.StatusInformation;
import com.example.pay.demo.UnifyPayController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


/**
 * 实现中信银行接口请求的工具类
 *
 * @author: chenjie
 * @date: 2019/3/08 15:12
 * 统一支付平台接口 UPI
 *AES工具类
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
 *                神兽保佑
 *               代码无BUG!
 */
public class ECITICSendParam {
    protected static Logger logger = LoggerFactory.getLogger(UnifyPayController.class);
    private static final String logExceptionFormat = "Capture Exception By GlobalExceptionHandler: Code: %s Detail: %s";

    /**
     * @param urlStr
     * @param paramStr
     * @param interfacename
     * @param flag
     * @return
     * @throws Exception
     */
    public static  Object  sendParam (String urlStr,String paramStr,String interfacename, String flag)throws Exception{
        String line = "";
        JSONObject listJson = new JSONObject();
        StringBuffer resultSting = new StringBuffer();
        String jsonReturnParam = "";
        String listJsonStr = "";
        URL url = new URL(urlStr);
        URLConnection con = url.openConnection();
        byte[] xmlData = paramStr.getBytes("GBK");
        //设置响应的请求参数
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
    //    System.out.println(">>>>>>>>>>接口地址为urlStr=" + urlStr);
    //    System.out.println(">>>>>>>>>>传入的报文xmlInfo=" + paramStr);

        out.write(paramStr);
        out.flush();
        out.close();
        //3.获取返回报文
        BufferedReader br = new BufferedReader(new InputStreamReader(
                con.getInputStream(), "GBK"));
        //对返回值报文进行打印
        for (line = br.readLine(); line != null; line = br.readLine()) {
            //对返回的报文进行结果判断);
            logger.info(">>>>>>>>>>>>>>>>>>>返回的结果报文内容为:---------" + line);
            //对返回的报文进行拼接,然后返回给业务层,在业务层进行判断
            resultSting.append(line);
        }


        if (resultSting != null || resultSting.length() > 0) {
            String result_str=resultSting.toString();
            List<Object> list=new ArrayList<>();
            if (result_str.contains("<verifyCode>")) {
                JSONObject returnjson = JsonUtils.xml2Json(resultSting.toString());
                System.out.println(returnjson.toString());
                Object o=  returnjson.getJSONObject("list").get("row").getClass();
             //   JSONObject js=  returnjson.getJSONObject("list").getJSONObject("row");
                if(((Class) o).getName().equals("com.alibaba.fastjson.JSONArray")){
                    list =returnjson.getJSONObject("list").getJSONArray("row");//row里面封装了list 所以转成JSONArray
                }else{
                    list.add(returnjson.getJSONObject("list").getJSONObject("row"));
                }
                listJson.put("platFormStatus", "AA");
                listJson.put("message", "请求成功");
                listJson.put("flag", flag);
                listJson.put("statusText", "交易成功");
                listJson.put("status", "AAAAAAA");
                listJson.put("verifyCodeList", list);
            }
            else{
                JSONObject returnjson = JsonUtils.xmlToJson(resultSting.toString());
                System.out.println(returnjson.toString());
                if (returnjson.getJSONObject("stream").toString().contains("list")) {
                    listJsonStr = returnjson.getJSONObject("stream").getJSONObject("list").getJSONObject("row").toString();
                } else {
                    listJsonStr = returnjson.getJSONObject("stream").toString();
                }
                listJson = JSON.parseObject(listJsonStr);
                listJson.put("platFormStatus", "AA");
                listJson.put("message", "请求成功");
                listJson.put("flag", flag);
                if (listJson.containsKey("statusText")) {
                    if (listJson.getString("statusText").equals("AAAAAAE")) {
                        listJson.put("statusText", "交易成功! 已提交银行处理，稍后到账。");
                    }
                }
            }

            //如果为 DLSBALQR ，DLBREGSN 这两个接口请求 返回的为AdjunctAccount 对象参数 附属账户相关信息
            if ("DLSBALQR".equals(interfacename) || interfacename.contains("DLSBALQR")||"DLBREGSN".equals(interfacename)||interfacename.contains("DLBREGSN")||"DLOLCACC".equals(interfacename)||interfacename.contains("DLOLCACC")) {
                AdjunctAccount information = new AdjunctAccount();
                information = JSONObject.parseObject(listJson.toJSONString(), AdjunctAccount.class);
                jsonReturnParam = listJson.toString();
                System.out.println(jsonReturnParam);
                return information;
            }
            //如果为 DLBALQRY 接口请求 返回的为 Account 对象参数 主账户相关信息
            if ("DLBALQRY".equals(interfacename) || interfacename.contains("DLBALQRY")) {
                Account information = new Account();
                information = JSONObject.parseObject(listJson.toJSONString(), Account.class);
                jsonReturnParam = listJson.toString();
                System.out.println(jsonReturnParam);
                return information;
            }
            //如果为 DLFRNOQR 查询冻结编号接口请求 返回的为 StatusInformation 对象参数   平台及银行支付状态相关信息拼上流水号和冻结编号的参数数据
            if ("DLFRNOQR".equals(interfacename) || interfacename.contains("DLFRNOQR")){
                StatusInformation information = new StatusInformation();
                information = JSONObject.parseObject(listJson.toJSONString(), StatusInformation.class);
                if (listJson.containsKey("clientID")) {//流水号
                    information.setClientID(listJson.getString("clientID"));
                }
                if (listJson.containsKey("freezeNo")) {//冻结编号
                    information.setFreezeNo(listJson.getString("freezeNo"));
                }
                jsonReturnParam = listJson.toString();
                System.out.println(jsonReturnParam);
                return information;
            }
            //支付联行号查询
            if("DLBNKCOD".equals(interfacename) || interfacename.contains("DLBNKCOD")){
                PaymentlLinkInformation information = new PaymentlLinkInformation();
                information = JSONObject.parseObject(listJson.toJSONString(), PaymentlLinkInformation.class);
                if (information.getTgfi() != null) {
                    information.setStatusText("交易成功!");
                }
                jsonReturnParam = listJson.toString();
                System.out.println(jsonReturnParam);
                return information;
            }
            //3.42非登录打印明细查询
            if ("DLPTDTQY".equals(interfacename) || interfacename.contains("DLPTDTQY")){
                StatusInformation information = new StatusInformation();
                information = JSONObject.parseObject(listJson.toJSONString(), StatusInformation.class);
                jsonReturnParam = listJson.toString();
                System.out.println(jsonReturnParam);
                return information;
            }
            else { //如果为其他接口请求 返回的为 StatusInformation 对象参数   统一返回平台及银行支付状态相关信息
                StatusInformation information = new StatusInformation();
                information = JSONObject.parseObject(listJson.toJSONString(), StatusInformation.class);
              /*  if (information.getStatus().toString().equals("AAAAAAE")||information.getStatus().toString().equals("AAAAAAA")) {
                    information.setStatusText("交易成功!");
                }*/
                jsonReturnParam = listJson.toString();
                System.out.println(jsonReturnParam);
                return information;
            }

        }
        return null;


    }


}
