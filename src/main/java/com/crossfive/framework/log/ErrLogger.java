package com.crossfive.framework.log;

import org.apache.log4j.Logger;


public class ErrLogger {
	
	public static Logger log = Logger.getLogger("errLogger");
	
	public static Logger getLogger() {
		return log;
	}
}
