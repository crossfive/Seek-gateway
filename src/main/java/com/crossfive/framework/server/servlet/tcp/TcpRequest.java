package com.crossfive.framework.server.servlet.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.crossfive.framework.common.ServerConstants;
import com.crossfive.framework.common.enums.PARSE_TYPE;
import com.crossfive.framework.common.session.Push;
import com.crossfive.framework.common.session.ServerProtocol;
import com.crossfive.framework.common.session.Session;
import com.crossfive.framework.common.session.SessionManager;
import com.crossfive.framework.server.request.RequestMessage;
import com.crossfive.framework.server.servlet.Request;
import com.crossfive.framework.util.RequestUtil;

public class TcpRequest implements Request {
	
	/** paramMap */
	private Map<String, Object> paramMap = new HashMap<String, Object>();
	
	private String command;
	
	private Channel channel;
	
//	private ServletContext sc;
	
	private String sessionId;
	
	private int requestId;
	
	private ChannelHandlerContext ctx;
	
	private byte[] content;
	
	private volatile boolean parse;
	
	public TcpRequest(ChannelHandlerContext ctx, RequestMessage rm) {
		this.ctx = ctx;
		this.channel = ctx.channel();
		this.requestId = rm.getRequestId();
		this.command = rm.getCommand();
		this.content = rm.getContent();
		
		sessionId = rm.getSessionId();
		SessionManager.getInstance().access(sessionId);
	}
	
	@Override
	public Map<String, Object> getParamterMap() {
		parseParam(content, PARSE_TYPE.JSON);
		return paramMap;
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
	public Object getParamterValues(String key) {
		parseParam(content, PARSE_TYPE.JSON);
		return paramMap.get(key);
	}

	@Override
	public Session getSession() {
		return getSession(true);
	}

	@Override
	public Session getSession(boolean allowCreate) {
		Session session = SessionManager.getInstance().getSession(sessionId, allowCreate);
		if (allowCreate && (null != session && !session.getId().equals(sessionId))) {
			// 以前的session过期了，或者不存在session
			sessionId = session.getId();
			session.setPush(newPush());
		} 
		
		if (session != null) {
			// 摸一下session
			session.access();
		}
		return session;
	}

	@Override
	public Session getNewSession() {
//		throw new UnsupportedOperationException("tcp request can not support this operation!");
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
	public <T>Object getAttachment(AttributeKey<T> key) {
		return ctx.attr(key);
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
		return ServerProtocol.TCP;
	}

	@Override
	public String getHeader(String key) {
		throw new UnsupportedOperationException("tcp request can not support this operation!");
	}

	@Override
	public String getCookieValue(String key) {
		throw new UnsupportedOperationException("tcp request can not support this operation!");
	}

	@Override
	public Collection<Cookie> getCookies() {
		throw new UnsupportedOperationException("tcp request can not support this operation!");
	}

	@Override
	public boolean isLongHttp() {
		throw new UnsupportedOperationException("tcp request can not support this operation!");
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
		if (null != channel && channel.isWritable()) {
			ChannelFuture future = channel.write(buffer);
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public String getIp() {
		return getRemoteAddress().getAddress().getHostAddress();
	}

	@Override
	public Push newPush() {
		return new TcpPush(channel);
	}
	
}
