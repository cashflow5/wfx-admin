package com.yougou.wfx.framework.base;

/**
 * 需友好处理的异常类
 */
public class WFXFriendlyException extends Exception {
	private static final long serialVersionUID = 1L;

	public Exception ex = null;
    public String className = "";
    public WFXFriendlyException() {}

    public WFXFriendlyException(String message) {
        super(message);
    }

    public WFXFriendlyException(Exception ex) {
        super(ex);
        this.ex = ex;
    }

    public WFXFriendlyException(Exception ex, String className) {
        super(ex);
        this.ex = ex;
    }
}
