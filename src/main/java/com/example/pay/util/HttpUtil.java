package com.example.pay.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
/**
 * http请求工具
 *HttpClientUtil
 * @author 陈杰
 *2018年5月7日
 */
public class HttpUtil {
	/**
	 * 忽视证书HostName
	 */
	private static HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
		public boolean verify(String s, SSLSession sslsession) {
			System.out.println("WARNING: Hostname is not matched for cert.");
			return true;
		}
	};

	/**
	 * Ignore Certification
	 */
	private static TrustManager ignoreCertificationTrustManger = new X509TrustManager() {


		private X509Certificate[] certificates;


		@Override
		public void checkClientTrusted(X509Certificate certificates[],
									   String authType) throws CertificateException {
			if (this.certificates == null) {
				this.certificates = certificates;
				System.out.println("init at checkClientTrusted");
			}


		}


		@Override
		public void checkServerTrusted(X509Certificate[] ax509certificate,
									   String s) throws CertificateException {
			if (this.certificates == null) {
				this.certificates = ax509certificate;
				System.out.println("init at checkServerTrusted");
			}


//            for (int c = 0; c < certificates.length; c++) {
//                X509Certificate cert = certificates[c];
//                System.out.println(" Server certificate " + (c + 1) + ":");
//                System.out.println("  Subject DN: " + cert.getSubjectDN());
//                System.out.println("  Signature Algorithm: "
//                        + cert.getSigAlgName());
//                System.out.println("  Valid from: " + cert.getNotBefore());
//                System.out.println("  Valid until: " + cert.getNotAfter());
//                System.out.println("  Issuer: " + cert.getIssuerDN());
//            }


		}


		@Override
		public X509Certificate[] getAcceptedIssuers() {
			// TODO Auto-generated method stub
			return null;
		}


	};


	public static String sendHttpPost(String url, String body,String sign) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpPost httpPost = new HttpPost(url);

		httpPost.setHeader("X-SPDB-Client-ID","d83fa07c-0b7f-40e5-b53a-8c21f3c0437f");
		httpPost.setHeader("X-SPDB-SIGNATURE",sign);
		httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
		httpPost.setEntity(new StringEntity(body));
		CloseableHttpResponse response = httpClient.execute(httpPost);
	//	System.out.println(response.getStatusLine().getStatusCode() + "\n");set
		HttpEntity entity = response.getEntity();
		String responseContent = EntityUtils.toString(entity, "UTF-8"); 
	//	System.out.println(responseContent);
		response.close();
		httpClient.close();
		return responseContent;
		}



	public static String httpsPost(String urlString,String content,String sign) {


		ByteArrayOutputStream buffer = new ByteArrayOutputStream(512);
		try {


			URL url = new URL(urlString);


			/*
			 * use ignore host name verifier
			 */
			HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(5*1000);
			connection.setRequestProperty("Content-Type","application/json");
			connection.setRequestProperty("charset","UTF-8");
			connection.setRequestProperty("X-SPDB-Client-ID","d83fa07c-0b7f-40e5-b53a-8c21f3c0437f");
			connection.setRequestProperty("X-SPDB-SIGNATURE",sign);
			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			connection.connect();
			// Prepare SSL Context
			TrustManager[] tm = { ignoreCertificationTrustManger };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());


			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			connection.setSSLSocketFactory(ssf);

			InputStream reader = connection.getInputStream();
			byte[] bytes = new byte[512];
			int length = reader.read(bytes);


			do {
				buffer.write(bytes, 0, length);
				length = reader.read(bytes);
			} while (length > 0);


			// result.setResponseData(bytes);
			System.out.println(buffer.toString());
			reader.close();

			connection.disconnect();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
		String repString= new String (buffer.toByteArray());
		return repString;
	}
}
