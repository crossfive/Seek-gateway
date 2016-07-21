package com.crossfive.framework.common;

import org.springframework.context.support.AbstractApplicationContext;

import com.crossfive.framework.common.config.Configuration;
import com.crossfive.framework.common.config.ServletConfig;
import com.crossfive.framework.common.init.Initializable;
import com.crossfive.framework.common.interceptor.InterceptorInitManager;
import com.crossfive.framework.server.IServer;
import com.crossfive.framework.server.ServerTest;
import com.crossfive.framework.server.mvc.invoker.InvocationInitManager;


public class InitManager implements Initializable {

	private AbstractApplicationContext ac;
	
	public void init() throws Exception {
		Configuration.init();
		InterceptorInitManager.init();
		InvocationInitManager.init(ac);
		startServer();
	}


	private void startServer() {
		ServletConfig sc = new ServletConfig();
		sc.setHttpPort(Integer.valueOf(Configuration.getProperty(Configuration.HTTP_PORT)));
		sc.setTcpPort(Integer.valueOf(Configuration.getProperty(Configuration.TCP_PORT)));
		sc.setIp(Configuration.getProperty(Configuration.IP));
		sc.setPackagePath(Configuration.getProperty(Configuration.PACKAGE_PATH));
		
		IServer server = new ServerTest(sc, ac);
		server.start();		
	}


	@Override
	public void setContext(AbstractApplicationContext context) {
		this.ac = context;
	}
	
}
