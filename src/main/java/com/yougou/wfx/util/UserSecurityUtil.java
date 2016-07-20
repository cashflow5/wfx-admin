package com.yougou.wfx.util;

import org.apache.shiro.SecurityUtils;

import com.yougou.permission.remote.RemoteServiceInterface;
import com.yougou.permission.remote.UserRemote;
import com.yougou.tools.common.utils.SpringContextHolder;


public class UserSecurityUtil {
	
	/**
	 * 获取当前登录用户
	 */
	public static UserRemote getSystemUser() {
		Object principal = SecurityUtils.getSubject().getPrincipal();
		if (null != principal) {
			RemoteServiceInterface remoteService = SpringContextHolder.getBean(RemoteServiceInterface.class);
			return remoteService.getUser(String.valueOf(principal));
		}
		return null;
	}
	
	/**
	 * 获取当前登录用户Id
	 */
	public static String getSystemUserId() {
		Object principal = SecurityUtils.getSubject().getPrincipal();
		if (null != principal) {
			RemoteServiceInterface remoteService = SpringContextHolder.getBean(RemoteServiceInterface.class);
			UserRemote user = remoteService.getUser(String.valueOf(principal));
			if (user != null) {
				return user.getId();
			}
		}
		return null;
	}
	
	/**
	 * 获取当前登录用户name
	 */
	public static String getSystemUserName() {
		Object principal = SecurityUtils.getSubject().getPrincipal();
		if (null != principal) {
			RemoteServiceInterface remoteService = SpringContextHolder.getBean(RemoteServiceInterface.class);
			UserRemote user = remoteService.getUser(String.valueOf(principal));
			if (user != null) {
				return user.getUsername();
			}
		}
		return null;
	}
	
	public static UserRemote getUserInfo() {
		RemoteServiceInterface remoteService = SpringContextHolder.getBean("remoteService");
		Object principal = SecurityUtils.getSubject().getPrincipal();
		if (null != principal) {
			UserRemote user = remoteService.getUser(String.valueOf(principal));			
			return user;
		}
		return null;		
	}
}
