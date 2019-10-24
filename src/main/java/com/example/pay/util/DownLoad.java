package com.example.pay.util;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import sun.misc.BASE64Decoder;

import com.cedarsoftware.util.io.JsonObject;

public class DownLoad {

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

	public static String postWithSoapMsg(String soapXML, String url)
			throws Exception {

		String result = "";
		OkHttpClient client = getClient();
		MediaType mediaType = MediaType.parse("application/xml;charset=utf-8");
		RequestBody body = RequestBody.create(mediaType, soapXML);
		Request request = new Request.Builder().url(url).post(body)
				.addHeader("content-type", "application/xml;charset=utf-8")
				.build();
		try (Response response = client.newCall(request).execute()) {
			result = new String(response.body().bytes());
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	public static String postWithJsonMsg(String jsonStr, String url,
			String clientId, String clientSecret) {

		String result = "";
		OkHttpClient client = getClient();
		MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
		RequestBody body = RequestBody.create(mediaType, jsonStr);
		Request request = new Request.Builder()
				.url(url)
				.post(body)
				.addHeader("content-type", "application/json;charset=utf-8")
				.addHeader("X-SPDB-Client-Id", clientId)
				.addHeader(
						"X-SPDB-SIGNATURE",
						CryptoSample.signBytes(jsonStr.getBytes(),
								clientSecret.getBytes())).build();

		try {

			Response response = client.newCall(request).execute();
			System.out.println("响应头:" + response.headers());
			String md5 = response.headers().get("X-SPDB-FilesMD5");
			String rspInfo = response.headers().get("rspInfo");
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] bytes = decoder.decodeBuffer(rspInfo);

			rspInfo = new String(bytes, "utf-8");
			System.out.println("rspInfo:" + rspInfo);
			String filename = (String) JsonParse.jsonMap(rspInfo).get(
					"qryRsltFlNm");
			filename = filename.substring(0, filename.lastIndexOf("#"));
			result = new String(response.body().bytes(), "GBK");
			String parentDir = null;
			try {
				URI uri = Thread.currentThread().getContextClassLoader()
						.getResource(".").toURI();
				parentDir = Paths.get(uri).toString() + "\\";
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			String filePath = parentDir + filename;
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filePath), "GBK"));
			out.write(result);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	private static Map<String, Properties> propertiesMap = new HashMap();

	public static String getValue(String fileName, String key) {
		Properties props = new Properties();
		try (FileInputStream is = new FileInputStream(fileName)) {
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null == props.get(key) ? "" : (String) props.get(key);
		// return (String) props.get(key);

	}

	public static void main(String[] args) {
		JsonObject obj = new JsonObject();
		String parentDir = null;
		try {
			URI uri = Thread.currentThread().getContextClassLoader()
					.getResource(".").toURI();
			parentDir = Paths.get(uri).toString() + "\\";
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		String configUrl = parentDir + "config.property";
		System.out.println(configUrl);
		obj.put("projectNo", getValue(configUrl, "projectNo"));
		obj.put("feePrjTp", getValue(configUrl, "feePrjTp"));
		obj.put("tranDate", getValue(configUrl, "tranDate"));
		obj.put("oldAskIttChannelId", getValue(configUrl, "oldAskIttChannelId"));
		obj.put("rqsSeqNo", getValue(configUrl, "rqsSeqNo"));
		obj.put("oldAskSysRplSqlNo", getValue(configUrl, "oldAskSysRplSqlNo"));
		
		String url = "https://etest2.spdb.com.cn/spdb/uat/api/focusCollPay/btchAgncTranFileDownload";
		
		System.out.println("请求数据:" + JsonUtil.ParseObject(obj));
		String result = postWithJsonMsg(JsonUtil.ParseObject(obj), url,
				"d83fa07c-0b7f-40e5-b53a-8c21f3c0437f",
				"nS6uP3oO5wK4dY8uX4aN0tT7hH6rU8jU7oW6aE7aI6iV6qO4qD");
		System.out.println(result);
	}

}
