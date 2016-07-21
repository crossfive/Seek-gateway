package com.crossfive.framework.common.interceptor;

import com.crossfive.framework.common.Constants;
import com.crossfive.framework.common.ServerConstants;
import com.crossfive.framework.common.session.Session;
import com.crossfive.framework.exception.ValidateException;
import com.crossfive.framework.server.mvc.rules.Rule;
import com.crossfive.framework.server.servlet.Request;

public class AuthenticationInterceptor implements Rule {

	@Override
	public void validate(Request request) throws ValidateException {
		// TODO Auto-generated method stub
		Session session = request.getSession();
		if (session == null || session.getAttribute(Constants.USER) == null) {
			throw new ValidateException(ServerConstants.VALIDATE_ERROR, "Not Login");
		}
	}
}
