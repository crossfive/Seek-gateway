package com.crossfive.framework.server.mvc.adaptor;

import com.crossfive.framework.server.servlet.Request;

public class SessionAdaptor extends Adaptor {
	
	private String key;
	
	public SessionAdaptor(String key) {
		this.key = key;
	}
	
	@Override
	public Object get(Request request) {
		return request.getSession().getAttribute(key);
	}
}
