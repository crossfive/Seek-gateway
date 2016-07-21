package com.crossfive.framework.server.mvc.invoker;

import org.springframework.context.support.AbstractApplicationContext;

import com.crossfive.framework.common.config.Configuration;


public class InvocationInitManager {

	public static void init(AbstractApplicationContext ac) throws Exception {
		String scanPath = Configuration.getProperty(Configuration.PACKAGE_PATH);
		InvocationFactory.getInstance().init(ac, scanPath);
	}
}
