package com.moretv.common.utils; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.type.TypeFactory;

/** 
 * 基于Jackson的Json管理器
 *
 * author:Amandayang
 * 
 * Date 2014-3-11 下午02:29:41
 */
public class JacksonUtils {
private static ObjectMapper om;
	
	public static ObjectMapper getObjectMapper(){
		if (om == null) {
			om = new ObjectMapper();
			//设置输出包含的属性
			om.getSerializationConfig().setSerializationInclusion(Inclusion.ALWAYS);
			//设置输入时忽略JSON字符串中存在而Java对象实际没有的属性
			om.getDeserializationConfig().set(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}
		return om;
	}
	
	/**
	 * 把Java对象转为Json格式的字符串
	 */
	public static String objectToJsonString(Object obj){
		String value = null;
		
		try {
			value = getObjectMapper().writeValueAsString(obj);
			value = new String(value.getBytes("UTF-8"), "UTF-8");
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println(value);
		return value;
	}
	
	
	/**
	 * 把Json格式的字符串转为Java对象
	 */
	public static <T> T jsonStringToObject(String json, Class<T> classType){
		
		T obj = null;
		try {
			//json = convertString(json, 2);
			obj = getObjectMapper().readValue(json, classType);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * 把Json格式的字符串转为Java对象List集合
	 */
	public static <T> List<T> jsonStringToList(String json, Class<T> lType){
		List<T> objs = new ArrayList<T>();
		try {
			objs = getObjectMapper().readValue(json, TypeFactory.collectionType(List.class, lType));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return objs;
	}
	
	
	public static void main(String[] args) {
//		TestBean obj = new TestBean();
//		obj.setName("周杰伦");
//		obj.setAge(33);
//		obj.setEmail("jaychou@superstar.com");
//		
//		java.util.List<String> names = new java.util.ArrayList<String>();
//		names.add("蔡依林(Jolin Tsai)");
//		names.add("侯佩岑(Patty Hou)");
//		names.add("林志玲(Chiling)");
//		obj.setGirlFriends(names);
//		String a = JsonManager.objectToJsonString(obj);
//		System.out.println(a);
//		
//		System.out.println(convertString(a, 1));
		
	}

}
 
