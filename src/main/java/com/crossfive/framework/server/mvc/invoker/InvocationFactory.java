package com.crossfive.framework.server.mvc.invoker;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.crossfive.framework.annotation.Action;
import com.crossfive.framework.annotation.Command;
import com.crossfive.framework.annotation.Syn;
import com.crossfive.framework.common.Constants;
import com.crossfive.framework.common.ServerConstants;
import com.crossfive.framework.common.dto.Result;
import com.crossfive.framework.common.dto.ServiceMeta;
import com.crossfive.framework.common.dto.UserDto;
import com.crossfive.framework.exception.ServletConfigException;
import com.crossfive.framework.exception.ValidateException;
import com.crossfive.framework.jdbc.dao.ApiMetaDao;
import com.crossfive.framework.jdbc.dao.ApiMetaDaoImpl;
import com.crossfive.framework.jdbc.domain.ApiMeta;
import com.crossfive.framework.log.ErrLogger;
import com.crossfive.framework.log.OpLogger;
import com.crossfive.framework.server.mvc.rules.InterceptorManager;
import com.crossfive.framework.server.servlet.Request;
import com.crossfive.framework.server.servlet.Response;
import com.crossfive.framework.util.ResultUtil;
import com.crossfive.framework.util.ScanUtil;
import com.crossfive.framework.util.Utils;


public class InvocationFactory {
	
	private static final Log logger = LogFactory.getLog(InvocationFactory.class);
	
	private static final InvocationFactory instance = new InvocationFactory();
	
	private InvocationFactory() {}
	
	public static InvocationFactory getInstance() {
		return instance;
	}
	
	private AbstractApplicationContext ac;
	
	protected static Map<String, ActionInvocation> handlerMap = new HashMap<String, ActionInvocation>();
	
	public void init(AbstractApplicationContext ac, String packagePath) throws Exception {
		
		this.ac = ac;

		String scanPath = packagePath;
		if (StringUtils.isEmpty(scanPath)) {
			logger.error("actionPackage path is null！！！！！");
			throw new Exception();
		}
		
		try {
			initHandleAction(scanPath);
			initInterceptor();
		}catch (Exception e) {
			throw e;
		}
	}

	private void initInterceptor() {
		for (Entry<String, ActionInvocation> entry : handlerMap.entrySet()) {
			// 先添加默认拦截器
			if (!InterceptorManager.needSkipDefaultInterceptor(entry.getKey())) {
				entry.getValue().addInterceptor(InterceptorManager.getDefaultRule());
			}
			// 再引入定制拦截器
			List<Integer> ruleList = InterceptorManager.getRules(entry.getKey());
			if (ruleList != null && !ruleList.isEmpty()) {
				for (int ruleId : ruleList) {
					entry.getValue().addInterceptor(InterceptorManager.getInterceptor(ruleId));
				}
			}
		}
	}

	private void initHandleAction(String scanPath) throws Exception {
		Set<Class<?>> set = ScanUtil.getClasses(scanPath);
		// 此处可导入插件Plugin，目前尚未开发
		// 当前仅遍历路径下类文件
		ApiMetaDao apiMetaDao = ac.getBean(ApiMetaDaoImpl.class);
		for (Class<?> clazz : set) {
			initHandleAction(clazz, apiMetaDao);
		}
	}

	private void initHandleAction(Class<?> clazz, ApiMetaDao apiMetaDao) throws Exception {
//		if (Modifier.isAbstract(clazz.getModifiers()) || Modifier.isInterface(clazz.getModifiers())) {
//			return;
//		}
		if (!Modifier.isInterface(clazz.getModifiers())) {
			return;
		}
		
		Action action = Utils.getAnnotation(clazz, Action.class);
		if (action == null) {
			return;
		}
		
		// 表示该方法只能同步访问
		boolean isSyn = false;
		Syn syn = clazz.getAnnotation(Syn.class);
		if (syn != null) {
			isSyn = syn.value();
		}
		
		createActionInvocation(clazz, isSyn, apiMetaDao);
	}
	
	private void createActionInvocation(Class<?> clazz, boolean isSyn, ApiMetaDao apiMetaDao) throws Exception {
		ActionInvocation ai;
 		String path = clazz.getName();
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (Modifier.isStatic(method.getModifiers()) || Modifier.isFinal(method.getModifiers())) {
				continue;
			}
			
			Command cmd = method.getAnnotation(Command.class);
			if (cmd == null) {
				continue;
			}
			
			if (handlerMap.containsKey(cmd.value())) {
				throw new ServletConfigException("Duplicate command exist" + cmd.value());
			}
			ApiMeta apiMeta = apiMetaDao.getApiMeta(cmd.value());
			int version = apiMeta.getVersion();
			int state = apiMeta.getState();
			ServiceMeta serviceMeta = JSON.parseObject(apiMeta.getServiceMeta(), ServiceMeta.class);
			int timeout = 3000;
			int retry = 1;
			
			if (serviceMeta != null) {
				timeout = serviceMeta.getTimeout();
				retry = serviceMeta.getRetry();
			}
			
			// build
			ai = new ActionInvocation(path, method, isSyn, version, timeout, retry, state); 
			ai.init();
			
			handlerMap.put(cmd.value(), ai);
			OpLogger.getLogger().info("@@@@@regist command " + cmd.value() +" success! entityName is "+ ai.toString());
		}
		
	}
	
	public void service(Request request, Response response) {
		ActionInvocation invocation = handlerMap.get(request.getCommand());
		try {
			if (null == invocation) {
				throw new RuntimeException("No such method，command is ："+request.getCommand());
			}
//			Result result = invocation.invoke(request, response);
			byte[] result = invocation.invoke(request, response);
			if (result != null) {
//				byte[] resultArr = JSON.toJSONBytes(result);
//				ActionInvocation.render(resultArr, request, response);
				ActionInvocation.render(result, request, response);
			}
			afterDeal(request, response, result);
		} catch (ValidateException ve) {
			Result result = ResultUtil.buildResult(ve.getCode(), ve.getMessage());
			byte[] resultArr = JSON.toJSONBytes(result);
//			byte[] bt = t.getMessage().getBytes();
			try {
				ActionInvocation.render(resultArr, request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		} catch (Throwable t) {
			if (t.getMessage() != null) {
				Result result = ResultUtil.buildResult(ServerConstants.INTERNAL_ERROR, t.getMessage());
				byte[] resultArr = JSON.toJSONBytes(result);
//				byte[] bt = t.getMessage().getBytes();
				try {
					ActionInvocation.render(resultArr, request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
			ErrLogger.getLogger().error("核心失败", t);
			Result result = ResultUtil.buildResult(ServerConstants.INTERNAL_ERROR, "程序执行失败!", t.getLocalizedMessage());
			byte[] resultArr = JSON.toJSONBytes(result);
			try {
				ActionInvocation.render(resultArr, request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
//			throw new RuntimeException(t);
		}
	}
	
	private void afterDeal(Request request, Response response, byte[] result) {
		Result r = JSONObject.parseObject(result, Result.class);
		if (request.getCommand().equalsIgnoreCase("user@login") && r.getCode() == ResultUtil.SUCCESS) {
			request.getNewSession().setAttribute(Constants.USER, ((JSONObject)r.getData()).toJavaObject(UserDto.class));
		}
	}

	public static void main(String[] args) {
		System.out.println(ActionInvocation.class.getName());
		System.out.println(ActionInvocation.class.getPackage().getName());
	}

}
