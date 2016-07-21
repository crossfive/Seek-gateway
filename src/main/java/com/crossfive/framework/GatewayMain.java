package com.crossfive.framework;

import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.crossfive.framework.common.CustomApplication;
import com.crossfive.framework.common.InitManager;
import com.crossfive.framework.common.config.Configuration;
import com.crossfive.framework.common.config.ServletConfig;
import com.crossfive.framework.server.IServer;
import com.crossfive.framework.server.ServerTest;

public class GatewayMain {

	private final static String applicationContextPath = "properties/applicationContext.xml";
	public static void main(String[] args) {
		try {
			JAXPConfigurator.configure("properties/proxool.xml", false);
			Log4jConfigurer.initLogging("properties/log4j.properties");
		} catch (ProxoolException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		CustomApplication application = new CustomApplication(new InitManager(), applicationContextPath);
		application.start();
//		AbstractApplicationContext ac = new FileSystemXmlApplicationContext(applicationContextPath);
//		ac.refresh();
		
//		try {
//			InitManager.init();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		ServletConfig sc = new ServletConfig();
//		sc.setHttpPort(Integer.valueOf(Configuration.getProperty(Configuration.HTTP_PORT)));
//		sc.setTcpPort(Integer.valueOf(Configuration.getProperty(Configuration.TCP_PORT)));
//		sc.setIp(Configuration.getProperty(Configuration.IP));
//		sc.setPackagePath(Configuration.getProperty(Configuration.PackagePath));
//		
//		IServer server = new ServerTest(sc, ac);
//		server.start();
	}
}
