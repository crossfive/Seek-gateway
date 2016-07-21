package com.crossfive.framework.server.mvc.adaptor;

import com.crossfive.framework.server.servlet.Request;

public class RequestAdaptor extends Adaptor {
	
	@Override
	public Object get(Request request) {
		return request;
	}
}
