package com.crossfive.framework.server.mvc.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterceptorManager {
	private static Rule defaultRule = null;
	private static Map<String, List<Integer>> cmdRuleMap = new HashMap<String, List<Integer>>();
	private static Map<Integer, Rule> ruleMap = new HashMap<Integer, Rule>();
	private static Map<String, Boolean> skipDefaultMap = new HashMap<String, Boolean>();
	
	public static void addRules(int ruleId, String cmd) {
		if (cmdRuleMap.containsKey(cmd)) {
			List<Integer> list = cmdRuleMap.get(cmd);
			if (!list.contains(ruleId)) {
				list.add(ruleId);
			}
		}else {
			List<Integer> list = new ArrayList<Integer>();
			list.add(ruleId);
			cmdRuleMap.put(cmd, list);
		}
	}
	
	public static List<Integer> getRules(String cmd) {
		return cmdRuleMap.get(cmd);
	}
	
	public static void registRule(int ruleId, Rule rule) {
		ruleMap.put(ruleId, rule);
	}
	
	public static Rule getInterceptor(int ruleId) {
		return ruleMap.get(ruleId);
	}
	
	public static boolean needSkipDefaultInterceptor(String cmd) {
		return skipDefaultMap.containsKey(cmd);
	}
	
	public static void setSkipDefaultInterceptor(String cmd) {
		skipDefaultMap.put(cmd, false);
	}

	public static void registDefaultRule(int ruleId, Rule rule) {
		defaultRule = rule;
	}
	
	public static Rule getDefaultRule() {
		return defaultRule;
	}
}
