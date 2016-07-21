package com.crossfive.framework.server.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.cookie.Cookie;

import java.io.IOException;
import java.util.Map;

import com.crossfive.framework.common.session.ServerProtocol;
import com.crossfive.framework.server.request.RequestMessage;
import com.crossfive.framework.server.servlet.Response;

public class WebSocketResponse implements Response {
	
	private Channel channel;
	
	private boolean close;

	public WebSocketResponse(Channel channel, RequestMessage message) {
		this.channel = channel;
	}

	@Override
	public Channel getChannel() {
		// TODO Auto-generated method stub
		return channel;
	}

	@Override
	public boolean isWritable() {
		return channel != null && channel.isWritable();
	}

	@Override
	public ChannelFuture write(Object obj) throws IOException {
		if (isWritable()) {
			ChannelFuture future = channel.write(obj);
			if (close) {
				future.addListener(ChannelFutureListener.CLOSE);
			}
		}
		return null;
	}

	@Override
	public ServerProtocol getProtocol() {
		// TODO Auto-generated method stub
		return ServerProtocol.WEBSOCKET;
	}

	@Override
	public void addCookie(Cookie cookie) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Cookie> getCookies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addHeader(String name, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] getContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStatus(HttpResponseStatus status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HttpResponseStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void markClose() {
		// TODO Auto-generated method stub
		this.close = true;
	}

	
}
