package com.crossfive.framework.common.interceptor;

import com.crossfive.framework.server.mvc.rules.InterceptorManager;

public class InterceptorInitManager {

	public static enum RuleType {
		DEFAULT(0),
		IP_STRICT(1),
		SESSION_IGNORE(2);
		public int ruleId;
		RuleType(int ruleId) {
			this.ruleId = ruleId;
		}
	}
	
	public static void init() {
		// 注册default interceptor
		InterceptorManager.registDefaultRule(RuleType.DEFAULT.ruleId, new AuthenticationInterceptor());
		// 跳过default的方法or类
		InterceptorManager.setSkipDefaultInterceptor("user@regist");
		InterceptorManager.setSkipDefaultInterceptor("user@login");
		// 注册normal rule
//		InterceptorManager.registRule(RuleType.IP_STRICT, new IPCheckInterceptor());
		// 注册方法的normal interceptor
//		InterceptorManager.addRules(RuleType.IP_STRICT, "backstage@checkLive");
		
	}
}
