package com.crossfive.framework.server.mvc.view;

public interface Result<T> {

	String getViewName();
	
	T getResult();
	
}
