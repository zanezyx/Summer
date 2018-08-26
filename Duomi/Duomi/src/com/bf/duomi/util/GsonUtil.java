package com.bf.duomi.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Gson解析工具类
 * @author Sunny
 *
 */
public class GsonUtil {
	

	private final static Gson gson=new Gson();
	
	private final static Gson pathGson=new GsonBuilder()
	
	.addSerializationExclusionStrategy(new ExclusionStrategy() {
		
		@Override
		public boolean shouldSkipField(FieldAttributes attrs) {
			
			return attrs.getName().equals("keyvalue")||attrs.getName().equals("rd");
		
		}
		
		@Override
		public boolean shouldSkipClass(Class<?> clazz) {
			// TODO Auto-generated method stub
			return false;
		}
	})
	.create();
	
	public static Gson getGsonInstance(){		
		return gson;
	}
	
	
	
	public static Gson getPathGsonInstance(){
		return pathGson;
	}
	


}
