package com.crossfive.framework.server.inbound.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.crossfive.framework.common.ServerConstants;
import com.crossfive.framework.log.DayLogger;
import com.crossfive.framework.log.ErrLogger;
import com.crossfive.framework.log.OpLogger;
import com.crossfive.framework.server.inbound.websocket.WebSocketDefaultHandler;
import com.crossfive.framework.server.mvc.invoker.InvocationFactory;
import com.crossfive.framework.server.servlet.Request;
import com.crossfive.framework.server.servlet.Response;
import com.crossfive.framework.server.servlet.http.HttpRespone;
import com.crossfive.framework.util.HttpUtil;
import com.crossfive.framework.util.Tuple;
import com.crossfive.framework.util.WebUtils;
/**
 * http服务器处理器
 * @author kira
 *
 */
public class HttpServerHandler extends ChannelInboundHandlerAdapter { // (1)  
	  
	private InvocationFactory servlet;
	
	/** uri正则表达 */
	private static final Pattern pattern = Pattern.compile("^/root/([\\w-/]*)\\.action([\\s\\S]*)?$");
	
	public HttpServerHandler(InvocationFactory servlet) {
		this.servlet = servlet;
	}
	
//    @Override  
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
//    	System.out.println("http read!");
//        if (msg instanceof HttpRequest) {  
//            HttpRequest request = (HttpRequest) msg;  
//            if (HttpHeaders.isContentLengthSet(request)) {  
//                reader = new ByteBufToBytes((int) HttpHeaders.getContentLength(request));  
//            }  
//        }  
//        
//        if (msg instanceof HttpContent) {  
//        	long startTime = System.currentTimeMillis();
//        	HttpContent httpContent = (HttpContent) msg;
//            ByteBuf content = httpContent.content();  
//            reader.reading(content);  
//            content.release();  
//  
//            if (reader.isEnd()) {  
//            	byte[] resultByte = reader.readFull();
//            	 System.out.println("@@@@@read time:"+(System.currentTimeMillis() - startTime)+"ms");
////            	String resultStr = new String(resultByte);  
////                System.out.println("Client said:" + resultStr);
//                // JSON解析协议，反射调用接口
//            	JSONObject jo = (JSONObject) JSONObject.parse(resultByte);
//            	System.out.println("@@@@@parse json time:"+(System.currentTimeMillis() - startTime)+"ms");
//            	String actionName = jo.getString("action");
//            	if (!actionName.endsWith("Action")) {
//            		System.out.println("非法协议!!");
//            		return;
//            	}
//            	
//            	Object clazz = null;
//            	Method method = null;
//            	Object[] args = null;
//            	byte[] bt = null;
//            	try {
//            		clazz = ac.getBean(actionName);
//            		System.out.println("@@@@@aquire class time:"+(System.currentTimeMillis() - startTime)+"ms");
////            		c =  clazz.getClass();
//            		// TODO:遍历method获得接口，可通过initAction存放map进行优化
//            		for (Method m : clazz.getClass().getMethods()) {
//            			if (m.getName().equalsIgnoreCase(jo.getString("method"))) {
//            				method = m;
//            				break;
//            			}
//            		}
//            		System.out.println("@@@@@aquire method time:"+(System.currentTimeMillis() - startTime)+"ms");
//            		Annotation[][] annoArr = method.getParameterAnnotations();
//                    Class<?>[] parameterTypes = method.getParameterTypes();
//                    args = new Object[parameterTypes.length];
//                    for (int count = 0; count < parameterTypes.length; count++) {
//                    	RequestParam param = (RequestParam) annoArr[count][0];
//                    	Object obj = jo.get(param.value());
//                    	if (obj != null) {
//                    		args[count] = obj;
//                    	}else {
//                    		System.out.println("缺少参数");
//                    		return;
//                    	}
//                    }
//                    System.out.println("@@@@@build time:"+(System.currentTimeMillis() - startTime)+"ms");
//                    // 调用接口
//                    bt = (byte[]) method.invoke(clazz, args);
//                    
//                    System.out.println("@@@@@cost time:"+(System.currentTimeMillis() - startTime)+"ms");
//            	} catch(NoSuchBeanDefinitionException e) {
//            		jo = new JSONObject();
//            		jo.put("errorMsg","找不到该入口");
//            		bt = jo.toJSONString().getBytes();
//            	} catch(NullPointerException e) {
//            		jo = new JSONObject();
//            		jo.put("errorMsg","找不到该方法");
//            		bt = jo.toJSONString().getBytes();
//            	} catch(Exception e) {
//	            	e.printStackTrace();
//            	}
//                
//               
//                ByteBuf bff = Unpooled.wrappedBuffer(bt);
//                // 构造返回
//                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, 
//                		bff);  
//                response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");  
//                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());  
//                response.headers().set(HttpHeaders.Names.CONNECTION, Values.KEEP_ALIVE);  
//                response.headers().set(HttpHeaders.Names.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
//                response.headers().set(HttpHeaders.Names.ACCESS_CONTROL_ALLOW_HEADERS, true);
//                response.headers().set(HttpHeaders.Names.ACCESS_CONTROL_ALLOW_METHODS, "POST,GET");
//
//                
//                ctx.channel().write(response);  
//                ctx.flush();  
//            }  
//        }  
//    }  
	
    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
    	System.out.println("http read!");
    	long start = System.currentTimeMillis();
        if (msg instanceof FullHttpRequest) {
        	try {
        		final FullHttpRequest httpRequest = (FullHttpRequest) msg;
        		
        		// 处理跨域
        		if (httpRequest.getUri().equalsIgnoreCase("/crossdomain.xml")) {
        			HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(ServerConstants.CROSSDOMAIN));
        			response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain;charset=UTF_8");
        			ctx.channel().write(response);
        			return;
        		}
        		
        		String uri = httpRequest.getUri();
        		Matcher matcher = pattern.matcher(uri);
        		
        		if (!matcher.find()) {
        			// 非正常请求
        			HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_GATEWAY);
        			ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        			return;
        		}
        		
        		if (HttpUtil.isWebSocketRequest(httpRequest)) {
        			OpLogger.getLogger().debug("get Websocket request");
        			// websocket
        			WebSocketServerHandshakerFactory wsf = new WebSocketServerHandshakerFactory(HttpUtil.getWebSocketLocation(httpRequest), "default-protocol", false);
        			WebSocketServerHandshaker handshaker = wsf.newHandshaker(httpRequest);
        			if (handshaker == null) {
        				wsf.sendUnsupportedVersionResponse(ctx.channel());
        			}else {
        				handshaker.handshake(ctx.channel(), (FullHttpRequest) httpRequest);
        				
        				// 获取cookies
//        				Map<String, Cookie> cookies = HttpUtil.getCookies(httpRequest);
//        				String sessionId = ctx.attr(ServerConstants.SESSIONID).get();
        				
        				String ip = WebUtils.getIp(httpRequest, ctx.channel());
        				
        				ctx.channel().pipeline().replace(HttpServerHandler.class, "wshandler", new WebSocketDefaultHandler(servlet, handshaker, ip));
        			}
        			
        			return;
        		}
        		
        		// 获得命令
        		String command = matcher.group(1);
        		
        		// 获取参数
        		Tuple<byte[], byte[]> requestContent = HttpUtil.getRequestContent(httpRequest);
        		
        		// 获取cookie
        		Map<String, Cookie> cookies = HttpUtil.getCookies(httpRequest);
        		
        		// 获取head
        		Map<String, String> headers = HttpUtil.getHeaders(httpRequest);
        		
        		// 解析消息
        		final Response response = new HttpRespone(ctx.channel());
        		
        		final Request request = new com.crossfive.framework.server.servlet.http.HttpRequest(ctx, httpRequest, requestContent.left, requestContent.right, command, cookies, headers, response, uri);
        		
        		if (!request.isLongHttp()) {
        			// 处理请求
        			servlet.service(request, response);
        			// 响应
        			HttpUtil.doResponse(ctx, request, response, httpRequest);
        			DayLogger.getLogger().info("invoke cost："+(System.currentTimeMillis() - start));
        		}
        		
        	}catch (Exception e) {
        		e.printStackTrace();
        		throw e;
//        		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
//        		ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        	}
        }
        
    }  
  
    @Override  
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {  
        System.out.println("read complete!");
        ctx.flush();  
    }  
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
//    	System.out.println("chanel actived"+ctx.name());
//    	String sessionId = ctx.attr(ServerConstants.SESSIONID).get();
//    	if (sessionId == null) {
//    		Session session = SessionManager.getInstance().getSession(null, true);
//    		sessionId = session.getId();
//    		ctx.attr(ServerConstants.SESSIONID).set(session.getId());
//    	}
    }
  
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)  
        // Close the connection when an exception is raised.  
        cause.printStackTrace();  
        ErrLogger.getLogger().error(cause);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        if (cause instanceof TooLongFrameException) {
        	response.content().writeBytes("Too long frame".getBytes());
        }
		ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        ctx.close();  
    }
    
    public static void main(String[] args) throws Exception {
    	String a = "/root/abc/gateway.action?abc=122.ppp";
    	Matcher m = pattern.matcher(a);
    	m.find();
    	System.out.println(URLEncoder.encode("=", "UTF-8"));
    	System.out.println(pattern.matcher(a).find());
    	
    }
} 