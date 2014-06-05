package com.wanda.ccs.data.price;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class RuleCondDayOfMonth extends RuleCondList {
	static final Map<String, String> DAY_OF_MONTH = new LinkedHashMap<String, String>();
	static {
		for (int i = 1; i <= 31; i++)
			DAY_OF_MONTH.put(Integer.toString(i), Integer.toString(i));
	}

	public RuleCondDayOfMonth(EnumRuleCondType dim) {
		super(dim);
	}

	public Map<String, String> getList() {
		return DAY_OF_MONTH;
	}

	Object getEveluateParam(Map<String, Object> params) {
		Calendar cal = Calendar.getInstance();
		cal.setTime((Date) params.get(RuleParamTypes.SHOW_TIME));
		return Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
	}
}
