package com.yougou.wfx.framework.ajax;

import java.io.Serializable;

/**
 * 用户ajax交互
 * 
 * @author wuyang
 *
 */
public class JsonResult implements Serializable {

	private static final long serialVersionUID = -2822557637045738584L;

	/** 状态 */
	private String state;

	/** 描述or提示 */
	private String msg;

	/** 业务数据 */
	private Object data;

	public enum StateCode {
		SUCCESS("success"), // 正常响应
		ERROR("error"), // 发生错误
		INVALID("invalid"),// 无效请求，例如数据权限等
		NOT_LOGIN("not_login");// 未登录or会话已过期
		
		

		private String code;
		
		private StateCode(String code) {
			this.code = code;
		}
		
		@Override
		public String toString() {
			return this.code;
		}
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "JsonResult [state=" + state + ", msg=" + msg + ", data=" + data
				+ "]";
	}

}
