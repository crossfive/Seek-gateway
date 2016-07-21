package com.crossfive.framework.server.mvc.adaptor;

import com.crossfive.framework.server.servlet.Request;
import com.crossfive.framework.util.Utils;

public class RequestParamAdaptor extends Adaptor {

	protected String name;
	
	protected Class<?> type;
	
	public RequestParamAdaptor(String name, Class<?> type) {
		this.name = name;
		this.type = type;
	}
	
	@Override
	public Object get(Request request) {
		Object obj = request.getParamterValues(name);
		try {
			return Utils.cast(obj, type);
		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String getType() {
		return type.getName();
	}
}
