package com.yougou.wfx.framework.jmx;

import com.yougou.wfx.framework.cache.LocalCache;
import com.yougou.wfx.framework.cache.LocalCacheUtil;


/**
 * 基于JMX动态配置缓存参数
 * @author zheng.x1
 * @date 2015年10月15日
 */
public class LocalCacheMBean {
      
    /**
     * 根据key获取本地缓存值
     * @param key
     * @return
     */
    public String getLocalCache(String key){
    	return LocalCacheUtil.getInstance().getLocalCacheForString(key);
    }
    
    
    /**
     * 获取本地缓存创建时间
     * @param key
     * @return
     */
    public String getCreateTime(String key){
    	return LocalCacheUtil.getInstance().getCreateTimeForString(key);
    }
    
    /**
     * 获取本地缓存失效时间
     * @param key
     * @return
     */
    public long getLocalExpiration(String key){
    	return LocalCacheUtil.getInstance().getLocalExpiration(key);
    }
    
    /**
     * 根据key清除本地缓存
     * @param key
     */
    public void delLocalCache(String key){
    	LocalCacheUtil.getInstance().delLocalCache(key);
    }
    
    
    /**
     * 批量清除本地缓存(字符串数组)
     * @param keys
     */
    public void delManyLocalCache(String[] keys){
    	LocalCacheUtil.getInstance().delManyLocalCache(keys);
    }
    
    /**
     * 清除本地所有缓存
     */
    public void delAllLocalCache(){
    	LocalCacheUtil.getInstance().delAllLocalCache();
    }

    /**
     * 获得所有缓存的命中统计
     * @return
     */
    public String getCacheStats(){
    	return LocalCacheUtil.getInstance().getCacheStats();
    }
	
    /**
     * 重新构建本地缓存对象
     * @param maximumSize
     */
    public void createLocalCache(long maximumSize){
    	new LocalCache(maximumSize);
    }
    
    /**
     * 设置本地缓存失效时间
     * @param key
     * @param localExpiration
     */
    public void updateLocalExpiration(String key,long localExpiration){
    	LocalCacheUtil.getInstance().updateLocalExpiration(key, localExpiration);
    }
    
    /**
     * 获取本地缓存剩余时间
     * @param key
     * @return
     */
    public long getRemainTime(String key){
    	return LocalCacheUtil.getInstance().getRemainTime(key);
    }
    
    /**
     * 获取本地缓存中key数量
     * @return
     */
    public long getKeysSize(){
    	return LocalCacheUtil.getInstance().getKeysSize();
    }
}
