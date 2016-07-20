package com.yougou.wfx.framework.base;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * 友好异常处理
 */
public class DefaultHandlerExceptionResolver extends SimpleMappingExceptionResolver {

	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		String errMsg = ex.getMessage();

		if (shouldApplyTo(request, handler)) {
			ex = new WFXFriendlyException(ex);
			
			StackTraceElement[] stacks = ex.getStackTrace();
			StringBuilder stringBuilder = new StringBuilder();
			if(stacks != null && stacks.length > 0){
				stringBuilder.append("异常栈：").append("</br>");
				for (int i = 0 ; i < stacks.length ; i++) {
					stringBuilder.append("&nbsp;&nbsp;at ").append(stacks[i].toString());
					stringBuilder.append("</br>");
				}
			}
			
			StackTraceElement[] stacks1 = ex.getCause().getStackTrace();
			if(stacks1 != null && stacks1.length > 0){
				stringBuilder.append("</br>");
				stringBuilder.append("Caused by: ").append(ex.getCause().getClass()).append(": ").append(ex.getCause().getMessage());
				stringBuilder.append("</br>");
				for (int i = 0 ; i < stacks1.length ; i++) {
					stringBuilder.append("&nbsp;&nbsp;at ").append(stacks1[i].toString());
					stringBuilder.append("</br>");
				}
			}
			ex.printStackTrace();
			String curUrl = "";
			String strQueryString = request.getQueryString();
			String url = request.getRequestURL().toString();
			if(StringUtils.isNotBlank(strQueryString)){
				curUrl = url + "?" + strQueryString;
			}else{
				curUrl = url;
			}
			request.setAttribute("curUrl",curUrl);
			
			request.setAttribute("method",request.getMethod());
			
			Map<String, String[]> params = request.getParameterMap();
	        String queryString = "";
	        for (String key : params.keySet()) {
	            String[] values = params.get(key);
	            for (int i = 0; i < values.length; i++) {
	                String value = values[i];
	                queryString += key + "=" + value + "&";
	            }
	        }
	        // 去掉最后一个空格
	        if(StringUtils.isNotBlank(queryString)){
	        	queryString = queryString.substring(0, queryString.length() - 1);
	        }
	        request.setAttribute("params",queryString);
	        
	        String strHeader = "";
	        Enumeration<String> headerNames = request.getHeaderNames();
	        int count = 0;
	        while(headerNames.hasMoreElements()){
	            String name = (String) headerNames.nextElement();
//	            if(!StringUtils.equals(name, "Cookie") && !StringUtils.equals(name, "Upgrade-Insecure-Requests")
//	            		&& !StringUtils.equals(name, "Accept-Language")
//	            		&& !StringUtils.equals(name, "Connection")){
//	            	strHeader += name + ":" + request.getHeader(name)+"<br/>";
//	            }
	            if(count!=0){
	            	strHeader += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	            }
	            strHeader += name + ":" + request.getHeader(name)+"<br/>";
	            count ++;
	        }
	        request.setAttribute("headers",strHeader);
	        request.setAttribute("errMsg", errMsg);
	        
			request.setAttribute("errorMessageException",stringBuilder.toString().replace("异常栈", "<b>异常栈</b>"));
			if(StringUtils.equals(request.getHeader("X-Requested-With"), "XMLHttpRequest") || request instanceof DefaultMultipartHttpServletRequest){
				request.setAttribute("ajax_error",true);
			}
			try {
				response.sendError(500);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return doResolveException(request, response, handler, ex);
		} else {
			return null;
		}
	}

}
