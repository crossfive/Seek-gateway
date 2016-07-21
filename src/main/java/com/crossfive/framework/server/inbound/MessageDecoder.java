package com.crossfive.framework.server.inbound;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import com.crossfive.framework.server.request.RequestMessage;

/**
 * TCP解码
 * @author kira
 *
 */
public class MessageDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {

		if(in.readableBytes() < 4) {
			// 小于4个字节,无用包
			return;
		}
		// 获得数据长度的索引
		int dataLen = in.getInt(in.readerIndex());
		
		if (in.readableBytes() < dataLen + 4) {
			// 可读字节数 小于 索引数+包头，丢弃
			return;
		}
		// 读包头, 获得长度
//		int head = in.readInt();
//		ByteBuf buffer = Unpooled.buffer(head);
//		in.readBytes(buffer, head);
		
		in.skipBytes(4);
		
		RequestMessage rm = new RequestMessage();
//		// 假设命令最长32字节
//		rm.setCommand(new String(buffer.readBytes(32).array()).trim());
//		// 获取请求requestId，编码时返回前端 用于前端查找该包属于哪个请求
//		rm.setRequestId(buffer.readInt());
//		// 获得包内容content
//		rm.setContent(buffer.readBytes(buffer.readableBytes()).array());
//		out.add(rm);
//		
//		buffer.release();
		byte[] commandBytes = new byte[32];
		in.readBytes(commandBytes);
		rm.setCommand(new String(commandBytes).trim());
		rm.setRequestId(in.readInt());
		byte[] contentBytes = new byte[dataLen - 36];
		in.readBytes(contentBytes);
		rm.setContent(contentBytes);
		out.add(rm);
		
	}

}
