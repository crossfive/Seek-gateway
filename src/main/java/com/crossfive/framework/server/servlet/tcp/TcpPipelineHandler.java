package com.crossfive.framework.server.servlet.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import com.crossfive.framework.server.inbound.MessageDecoder;
import com.crossfive.framework.server.inbound.tcp.TcpServerHandler;
import com.crossfive.framework.server.mvc.invoker.InvocationFactory;

public class TcpPipelineHandler extends ChannelInitializer<SocketChannel> {
	
	private InvocationFactory ac;
	
	public void init(InvocationFactory ac) {
		this.ac = ac;
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// TODO Auto-generated method stub
//		ch.pipeline().addLast("tcpOutHandler", new TcpOutBoundHandler());
//		ch.pipeline().addLast("tcpEncoder", new MessageEncoder());
		ch.pipeline().addLast("tcpDecoder", new MessageDecoder());
		ch.pipeline().addLast("tcpHandler", new TcpServerHandler(ac));  
		
	}
}


