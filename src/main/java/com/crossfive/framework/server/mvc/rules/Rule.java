package com.crossfive.framework.server.mvc.rules;

import com.crossfive.framework.exception.ValidateException;
import com.crossfive.framework.server.servlet.Request;

public interface Rule {

	public void validate(Request request) throws ValidateException;
}
