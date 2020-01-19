/**
 * 配置类，这里存放所有配置，商户可以扩展该类<br />
 * <b>[注意]</b> 使用收付直通车API时，请务必先配置该类中的变量，如appid等
 *
 * @author xiezz
 * @version 1.1.2
 */
package com.example.pay.util;




public class Configure {

    // SDK版本号
    private static final String version = "1.1.3";

    // 商户号，格式如A0000001
    private static String appid = "Q0000974";

    // 商户私有密钥，该密钥需要严格保密，只能出现在后台服务端代码中，不能放在可能被用户查看到的地方，如html、js代码等
    // 在发送报文给收付直通车时，会使用该密钥进行签名
    // 在收到收付直通车返回的报文时，将使用该密钥进行验签
    private static String commKey = "CB3B2A81E3F04871969DD94602BE38B6";

    // 商户客户端证书路径，该证书需要严格保密，支持classpath路径或绝对路径
    // 在发送报文给收付直通车时，会使用该密钥进行签名（RSA算法方式）
    private static String mrch_cert = "key/appsvr_client 20200107Q0000974.pfx";
    // 以下证书参数一般为默认值，无需更改
    private static String mrch_cert_pwd = "123456";
    // 收付直通车服务器证书，RSA算法验签使用   
    private static String epay_cert_test = "key/appsvr_server_test.crt";
    private static String epay_cert_prod = "key/appsvr_server_prod.crt";

    
    // 二级商户名称，可为空
    private static String sub_mrch = "SDK-JAVA测试商户";

    // 本机IP地址，POST直联交易时使用
    private static String ip = IPUtil.getLocalIp();

    // 是否为开发测试模式，true时将连接测试环境，false时将连接生产环境
    private static boolean devEnv = true;

    // 是否验签，true验证应答报文签名，false不验证签名，建议不要修改此项为false，生产环境请务必更改为true
    private static boolean needChkSign = false;

    // HTTP POST通讯类，如果需要使用自己的类库，请修改该值
    public static String httpsRequestClassName = "com.cib.epay.sdk.comm.HttpsPostRequest";

    // 代付是否为批量-批量模式
    private static boolean isEnBatch2Batch = false;

    // 代收是否为批量-批量模式
    private static boolean isAcqEnBatch2Batch = true;

    // 快捷支付API地址，测试环境地址可根据需要修改
    public static final String EP_PROD_API = "https://pay.cib.com.cn/acquire/easypay.do";
    public static final String EP_DEV_API = "https://220.250.30.210:37031/acquire/easypay.do";

    
    // 网关支付API地址，测试环境地址可根据需要修改
    public static final String GP_PROD_API = "https://pay.cib.com.cn/acquire/cashier.do";
    public static final String GP_DEV_API = "https://220.250.30.210:37031/acquire/cashier.do";

    // 智能代付API地址，测试环境地址可根据需要修改
    public static final String PY_PROD_API = "https://pay.cib.com.cn/payment/api";
    public static final String PY_DEV_API = "https://220.250.30.210:37031/payment/api";

    
    // 以下四个变量为SDK出现错误时，返回的字符串，可以根据需要自己设置（注意不是服务端返回的）
    // 通讯失败时返回的报文
    public static String TXN_ERROR_RESULT = "{\"errcode\":\"EPAY_29001\",\"errmsg\":\"[EPAY_29001]通讯错误或超时，交易未决\"}";
    // 系统异常时返回的报文
    public static String SYS_ERROR_RESULT = "{\"errcode\":\"EPAY_29099\",\"errmsg\":\"[EPAY_29099]未知错误，请检查是否为最新版本SDK或是否配置错误\"}";
    // 对账文件下载，写入文件异常返回报文
    public static String FILE_ERROR_RESULT = "{\"errcode\":\"EPAY_29002\",\"errmsg\":\"[EPAY_29002]写入对账文件失败\"}";
    // 验签失败
    public static String SIGN_ERROR_RESULT = "{\"errcode\":\"EPAY_29098\",\"errmsg\":\"[EPAY_29098]应答消息验签失败，交易未决\"}";
    // 对账文件下载，下载成功返回报文
    public static String SUCCESS_RESULT = "{\"errcode\":\"EPAY_00000\",\"errmsg\":\"[EPAY_00000]下载成功\"}";

    public static String getAppid() {
        return appid;
    }

    public static void setAppid(String appid) {
        Configure.appid = appid;
    }

    public static String getCommKey() {
        return commKey;
    }

    public static void setCommKey(String commKey) {
        Configure.commKey = commKey;
    }

    public static String getSub_mrch() {
        return sub_mrch;
    }

    public static void setSub_mrch(String sub_mrch) {
        Configure.sub_mrch = sub_mrch;
    }

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        Configure.ip = ip;
    }

    public static boolean isDevEnv() {
        return devEnv;
    }

    public static void setDevEnv(boolean isDevEnv) {
        Configure.devEnv = isDevEnv;
    }

    public static String getVersion() {
        return version;
    }

    public static String getMrch_cert() {
        return mrch_cert;
    }

    public static void setMrch_cert(String mrch_cert) {
        Configure.mrch_cert = mrch_cert;
    }

    public static String getMrch_cert_pwd() {
        return mrch_cert_pwd;
    }

    public static void setMrch_cert_pwd(String mrch_cert_pwd) {
        Configure.mrch_cert_pwd = mrch_cert_pwd;
    }

    public static String getEpay_cert_test() {
        return epay_cert_test;
    }

    public static void setEpay_cert_test(String epay_cert_test) {
        Configure.epay_cert_test = epay_cert_test;
    }

    public static String getEpay_cert_prod() {
        return epay_cert_prod;
    }

    public static void setEpay_cert_prod(String epay_cert_prod) {
        Configure.epay_cert_prod = epay_cert_prod;
    }

    public static boolean isNeedChkSign() {
        return needChkSign;
    }

    public static void setNeedChkSign(boolean needChkSign) {
        Configure.needChkSign = needChkSign;
    }

    public static boolean isAcqEnBatch2Batch() {
        return isAcqEnBatch2Batch;
    }

    public static void setAcqEnBatch2Batch(boolean isAcqEnBatch2Batch) {
        Configure.isAcqEnBatch2Batch = isAcqEnBatch2Batch;
    }

    public static boolean isEnBatch2Batch() {
        return isEnBatch2Batch;
    }

    public static void setEnBatch2Batch(boolean isEnBatch2Batch) {
        Configure.isEnBatch2Batch = isEnBatch2Batch;
    }
}
