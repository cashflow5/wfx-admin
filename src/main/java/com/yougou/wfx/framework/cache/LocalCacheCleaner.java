package com.yougou.wfx.framework.cache;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yougou.wfx.framework.jmx.ConfigMBean;

/**
 * 本地缓存调度
 * @author zheng.x1
 * @date 2015年10月19日 
 */
@Service
@Lazy(false)
public class LocalCacheCleaner{

	protected final Logger logger = LoggerFactory.getLogger(LocalCacheCleaner.class);

	/**
	 * 每分钟执行一次
	 * @param pJointPoint
	 * @throws Throwable
	 */
	@Scheduled(fixedRate = 6000*1000)
	public void excuseJob() throws Throwable {
		logger.info("开始缓存清理......");
		Set<String> keys = LocalCache.getInstance().asMap().keySet();
		int num = 0;
		for(String key : keys){
			long createTime = 0;
			Date date_createTime = LocalCacheUtil.getInstance().getCreateTime(key); //获取创建时间
			if(null!=date_createTime){
				createTime = date_createTime.getTime();
			}
			long localExpiration = LocalCacheUtil.getInstance().getLocalExpiration(key);
			if(localExpiration>0 && createTime>0){
				long cTime = new Date().getTime() - createTime;
				if(cTime >= localExpiration*1000){
					LocalCacheUtil.getInstance().delLocalCache(key);
					num++;
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					logger.info("本地缓存过期被删除，key="+key+",创建时间："+sdf.format(new Date(createTime))+",删除时间："+sdf.format(new Date())+"，保存时效："+cTime+"秒");
				}
			}
			//每删除M条的时候开始停顿N毫秒
			int m = Integer.parseInt(ConfigMBean.getInstance().getConfigValueByKey("num", "100"));
			int n = Integer.parseInt(ConfigMBean.getInstance().getConfigValueByKey("time", "1000"));
			if(num%m==0){
				Thread.sleep(n);
			}
		}
		logger.info("结束缓存清理......");
	}
}