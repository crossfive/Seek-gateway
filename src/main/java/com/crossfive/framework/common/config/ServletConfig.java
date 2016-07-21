package com.crossfive.framework.common.config;

import java.util.List;

import com.crossfive.framework.common.session.listener.SessionAttributeListener;
import com.crossfive.framework.common.session.listener.SessionListener;

public class ServletConfig {

	public String ip;
	public int httpPort;
	public int tcpPort;
	public List<SessionListener> sessionListeners;
	public List<SessionAttributeListener> sessionAttributeListeners;
	public String packagePath;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getHttpPort() {
		return httpPort;
	}
	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}
	public int getTcpPort() {
		return tcpPort;
	}
	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}
	public List<SessionListener> getSessionListeners() {
		return sessionListeners;
	}
	public void setSessionListeners(List<SessionListener> sessionListeners) {
		this.sessionListeners = sessionListeners;
	}
	public List<SessionAttributeListener> getSessionAttributeListeners() {
		return sessionAttributeListeners;
	}
	public void setSessionAttributeListeners(
			List<SessionAttributeListener> sessionAttributeListeners) {
		this.sessionAttributeListeners = sessionAttributeListeners;
	}
	public String getPackagePath() {
		return packagePath;
	}
	public void setPackagePath(String packagePath) {
		this.packagePath = packagePath;
	}
	
}
