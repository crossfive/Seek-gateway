package com.crossfive.framework.server.servlet.rtsp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.rtsp.RtspDecoder;

import com.crossfive.framework.server.inbound.rtsp.RtspServerHandler;
import com.crossfive.framework.server.mvc.invoker.InvocationFactory;

public class RtspPipelineHandler extends ChannelInitializer<SocketChannel> {
	
	private InvocationFactory ac;
	
	public void init(InvocationFactory ac) {
		this.ac = ac;
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// TODO Auto-generated method stub
//		ch.pipeline().addLast("tcpOutHandler", new TcpOutBoundHandler());
//		ch.pipeline().addLast("tcpEncoder", new MessageEncoder());
		ch.pipeline().addLast("rtspDecoder", new RtspDecoder());
		ch.pipeline().addLast("rtspHandler", new RtspServerHandler(ac));  
		
	}
}


