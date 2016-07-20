package com.yougou.wfx.admin.index.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yougou.wfx.framework.base.BaseController;
import com.yougou.wfx.util.HttpUtil;

@Controller
@RequestMapping
public class LoginController extends BaseController {

	@Value("#{wfxProps['cas.server.url']}")
	private String casServerUrl;

	@RequestMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String url = HttpUtil.getServiceRoot(request) + "/index.sc";
		return "redirect:" + casServerUrl + "/login?service=" + url;
	}

	/**
	 * 禁止访问
	 */
	@RequestMapping("/denied")
	public String denied(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "denied";
	}
	
	/**
	 * error
	 */
	@RequestMapping("/error")
	public String error(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "/error/500";
	}

	private void clearSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}

}
