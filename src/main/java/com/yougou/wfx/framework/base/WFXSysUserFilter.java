package com.yougou.wfx.framework.base;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.yougou.permission.core.SysUserFilter;

public class WFXSysUserFilter extends SysUserFilter{
	@Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
    	String loginUrl = getLoginUrl();
    	String queryStr = WebUtils.toHttp(request).getQueryString();
    	if(null!=queryStr){
    		queryStr = WebUtils.decodeRequestString(WebUtils.toHttp(request), queryStr);
		}
    	defaulte(request,loginUrl,queryStr);
    	loginUrl = getLoginUrl();
    	
    	if(StringUtils.equals(((HttpServletRequest)request).getHeader("X-Requested-With"), "XMLHttpRequest") || request instanceof DefaultMultipartHttpServletRequest){
			((HttpServletResponse)response).sendError(401);
		}else{
			WebUtils.issueRedirect(request, response, loginUrl);
		}
    }
}
