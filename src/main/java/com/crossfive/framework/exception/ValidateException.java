package com.crossfive.framework.exception;

public class ValidateException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9032476434323860787L;
	private int code;
	
	public ValidateException(int code, String msg) {
		super(msg);
		this.code = code;
	}
	
	public ValidateException(int code, String msg, Throwable t) {
		super(msg, t);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
}
