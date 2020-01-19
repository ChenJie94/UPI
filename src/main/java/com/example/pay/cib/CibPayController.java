package com.example.pay.cib;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * ClassName: CibPayController
 * Description:
 * date: 2020/1/19 9:11
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
public class CibPayController {
    protected static Logger logger = LoggerFactory.getLogger(CibPayController.class);
    public Object cibPay(String remote_ip,String interfacename, JSONObject jsonobject, String flag) throws  Exception{
        String urlStr = "";
        String returnHtml = "";
        if ("GpPay".equals(interfacename) || interfacename.contains("GpPay")) {
            logger.info("访问兴业银行网关支付接口");
            try {
                String order_no = jsonobject.getString("order_no");
                String order_amount = jsonobject.getString("order_amount");
                String order_title = jsonobject.getString("order_title");
                String order_desc = jsonobject.getString("order_desc");
           //     String remote_ip = jsonobject.getRemoteAddr();
                // 【重要】出于安全考虑，在调用函数前，需要对上面的参数进行防护过滤等操作
                //	System.out.println(gpPay(order_no, order_amount, order_title, order_desc, remote_ip));
                returnHtml=gpPay(order_no, order_amount, order_title, order_desc, remote_ip);
                return returnHtml;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return returnHtml;
    }

    /**
     * 网关支付交易跳转页面生成接口<br />
     * 该方法将生成跳转页面的全部HTML代码，商户直接输出该HTML代码至某个URL所对应的页面中，即可实现跳转，可以参考示例epay_redirect
     * .jsp<br />
     * [重要]各传入参数SDK都不作任何检查、过滤，请务必在传入前进行安全检查或过滤，保证传入参数的安全性，否则会导致安全问题。
     *
     * @param order_no
     *            订单号
     * @param order_amount
     *            金额，单位元，两位小数，例：8.00
     * @param order_title
     *            订单标题
     * @param order_desc
     *            订单描述
     * @param remote_ip
     *            客户端IP地址
     * @return 跳转页面HTML代码
     */
    public static String gpPay(String order_no, String order_amount, String order_title, String order_desc,
                               String remote_ip) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("order_no", order_no);
        params.put("order_amount", order_amount);
        params.put("order_title", order_title);
        params.put("order_desc", order_desc);
        params.put("order_ip", remote_ip);

        return new GpPay().build(params);
    }
}
