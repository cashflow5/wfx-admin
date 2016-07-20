package com.yougou.wfx.framework.base;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.alibaba.fastjson.JSONObject;
import com.yougou.wfx.framework.ajax.JsonResult.StateCode;
import com.yougou.wfx.util.UserSecurityUtil;

public class BaseController {

	public static final String STATUS = "status";
	public static final String WARN = "warn";
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";

	protected final Logger logger = LoggerFactory.getLogger(super.getClass().getName());

	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		// 对于需要转换为Date类型的属性，使用DateEditor进行处理
		binder.registerCustomEditor(Date.class, new CustomerDateEditor());
	}

	/**
	 * 生成ajax结果
	 * 
	 * @param code
	 * @param msg
	 * @param resultObj
	 * @return
	 */
	protected String jsonResult(StateCode code, String msg, Object resultObj) {
		Assert.notNull(code, "StateCode不能为空");

		if (StringUtils.isBlank(msg)) {
			if (code == StateCode.ERROR) {
				msg = "系统发生异常，请稍后再试";
			} else if (code == StateCode.NOT_LOGIN) {
				msg = "未登录或者已失效，请重新登录";
			} else if (code == StateCode.INVALID) {
				msg = "非法访问";
			} else if (code == StateCode.SUCCESS) {
				msg = "success";
			}
		}
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("state", code.toString());
			jsonObj.put("msg", msg);
			jsonObj.put("data", resultObj == null ? "" : resultObj);
			return jsonObj.toString();
		} catch (Exception ex) {
			logger.error("ajax返回结果时发生异常", ex);
			return "{\"state\":\"error\",\"msg\":\"系统发生异常，请稍后再试！\",\"data\":null}";
		}
	}

	protected String jsonResult(StateCode code, String msg) {
		return jsonResult(code, msg, null);
	}
	
	protected String getSysUserName(){
		return UserSecurityUtil.getSystemUser().getLogin_name();
	}
	
	protected String getSysUserId(){
		return UserSecurityUtil.getSystemUser().getLogin_name();
	}
}
