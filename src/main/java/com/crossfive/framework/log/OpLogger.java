package com.crossfive.framework.log;

import org.apache.log4j.Logger;


public class OpLogger{
	
	public static Logger log = Logger.getLogger("opLogger");
	
	public static Logger getLogger() {
		return log;
	}
}
