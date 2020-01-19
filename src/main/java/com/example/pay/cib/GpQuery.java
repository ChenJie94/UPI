/**
 * 网关支付订单查询接口
 *
 * @author xiezz
 * @version 1.1.2
 */
package com.example.pay.cib;

import com.example.pay.util.Configure;
import com.example.pay.util.DateTimeUtil;
import com.example.pay.util.SignAlgorithm;
import com.example.pay.util.Signature;

import java.util.Map;



public class GpQuery extends IPostService {

    private static final String SERVICE_NAME = "cib.epay.acquire.cashier.query";
    private static final String SERVICE_VER = "02";

    public String exec(Map<String, String> params) {

        if (!params.containsKey("order_date")) {
            params.put("order_date", DateTimeUtil.getDate());
        }
        params.put("appid", Configure.getAppid());
        params.put("service", SERVICE_NAME);
        params.put("ver", SERVICE_VER);
        params.put("timestamp", DateTimeUtil.getDateTime());
        params.put("sign_type", SignAlgorithm.get(SERVICE_NAME));
        params.put("mac", Signature.generateMAC(params));

        return txn(Configure.isDevEnv() ? Configure.GP_DEV_API : Configure.GP_PROD_API, params);
    }
}
