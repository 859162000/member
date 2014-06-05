package com.wanda.ccs.data.price;

import java.util.LinkedHashMap;
import java.util.Map;

public class RuleCondCustomer extends RuleCondList {
	static final Map<String, String> CUSTOMER = new LinkedHashMap<String, String>();
	static {
		CUSTOMER.put("1", "男");
		CUSTOMER.put("2", "女");
		CUSTOMER.put("3", "生日");
	}

	public RuleCondCustomer(EnumRuleCondType dim) {
		super(dim);
	}

	public Map<String, String> getList() {
		return CUSTOMER;
	}
}
