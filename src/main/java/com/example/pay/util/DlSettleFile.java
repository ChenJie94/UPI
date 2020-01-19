/**
 * 对账文件下载接口
 *
 * @author xiezz
 * @version 1.1.2
 */
package com.example.pay.util;


import com.example.pay.cib.IDownloadService;

import java.util.Map;




public class DlSettleFile extends IDownloadService {

    private static final String SERVICE_NAME_ACQ = "cib.epay.acquire.settleFile";
    private static final String SERVICE_NAME_PYM = "cib.epay.payment.receiptFile";
    private String service_name;
    private String service_ver;

    public DlSettleFile() {
        this.service_name = SERVICE_NAME_ACQ;
        this.service_ver = "01";
    }

    public DlSettleFile(int type) {
        if (type != 0) {
            this.service_name = SERVICE_NAME_PYM;
            this.service_ver = "01";
        } else {
            this.service_name = SERVICE_NAME_ACQ;
            this.service_ver = "01";
        }
    }

    private void addCommonParams(Map<String, String> params) {

        params.put("appid", Configure.getAppid());
        params.put("service", this.service_name);
        params.put("ver", this.service_ver);
        params.put("timestamp", DateTimeUtil.getDateTime());
        params.put("sign_type", SignAlgorithm.get(service_name));
        params.put("mac", Signature.generateMAC(params));
    }

    public Object download(Map<String, String> params) {

        addCommonParams(params);
        if (SERVICE_NAME_ACQ.equals(this.service_name))
            return download(Configure.isDevEnv() ? Configure.GP_DEV_API : Configure.GP_PROD_API, params);
        else
            return download(Configure.isDevEnv() ? Configure.PY_DEV_API : Configure.PY_PROD_API, params);
    }

    public String downloadToFile(Map<String, String> params, String filename) {

        addCommonParams(params);
        if (SERVICE_NAME_ACQ.equals(this.service_name))
            return download(Configure.isDevEnv() ? Configure.GP_DEV_API : Configure.GP_PROD_API, params, filename);
        else
            return download(Configure.isDevEnv() ? Configure.PY_DEV_API : Configure.PY_PROD_API, params, filename);
    }
}
