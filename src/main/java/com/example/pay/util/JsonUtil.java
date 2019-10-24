package com.example.pay.util;

import com.cedarsoftware.util.io.JsonObject;
import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class JsonUtil {
	
	private static Map<String, Object> config=new HashMap<String,Object>();
	
	public static String ParseObject(Object obj){
		config.put(JsonWriter.PRETTY_PRINT, true);
		config.put(JsonWriter.TYPE, false);
		config.put(JsonWriter.SKIP_NULL_FIELDS, true);
		return JsonWriter.objectToJson(obj,config);
	}
	
	public static JsonObject<String,Object> ParseJson(String jsonStr) throws UnsupportedEncodingException{
		JsonObject<String,Object> jo=new JsonObject<>();;	
	 
			InputStream ba=new ByteArrayInputStream(jsonStr.getBytes("UTF-8"));
			 jo=(JsonObject)JsonReader.jsonToJava(ba,config);
		 
		return jo;
	}

}
