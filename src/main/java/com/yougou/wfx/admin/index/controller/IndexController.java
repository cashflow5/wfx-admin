package com.yougou.wfx.admin.index.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yougou.wfx.framework.base.BaseController;

@Controller
@RequestMapping
public class IndexController extends BaseController {

	@RequestMapping("/index")
	public String index(HttpServletRequest request) {
		
		return "index";
	}
	
	@RequestMapping("/404")
	public String errorPage404(HttpServletRequest request) {
		return "/error/404";
	}
	
	@RequestMapping("/500")
	public String errorPage500(HttpServletRequest request) {
		return "/error/500";
	}
}
