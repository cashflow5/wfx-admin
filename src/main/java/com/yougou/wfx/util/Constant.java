package com.yougou.wfx.util;

public class Constant {


	/**
	 * 系统用户登陆常量标识
	 */
	public static final String LOGIN_USER = "login_user";

	/**
	 * 登录用户资源集合
	 */
	public static final String LOGIN_USER_RESOURCES = "login_user_resources";

	/**
	 * 登录用户菜单
	 */
	public static final String LOGIN_USER_MENU = "login_user_menu";

	/**
	 * 未登录或session过期
	 */
	public static final String NOT_LOGIN = "notLogIn";

	/**
	 * 无访问权限
	 */
	public static final String NO_PRIVILEGE = "noPrivilege";

	/**
	 * 可访问
	 */
	public static final String ACCESSABLE = "accessable";

	public static final String SYSTEM_LEVEL = "0"; // 系统超级管理员
	
	/**
	 * 菜单是否pjax体验
	 */
	public static final String REQ_PARAM_IS_PJAX = "param_isPjax";
	
	/**
	 * 菜单是否pjax体验
	 */
	public static final String IS_PJAX = "isPjax";
	
	/**
	 *VPN访问地址 
	 */
	public static final String VPN_N = "0";//普通登录
	public static final String VPN_Y = "1";//VPN登录
	
	/* 操作日志模块名 */
	public static final String MODULE_COMMODITY = "COMMODITY";//商品
	public static final String MODULE_BASIC_SET = "BASIC_SET";//基础设置
	
	/*店铺状态*/
	public static final Integer SHOP_OPEN = 1;
	public static final Integer SHOP_CLOSE = 2;
	
	/* 会员种类 */
	public static final Integer MEMBER_NORMAL = 1;
	
}
