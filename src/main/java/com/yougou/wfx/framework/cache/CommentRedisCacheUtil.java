package com.yougou.wfx.framework.cache;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import com.yougou.tools.common.utils.SpringContextHolder;


public class CommentRedisCacheUtil {
	
	private static CommentRedisCacheUtil commentRedisCacheUtil;
	private static RedisTemplate<String, Object> redisTemplate;
	
	private CommentRedisCacheUtil(){};
	
	public static CommentRedisCacheUtil getInstance(){
		if(commentRedisCacheUtil == null){
			commentRedisCacheUtil = new CommentRedisCacheUtil();
			redisTemplate = SpringContextHolder.getBean("cacheRedisTemplate");
		}
		return commentRedisCacheUtil;
	} 
	
	public void put(String key ,Object value ,String []nameSpace ,long expiration){
//		for (String nps : nameSpace) {
//			if(StringUtils.isBlank(nps)){
//				continue;
//			}
			redisTemplate.opsForValue().set(key, value, expiration, TimeUnit.SECONDS);
//		}
	}
	
	public Object get(String key ,String []nameSpace){
		Object object = null;
//		for (String nps : nameSpace) {
//			if(StringUtils.isBlank(nps)){
//				continue;
//			}
			object = redisTemplate.opsForValue().get(key);
//		}
		return object;
	}

}
