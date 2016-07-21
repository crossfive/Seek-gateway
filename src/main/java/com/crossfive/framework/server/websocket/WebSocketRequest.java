package com.crossfive.framework.server.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.crossfive.framework.common.enums.PARSE_TYPE;
import com.crossfive.framework.common.session.Push;
import com.crossfive.framework.common.session.ServerProtocol;
import com.crossfive.framework.common.session.Session;
import com.crossfive.framework.common.session.SessionManager;
import com.crossfive.framework.server.request.RequestMessage;
import com.crossfive.framework.server.servlet.Request;
import com.crossfive.framework.server.servlet.Response;
import com.crossfive.framework.server.servlet.tcp.TcpPush;
import com.crossfive.framework.util.RequestUtil;

public class WebSocketRequest implements Request{
	
	private Map<String, Object> paramMap = new HashMap<String, Object>();
	
	private String command;
	
	private Channel channel;
	
	private String sessionId;
	
	private int requestId;
	
	private ChannelHandlerContext ctx;
	
	private Map<String, Cookie> cookies;
	
	private Map<String, String> headers;
	
	private Response response;
	
	private byte[] content;
	
	private volatile boolean parse;
	
	private String url;
	
	private boolean mark;
	
	public WebSocketRequest(ChannelHandlerContext ctx, RequestMessage rm,
			String ip, boolean mark) {
		this.ctx = ctx;
		this.channel = ctx.channel();
		this.requestId = rm.getRequestId();
		this.command = rm.getCommand();
		this.content = rm.getContent();
		this.url = ip;
		
		sessionId = rm.getSessionId();
		SessionManager.getInstance().access(sessionId);
	}

	private void parseParam(byte[] bytes, PARSE_TYPE type) {
		if (parse || bytes == null) {
			return;
		}
		
		try {
			if (type.equals(PARSE_TYPE.URL)) {
				RequestUtil.parseParam(new String(bytes), paramMap);
			}else {
				RequestUtil.parseParamByJson(bytes, paramMap);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		parse = true;
	}
	
	@Override
	public Map<String, Object> getParamterMap() {
		try {
			parseParam(content, PARSE_TYPE.URL);
		} catch (Exception e) {
			parseParam(content, PARSE_TYPE.JSON);
		}
		return paramMap;
	}

	@Override
	public Object getParamterValues(String key) {
		try {
			parseParam(content, PARSE_TYPE.URL);
		} catch (Exception e) {
			parseParam(content, PARSE_TYPE.JSON);
		}
		return paramMap.get(key);
	}

	@Override
	public Session getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Session getSession(boolean allowCreate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Session getNewSession() {
		// TODO Auto-generated method stub
		return null;
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
		this.sessionId = sessionId;
	}

	@Override
	public <T> void setAttachment(AttributeKey<T> key, T obj) {
		ctx.attr(key).set(obj);
	}

	@Override
	public ServerProtocol getProtocol() {
		return ServerProtocol.WEBSOCKET;
	}

	@Override
	public String getHeader(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCookieValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Cookie> getCookies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLongHttp() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		return (InetSocketAddress) channel.remoteAddress();
	}

	@Override
	public byte[] getContent() {
		return content;
	}

	@Override
	public void pushAndClose(ByteBuf buffer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getIp() {
		// TODO Auto-generated method stub
		return getRemoteAddress().getAddress().getHostAddress();
	}

	@Override
	public Push newPush() {
		return new TcpPush(channel);
	}

	@Override
	public <T> Object getAttachment(AttributeKey<T> key) {
		return ctx.attr(key);
	}
	
}
