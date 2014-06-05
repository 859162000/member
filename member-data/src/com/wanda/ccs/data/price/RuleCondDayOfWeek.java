package com.wanda.ccs.data.price;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class RuleCondDayOfWeek extends RuleCondList {
	static final Map<String, String> DAY_OF_WEEK = new LinkedHashMap<String, String>();
	static {
		DAY_OF_WEEK.put("1", "一");
		DAY_OF_WEEK.put("2", "二");
		DAY_OF_WEEK.put("3", "三");
		DAY_OF_WEEK.put("4", "四");
		DAY_OF_WEEK.put("5", "五");
		DAY_OF_WEEK.put("6", "六");
		DAY_OF_WEEK.put("7", "日");
	}

	public RuleCondDayOfWeek(EnumRuleCondType dim) {
		super(dim);
	}

	public Map<String, String> getList() {
		return DAY_OF_WEEK;
	}

	Object getEveluateParam(Map<String, Object> params) {
		Calendar cal = Calendar.getInstance();
		cal.setTime((Date) params.get(RuleParamTypes.SHOW_TIME));
		cal.add(Calendar.HOUR_OF_DAY, -6);

		switch (cal.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.MONDAY:
			return "1";
		case Calendar.TUESDAY:
			return "2";
		case Calendar.WEDNESDAY:
			return "3";
		case Calendar.THURSDAY:
			return "4";
		case Calendar.FRIDAY:
			return "5";
		case Calendar.SATURDAY:
			return "6";
		default:
			return "7";
		}
	}
}
