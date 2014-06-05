package com.wanda.ccs.data.price;

public class RuleCondTicketNum extends RuleCond {
	public RuleCondTicketNum(EnumRuleCondType dim) {
		super(dim);
	}

	@Override
	public boolean parseRule(String rule) {
		if (rule == null)
			return false;
		if (!rule.startsWith(dim.name() + DELIMITER))
			return false;
		rule = rule.substring(dim.name().length() + DELIMITER.length());

		return true;
	}

	@Override
	public String genRule() {
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}

	@Override
	public String genDesc() {
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}
}
