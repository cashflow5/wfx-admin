package com.yougou.wfx.framework.cache;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.yougou.tools.common.utils.SpringContextHolder;
import com.yougou.wfx.framework.base.JsonRedisSerializable;

public class WFXRedisUtil {
	
	public enum WFXModuleType {

		COMMON("common"),
		ADDRESS("address"),
		AFTERSERVIVE("mobile"),
		CARDS("cards"),
		COMMENT("comment"),
		COUPON("coupon"),
		DEALERS("dealers"),
		FAVORITES("favorites"),
		INTEGRAL("integral"),
		LOGIN("login"),
		REGISTER("register"),
		MESSAGE("message"),
		MONITOR("monitor"),
		ACCOUNT("account"),
		SECURITY("security"),
		LOGISTICS("logistics");
		
		private String name;
		public String getName() {
			return name;
		}
		WFXModuleType(String name){
			this.name = name;
		}
	}
	
	private static RedisTemplate<String, String> redisTemplate =  SpringContextHolder.getBean("cacheRedisTemplate");
	private final static Logger logger = Logger.getLogger(WFXRedisUtil.class);
	
	private static ValueOperations<String,String> opsForValue(){
		redisTemplate.setValueSerializer(new JsonRedisSerializable<Object>());
		return redisTemplate.opsForValue();
	}
	
	public static String getKey(WFXModuleType moduleType,String key){
		return "member:"+ moduleType.getName() +":" + key;
	}
	
	public static String getString(WFXModuleType moduleType,String key){
		return opsForValue().get(getKey(moduleType,key));
	}
	
	public static int getInt(WFXModuleType moduleType,String key){
		int result = 0;
		String val = opsForValue().get(getKey(moduleType,key));
		if(StringUtils.isNotBlank(val)){
			try{
				result = Integer.valueOf(val);
			}catch(Exception ex){
				logger.error("[MemberRedisUtil]转换数据[String->int]时发生异常,val=" + val, ex);
			}
		}
		return result;
	}
	
	public static void set(WFXModuleType moduleType,String key,String value){
		long expire = redisTemplate.getExpire(getKey(moduleType,key));
		if(expire >-1){
			if(expire == 0){
				expire = 1;
			}
			opsForValue().set(getKey(moduleType,key), value,expire, TimeUnit.SECONDS);
		}else{
			opsForValue().set(getKey(moduleType,key), value);
		}
	}
	
	public static void set(WFXModuleType moduleType,String key,String value,int timeout,TimeUnit unit){
		opsForValue().set(getKey(moduleType,key), value, timeout, unit);
	}
	
	public static void set(WFXModuleType moduleType,String key,int value){
		long expire = redisTemplate.getExpire(getKey(moduleType,key));
		if(expire >-1){
			if(expire == 0){
				expire = 1;
			}
			opsForValue().set(getKey(moduleType,key), String.valueOf(value),expire, TimeUnit.SECONDS);
		}else{
			opsForValue().set(getKey(moduleType,key), String.valueOf(value));
		}
	}
	
	public static void set(WFXModuleType moduleType,String key,int value,int timeout,TimeUnit unit){
		opsForValue().set(getKey(moduleType,key), String.valueOf(value), timeout, unit);
	}
	
	public static void delete(WFXModuleType moduleType,String key){
		redisTemplate.delete(getKey(moduleType,key));
	}
	
	public static void expire(WFXModuleType moduleType,String key,int timeout,TimeUnit unit){
		redisTemplate.expire(getKey(moduleType,key), timeout, unit);
	}
}
