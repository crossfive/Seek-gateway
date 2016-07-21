package com.crossfive.framework.server.inbound.rtsp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpMessage;

import com.crossfive.framework.server.mvc.invoker.InvocationFactory;

public class RtspServerHandler extends ChannelInboundHandlerAdapter {

	private InvocationFactory servlet;
	
	public RtspServerHandler(InvocationFactory servlet) {
		this.servlet = servlet;
	}
	
	@Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception { 
		if (msg instanceof FullHttpMessage) {
			HttpMessage message = (HttpMessage)msg;
			DecoderResult result = message.getDecoderResult();
		}
	}
	
	@Override 
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception { 
        ctx.flush(); 
    }
}
