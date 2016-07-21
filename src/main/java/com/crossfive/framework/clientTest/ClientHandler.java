package com.crossfive.framework.clientTest;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.crossfive.framework.server.request.RequestMessage;

/**
 * 客户端处理器
 * @author kira
 *
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {  
	@Override  
	public void channelActive(ChannelHandlerContext ctx) throws Exception {  
//		 String command = "user@getInfo";
//		 JSONObject jo = new JSONObject();
//         jo.put("username", "gaoyf2");
//         jo.put("password", "1234");
//         jo.put("id", 1);
//         
//         byte[] body = jo.toJSONString().getBytes();
//         ByteBuf buf = WrapperUtil.wrapper(command, 1, body);
//         ctx.write(buf);
//         ctx.flush();
		System.out.println("client active");
	}
	
	@Override  
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
//		if (msg instanceof RequestMessage) {
//			RequestMessage rm = (RequestMessage) msg;
//			System.out.println("command"+ rm.getCommand());
//			System.out.println("result"+ rm.getContent());
//		}
		System.out.println("client read");
		RequestMessage m = (RequestMessage) msg;
		System.out.println(new String(m.getContent()));
	}
	
	
}
