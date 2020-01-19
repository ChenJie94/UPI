/**
 * 服务签名算法
 *
 * @author xiezz
 * @version 1.1.2
 */
package com.example.pay.util.common;


import java.util.HashMap;
import java.util.Map;

public class SignAlgorithm {
    public final static String SIGN_TYPE_SHA1 = "SHA1";
    public final static String SIGN_TYPE_RSA = "RSA";
    private final static String DEFAULT_SIGN_TYPE = SIGN_TYPE_RSA;
    private final static Map<String, String> signAlg = new HashMap<String, String>() {{
        put("cib.epay.acquire.easypay.acctAuth", SIGN_TYPE_RSA);
        put("cib.epay.acquire.easypay.quickAuthSMS", SIGN_TYPE_RSA);
        put("cib.epay.acquire.checkSms", SIGN_TYPE_RSA);
        put("cib.epay.acquire.easypay.cancelAuth", SIGN_TYPE_RSA);
        put("cib.epay.acquire.easypay.acctAuth.query", SIGN_TYPE_RSA);
        put("cib.epay.acquire.easypay", SIGN_TYPE_RSA);
        put("cib.epay.acquire.easypay.query", SIGN_TYPE_RSA);
        put("cib.epay.acquire.easypay.refund", SIGN_TYPE_RSA);
        put("cib.epay.acquire.easypay.refund.query", SIGN_TYPE_RSA);
        put("cib.epay.acquire.authAndPay", SIGN_TYPE_RSA);
        put("cib.epay.acquire.easypay.quickAuth", SIGN_TYPE_RSA);

        put("cib.epay.acquire.cashier.netPay", SIGN_TYPE_RSA);
        put("cib.epay.acquire.cashier.quickNetPay", SIGN_TYPE_RSA);
        put("cib.epay.acquire.cashier.query", SIGN_TYPE_RSA);
        put("cib.epay.acquire.cashier.refund", SIGN_TYPE_RSA);
        put("cib.epay.acquire.cashier.refund.query", SIGN_TYPE_RSA);

        put("cib.epay.payment.getMrch", SIGN_TYPE_RSA);
        put("cib.epay.payment.pay", SIGN_TYPE_RSA);
        put("cib.epay.payment.get", SIGN_TYPE_RSA);

        put("cib.epay.acquire.settleFile", SIGN_TYPE_RSA);
        put("cib.epay.payment.receiptFile", SIGN_TYPE_RSA);
        
        //=======start====== 2017-11-14 add by xiaow 新增接口
        put("cib.epay.acquire.easypay.entrustAuth", SIGN_TYPE_RSA);
        put("cib.epay.acquire.easypay.entrustAuthQuery", SIGN_TYPE_RSA);
        put("cib.epay.acquire.easypay.entrustAuthSync", SIGN_TYPE_RSA);
        put("cib.epay.acquire.easypay.entrustCancelAuth", SIGN_TYPE_RSA);
        put("cib.epay.acquire.easypay.entrustQuickAuth", SIGN_TYPE_RSA);
        put("cib.epay.acquire.entrust.quickAuthSMS", SIGN_TYPE_RSA);
        put("cib.epay.acquire.quickpay.query", SIGN_TYPE_RSA);
        put("cib.epay.acquire.singleauth.query", SIGN_TYPE_RSA);
        put("cib.epay.acquire.singleauth.quickSingleAuth", SIGN_TYPE_RSA);
        put("cib.epay.acquire.batchQuickpay", SIGN_TYPE_RSA);
        put("cib.epay.acquire.batchQuickQueryApi", SIGN_TYPE_RSA);
        put("cib.epay.acquire.quickpay.corp.query", SIGN_TYPE_RSA);
        put("cib.epay.acquire.account.queryBalance", SIGN_TYPE_RSA);
        put("cib.epay.acquire.quickpay.corp.pay", SIGN_TYPE_RSA);
        put("cib.epay.acquire.quickpay", SIGN_TYPE_RSA);
        //=======end====== 2017-11-14 add by xiaow 新增接口
    }};

    public static String get(String service) {
        if (signAlg.containsKey(service))
            return signAlg.get(service);
        else
            return DEFAULT_SIGN_TYPE;
    }
}
