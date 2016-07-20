package com.yougou.wfx;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.yougou.tools.common.utils.DateUtil;

public class TestApp {
	@Test
	public void testDate(){
		String d = DateUtil.convertDateToString("yyyy-MM-dd HH:mm:ss",new Date());
		String ds = d + " 00:00:00";
		String de = d + " 23:59:59";
		//System.out.println(ds);
		//System.out.println(de);
	}
	
	@Test
	public void testDate2(){
		Calendar dateS = Calendar.getInstance();
		dateS.set(Calendar.HOUR_OF_DAY, 0);
		dateS.set(Calendar.MINUTE, 0);
		dateS.set(Calendar.SECOND, 0);
		Date d = dateS.getTime();
		String dsss = DateUtil.convertDateToString("yyyy-MM-dd HH:mm:ss",d);
		System.out.println(dsss);
		Calendar datee = Calendar.getInstance();
		datee.set(Calendar.HOUR_OF_DAY, 23);
		datee.set(Calendar.MINUTE, 59);
		datee.set(Calendar.SECOND, 59);
		datee.set(Calendar.MILLISECOND, 0);
		Date de = datee.getTime();
		String adfdf = DateUtil.convertDateToString("yyyy-MM-dd HH:mm:ss",de);
		System.out.println(adfdf);
	}
}
