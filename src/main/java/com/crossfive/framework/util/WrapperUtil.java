package com.crossfive.framework.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.DeflaterOutputStream;

import com.crossfive.framework.common.ServerConstants;
import com.crossfive.framework.server.request.RequestMessage;
/**
 * 包装工具类
 * @author kira
 *
 */
public final class WrapperUtil {

	static boolean unsafe = false;
		
	private static ByteBufAllocator alloc = new PooledByteBufAllocator(unsafe);
	
	// 压缩
	public static boolean compress;
	
	public static final byte[] CRLF = "\r\n".getBytes();
	
	public static final byte[] EMPTY_BYTE = new byte[0];
	
	private WrapperUtil() {}

	public static ByteBuf wrapper (String command, int requestId, byte[] body) {
		byte[] commandBytes = command.getBytes();	// command
		commandBytes = Arrays.copyOf(commandBytes, 32);	// make bigger to 32bit
		
		byte[] bodyBytes = body;
		if (compress) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			DeflaterOutputStream dis = new DeflaterOutputStream(out);
			try {
				dis.write(body);
				dis.finish();
				dis.close();
				bodyBytes= out.toByteArray();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} finally {
				if (dis != null) {
					try{
						dis.close();
					}catch(IOException ioe) {
					}
				}
			}
		}
		int dataLen = 36 + bodyBytes.length;
//		ByteBuf buffer = Unpooled.buffer(dataLen + 4);
		ByteBuf buffer = alloc.buffer(dataLen + 4, dataLen + 4);
		buffer.writeInt(dataLen);	// 写包长
		buffer.writeBytes(commandBytes); // 写命令
		buffer.writeInt(requestId); // 写requestId
		buffer.writeBytes(bodyBytes); // 写内容
		return buffer;
	}
	
	public static BinaryWebSocketFrame wrapperWebSocketBinaryFrame (String command, int requestId, byte[] body) {
		byte[] commandBytes = command.getBytes();	// command
		commandBytes = Arrays.copyOf(commandBytes, 32);	// make bigger to 32bit
		
		byte[] bodyBytes = body;
		if (compress) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			DeflaterOutputStream dis = new DeflaterOutputStream(out);
			try {
				dis.write(body);
				dis.finish();
				dis.close();
				bodyBytes= out.toByteArray();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} finally {
				if (dis != null) {
					try{
						dis.close();
					}catch(IOException ioe) {
					}
				}
			}
		}
		
		int dataLen = 36 + bodyBytes.length;
//		ByteBuf buffer = Unpooled.buffer(dataLen + 4);
		ByteBuf buffer = alloc.buffer(dataLen + 4, dataLen + 4);
		buffer.writeInt(dataLen);	// 写包长
		buffer.writeBytes(commandBytes); // 写命令
		buffer.writeInt(requestId); // 写requestId
		buffer.writeBytes(bodyBytes); // 写内容
		
		BinaryWebSocketFrame frame = new BinaryWebSocketFrame(buffer);
		return frame;
	}
	
	public static TextWebSocketFrame wrapperWebSocketTextFrame (String command, int requestId, byte[] body) {
		byte[] bodyBytes = body;
		if (compress) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			DeflaterOutputStream dis = new DeflaterOutputStream(out);
			try {
				dis.write(body);
				dis.finish();
				dis.close();
				bodyBytes= out.toByteArray();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} finally {
				if (dis != null) {
					try{
						dis.close();
					}catch(IOException ioe) {
					}
				}
			}
		}
		
		int dataLen = bodyBytes.length;
//		ByteBuf buffer = Unpooled.buffer(dataLen + 4);
		ByteBuf buffer = alloc.buffer(dataLen, dataLen);
		buffer.writeBytes(bodyBytes); // 写内容
		
		TextWebSocketFrame frame = new TextWebSocketFrame(buffer);
		return frame;
	}
	
	public static RequestMessage wrapperRm (String command, int requestId, byte[] body) {
		return new RequestMessage(requestId, body, command, null);
	}

	public static String getContentType() {
		if (compress) {
			return ServerConstants.CONTENT_TYPE_COMPRESS;
		}
		return ServerConstants.CONTENT_TYPE;
	}
	
	public Object wapper(byte[] bytes) {
		ByteBuf buffer = alloc.heapBuffer(bytes.length);
		buffer.writeBytes(bytes);
		return buffer;
	}
}
