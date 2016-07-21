package com.crossfive.framework.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Configuration {
	
	public static final String HTTP_PORT = "server.http.port";
	
	public static final String TCP_PORT = "server.tcp.port";
	
	public static final String IP = "server.ip";
	
	public static final String SESSION_LISTENERS = "server.sessionListener";
	
	public static final String SESSION_ATTRIBUTE_LISTENERS = "server.sessionAttributeListener";
	
	public static final String DYNAMIC_UPDATE_PATH = "server.dynamicUpdate.path";

	public static final String PACKAGE_PATH = "server.package.path";
	
	public static final String DUBBO_REGISTRY_CENTER = "server.dubbo.registry.center";
	public static final String DUBBO_APPLICATION_NAME = "server.dubbo.application.name";
	public static final String DUBBO_MONITOR_ADDRESS = "server.dubbo.monitor.address";
	
	private static Map<String, String> propertiesMap = new HashMap<String, String>();
	
	public static void init() {
		File pFile = new File("properties/config.properties"); 
		FileInputStream   pInStream=null;
		try {
			pInStream = new FileInputStream(pFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
		}
		Properties p = new Properties();
		try {
			p.load(pInStream);       //Properties 对象已生成，包括文件中的数据
		} catch (IOException e) {
			e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
		}
		Enumeration enu = p.propertyNames();     //取出所有的key
		while(enu.hasMoreElements()) {
			String key = (String) enu.nextElement();
			propertiesMap.put(key, p.getProperty(key));
		}
	}
	
	public static String getProperty(String key) {
		return propertiesMap.get(key);
	}
	
}
