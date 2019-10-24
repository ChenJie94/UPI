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
public class DLFCSOUT {
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
        stream.setTranAmt(jsonobject.getString("tranAmt"));
        stream.setMemo(jsonobject.getString("memo"));
        stream.setRecvAccNo(jsonobject.getString("recvAccNo"));
        stream.setRecvAccNm(jsonobject.getString("recvAccNm"));
        stream.setPayType(jsonobject.getString("payType"));
        stream.setSameBank(jsonobject.getString("sameBank"));
        stream.setRecvBankNm(jsonobject.getString("recvBankNm"));
        stream.setPreFlg(jsonobject.getString("preFlag"));
        stream.setRecvTgfi(jsonobject.getString("recvTgfi"));
        jsonBean.setStream(stream);
        String paramStr = jsonObject.toJSONString(jsonBean);
        String paramJson=jsonObject.parse(paramStr).toString();
        JSONObject json = JSON.parseObject(paramStr);
        jsonobject = json.parseObject(paramJson);

        String param_DLFCSOUT = JsonUtils.jsonToPrettyXml(jsonobject);
        JSONObject paramStrJson = JsonUtils.xmlToJson(param_DLFCSOUT);
        JSONObject aujunctParam = JSON.parseObject(paramStrJson.toJSONString());
        JSONObject paramJson2 = aujunctParam.getJSONObject("stream");
        String subAccNo = paramJson2.get("accountNo").toString();
        BigDecimal tranAmt = paramJson2.getBigDecimal("tranAmt");
        //替换paramJson的部分参数 生成附属账户查询的报文，用以查询要付款的支付账户当前账户余额是否足以完成支付
        paramJson2.replace("action", "DLSBALQR");
        paramJson2.remove("clientID");
        paramJson2.remove("accountNo");
        paramJson2.remove("recvAccNo");
        paramJson2.remove("recvAccNm");
        paramJson2.remove("tranAmt");
        paramJson2.remove("memo");
        paramJson2.remove("preFlg");
        paramJson2.remove("sameBank");
        paramJson2.remove("payType");
        paramJson2.remove("recvBankNm");
        paramJson2.put("accountNo", "8111301012900478766");
        paramJson2.put("subAccNo", subAccNo);
        aujunctParam.put("stream", paramJson2);
        System.out.println(aujunctParam);
        String param = JsonUtils.jsonToPrettyXml(aujunctParam);
        AdjunctAccount adjunctccount = (AdjunctAccount) UnifyPayController.adjunctAccountQry(paramUrl, param, flag);
        BigDecimal kyamt = adjunctccount.getKYAMT();//可用余额
        BigDecimal sjamt = adjunctccount.getSJAMT();//实际余额
        if (sjamt.compareTo(tranAmt) >= 0) {//判断实际余额是否大于要转出出金的金额
            urlStr = paramUrl + "/DLFCSOUT";
            Object obinfo= ECITICSendParam.sendParam(urlStr,param_DLFCSOUT,interfacename,flag);
            return  obinfo;
        } else {
            StatusInformation information = new StatusInformation();
            JSONObject listJson = new JSONObject();
            listJson.put("platFormStatus", "AB");
            listJson.put("message", "交易失败，实际可用余额小于转出金额");
            listJson.put("flag", flag);
            information = json.parseObject(listJson.toJSONString(), StatusInformation.class);
            jsonReturnParam = listJson.toString();
            System.out.println(jsonReturnParam);
            return information;
        }
    }

}
