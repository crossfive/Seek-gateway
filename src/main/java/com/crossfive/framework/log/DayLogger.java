package com.crossfive.framework.log;

import org.apache.log4j.Logger;


public class DayLogger{
	
	public static Logger log = Logger.getLogger("dayLogger");
	
	public static Logger getLogger() {
		return log;
	}
}
