/**
 * 
 */
package com.example.pay.util;

import com.cedarsoftware.util.io.JsonObject;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * HttpClientUtil
 */
public class HttpClientUtil { 
	
	private static final OkHttpClient client= new OkHttpClient.Builder()
					.connectionPool(new ConnectionPool(500, 5, TimeUnit.MINUTES))
					.connectTimeout(10000, TimeUnit.MILLISECONDS)
					.readTimeout(60000, TimeUnit.MILLISECONDS)
					.build();
	

	public static String postWithSoapMsg(String soapXML,String url) throws Exception{
		
		String result="";
		
		MediaType mediaType = MediaType.parse("application/xml;charset=utf-8");
		RequestBody body = RequestBody.create(mediaType,soapXML);
		Request request = new Request.Builder()
				  .url(url)
				  .post(body)
				  .addHeader("content-type", "application/xml;charset=utf-8")
				  .build();
		try(Response response = client.newCall(request).execute()){
			result=new String(response.body().bytes());
		}catch(Exception e){
			throw e;
		}		
		return result;
	}

	
	public static String postWithJsonMsg(String jsonStr,String url,String clientId,String clientSecret){
		
		String result="";
		MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
		RequestBody body = RequestBody.create(mediaType, jsonStr);
		Request request = new Request.Builder()
				.url(url)
				.post(body)
				.addHeader("content-type", "application/json;charset=utf-8")
				.addHeader("X-SPDB-Client-Id", clientId)
				.addHeader("X-SPDB-SIGNATURE", CryptoSample.signBytes(jsonStr.getBytes(), clientSecret.getBytes())).build();
		
		try{
			
		Response response = client.newCall(request).execute();
		
		result=new String(response.body().bytes(), "UTF-8");
		
		}catch(IOException e){
			e.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args){
		JsonObject obj=new JsonObject();
		obj.put("accountId", "CAnwFbOUCbwICfAVs5QJvC7J6gpVrdRPJWu1kTFXf5wnb5ceSl9OE5UK08x6fxT6Ntrt883m6AA");
		String url="http://127.0.0.1:9093/partner/account/queryAccount";		
		
		//String jsonSTR="{\"accountId\": \"CAnwFbOUCbwICfAVs5QJvGsibK6O0tRQq4fAKmf8SiblQnb5ceSl9OE1mibtn09U3FONtrt883m6AA\"}";
		System.out.println(JsonUtil.ParseObject(obj));
		String result=postWithJsonMsg(JsonUtil.ParseObject(obj),url,"d83fa07c-0b7f-40e5-b53a-8c21f3c0437f","nS6uP3oO5wK4dY8uX4aN0tT7hH6rU8jU7oW6aE7aI6iV6qO4qD");
		System.out.println(result);
	}
	
	
}
