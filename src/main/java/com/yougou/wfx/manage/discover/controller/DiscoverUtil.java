package com.yougou.wfx.manage.discover.controller;

import org.apache.commons.lang.StringUtils;

public class DiscoverUtil {
	public static String[] parseIds(String ids){
		if(StringUtils.isBlank(ids)){
			return null;
		}
		ids = ids.substring(1,ids.length()-1);
		String[] idArr = ids.split(",");
		for(int i=0;i<idArr.length;i++){
			idArr[i] = idArr[i].substring(1,idArr[i].length()-1);
			
			System.out.println("idArr:" + idArr[i]);
		}
		return idArr;
	}
}
