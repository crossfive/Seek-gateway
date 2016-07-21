package com.crossfive.framework.server.inbound.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import com.crossfive.framework.common.ServerConstants;
import com.crossfive.framework.common.session.Session;
import com.crossfive.framework.common.session.SessionManager;
import com.crossfive.framework.log.ErrLogger;
import com.crossfive.framework.server.mvc.invoker.InvocationFactory;
import com.crossfive.framework.server.request.RequestMessage;
import com.crossfive.framework.server.servlet.Request;
import com.crossfive.framework.server.servlet.Response;
import com.crossfive.framework.server.servlet.tcp.TcpPush;
import com.crossfive.framework.server.servlet.tcp.TcpRequest;
import com.crossfive.framework.server.servlet.tcp.TcpResponse;
/**
 * tcp服务器处理器
 * @author kira
 *
 */
public class TcpServerHandler extends SimpleChannelInboundHandler<RequestMessage> { // (1)  
	  
	private InvocationFactory servlet;
	
	public TcpServerHandler(InvocationFactory servlet) {
		this.servlet = servlet;
	}
	
    @Override  
    public void channelRead0(ChannelHandlerContext ctx, RequestMessage msg) throws Exception {  
    	System.out.println("tcp read!");
    	if (msg instanceof RequestMessage) {
    		RequestMessage message = (RequestMessage) msg;
    		message.setSessionId(ctx.attr(ServerConstants.SESSIONID).get());
    		
    		Request request = new TcpRequest(ctx, message);
    		Response response = new TcpResponse(ctx.channel());
    		
    		long startTime = System.currentTimeMillis();
    		servlet.service(request, response);
//    		ByteBuf buf = WrapperUtil.wrapper(request.getCommand(), request.getRequestId(), result);
//    		ByteBuf buf2 = Unpooled.wrappedBuffer(result);
//    		ctx.write(result);
//    		ctx.flush();
    		System.out.println("@@@@@cost time:"+(System.currentTimeMillis() - startTime)+"ms");
    		
//    		long startTime = System.currentTimeMillis();
//    		
//        	String actionName = message.getCommand().split("@")[0];
//        	if (!actionName.endsWith("Action")) {
//        		System.out.println("非法协议!!");
//        		return;
//        	}
//        	String methodName = message.getCommand().split("@")[1];
//        	Object clazz = null;
//        	Method method = null;
//        	Object[] args = null;
//        	byte[] bt = null;
//        	JSONObject jo = null;
//        	try {
//        		clazz = ac.getBean(actionName);
//        		System.out.println("@@@@@aquire class time:"+(System.currentTimeMillis() - startTime)+"ms");
////        		c =  clazz.getClass();
//        		// TODO:遍历method获得接口，可通过initAction存放map进行优化
//        		for (Method m : clazz.getClass().getMethods()) {
//        			if (m.getName().equalsIgnoreCase(methodName)) {
//        				method = m;
//        				break;
//        			}
//        		}
//        		System.out.println("@@@@@aquire method time:"+(System.currentTimeMillis() - startTime)+"ms");
//        		Annotation[][] annoArr = method.getParameterAnnotations();
//                Class<?>[] parameterTypes = method.getParameterTypes();
//                args = new Object[parameterTypes.length];
//                
//                // JSON解析协议，反射调用接口
//        		byte[] resultByte = message.getContent();
//            	jo = (JSONObject) JSONObject.parse(resultByte);
//            	
//                for (int count = 0; count < parameterTypes.length; count++) {
//                	RequestParam param = (RequestParam) annoArr[count][0];
//                	Object obj = jo.get(param.value());
//                	if (obj != null) {
//                		args[count] = obj;
//                	}else {
//                		System.out.println("缺少参数");
//                		return;
//                	}
//                }
//                System.out.println("@@@@@build time:"+(System.currentTimeMillis() - startTime)+"ms");
//                // 调用接口
//                bt = (byte[]) method.invoke(clazz, args);
//                
//                System.out.println("@@@@@cost time:"+(System.currentTimeMillis() - startTime)+"ms");
//        	} catch(NoSuchBeanDefinitionException e) {
//        		jo = new JSONObject();
//        		jo.put("errorMsg","找不到该入口");
//        		bt = jo.toJSONString().getBytes();
//        	} catch(NullPointerException e) {
//        		jo = new JSONObject();
//        		jo.put("errorMsg","找不到该方法");
//        		bt = jo.toJSONString().getBytes();
//        	} catch(Exception e) {
//            	e.printStackTrace();
//        	}
//        	
//        	System.out.println(new String(bt));
    	}
    }
    
    @Override  
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {  
        System.out.println("read complete!");
        ctx.flush();  
    }  
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    	System.out.println("chanel actived "+ctx.name());
    	String sessionId = ctx.attr(ServerConstants.SESSIONID).get();
    	if (sessionId == null) {
    		Session session = SessionManager.getInstance().getSession(null, true);
    		session.setPush(new TcpPush(ctx.channel()));
    		ctx.attr(ServerConstants.SESSIONID).set(session.getId());
    		System.err.println("session create "+session.id);
    	}
    }
  
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)  
        // Close the connection when an exception is raised.  
        cause.printStackTrace();  
        ErrLogger.getLogger().error(cause);
        ctx.flush();
//        ctx.close();  
    }
} 