package com.crossfive.framework.common;

import io.netty.util.AttributeKey;

/**
 * 常量类
 * @author kira
 *
 */
public class ServerConstants {
	
	public final static int VALIDATE_ERROR = 100;
	public final static int INTERNAL_ERROR = 10000;
	
	public static final AttributeKey<String> SESSIONID = AttributeKey.valueOf("SessionId");
	
	public static final byte[] CROSSDOMAIN = ("<cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"*\" /></cross-domain-policy>\0").getBytes();

	public static final String COMMAND = "command";
	
	public static final String SESSION_KEY = "ticket";

	public static final String LONG_HTTP = "longhttp";
	
	public static final String CONTENT_TYPE_COMPRESS = "application/x-gzip-compressed";

//	public static final String CONTENT_TYPE = "application/json";
	public static final String CONTENT_TYPE = "text/plain";

}
