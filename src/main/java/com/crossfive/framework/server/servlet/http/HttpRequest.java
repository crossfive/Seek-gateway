package com.crossfive.framework.server.servlet.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.crossfive.framework.common.ServerConstants;
import com.crossfive.framework.common.session.Push;
import com.crossfive.framework.common.session.ServerProtocol;
import com.crossfive.framework.common.session.Session;
import com.crossfive.framework.common.session.SessionManager;
import com.crossfive.framework.server.servlet.Request;
import com.crossfive.framework.server.servlet.Response;
import com.crossfive.framework.server.servlet.tcp.TcpPush;
import com.crossfive.framework.util.RequestUtil;

public class HttpRequest implements Request {
	
	private Map<String, Object> paramMap = new HashMap<String, Object>();
	
	private String command;
	
	private Channel channel;
	
	private String sessionId;
	
	private int requestId;
	
	private ChannelHandlerContext ctx;
	
	private Map<String, Cookie> cookies;
	
	private Map<String, String> headers;
	
	private Response response;
	
	private byte[] postContent;
	
	private byte[] getContent;
	
	private volatile boolean getParse;
	
	private volatile boolean postParse;
	
	private String url;
	
	private boolean longHttp = false;

	public HttpRequest (ChannelHandlerContext ctx,
			io.netty.handler.codec.http.HttpRequest httpRequest,
			byte[] getContent,
			byte[] postContent,
			String command,
			Map<String, Cookie> cookies,
			Map<String, String> headers,
			Response response, String uri) {
		this.ctx = ctx;
		this.channel = ctx.channel();
		this.response = response;
		this.cookies = cookies;
		this.getContent = getContent;
		this.postContent = postContent;
		this.url = uri;
		
		if ("gateway".equalsIgnoreCase(command)) {
			Object value = getParamterValues(ServerConstants.COMMAND);
			String[] values = (String[]) value;
			this.command = null == value ? null : values[0];
		}else {
			this.command = command;
		}
		
		this.sessionId = getCookieValue(ServerConstants.SESSION_KEY);
		SessionManager.getInstance().access(sessionId);
		
		if (ServerConstants.LONG_HTTP.equalsIgnoreCase(this.command)) {
			Session session = getSession(false);
			if (null != session) {
//				HttpPush push = new HttpPush(channel, new HttpChunkAction(ctx, httpRequest));
//				session.setPush(push);
			}
			this.longHttp = true;
		}
	} 
	
	@Override
	public Map<String, Object> getParamterMap() {
		parseParam();
		return paramMap;
	}

	private void parseParam() {
		paramGetParam(getContent);
		paramPostParam(postContent);
	}

	private void paramPostParam(byte[] bytes) {
		if (postParse || null == bytes) {
			return;
		}
		
//		String content = new String(bytes);
		if (bytes.length <= 0) {
			return;
		}
		
		if (null == paramMap) {
			this.paramMap = new HashMap<String, Object>();
		}
		
		try {
			RequestUtil.parseParamByJson(bytes, paramMap);
		}catch (Exception e) {
			System.err.println("parse post error");
		}
		
		postParse = true;
	}

	private void paramGetParam(byte[] bytes) {
		if (getParse || null == bytes) {
			return;
		}
		
		String content = new String(bytes);
		if (StringUtils.isEmpty(content)) {
			return;
		}
		
		if (null == paramMap) {
			this.paramMap = new HashMap<String, Object>();
		}
		
		try {
			RequestUtil.parseParam(content, paramMap);
		}catch (Exception e) {
			System.err.println("parse get error");
		}
		
		getParse = true;
	}
	

	@Override
	public Object getParamterValues(String key) {
		parseParam();
		return null == paramMap ? null : paramMap.get(key);
	}

	@Override
	public Session getSession() {
		return getSession(false);
	}

	@Override
	public Session getSession(boolean allowCreate) {
		System.out.println("now SessionId="+sessionId);
		Session session = SessionManager.getInstance().getSession(sessionId, allowCreate);
		if (allowCreate && (null != session && !session.getId().equals(sessionId))) {
			// 以前没有session 或者以前的session 失效了
			sessionId = session.getId();
			response.addCookie(new DefaultCookie(ServerConstants.SESSION_KEY, session.getId()));
			session.setPush(new TcpPush(channel));
		}
		if (session != null) {
			session.access();
		}
		
		return session;
	}

	@Override
	public Session getNewSession() {
		return getSession(true);
	}

	@Override
	public String getCommand() {
		return command;
	}

	@Override
	public Channel getChannel() {
		return channel;
	}

	@Override
	public int getRequestId() {
		return requestId;
	}

	@Override
	public void setSessionId(String sessionId) {
		ctx.attr(ServerConstants.SESSIONID).set(sessionId);
		this.sessionId = sessionId;
	}

	@Override
	public <T> void setAttachment(AttributeKey<T> key, T obj) {
		ctx.attr(key).set(obj);		
	}

	@Override
	public ServerProtocol getProtocol() {
		return ServerProtocol.HTTP;
	}

	@Override
	public String getHeader(String key) {
		return headers.get(key);
	}

	@Override
	public String getCookieValue(String key) {
		if (cookies != null) {
			Cookie cookie = cookies.get(key);
			if (null != cookie) {
				return cookie.value();
			}
		}
		return null;
	}

	@Override
	public Collection<Cookie> getCookies() {
		return cookies.values();
	}

	@Override
	public boolean isLongHttp() {
		return longHttp;
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		return (InetSocketAddress) channel.remoteAddress();
	}

	@Override
	public byte[] getContent() {
		if (postContent == null) {
			return getContent;
		}
		return postContent;
	}

	@Override
	public void pushAndClose(ByteBuf buffer) {
		
	}

	@Override
	public String getIp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Push newPush() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Object getAttachment(AttributeKey<T> key) {
		return ctx.attr(key);
	}

}
