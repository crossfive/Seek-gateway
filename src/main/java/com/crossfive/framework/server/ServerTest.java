package com.crossfive.framework.server;

import org.springframework.context.support.AbstractApplicationContext;

import com.crossfive.framework.common.config.ServletConfig;

public class ServerTest implements IServer {  
	  
	private AbstractApplicationContext ac;
	
	private ServletConfig sc;
	
	public ServerTest(ServletConfig sc, AbstractApplicationContext ac) {
		this.sc = sc;
		this.ac = ac;
	}
	
	@Override
	public void start() {
		Server server = new Server(sc, ac);
		try {
			server.run();
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		
	}  
	  
	}  
