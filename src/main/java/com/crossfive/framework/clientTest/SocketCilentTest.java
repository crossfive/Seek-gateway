package com.crossfive.framework.clientTest;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.alibaba.fastjson.JSONObject;
import com.crossfive.framework.util.WrapperUtil;

public class SocketCilentTest {

//	public static Channel ch = getChannel();
	
	 public static void main(String[] args) throws Exception {      
	 
//		 send2();
		 get();
		 
		 
//		 send();
//		 String command = "user@getInfo";
//		 JSONObject jo = new JSONObject();
//         jo.put("username", "gaoyf2");
//         jo.put("password", "1234");
//         jo.put("id", 1);
//         
//         byte[] body = jo.toJSONString().getBytes();
         
//		 
//		 for (int i = 0; i < 10; i++) {
//			 ByteBuf buf = WrapperUtil.wrapper(command, 1, body);
//			 sendMsg(ch, buf);
//			 Thread.sleep(10000);
//		 }
			
	 }
	 
	 public static final void get() {
		 EventLoopGroup group = new NioEventLoopGroup();
		 try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class);
			b.option(ChannelOption.TCP_NODELAY, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
//					pipeline.addLast("encoder", new StringEncoder());
//					pipeline.addLast("encoder", new ClientEncoder());
//					pipeline.addLast("decoder", new ClientDecoder());
//					ch.pipeline().addLast("httpDecoder", new HttpRequestDecoder());
					ch.pipeline().addLast("decoder", new ClientDecoder());
					ch.pipeline().addLast("handler", new ClientHandler());
				}
			});
			b.option(ChannelOption.SO_KEEPALIVE, true);
			
			ChannelFuture f = b.connect("127.0.0.1" ,8081).sync();
			
			String command = "user@login";
            JSONObject jo = new JSONObject();
            jo.put("username", "gaoyf");
            jo.put("password", "1234");
            jo.put("id", 2);
            
            byte[] body = jo.toJSONString().getBytes();
            ByteBuf buf = WrapperUtil.wrapper(command, 1, body);
            f.channel().write(buf);
            f.channel().flush();
			
			for (int i = 0; i < 10; i++) {
				command = "user@read";
	            jo = new JSONObject();
	            jo.put("username", "gaoyf3");
	            jo.put("password", "1234");
	            jo.put("id", 1);
	            
	            body = jo.toJSONString().getBytes();
	            buf = WrapperUtil.wrapper(command, 1, body);
	            f.channel().write(buf);
	            f.channel().flush();
	            
	            Thread.sleep(10000);
			}
			
			
		 }catch (Exception e) {
			 e.printStackTrace();
		 }finally {
	            group.shutdownGracefully();
	        }
			
	 }
	 
//	 public static final Channel getChannel(){
//		 
//			if(ch == null) {
//				try {
//					ch = b.connect("127.0.0.1" ,8081).sync().channel();
//				} catch (Exception e) {
//					return null;
//				}
//			}
//			return ch;
//		}

		public static void sendMsg(Channel channel, Object msg) throws Exception {
			if(channel!=null){
				channel.write(msg);
			}else{
			}
		}
		
		public static void send() {
			String command = "user@findAll";
			 JSONObject jo = new JSONObject();
	         jo.put("username", "gaoyf2");
	         jo.put("password", "1234");
	         jo.put("id", 1);
	         
	         byte[] body = jo.toJSONString().getBytes();
	         ByteBuf buf = WrapperUtil.wrapper(command, 1, body);
	         byte[] bu = buf.array();
	         
			try {
				Socket socket = new Socket("127.0.0.1" ,8081);
				socket.setKeepAlive(true);
//				ByteBuffer b = ByteBuffer.wrap(bu);
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream()); 
//				dos.write(b.array());
				dos.write(bu);
				dos.flush();
				
				Thread.sleep(3000);
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				while(true){  
	                //读取客户端数据    
	                String reciver = dis.readUTF();  
	                System.out.println("客户端发过来的内容:" + reciver);   
	            }
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e ) {
				e.printStackTrace();
			}
		}
		
		public static void send2() {
//	        String content = "TRVAP01080524A2232.9806N11404.9355E000.1061830323.8706000908000102,460,0,9520,3671#";
			String content = "TRVAP00353456789012345#";
	        byte[] bu = content.getBytes();
			try {
				Socket socket = new Socket("101.201.33.141" ,8808);
//				Socket socket = new Socket("127.0.0.1" ,8808);
				socket.setSoTimeout(120000);
				socket.setKeepAlive(true);
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream()); 
				dos.write(bu);
				dos.flush();
				int i =0;
				Thread.sleep(3000);
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				byte[] b = new byte[1];
				while((i=dis.read(b)) != -1){  
	                //读取客户端数据    
	                System.out.print(new String(b));   
	                if (b[0] == "#".getBytes()[0]) {
	                	break;
	                }
	            }  
				
				Thread.sleep(3000);
				content = "TRVAP01080524A2232.9806N11404.9355E000.1061831323.8706000908000102,460,0,9520,3671#";
				bu = content.getBytes();
				dos.write(bu);
				dos.flush();
				
				i = 0;
				b = new byte[1];
				StringBuilder sb = new StringBuilder();
				boolean canquit = false;
				while((i=dis.read(b)) != -1){  
	                //读取客户端数据    
					System.out.print(new String(b));
					sb.append(new String(b));
					if (sb.toString().contains("BP61")) {
						canquit = true;
					}
	                if (canquit && b[0] == "#".getBytes()[0]) {
	                	break;
	                }
	            }  
				System.out.println(sb.toString());
				Thread.sleep(3000);
				content = "TRVAP61#";
				bu = content.getBytes();
				dos.write(bu);
				dos.flush();
				sb = new StringBuilder();
				while((i=dis.read(b)) != -1){  
	                //读取客户端数据    
					sb.append(new String(b));
	                if (canquit && b[0] == "#".getBytes()[0]) {
	                	break;
	                }
	            }
				System.out.println(sb.toString());
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e ) {
				e.printStackTrace();
			}
		}
		
		public static byte uniteBytes(byte src0, byte src1) { 
			byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue(); 
			_b0 = (byte)(_b0 << 4); 
			byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue(); 
			byte ret = (byte)(_b0 ^ _b1); 
			return ret; 
			} 

		/** 
		 * 将指定字符串src，以每两个字符分割转换为16进制形式 
		 * 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9} 
		 * @param src String 
		 * @return byte[] 
		 */ 
		public static byte[] HexString2Bytes(String src){ 
			int len = src.length() / 2;
			byte[] ret = new byte[len]; 
			byte[] tmp = src.getBytes(); 
			for(int i=0; i<len; i++){ 
				ret[i] = uniteBytes(tmp[i*2], tmp[i*2+1]); 
			} 
			return ret; 
		} 
}
