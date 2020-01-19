/**
 * 文件下载接口
 *
 * @author xiezz
 * @version 1.1.2
 */
package com.example.pay.util;


import com.example.pay.cib.IDownloadService;
import com.example.pay.cib.IPostService;

import java.util.Map;


public class DlFile extends IDownloadService {

    private static final String SERVICE_NAME = "cib.epay.acquire.download";
    private static final String SERVICE_VER = "01";

    @Override
    public Object download(Map<String, String> params) {

        params.put("appid", Configure.getAppid());
        params.put("service", SERVICE_NAME);
        params.put("ver", SERVICE_VER);
        params.put("timestamp", DateTimeUtil.getDateTime());
        params.put("sign_type", SignAlgorithm.get(SERVICE_NAME));
        params.put("mac", Signature.generateMAC(params));

        return download(Configure.isDevEnv() ? Configure.GP_DEV_API : Configure.GP_PROD_API, params);
    }

    @Override
    public String downloadToFile(Map<String, String> params, String filename) {

        params.put("appid", Configure.getAppid());
        params.put("service", SERVICE_NAME);
        params.put("ver", SERVICE_VER);
        params.put("timestamp", DateTimeUtil.getDateTime());
        params.put("sign_type", SignAlgorithm.get(SERVICE_NAME));
        params.put("mac", Signature.generateMAC(params));

        return download(Configure.isDevEnv() ? Configure.GP_DEV_API : Configure.GP_PROD_API, params, filename);
    }

    /**
     * 商户结算账户余额查询接口
     * @author wot_xiaowei_xdl
     *
     */
    public static class QueryBalance extends IPostService {

        private static final String SERVICE_NAME = "cib.epay.acquire.account.queryBalance";
        private static final String SERVICE_VER = "01";

        public String exec(Map<String, String> params) {
            params.put("appid", Configure.getAppid());
            params.put("service", SERVICE_NAME);
            params.put("ver", SERVICE_VER);
            params.put("timestamp", DateTimeUtil.getDateTime());
            params.put("sign_type", SignAlgorithm.get(SERVICE_NAME));
            params.put("mac", Signature.generateMAC(params));
            return txn(Configure.isDevEnv() ? Configure.EP_DEV_API : Configure.EP_PROD_API, params);
        }
    }
}
