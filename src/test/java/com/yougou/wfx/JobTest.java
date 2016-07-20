/*package com.yougou.wfx;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yougou.wfx.order.api.background.IOrderBackgroundApi;

*//**
 * 
 * JOB测试
 * 
 * @author li.lq
 * @Date 2016年5月6日
 *//*
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
			locations={
					"classpath*:/applicationContext-bootstrap.xml",
					"classpath:applicationContext-dubbo-wfx-development.xml"
			}
)
public class JobTest {
	@Resource
	IOrderBackgroundApi orderBackgroundApi;
	//ApplicationContext c = new ClassPathXmlApplicationContext("classpath:applicationContext-bootstrap.xml");
	
	

	@Test
	public void test_closeOrders() {
		orderBackgroundApi.closeOrders();
	}

}
*/