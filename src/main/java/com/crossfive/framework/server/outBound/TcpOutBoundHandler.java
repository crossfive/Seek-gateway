package com.crossfive.framework.server.outBound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class TcpOutBoundHandler extends ChannelOutboundHandlerAdapter {

	@Override
	// 向client发送消息
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//		RequestMessage rm = (RequestMessage) msg;
//		ByteBuf msg2 = WrapperUtil.wrapper(rm.getCommand(), rm.getRequestId(), rm.getContent());
//		ctx.write(msg2);
//		ctx.flush();
		ctx.write(msg);
		ctx.flush();
	}
	
}
