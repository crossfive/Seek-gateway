package com.crossfive.framework.server.servlet.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.Cookie;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.crossfive.framework.common.session.ServerProtocol;
import com.crossfive.framework.server.servlet.Response;

public class HttpRespone implements Response {
	
	private Channel channel;
	
	private HttpResponse httpResponse;
	
	private Map<String, String> headers;
	
	private Map<String, Cookie> cookies;
	
	private ByteArrayOutputStream outputStream;
	
	public HttpRespone(Channel channel) {
		this.channel = channel;
	}

	@Override
	public Channel getChannel() {
		return channel;
	}

	@Override
	public boolean isWritable() {
		return channel.isWritable();
	}

	@Override
	public ChannelFuture write(Object obj) throws IOException {
		if (channel.isWritable()) {
//			ByteBuf buf = ((ByteBuf)obj);
//			byte[] b = new byte[buf.capacity()];
//			buf.getBytes(0, getOutputStream(), buf.writableBytes());
//			buf.getBytes(0, getOutputStream(), buf.capacity());
			getOutputStream().write((byte[])obj);
		}
		return null;
	}

	@Override
	public ServerProtocol getProtocol() {
		return ServerProtocol.HTTP;
	}

	@Override
	public void addCookie(Cookie cookie) {
		getInternalCookies().put(cookie.name(), cookie);
	}

	@Override
	public Map<String, Cookie> getCookies() {
		return cookies;
	}

	@Override
	public Map<String, String> getHeaders() {
		return headers;
	}

	@Override
	public void addHeader(String name, String value) {
		getInternalHeaders().put(name, value);
	}

	@Override
	public byte[] getContent() {
		return getOutputStream().toByteArray();
	}

	private synchronized ByteArrayOutputStream getOutputStream() {
		if (outputStream == null) {
			outputStream = new ByteArrayOutputStream();
		}
		return outputStream;
	}

	@Override
	public synchronized void setStatus(HttpResponseStatus status) {
		if (httpResponse == null) {
			httpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);
			return;
		}
		httpResponse.setStatus(status);
	}

	@Override
	public synchronized HttpResponseStatus getStatus() {
		if (httpResponse == null) {
			return HttpResponseStatus.OK;
		}
		return httpResponse.getStatus();
	}

	@Override
	public void markClose() {
		// nothing todo
	}

	private synchronized Map<String, Cookie> getInternalCookies() {
		if (cookies == null) {
			cookies = new HashMap<String, Cookie>(16);
		}
		return cookies;
	}
	
	private synchronized Map<String, String> getInternalHeaders() {
		if (headers == null) {
			headers = new HashMap<String, String>(16);
		}
		return headers;
	}
}
