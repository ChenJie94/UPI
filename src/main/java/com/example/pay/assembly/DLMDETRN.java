package com.example.pay.assembly;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.pay.bean.AdjunctAccount;
import com.example.pay.bean.StatusInformation;
import com.example.pay.bean.ecitic.JsonBean;
import com.example.pay.bean.ecitic.JsonList;
import com.example.pay.bean.ecitic.JsonRow;
import com.example.pay.bean.ecitic.Stream;
import com.example.pay.demo.UnifyPayController;
import com.example.pay.util.ECITICSendParam;
import com.example.pay.util.JsonUtils;

import java.math.BigDecimal;

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
 *                神兽保佑
 *               代码无BUG!
 */
public class DLMDETRN {
    /**
     *
     * @param urlStr
     * @param jsonobject
     * @param interfacename
     * @param flag
     * @param paramUrl
     * @return
     * @throws Exception
     */
    public static Object escapeParamer(String urlStr ,JSONObject jsonobject,String interfacename, String flag,String paramUrl) throws Exception {
        Stream stream= new Stream();
        JsonRow row = new JsonRow();
        JsonList list=new JsonList();
        JsonBean jsonBean= new JsonBean();
        JSONObject jsonObject=new JSONObject();
        String jsonReturnParam = "";
        stream.setAction(jsonobject.getString("action"));
        stream.setUserName(jsonobject.getString("userName"));
        stream.setAccountNo(jsonobject.getString("accountNo"));
        stream.setClientID(jsonobject.getString("clientID"));
        stream.setPayAccNo(jsonobject.getString("payAccNo"));
        stream.setTranAmt(jsonobject.getString("tranAmt"));
        stream.setMemo(jsonobject.getString("memo"));
        stream.setRecvAccNo(jsonobject.getString("recvAccNo"));
        stream.setRecvAccNm(jsonobject.getString("recvAccNm"));
        stream.setTranType(jsonobject.getString("tranType"));
        stream.setFreezeNo(jsonobject.getString("freezeNo"));
        stream.setTranFlag(jsonobject.getString("tranFlag"));
        jsonBean.setStream(stream);

        String paramStr = jsonObject.toJSONString(jsonBean);
        String paramJson=jsonObject.parse(paramStr).toString();

        JSONObject json = JSON.parseObject(paramStr);
        jsonobject = json.parseObject(paramJson);

        String param_DLMDTERN = JsonUtils.jsonToPrettyXml(jsonobject);

        JSONObject paramStrJson = JsonUtils.xmlToJson(param_DLMDTERN);
        JSONObject aujunctParam = JSON.parseObject(paramStrJson.toJSONString());
        JSONObject paramJson2 = aujunctParam.getJSONObject("stream");
        String subAccNo = paramJson2.get("payAccNo").toString();
        String subAccNo_after = paramJson2.get("recvAccNo").toString();
        BigDecimal tranAmt = paramJson2.getBigDecimal("tranAmt");
        //替换paramJson2的部分参数 生成附属账户查询的报文，用以查询要付款的支付账户当前账户余额是否足以完成支付
        paramJson2.replace("action", "DLSBALQR");
        paramJson2.remove("clientID");
        paramJson2.remove("tranType");
        paramJson2.remove("recvAccNo");
        paramJson2.remove("recvAccNm");
        paramJson2.remove("tranAmt");
        paramJson2.remove("freezeNo");
        paramJson2.remove("ofreezeamt");
        paramJson2.remove("memo");
        paramJson2.remove("tranFlag");
        paramJson2.remove("payAccNo");
        paramJson2.put("subAccNo", subAccNo);
        aujunctParam.put("stream", paramJson2);
        String param = JsonUtils.jsonToPrettyXml(aujunctParam);
        AdjunctAccount adjunctccount = (AdjunctAccount) UnifyPayController.adjunctAccountQry(paramUrl, param, flag);

    //    BigDecimal kyamt = adjunctccount.getKYAMT();//可用余额
        BigDecimal sjamt = adjunctccount.getSJAMT();//实际余额
        if (sjamt==null){
            StatusInformation information = new StatusInformation();
            JSONObject listJson = new JSONObject();
            listJson.put("platFormStatus", "AB");
            listJson.put("message", "交易失败，没有查到该附属账户信息！");
            listJson.put("flag", flag);
            listJson.put("status", "EEEEEEE");
            listJson.put("statusText", "交易失败，没有查到该附属账户信息！");
            information = json.parseObject(listJson.toJSONString(), StatusInformation.class);
            jsonReturnParam = listJson.toString();
            System.out.println(jsonReturnParam);
            return information;
        }else {
            if (sjamt.compareTo(tranAmt) >= 0) {//判断实际余额是否大于要支付的金额
                urlStr = paramUrl + "/DLMDETRN";
                Object obinfo = ECITICSendParam.sendParam(urlStr, param_DLMDTERN, interfacename, flag);
                return obinfo;
            } else {
                StatusInformation information = new StatusInformation();
                JSONObject listJson = new JSONObject();
                listJson.put("platFormStatus", "AB");
                listJson.put("message", "交易失败，实际可用余额小于付款金额!");
                listJson.put("flag", flag);
                listJson.put("status", "EEEEEEE");
                listJson.put("statusText", "交易失败，实际可用余额小于付款金额！");
                information = json.parseObject(listJson.toJSONString(), StatusInformation.class);
                jsonReturnParam = listJson.toString();
                System.out.println(jsonReturnParam);
                return information;
            }
        }
    }



}
