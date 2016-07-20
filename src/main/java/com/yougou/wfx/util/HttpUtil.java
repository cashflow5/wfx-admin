package com.yougou.wfx.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public final class HttpUtil {

	/**
	 * 设置session
	 */
	public static void setSessionAttribute(String key, Object value, HttpServletRequest request) {
		request.getSession().setAttribute(key, value);
	}

	/**
	 * 获取session
	 */
	public static Object getSessionAttribute(String key, HttpServletRequest request) {
		return request.getSession().getAttribute(key);
	}

	/**
	 * 删除session
	 */
	public static void removeSessionAttribute(String key, HttpServletRequest request) {
		request.getSession().removeAttribute(key);
	}

	/**
	 * 获取输出信息
	 * 
	 * @throws IOException
	 * @Param obj 要输出到界面的信息
	 */
	public static void getOutPut(String obj, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		out.print(obj);
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		// updateby wuyang 获取ip如：172.20.1.54, 183.62.162.113 时，截取第二个
		if (StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
			String[] strIps = StringUtils.stripToEmpty(ip).split(",");
			if (strIps != null && strIps.length > 1) {
				return StringUtils.stripToEmpty(strIps[1]);
			}
		}

		return ip;

	}

	// 判断浏览器类型
	public static Integer getBrowingType(HttpServletRequest request) {
		String agent = request.getHeader("USER-AGENT");
		if (null != agent && -1 != agent.indexOf("MSIE")) {
			return 1;
		} else if (null != agent && -1 != agent.indexOf("Firefox")) {
			return 2;
		} else if (null != agent && -1 != agent.indexOf("Safari")) {
			return 3;
		} else {
			return 4;
		}
	}

	// 获取来源头信息，获取域名
	public static String getDomain(HttpServletRequest request) {
		String domain = null;

		String domainreferer = request.getHeader("referer"); // request.getRequestURL().toString();
																// //获取头信息（原请求的URL)
		if (domainreferer != null) {
			String replaceReferer = domainreferer.replace("http://", "");
			if (domainreferer != null && replaceReferer.indexOf("/") > 0) {
				domain = replaceReferer.substring(0, replaceReferer.indexOf("/"));// 获取原URL域名
			} else {
				domain = replaceReferer.substring(0, replaceReferer.length());
			}

		}
		return domain;
	}

	// 拦截器判断域名和客户端I地址（优先用域名判断，当域名为null则用IP地址判断）
	public static boolean IsTrackIntercepter(HttpServletRequest request) {
		String domainreferer = request.getHeader("referer");// 获取头信息（原请求的URL)
		String remoteIp = getIpAddr(request);// 获取客户端IP地址
		String serviceIp = request.getLocalAddr();// 获取服务端IP地址
		// String servicePath = request.getServerName() + ":" +
		// String.valueOf(request.getServerPort());
		boolean IptrueOrfalse = false; // 过滤域名和IP限制
		String domain = null;
		if (domainreferer != null) {
			String replaceReferer = domainreferer.replace("http://", "");
			if (domainreferer != null && replaceReferer.indexOf("/") != -1) {
				domain = replaceReferer.substring(0, replaceReferer.indexOf("/"));// 获取原URL域名
			} else {
				domain = replaceReferer;
			}
			if (domain != null && !domain.startsWith("www.yougou.com")) {
				IptrueOrfalse = true;
			}

		} else // 如果获取不到头信息（域名+path）
		{
			if (!remoteIp.equals(serviceIp)) // 如果客户端IP和服务端IP 不相等，此时算点击量和展示量
			{
				IptrueOrfalse = true;
			}
		}
		return IptrueOrfalse;

	}

	/**
	 * 获取服务器路径
	 * 
	 * @param request
	 * @return
	 */
	public static String getServiceRoot(HttpServletRequest request) {
		String url = request.getRequestURL().toString();
		return url.substring(0, url.lastIndexOf('/'));
	}

	/**
	 * 封装转码方法
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static String getEncoding(String EncondingName) throws UnsupportedEncodingException {
		return URLEncoder.encode(URLEncoder.encode(EncondingName, "UTF-8"), "UTF-8");
	}

	/**
	 * 获取完整的URL
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String getUrl(HttpServletRequest request) {
		StringBuilder urlBuilder = new StringBuilder();
		try {
			urlBuilder.append(getRootPath(request, false));
			// 格式化中文字符，防止出现乱码
			if (StringUtils.isNotBlank(request.getQueryString())) {
				String query = "?" + new String(request.getQueryString().trim().getBytes("ISO-8859-1"));
				urlBuilder.append(query);
			}
			return URLEncoder.encode(urlBuilder.toString(), "UTF-8");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获得根路径
	 * 
	 * @param request
	 * @param includePort
	 * @return
	 */
	public static String getRootPath(HttpServletRequest request, boolean includePort) {
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(request.getScheme());
		urlBuilder.append("://");
		urlBuilder.append(request.getServerName());
		if (includePort) {
			urlBuilder.append(":");
			urlBuilder.append(request.getServerPort());
		}
		String context = request.getContextPath();
		if (StringUtils.isBlank(context)) {
			context = getOriginalUri(request);
		}
		urlBuilder.append(context);
		return urlBuilder.toString();
	}

	/**
	 * 利用urlrewrite.jar加入的属性获得原始URI
	 * 
	 * @param request
	 * @return
	 */
	public static String getOriginalUri(HttpServletRequest request) {
		return request.getAttribute("javax.servlet.forward.request_uri").toString();
	}
}
