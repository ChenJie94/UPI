package com.example.pay.assembly;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.pay.bean.ecitic.JsonBean;
import com.example.pay.bean.ecitic.JsonList;
import com.example.pay.bean.ecitic.JsonRow;
import com.example.pay.bean.ecitic.Stream;
import com.example.pay.util.ECITICSendParam;
import com.example.pay.util.JsonUtils;

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
public class DLPGOSTT {
    /**
     *
     * @param urlStr
     * @param jsonobject
     * @param interfacename
     * @param flag
     * @return
     * @throws Exception
     */
    public static Object escapeParamer(String urlStr ,JSONObject jsonobject,String interfacename, String flag) throws Exception {
        Stream stream= new Stream();
        JsonRow row = new JsonRow();
        JsonList list=new JsonList();
        JsonBean jsonBean= new JsonBean();
        JSONObject jsonObject=new JSONObject();

        stream.setAction(jsonobject.getString("action"));
        stream.setUserName(jsonobject.getString("userNmae"));
        stream.setMctNo(jsonobject.getString("mctno"));//商户编号varchar(8)
        stream.setMctJnlNo(jsonobject.getString("mctJnlNo"));//商户流水号varchar(20)
        jsonBean.setStream(stream);

        String paramStr = jsonObject.toJSONString(jsonBean);
        String paramJson=jsonObject.parse(paramStr).toString();

        JSONObject json = JSON.parseObject(paramStr);
        jsonobject = json.parseObject(paramJson);

        String param = JsonUtils.jsonToPrettyXml(jsonobject);
        Object obinfo= ECITICSendParam.sendParam(urlStr,param,interfacename,flag);
        return  obinfo;
    }
}
