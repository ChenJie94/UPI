package com.example.pay.assembly;
import com.example.pay.util.SpdBankUtil;
import okhttp3.*;

import javax.net.ssl.*;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
/**
 * ClassName: Upload
 * Description:
 * date: 2019/8/29 16:44
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

public class Upload {

    private OkHttpClient getClient() {
        SSLContext sslContext = null;
        X509TrustManager trustMgr = null;
        try {
            // 获取一个SSLContext实例 ，TLS安全套接字协议的实现
            sslContext = SSLContext.getInstance("TLS");
            // 管理X509证书，验证远程安全套接字
            trustMgr = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0,
                                               String arg1) throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] arg0,
                                               String arg1) throws CertificateException {
                }
            };
            // 初始化SSLContext实例
            sslContext.init(null, new TrustManager[] { trustMgr },
                    new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            // 加密算法报错
            e.printStackTrace();
        }
        // 主机名验证处理策略
        HostnameVerifier verifier = new HostnameVerifier() {

            // 验证主机名和服务器验证方案的匹配是可接受的(arg0表示：hostname主机名,arg1表示：session到主机的连接上使用的SSLSession)
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;// 如果主机名是可接受的，则返回true
            }
        };

        return new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), trustMgr)
                .hostnameVerifier(verifier).connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).build();
    }

    private byte[] readFile(String fileName) {
        byte[] result = null;
        try (InputStream is = new FileInputStream(fileName);
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = is.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
            result = os.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 请求报文
    public void sendJsonRequest(String url, String reqFile, String clientId,
                                byte[] clientSecretBytes, String MD5) throws Exception {

        System.out.println("------------------------------------------------");
        System.out.println("ApiTest \nURL:\n" + url);
        System.out.println("ApiTest \nfile:\n" + reqFile);
        Request request = null;
        if (reqFile != null) {
            byte[] reqBytes = readFile(reqFile);
            try {
                System.out.println("ReqInUTF-8:\n"
                        + new String(reqBytes, "gbk"));
            } catch (UnsupportedEncodingException e) {
                System.err.println("file content is not in utf-8");
                throw e;
            }
            System.out.println("ReqInBytes:\n"
                    + DatatypeConverter.printHexBinary(reqBytes));

            String signature = SpdBankUtil.signBytes(reqBytes,
                    clientSecretBytes);
            MediaType mediaType = MediaType
                    .parse("application/json;charset=gbk");
            RequestBody body = RequestBody.create(mediaType, reqBytes);
            request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("content-type", "application/json;charset=gbk")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("X-SPDB-Client-ID", clientId)
                    .addHeader("X-SPDB-FilesMD5", MD5)//合作方需将批量文件的MD5填充至报文头
                    .addHeader("X-SPDB-SIGNATURE", signature).build();
        } else {
            request = new Request.Builder().url(url)
                    .addHeader("cache-control", "no-cache")
                    .addHeader("X-SPDB-Client-ID", clientId).build();
        }
        try {
            OkHttpClient client = getClient();
            Response response = client.newCall(request).execute();
            System.out.println("Rsp:\n"
                    + new String(response.body().bytes(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 上传批量文件
     *
     * @param url
     *            //调用地址
     * @param reqParamsFile
     *            //文件名称
     * @param clientId
     *            //客户机标识
     * @param clientSecretBytes
     *            //客户机密钥
     * @param MD5
     *            //文件MD5校验值
     */
    public void sendFormData(String url, String reqParamsFile, String clientId,
                             byte[] clientSecretBytes, String MD5) {
        System.out.println("------------------------------------------------");
        System.out.println("ApiTest \nURL:\n" + url);
        System.out.println("ApiTest \nfile:\n" + reqParamsFile);
        Properties props = new Properties();
        try (FileInputStream is = new FileInputStream(reqParamsFile)) {
            props.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MediaType FILE_TYPE_TXT = null;
        okhttp3.MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        for (Entry<Object, Object> obj : props.entrySet()) {
            String key = (String) obj.getKey();
            String value = (String) obj.getValue();
            if ("@mediaType".equals(key)) {
                FILE_TYPE_TXT = MediaType.parse(value);
            } else {
                if (value.contains("@@")) {
                    String filePath = value.substring(2);
                    String fileName = filePath.substring(filePath
                            .lastIndexOf("/") + 1);
                    builder.addFormDataPart(key, fileName, RequestBody.create(FILE_TYPE_TXT, new File(filePath)));
                } else {
                    builder.addFormDataPart(key, value);
                }
            }
        }

        RequestBody requstBody = builder.build();

        byte[] contentBytes = null;
		/*try (ByteArrayOutputStream os = new ByteArrayOutputStream();
				Buffer buffer = new Buffer();) {
			requstBody.writeTo(buffer);
			buffer.copyTo(os);
			contentBytes = os.toByteArray();
			System.out.println("ReqInBytes:\n"
					+ DatatypeConverter.printHexBinary(contentBytes));
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
        contentBytes = MD5.getBytes();

        String signature = SpdBankUtil.signBytes(contentBytes,
                clientSecretBytes);

        Request request = new Request.Builder()
                .url(url)
                .post(requstBody)
                .addHeader("content-type", "multipart/form-data")
                .addHeader("X-SPDB-Client-ID", clientId)
                .addHeader("X-SPDB-FilesMD5", MD5)
                .addHeader("X-SPDB-SIGNATURE", signature)
                .build();

        try {
            OkHttpClient client = getClient();
            Response response = client.newCall(request).execute();
            System.out.println("Rsp:\n"
                    + new String(response.body().bytes(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void printErrMsg() {
        System.err
                .println("Usage : java -jar apitest.jar [method] [url] [clientId] [clientSecret] [reqFile]");
        System.err.println("           -method : 		jsonget|jsonpost|formdata");
        System.err.println("           -url : 		URL地址");
        System.err.println("           -clientId : 	 Client-ID");
        System.err.println("           -clientSecret : Client-Secret");
        System.err.println("           -MD5 :        FilesMD5");
        System.err.println("           -reqFile : JsonPost请求文件|FormData请求文件");
        System.exit(1);
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //http://10.134.34.197:9000/spdb/uat/api/
        String[] bb = {
                "formdata",
                "https://etest2.spdb.com.cn/spdb/uat/api/focusCollPay/btchAgncTranFileUpload",
                "d83fa07c-0b7f-40e5-b53a-8c21f3c0437f",
                "nS6uP3oO5wK4dY8uX4aN0tT7hH6rU8jU7oW6aE7aI6iV6qO4qD",
                "C:/Users/Administrator/Desktop/apiClient/apitestdata/fileUpload.properties",
                "eab3c61f80c3bcc37702b6e2e01960c6" };// MD5为文件实际MD5校验值
        args = bb;
        if (args.length < 3) {
            printErrMsg();
        }

        String method = args[0];
        String url = args[1];
        String clientId = args[2];
        String MD5 = args[5];

        Upload test = new Upload();
        if ("jsonget".equals(method)) {
            test.sendJsonRequest(url, null, clientId, null, MD5);
        } else if ("jsonpost".equals(method)) {
            if (args.length < 5) {
                printErrMsg();
            }

            String clientSecret = args[3];
            String reqFile = args[4];
            test.sendJsonRequest(url, reqFile, clientId,
                    clientSecret.getBytes(), MD5);
        } else if ("formdata".equals(method)) {
            if (args.length < 5) {
                printErrMsg();
            }
            String clientSecret = args[3];//"nS6uP3oO5wK4dY8uX4aN0tT7hH6rU8jU7oW6aE7aI6iV6qO4qD"
            String reqFile = args[4];//"C:\\Users\\Administrator\\Desktop\\apiClient\\apitestdata\\fileUpload.properties"
            test.sendFormData(url, reqFile, clientId, clientSecret.getBytes(),MD5);
        } else {
            printErrMsg();
            System.exit(0);
        }
    }

}

