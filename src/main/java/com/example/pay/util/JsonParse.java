package com.example.pay.util;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

public class JsonParse {

    public static Map<String,Object> jsonMap(String buffer){
   	 Map<String,Object> map=new HashMap<String,Object>();
   	 if(buffer !=null && !"".equals(buffer)){
   		 JSONObject json=JSONObject.parseObject(buffer);//.fromObject(buffer);
   		 for (Object k:json.keySet()){
   			 Object v= json.get(k);
   			 if(v instanceof JSONArray){
   				List<Map<String,Object>> list =new  ArrayList<Map<String,Object>>();
   				Iterator<Object> it=((JSONArray) v).iterator();
   				while (it.hasNext()){
   					Object json2=it.next();
   					list.add(jsonMap(json2.toString()));
  					
   				}
   				map.put(k.toString(), list);
   			 }else{
   				 map.put(k.toString(), v);
   			 }    			    			 
   		 }
   		 return map; 
   		 }else{
   			 return null;
   		 }    	 		   	 
    }
    
    public static Map<String,Object> rspHeaderMap(String buffer) {
    	 Map<String,Object> mapHeader=new HashMap<String,Object>();
    	mapHeader=(Map<String, Object>)  jsonMap(buffer).get("RspHeader");
    	 return mapHeader;
    }
    public static Map<String,Object> rspBodyMap(String buffer){
    	Map<String,Object> mapBody=new HashMap<String,Object>();
    	mapBody=(Map<String, Object>)  jsonMap(buffer).get("RspBody");
		return mapBody;
    	
    }
    public static  void LoopResultMap(String buffer){
    	 List<Map<String,Object>> mapNN=new ArrayList<Map<String,Object>>();
    	mapNN= (List<Map<String,Object>>) rspBodyMap(buffer).get("LoopResult");		 
		 Map<String,Object> mapt=new HashMap<String,Object>();		 
			Iterator<Map<String, Object>> it=mapNN.iterator();
			while (it.hasNext()){
				mapt=it.next();
				
				System.out.println(mapt);
			}
			
			
			
    }
    

    public static void main(String[] args) {
    	String buffer = "{\"transNo\":\"04971904090170502458473852\",\"statusCode\":\"0002\",\"statusMsg\":\"统一社会信用代码到期日crCtfExpDt 不能为空\"}";
    	System.out.println(buffer);
    	JSONObject json=JSONObject.parseObject(buffer);
    	System.out.println(jsonMap(buffer).get("statusCode"));
	}
}
