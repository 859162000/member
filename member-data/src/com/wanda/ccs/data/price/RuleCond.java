package com.wanda.ccs.data.price;

import java.util.Map;

public abstract class RuleCond {
	static final String DELIMITER = "!#!";
	EnumRuleCondType dim;

	public RuleCond(EnumRuleCondType dim) {
		this.dim = dim;
	}

	public EnumRuleCondType getType() {
		return dim;
	}

	public static String getType(String rule) {
		if (rule == null)
			return null;
		int idx = rule.indexOf(DELIMITER);
		if (idx <= 0)
			return null;
		return rule.substring(0, idx);
	}

	public boolean match(String rule) {
		if (rule == null || rule.equals(""))
			return false;
		return rule.startsWith(dim.name() + DELIMITER);
	}

	public boolean hasValue() {
		return false;
	}

	/**
	 * 读取规则
	 * 
	 * @param rule
	 * @return true - 成功 false - 不匹配
	 */
	abstract public boolean parseRule(String rule);

	/**
	 * 生成规则
	 * 
	 * @return
	 */
	abstract public String genRule();

	/**
	 * 生成规则文字说明
	 * 
	 * @return
	 */
	abstract public String genDesc();

	/**
	 * 判断条件是否成立
	 * 
	 * @param params
	 * @return
	 */
	public boolean matches(Map<String, Object> params) {
		return false;
	}
}
