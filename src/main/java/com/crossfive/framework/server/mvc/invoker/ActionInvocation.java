package com.crossfive.framework.server.mvc.invoker;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.MonitorConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.fastjson.JSON;
import com.crossfive.framework.annotation.RequestParam;
import com.crossfive.framework.annotation.Syn;
import com.crossfive.framework.common.Constants;
import com.crossfive.framework.common.config.Configuration;
import com.crossfive.framework.common.dto.UserDto;
import com.crossfive.framework.common.session.ServerProtocol;
import com.crossfive.framework.handler.SameObjectChecker;
import com.crossfive.framework.server.mvc.adaptor.Adaptor;
import com.crossfive.framework.server.mvc.adaptor.RequestParamAdaptor;
import com.crossfive.framework.server.mvc.rules.Rule;
import com.crossfive.framework.server.servlet.Request;
import com.crossfive.framework.server.servlet.Response;
import com.crossfive.framework.util.Tuple;
import com.crossfive.framework.util.WrapperUtil;

public class ActionInvocation {

	protected String path;
	
	protected Method method;
	
	protected String methodName;
	
	protected Adaptor[] paramsAdaptors;
	
	/** 是否同步 */
	protected boolean syn;
	
	protected boolean needValidate;
	
	protected List<Rule> validateInterceptor;
	
	protected GenericService genericService;
	
	protected int version;
	
	protected int timeout;
	
	protected int retry;
	
	protected int state;
	
	private static final String CLASS_KEY = "class";
	
	public ActionInvocation(String path, Method method, boolean syn, int version, int timeout, int retry, int state) {
		this.path = path;
		this.method = method;
		this.methodName = method.getName();
		this.syn = syn;
		this.version = version;
		this.timeout = timeout;
		this.retry = retry;
		this.state = state;
	}
	
	
	public ActionInvocation(String path, Method method, boolean syn) {
		this.path = path;
		this.method = method;
		this.methodName = method.getName();
		this.syn = syn;
		initMeta();
	}
	
	/**
	 * 提供根据meta解析的方式
	 */
	private void initMeta() {
		// TODO 分析api的标签获取meta信息
		
	}


	public void init() throws Exception {
		initSyn();
		initParam();
		initGenericService();
	}

	private void initGenericService() throws Exception {
		ApplicationConfig application = new ApplicationConfig();
        application.setName(Configuration.getProperty(Configuration.DUBBO_APPLICATION_NAME));

        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setAddress(Configuration.getProperty(Configuration.DUBBO_REGISTRY_CENTER));
        
        // 服务消费者缺省值配置
        ConsumerConfig consumer = new ConsumerConfig();
        
        ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();

        reference.setApplication(application);
        reference.setRegistry(registry);
        reference.setConsumer(consumer);
        // 这里可以自定义loadbalance方案
        reference.setLoadbalance("consistenthash");
        

        String monitorAddress = Configuration.getProperty(Configuration.DUBBO_MONITOR_ADDRESS);
        if(monitorAddress != null){
            MonitorConfig monitor = new MonitorConfig();
            monitor.setAddress(monitorAddress);
            reference.setMonitor(monitor);
        }

        reference.setInterface(path); // 弱类型接口名
//        reference.setVersion(version);
        reference.setTimeout(Integer.valueOf((int) timeout));
        reference.setRetries((int)retry);
        reference.setGeneric(true); // 声明为泛化接口

        // 定制化ReferenceConfig，设置属性
//        if (properties != null) {
//            for (Pair<String, String> meta : properties)
//                BeanUtil.setProperty(reference, meta.getLeft(), meta.getRight());
//        }

//        if (!syn) {
//            reference.setAsync(true);
//            reference.setFilter(getDubboAsyncInvokeFilter());// 增加dubbo的异步服务调用callback filter.
//        }

//        if (StringUtils.isNotBlank(url)) {
//            reference.setUrl(url);
//        }

        try {
            genericService = reference.get();
        } catch (Throwable e) {
            throw new Exception("创建泛化调用器失败", e);
        }
	}

	private void initSyn() {
		Syn syn = method.getAnnotation(Syn.class);
		if (syn != null) {
			this.syn = syn.value();
		}
	}
	
	public byte[] invoke(Request request, Response response) throws Exception {
		if (validateInterceptor != null) {
			for (Rule rule : validateInterceptor) {
				rule.validate(request);
			}
		}
		if (syn) {
			synchronized(request.getSession()) {
				return _invoke(request, response);
			}
		}else {
			return _invoke(request, response);
		}
	}

	protected byte[] _invoke(Request request, Response response) throws Exception {
		
//		Object[] params = adapt(request);
//		return (Result) method.invoke(obj, params);
		RpcContext.getContext().set("_session", request.getSession());
		if (request.getSession() != null) {
			UserDto userDto = (UserDto)request.getSession().getAttribute(Constants.USER);
			if (userDto != null) {
				RpcContext.getContext().setAttachment(Constants._USER_ID, userDto.getUserId());
			}
		}
		Tuple<String[], Object[]> params = dubboAdapt(request);
		
		Object result = genericService.$invoke(method.getName(), params.left, params.right);
		return dealResult(result);
	}

	private byte[] dealResult(Object result) {
		if (result == null) {
			return null;
		}
		if (result instanceof Map<?, ?>) {
            removeKey((Map<?, ?>) result, CLASS_KEY, new SameObjectChecker());
        } else if (result instanceof Collection<?>) {
            removeKey((Collection<?>) result, CLASS_KEY, new SameObjectChecker());
        } else if (result instanceof Object[]) {
            removeKey((Object[]) result, CLASS_KEY, new SameObjectChecker());
        }
		
		return JSON.toJSONBytes(result);
	}


	protected void initParam() {
		Annotation[][] annoArr = method.getParameterAnnotations();
		Class<?>[] parameterTypes = method.getParameterTypes();
		paramsAdaptors = new Adaptor[parameterTypes.length];
		for (int count = 0; count < parameterTypes.length; count++) {
			Annotation[] anns = annoArr[count];
			RequestParam requestParam = null;
			
			for (Annotation ann : anns) {
				if (ann instanceof RequestParam) {
					requestParam = (RequestParam) ann;
					break;
				}
			}
			
			if (requestParam == null) {
				continue;
			}
			
			if (requestParam != null) {
				paramsAdaptors[count] = new RequestParamAdaptor(requestParam.value(), parameterTypes[count]);
			}
			
		}

	}
	
	@Deprecated
	private Object[] adapt(Request request) {
		if (paramsAdaptors.length <= 0) {
			return new Object[] {};
		}
		Object[] parameters = new Object[paramsAdaptors.length];
		for (int i = 0; i < paramsAdaptors.length; i++) {
			parameters[i] = paramsAdaptors[i].get(request);
		}
		return parameters;
	}
	
	private Tuple<String[],Object[]> dubboAdapt(Request request) {
		if (paramsAdaptors.length <= 0) {
			return new Tuple<String[],Object[]>();
		}
		Object[] parameters = new Object[paramsAdaptors.length];
		String[] parametersTypes = new String[paramsAdaptors.length];
		for (int i = 0; i < paramsAdaptors.length; i++) {
			parameters[i] = paramsAdaptors[i].get(request);
			parametersTypes[i] = paramsAdaptors[i].getType();
		}
		return new Tuple<String[],Object[]>(parametersTypes, parameters);
	}
	
	public String toString() {
		return path + '.' + methodName;
	}

	public static void render(byte[] result, Request request, Response response) throws Exception {
		if (ServerProtocol.TCP.equals(response.getProtocol())) {
			response.write(WrapperUtil.wrapper(request.getCommand(), request.getRequestId(), result));
		} else if (ServerProtocol.HTTP.equals(response.getProtocol())) {
			// TODO:HTTP
//			response.write(WrapperUtil.wrapper(request.getCommand(), request.getRequestId(), result));
			response.write(result);
		} else if (ServerProtocol.WEBSOCKET.equals(response.getProtocol())) {
			// TODO:WEB SOCKET
			response.write(WrapperUtil.wrapperWebSocketTextFrame(request.getCommand(), request.getRequestId(), result));
		}
	}
	
	public void addInterceptor(Rule rule) {
		if (validateInterceptor == null) {
			validateInterceptor = new ArrayList<Rule>();
		}
		validateInterceptor.add(rule);
	}
	
	public void setNeedSkipDefault(boolean needSkip) {
		this.needValidate = needSkip;
	}
	
	// 针对对象数组，是DTO[]的过滤class字段
    private void removeKey(Object[] array, String key, SameObjectChecker objs) {
        if (array != null && objs.checkThenPush(array)) {
            Class<?> componentClass = array.getClass().getComponentType();
            if (componentClass != null && componentClass.isAssignableFrom(Map.class)) {
                for (Object object : array)
                    removeKey((Map<?, ?>) object, key, objs);
            }
            objs.pop();
        }
    }

    private void removeKey(Map<?, ?> map, String key, SameObjectChecker objs) {
        if (map != null && objs.checkThenPush(map)) {
            map.remove(key);
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof Map<?, ?>) {
                    removeKey((Map<?, ?>) value, key, objs);
                } else if (value instanceof Collection<?>) {
                    removeKey((Collection<?>) value, key, objs);
                } else if (value instanceof Object[]) {
                    removeKey((Object[]) value, key, objs);
                } 
            }
            objs.pop();
        }
    }

    private void removeKey(Collection<?> collection, String key, SameObjectChecker objs) {
        if (collection != null && objs.checkThenPush(collection)) {
            for (Object value : collection) {
                if (value instanceof Map<?, ?>) {
                    removeKey((Map<?, ?>) value, key, objs);
                } else if (value instanceof Collection<?>) {
                    removeKey((Collection<?>) value, key, objs);
                } else if (value instanceof Object[]) {
                    removeKey((Object[]) value, key, objs);
                }
            }
            objs.pop();
        }
    }
	
}
