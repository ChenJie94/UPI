package com.example.pay.assembly;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.pay.bean.ecitic.JsonBean;
import com.example.pay.bean.ecitic.Stream;
import com.example.pay.util.ECITICSendParam;
import com.example.pay.util.JsonUtils;

/**
 * ClassName: DLPTDTQY
 * Description:
 * date: 2019/8/30 11:14
 *
 * @author 陈杰
 * @version 1.0
 * @since JDK 1.8
 * .........┌─┐              ┌─┐
 * ...┌──┘  ┴───────┘  ┴──┐
 * ...│                                  │
 * ...│          ───                  │
 * ...│     ─┬┘       └┬─          │
 * ...│                                  │
 * ...│           ─┴─                 │
 * ...│                                  │
 * ...└───┐                  ┌───┘
 * ...........│                  │
 * ...........│                  │
 * ...........│                  │
 * ...........│                  └──────────────┐
 * ...........│                                                │
 * ...........│                                                ├─┐
 * ...........│                                                ┌─┘
 * ...........│                                                │
 * ...........└─┐    ┐    ┌───────┬──┐    ┌──┘
 * ...............│  ─┤  ─┤              │  ─┤  ─┤
 * ...............└──┴──┘              └──┴──┘
 * --------------------------------神兽保佑--------------------------------
 * --------------------------------代码无BUG!------------------------------
 */
public class DLPTDTQY {

    public static Object escapeParamer(String urlStr ,JSONObject jsonobject,String interfacename, String flag) throws Exception {
        Stream stream= new Stream();
        JsonBean jsonBean= new JsonBean();
        JSONObject jsonObject=new JSONObject();

        stream.setAction(jsonobject.getString("action"));
        stream.setUserName(jsonobject.getString("userName"));
        stream.setMainAccNo(jsonobject.getString("mainAccNo"));
        stream.setSubAccNo(jsonobject.getString("subAccNo"));
        stream.setStartDate(jsonobject.getString("startDate"));
        stream.setEndDate(jsonobject.getString("endDate"));
        stream.setStartRecord(jsonobject.getInteger("startRecord"));
        stream.setPageNumber(jsonobject.getInteger("pageNumber"));
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
