/**
 * 使用URLConnection的通讯器
 *
 * @author xiezz
 * @version 1.1.2
 */
package com.example.pay.util.comm;

import com.example.pay.util.common.Configure;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;



public class HttpsPostRequest extends IRequestService {
    
    private final static int BUFFER_SIZE = 2048;

    public Object sendPost(String url, Map<String, String> params) throws KeyManagementException, NoSuchAlgorithmException, IOException {

        //拼装参数Map为String，含URLEncode
        StringBuffer paramstr = new StringBuffer();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            paramstr.append(entry.getKey())
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                    .append("&");
        }
        String param = paramstr.toString();
        return this.sendPost(url, param.substring(0, param.length() - 1));
    }

    public Object sendPost(String url, String params) throws IOException, KeyManagementException, NoSuchAlgorithmException {

        URL connurl = new URL(url);
        HttpURLConnection conn = null;
        Object ret = null;
        OutputStream outStream = null;
        InputStream inStream = null;

        logInfo("[request]:"+params);
        try {
            conn = (HttpURLConnection) connurl.openConnection();
            //测试环境忽略SSL证书验证
            if (Configure.isDevEnv() && connurl.getProtocol().equals("https")) {
                ignoreSSLVerify((HttpsURLConnection) conn);
            }

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            outStream = conn.getOutputStream();
            outStream.write(params.getBytes("UTF-8"));
            outStream.flush();

            inStream = conn.getInputStream();
            byte[] bin = readInputStream(inStream);

            if ("application/octet-stream".equals(conn.getContentType())) {
                ret = bin;
                logInfo("[response]:is application/octet-stream");
            } else {
                ret = new String(bin, "UTF-8");
                logInfo("[response]:"+ret);
            }
        } finally {
        
            if (inStream != null) inStream.close();
            if (outStream != null) outStream.close();
            if (conn != null) conn.disconnect();
        }
        return ret;
    }

    private static byte[] readInputStream(InputStream inStream) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = null;
        byte[] buffer = new byte[BUFFER_SIZE];
        int len = 0;
        try {
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            data = outStream.toByteArray();
        } finally {
            if (outStream != null)
                outStream.close();
        }
        return data;
    }

    private static void ignoreSSLVerify(HttpsURLConnection conn) throws NoSuchAlgorithmException, KeyManagementException {

        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }
        }};

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());

        conn.setSSLSocketFactory(sc.getSocketFactory());

        conn.setHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }
    
    private void logInfo(String content){
        //NOTE:生产上请自行打印日志
        System.out.println("[日志打印请自行修改]"+content);
    }
}
