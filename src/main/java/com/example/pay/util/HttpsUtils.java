package com.example.pay.util;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * Http发送post请求工具，兼容http和https两种请求类型
 */
public class HttpsUtils {

    /**
     * 请求超时时间
     */
    private static final int TIME_OUT = 120000;

    /**
     * Https请求
     */
    private static final String HTTPS = "https";

    /**
     * 发送JSON格式参数POST请求
     * 
     * @param url 请求路径
     * @param params JSON格式请求参数
     * @return 服务器响应对象
     * @throws IOException
     */
    public static Response post(String url, String params) throws IOException {
        return doPostRequest(url, null, null, params);
    }

    /**
     * 字符串参数post请求
     * 
     * @param url 请求URL地址
     * @param map
     * @param paramMap 请求字符串参数集合
     * @return 服务器响应对象
     * @throws IOException
     */
    public static Response post(String url,Map<String, String> paramMap) throws IOException {
        return doPostRequest(url, paramMap, null, paramMap.get("content").toString());
    }

    /**
     * 带上传文件的post请求
     * 
     * @param url 请求URL地址
     * @param paramMap 请求字符串参数集合
     * @param fileMap 请求文件参数集合
     * @return 服务器响应对象
     * @throws IOException
     */
    public static okhttp3.Response post(String url, Map<String, String> paramMap, Map<String, File> fileMap) throws IOException {
     //   return doPostRequest(url, paramMap, fileMap,null);// paramMap.get("content").toString()
        JSONObject JSON = new JSONObject();
        String filePath=fileMap.get("textFile").getPath();
        String clientId=paramMap.get("clientID").toString();
        byte[] clientSecretBytes =paramMap.get("clientSecret").getBytes();
        String MD5=paramMap.get("FilesMD5");
        Map maps = (Map)JSON.parse(paramMap.get("content").toString());

        return  sendFormData(url,filePath,clientId,clientSecretBytes,MD5,maps);
    }

    /**
     * 发送Get请求
     * 
     * @param url 请求URL
     * @return 服务器响应对象
     * @throws IOException
     */
    public static Response get(String url) throws IOException {
        if (null == url || url.isEmpty()) {
            throw new RuntimeException("The request URL is blank.");
        }

        // 如果是Https请求
        if (url.startsWith(HTTPS)) {
            getTrust();
        }
        Connection connection = Jsoup.connect(url);
        connection.method(Connection.Method.GET);
        connection.timeout(TIME_OUT);
        connection.ignoreHttpErrors(true);
        connection.ignoreContentType(true);

        Response response = connection.execute();
        return response;
    }



    public static okhttp3.Response sendFormData(String url, String reqParamsFile, String clientId,
                                                byte[] clientSecretBytes, String MD5,Map<String, String> paramMap) {
        System.out.println("------------------------------------------------");
        System.out.println("ApiTest \nURL:\n" + url);
        System.out.println("ApiTest \nfile:\n" + reqParamsFile);
        Properties props = new Properties();
        okhttp3.Response response =null;
        System.out.println(paramMap.get("tranDate"));
     /*   try (FileInputStream is = new FileInputStream(reqParamsFile)) {
            props.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        MediaType FILE_TYPE_TXT = null;
        okhttp3.MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

     /*   for (Entry<Object, Object> obj : props.entrySet()) {
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
        }*/

        String fileName = reqParamsFile.substring(reqParamsFile.lastIndexOf("\\") + 1);
        builder.addFormDataPart("textFile", fileName, RequestBody.create(FILE_TYPE_TXT, new File(reqParamsFile)));

        builder.addFormDataPart("projectNo",paramMap.get("projectNo"));
        builder.addFormDataPart("feePrjTp",paramMap.get("feePrjTp"));
        builder.addFormDataPart("tranTpCode",paramMap.get("tranTpCode"));
        builder.addFormDataPart("tranDate",paramMap.get("tranDate"));
        builder.addFormDataPart("oldIttChannelId",paramMap.get("oldIttChannelId"));
        builder.addFormDataPart("channel",paramMap.get("channel"));
        builder.addFormDataPart("totalNum",paramMap.get("totalNum"));
        builder.addFormDataPart("totalAmt",paramMap.get("totalAmt"));
        builder.addFormDataPart("fileName",fileName+"#"+MD5);

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
            response = client.newCall(request).execute();
        /*    System.out.println("Rsp:\n"
                    + new String(response.body().bytes(), "UTF-8"));*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  response;
    }

    private static OkHttpClient getClient() {
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




    /**
     * 执行post请求
     * 
     * @param url 请求URL地址
     * @param paramMap 请求字符串参数集合
     * @param fileMap 请求文件参数集合
     * @return 服务器响应对象
     * @throws IOException
     */
    private static Response doPostRequest(String url, Map<String, String> paramMap, Map<String, File> fileMap, String jsonParams) throws IOException {
        Map<String,String> map =new HashMap<String,String>();
        map.put("X-SPDB-Client-ID","d83fa07c-0b7f-40e5-b53a-8c21f3c0437f");
        map.put("X-SPDB-SIGNATURE",paramMap.get("sign").toString());
        if (null == url || url.isEmpty()) {
            throw new RuntimeException("The request URL is blank.");
        }

        // 如果是Https请求
        if (url.startsWith(HTTPS)) {
            getTrust();
        }

        Connection connection = Jsoup.connect(url);
        connection.method(Connection.Method.POST);
        connection.timeout(TIME_OUT);
        connection.ignoreHttpErrors(true);
        connection.ignoreContentType(true);

        // 收集上传文件输入流，最终全部关闭
        List<InputStream> inputStreamList = null;
        try {
            String boundary = "-----------------------------" + String.valueOf(new Date().getTime());
      //      connection.setRequestProperty("content-type", "multipart/form-data; boundary=" + boundary);
            connection.header("Content-Type", "multipart/form-data; boundary=" + boundary);
            // 添加文件参数
            if (null != fileMap && !fileMap.isEmpty()) {
                inputStreamList = new ArrayList<InputStream>();
                InputStream in = null;
                File file = null;
                Set<Entry<String, File>> set = fileMap.entrySet();
                for (Entry<String, File> e : set) {
                    file = e.getValue();
                    in = new FileInputStream(file);
                    inputStreamList.add(in);
                    connection.data(e.getKey(), file.getName(), in);
                }
                connection.requestBody(jsonParams);
                map.put("X-SPDB-FilesMD5",Md5Util.getFileMD5String(file));
                connection.headers(map);
            }

            // 设置请求体为JSON格式内容
            else if (null != jsonParams && !jsonParams.isEmpty()) {
                connection.header("Content-Type", "application/json;charset=UTF-8");
                connection.headers(map);
                connection.requestBody(jsonParams);
            }

            // 普通表单提交方式
            else {
                connection.header("Content-Type", "application/x-www-form-urlencoded");
            }

            // 添加字符串类参数
            if (null != paramMap && !paramMap.isEmpty()) {
                connection.data(paramMap);
            }
            Response response = connection.execute();
            System.out.println(new String(response.body().getBytes("UTF-8") ,"UTF-8"));
            return response;
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }

        // 关闭上传文件的输入流
        finally {
            if (null != inputStreamList) {
                for (InputStream in : inputStreamList) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 获取服务器信任
     */
    private static void getTrust() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[] { new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            } }, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}