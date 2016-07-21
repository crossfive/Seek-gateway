package com.crossfive.framework.exception;

public class ServletConfigException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2200154899384092741L;

	public ServletConfigException(String msg) {
		super(msg);
	}
	
	public ServletConfigException(String msg, Throwable t) {
		super(msg, t);
	}
}
