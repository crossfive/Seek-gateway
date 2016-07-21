package com.crossfive.framework.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

import org.springframework.context.support.AbstractApplicationContext;

import com.crossfive.framework.common.config.ServletConfig;
import com.crossfive.framework.common.session.SessionManager;
import com.crossfive.framework.log.OpLogger;
import com.crossfive.framework.server.mvc.invoker.InvocationFactory;
import com.crossfive.framework.server.servlet.http.HttpPipelineHandler;
import com.crossfive.framework.server.servlet.tcp.TcpPipelineHandler;
/**
 * 服务器
 * @author kira
 *
 */
public class Server {
	private AbstractApplicationContext ac;
	private ServletConfig sc;
	
	public Server(ServletConfig sc, AbstractApplicationContext ac) {  
		this.sc = sc;
		this.ac = ac;
	}  


	public void run() throws Exception {  
		// 服务器工作组
		EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)  
		EventLoopGroup workerGroup = new NioEventLoopGroup();  
		
		EventLoopGroup httpBossGroup = new NioEventLoopGroup(); // (1)  
		EventLoopGroup httpWorkerGroup = new NioEventLoopGroup();  
		
//		EventLoopGroup rtspBossGroup = new NioEventLoopGroup(); // (1)  
//		EventLoopGroup rtspWorkerGroup = new NioEventLoopGroup();  
		try {  
			
			InvocationFactory servlet  = InvocationFactory.getInstance();
//			servlet.init(ac, sc.getPackagePath());
			
			// 服务器启动类
			ServerBootstrap b = new ServerBootstrap(); 
			// 注册
			b.group(bossGroup, workerGroup);	
			// 使用nioServerSocketChannal
			b.channel(NioServerSocketChannel.class);
			
			TcpPipelineHandler tcpChannel = new TcpPipelineHandler();
			tcpChannel.init(servlet);
			
			b.childHandler(tcpChannel)
			.option(ChannelOption.SO_BACKLOG, 128)          // (5)  监听socket设置
			.childOption(ChannelOption.SO_KEEPALIVE, true) // (6)  客户端socket设置
			.childOption(ChannelOption.TCP_NODELAY, true)
			.childOption(ChannelOption.SO_REUSEADDR, true);
			
			ChannelFuture f1 = b.bind(new InetSocketAddress(sc.getIp() , sc.getTcpPort())).sync(); // (7)  绑定端口
			OpLogger.getLogger().info("tcp afer bind"+sc.getTcpPort());
			
			// 服务器启动类
			ServerBootstrap httpb = new ServerBootstrap(); 
			// 注册
			httpb.group(httpBossGroup, httpWorkerGroup);	
			// 使用nioServerSocketChannal
			httpb.channel(NioServerSocketChannel.class);
			
			HttpPipelineHandler httpPipelineHandler = new HttpPipelineHandler();
			httpPipelineHandler.init(servlet);
			
			httpb.childHandler(httpPipelineHandler)
			.option(ChannelOption.SO_BACKLOG, 128)          // (5)  监听socket设置
			.childOption(ChannelOption.SO_KEEPALIVE, true) // (6)  客户端socket设置
			.childOption(ChannelOption.TCP_NODELAY, true);

			// Bind and start to accept incoming connections.  
			ChannelFuture f2 = httpb.bind(new InetSocketAddress(sc.getIp(), sc.getHttpPort())).sync();
			OpLogger.getLogger().info("http afer bind"+sc.getHttpPort());
			
//			ServerBootstrap tcpb2 = new ServerBootstrap(); 
//			// 注册
//			tcpb2.group(bossGroup, workerGroup);	
//			// 使用nioServerSocketChannal
//			tcpb2.channel(NioServerSocketChannel.class);
//			tcpb2.childHandler(tcpChannel);
//			tcpb2.option(ChannelOption.SO_REUSEADDR, true);
//			tcpb2.childOption(ChannelOption.SO_KEEPALIVE, true);
//			tcpb2.childOption(ChannelOption.SO_REUSEADDR, true);
//			
//			ChannelFuture f4 = tcpb2.bind(new InetSocketAddress(sc.getIp(), 8083)).sync();
//			OpLogger.getLogger().info("tcp2 after bind8083");
			
//			// 服务器启动类
//			ServerBootstrap rtspb = new ServerBootstrap(); 
//			// 注册
//			rtspb.group(rtspBossGroup, rtspWorkerGroup);	
//			// 使用nioServerSocketChannal
//			rtspb.channel(NioServerSocketChannel.class);
//
//			RtspPipelineHandler rtspPipelineHandler = new RtspPipelineHandler();
//			rtspPipelineHandler.init(servlet);
//
//			rtspb.childHandler(rtspPipelineHandler)
//			.option(ChannelOption.SO_BACKLOG, 128)          // (5)  监听socket设置
//			.childOption(ChannelOption.SO_KEEPALIVE, true) // (6)  客户端socket设置
//			.childOption(ChannelOption.TCP_NODELAY, true);
//
//			// Bind and start to accept incoming connections.  
//			ChannelFuture f3 = rtspb.bind(new InetSocketAddress(sc.getIp(), 8083)).sync();
//			System.out.println("rtsp afer bind8083");
			
			// 启动session清理线程
			SessionManager.getInstance().startSessionCheckThread();
			
			// Wait until the server socket is closed.  
			// In this example, this does not happen, but you can do that to gracefully  
			// shut down your server.  
			f1.channel().closeFuture().sync();
			f2.channel().closeFuture().sync();
//			f3.channel().closeFuture().sync();
//			f4.channel().closeFuture().sync();
			System.out.println("close");
			
		} finally {  
			workerGroup.shutdownGracefully();  
			bossGroup.shutdownGracefully();  
		}  
	} 

}
