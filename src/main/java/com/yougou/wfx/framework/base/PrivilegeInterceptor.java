package com.yougou.wfx.framework.base;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.common.collect.Lists;
import com.yougou.permission.remote.AppMenuInfo;
import com.yougou.permission.remote.RemoteServiceInterface;
import com.yougou.permission.remote.ResourceRemote;
import com.yougou.permission.remote.UserRemote;
import com.yougou.tools.common.utils.SpringContextHolder;
import com.yougou.wfx.system.model.UserMenu;
import com.yougou.wfx.util.Constant;
import com.yougou.wfx.util.HttpUtil;
import com.yougou.wfx.util.UserMenuUtil;
import com.yougou.wfx.util.UserSecurityUtil;

@Service
public class PrivilegeInterceptor extends HandlerInterceptorAdapter {
	
	 @Autowired
	 private Properties wfxProps;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(StringUtils.equals(request.getHeader("X-Requested-With"), "XMLHttpRequest")){
			return true;
		}
		Object obj = HttpUtil.getSessionAttribute(Constant.LOGIN_USER_MENU, request);
		Object isPjax = request.getHeader("X-PJAX");
		if(obj == null){
			RemoteServiceInterface remoteService = SpringContextHolder.getBean("remoteService");
			UserRemote userRemote = UserSecurityUtil.getSystemUser();
			if(userRemote == null){
				return true;
			}
			String sysUserLoginName = userRemote.getLogin_name();
			List<ResourceRemote> lstResource = Lists.newArrayList();
			//root-1443 测试
			//root-1256 生产
			AppMenuInfo menuInfo = remoteService.getMenus(sysUserLoginName,"root-1256");
			if(menuInfo != null){
				for(ResourceRemote item : menuInfo.getAppMenuList()){
					lstResource.add(item);
				}
			}
			//老系统
			/**
			List<ResourceRemote> memberResource = Lists.newArrayList();
			AppMenuInfo menuInfo2 = remoteService.getMenus(sysUserLoginName,"root-1398");
			if(menuInfo2 != null){
				for(ResourceRemote item : menuInfo2.getAppMenuList()){
					memberResource.add(item);
				}
			}
			*/
			if(!lstResource.isEmpty()){
				List<UserMenu> sortMenu = UserMenuUtil.sortUserMenu(lstResource);
				HttpUtil.setSessionAttribute(Constant.LOGIN_USER_RESOURCES, sortMenu, request);
				HttpUtil.setSessionAttribute(Constant.LOGIN_USER_MENU, sortMenu, request);
				/**
				List<UserMenu> memberSortMenu = UserMenuUtil.sortUserMenu(memberResource);
				HttpUtil.setSessionAttribute("memberMenu", memberSortMenu, request);
				*/
			}else{
				HttpUtil.setSessionAttribute(Constant.LOGIN_USER_RESOURCES, null, request);
				HttpUtil.setSessionAttribute(Constant.LOGIN_USER_MENU, null, request);
			}
		}else{
			
		}
		
		//判断是否VPN登录
		if (wfxProps.get("vpn.host")!=null && String.valueOf(request.getRequestURL()).startsWith((String)wfxProps.get("vpn.host"))) {
        	request.getSession().setAttribute("accessMethod", Constant.VPN_Y);
        	request.setAttribute("omsHost", wfxProps.getProperty("oms.host.vpn"));
        	request.setAttribute("fmsHost", wfxProps.getProperty("fms.host.vpn"));
        	request.setAttribute("mmsHost", wfxProps.getProperty("mms.host.vpn"));
        	request.setAttribute("dmsHost", wfxProps.getProperty("dms.host.vpn"));
        	request.setAttribute("wmsHost", wfxProps.getProperty("wms.host.vpn"));
        	request.setAttribute("outsideHost", wfxProps.getProperty("outside.host.vpn"));
        	request.setAttribute("tmsHost", wfxProps.getProperty("tms.host.vpn"));
        } else {
        	request.getSession().setAttribute("accessMethod", Constant.VPN_N);
        	request.setAttribute("omsHost", wfxProps.getProperty("oms.host"));   	
        	request.setAttribute("fmsHost", wfxProps.getProperty("fms.host"));
        	request.setAttribute("mmsHost", wfxProps.getProperty("mms.host"));
        	request.setAttribute("dmsHost", wfxProps.getProperty("dms.host"));
        	request.setAttribute("wmsHost", wfxProps.getProperty("wms.host"));
        	request.setAttribute("outsideHost", wfxProps.getProperty("outside.host"));
        	request.setAttribute("tmsHost", wfxProps.getProperty("tms.host"));
        }
		request.setAttribute("jobHost", wfxProps.getProperty("job.host"));
		request.setAttribute(Constant.REQ_PARAM_IS_PJAX, isPjax);
		return true;
	}
}
