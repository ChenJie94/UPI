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
public class DLBREGSN {
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
        stream.setUserName(jsonobject.getString("userName"));
        stream.setMainAccNo(jsonobject.getString("mainAccNo"));
        stream.setAppFlag(jsonobject.getString("appFlag"));//应用系统， 2：B2B电子商务；3：投标保证金
        stream.setAccGenType(jsonobject.getString("accGenType"));//附属账户生成方式 ，0：自动输入 ；1：手动生成
        stream.setSubAccNm(jsonobject.getString("subAccNm"));//附属账户名称，可空，appFlag为2时必输，appFlag为3时可空，若不为空则其值必须为客户名称
        stream.setAccType(jsonobject.getString("accType"));//附属账户类型 ，03：一般交易账号；04：保证金账号；11：招投标保证金
        stream.setCalInterestFlag(jsonobject.getString("calInterestFlag"));//是否计算利息标志 ， 0：不计息；1：不分段计息；2：分段计息；当appFlag为3时，是否计算利息标志必须为0
        stream.setOverFlag(jsonobject.getString("overFlag"));//是否允许透支，0：不允许；1：限额透支；2：全额透支 ；appFlag为3时，必须为0
        stream.setAutoAssignInterestFlag(jsonobject.getString("autoAssignInterestFlag"));//自动分配利息标示char(1)，0：否；1：是；appFlag为3时，必须为0
        stream.setAutoAssignTranFeeFlag(jsonobject.getString("autoAssignTranFeeFlag"));//自动分配转账手续费标char(1)，0：否；1：是；appFlag为3时，必须为0
        stream.setFeeType(jsonobject.getString("freeType"));//手续费收取方式 char(1)，0：不收取；1：实时收取；2：月末累计；appFlag为3时，必须为0
        stream.setRealNameParm(jsonobject.getString("realNameParam"));//实名制更换char(1) ，0：账户名与账号全不换；1：账户名与账号全换；2：换账户名；3：换账号；appFlag为3时，必须为0
        stream.setSubAccPrintParm(jsonobject.getString("subAccPrintParm"));//附属账户凭证打印更换 char(1)，0：全部显示；1：显示附属账户名和账号；2：显示实体账户名和账号;3：显示附属账户名和实体账号；4：显示实体账户名和附属账号；appFlag为3时，必须为0
        stream.setMngNode(jsonobject.getString("mngNode"));//会员确认中心
        stream.setVtlCustNm(jsonobject.getString("vtlCustNm"));//虚拟客户名称
        stream.setLegalPersonNm(jsonobject.getString("legalPersonNm"));//法人名称
        stream.setCustCertType(jsonobject.getString("custCertType"));//客户证件类型(0为身份证)
        stream.setCustCertNo(jsonobject.getString("custCertNo"));//身份证号码
        stream.setBranch(jsonobject.getString("branch"));//所属机构
        stream.setCommAddress(jsonobject.getString("commAddress"));//通讯地址

        row.setContactName(jsonobject.getString("contactName"));//联系人姓名
        row.setContactPhone(jsonobject.getString("contactPhone"));//联系电话
        row.setMailAddress(jsonobject.getString("mailAddress"));//邮箱地址
        list.setRow(row);
        stream.setList(list);
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
