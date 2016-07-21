package com.crossfive.framework.server.servlet.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import com.crossfive.framework.server.inbound.http.HttpServerHandler;
import com.crossfive.framework.server.mvc.invoker.InvocationFactory;

public class HttpPipelineHandler extends ChannelInitializer<SocketChannel> {
	
	private InvocationFactory servlet;
	
	public void init(InvocationFactory ac) {
		this.servlet = ac;
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码  
		ch.pipeline().addLast("httpEncoder", new HttpResponseEncoder());  
        // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
		ch.pipeline().addLast("httpDecoder", new HttpRequestDecoder());
		ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536)); 
		ch.pipeline().addLast("httpHandler", new HttpServerHandler(servlet));  
		
	}
}


