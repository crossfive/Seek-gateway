package com.crossfive.framework.server.servlet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Map;

import com.crossfive.framework.common.session.Push;
import com.crossfive.framework.common.session.ServerProtocol;
import com.crossfive.framework.common.session.Session;

public interface Request {
	
	public Map<String, Object> getParamterMap();
	
	public Object getParamterValues(String key);
	/**
	 * 获得已有的session，若session不存在，则返回null
	 * @return
	 */
	public Session getSession();
	
	public Session getSession(boolean allowCreate);
	/**
	 * 获得session, 如果session不存在，则创建并返回
	 * @return
	 */
	public Session getNewSession();
	
	public String getCommand();
	
	public Channel getChannel();
	
	public int getRequestId();
	
	public void setSessionId(String sessionId);
	
	public <T> void setAttachment(AttributeKey<T> key, T obj);
	
	public ServerProtocol getProtocol();
	
	public String getHeader(String key);
	
	public String getCookieValue(String key);
	
	public Collection<Cookie> getCookies();
	
	public boolean isLongHttp();
	
	public InetSocketAddress getRemoteAddress();
	
	public byte[] getContent();
	
	public void pushAndClose(ByteBuf buffer);
	
	public String getIp();
	
	public Push newPush();

	<T> Object getAttachment(AttributeKey<T> key);
}
