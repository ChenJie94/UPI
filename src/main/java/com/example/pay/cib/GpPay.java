/**
 * 网关支付交易
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




public class GpPay extends IRedirectService {

    private static final String SERVICE_NAME_1 = "cib.epay.acquire.cashier.netPay";
    private static final String SERVICE_VER = "01";
    private static final String SERVICE_CUR = "CNY";

    public String build(Map<String, String> params) {

        params.put("appid", Configure.getAppid());
        params.put("service", SERVICE_NAME_1);
        params.put("sign_type", SignAlgorithm.get(SERVICE_NAME_1));
        params.put("ver", SERVICE_VER);
        params.put("sub_mrch", Configure.getSub_mrch());
        params.put("cur", SERVICE_CUR);
        params.put("order_time", DateTimeUtil.getDateTime());
        params.put("timestamp", DateTimeUtil.getDateTime());
        params.put("mac", Signature.generateMAC(params));

        return buildRedirectFullPage(Configure.isDevEnv() ? Configure.GP_DEV_API : Configure.GP_PROD_API, params);
    }
}
