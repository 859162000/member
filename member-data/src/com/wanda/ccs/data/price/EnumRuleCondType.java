package com.wanda.ccs.data.price;

import java.util.Hashtable;
import java.util.Vector;

import com.wanda.ccs.model.IDimType;

public enum EnumRuleCondType implements IDimType {
	CHANNEL(LDIMTYPE_OPEN_CHANNEL, "购票渠道", true, false, RuleCondDim.class), // 购票渠道
	HALL_TYPE(LDIMTYPE_HALL_TYPE, "影厅类型", true, true, RuleCondDim.class), // 影厅类型
	PROJECT_TYPE(LDIMTYPE_FILM_SHOW_SET, "放映制式", true, true, RuleCondDim.class), // 放映制式
	SCENE_TYPE(LDIMTYPE_SCENETYPE, "场次类型", true, true, RuleCondDim.class), // 场次类型
	HOLIDAY(LDIMTYPE_HOLIDAY, "假日", true, true, RuleCondDim.class), // 假日
	DAY_OF_WEEK(0, "星期", true, true, RuleCondDayOfWeek.class), // 星期
	DAY_OF_MONTH(0, "月日", true, true, RuleCondDayOfMonth.class), // 月日
	TIME_RANGE(0, "时间范围", true, true, RuleCondTimeRange.class), // 时间范围
	DATE_RANGE(0, "日期范围", true, true, RuleCondDateRange.class), // 日期范围
	TICKET_PRICE(0, "标准票价", true, true, RuleCondTicketPrice.class), // 标准票价范围
	CITY(0, "城市", true, true, RuleCondCity.class), // 城市
	CINEMA(0, "影城", true, true, RuleCondCinema.class), // 影城
	FILM(0, "电影", true, true, RuleCondFilm.class), // 电影
	CUSTOMER(0, "客户身份", false, false, RuleCondCustomer.class), // 客户身份
	TICKET_NUMBER(0, "购票数量", false, false, RuleCondTicketNum.class), // 购票数量
	LOWER_PRICRE(0,"最低票价", true, true, RuleCondLowerPrice.class), //最低票价
	HALL_NO(0,"影厅号", true, true, RuleCondHallNo.class); //影厅号

	long dimType;
	String title;
	boolean forLimit;
	boolean forPrice;
	Class<? extends RuleCond> impl;

	EnumRuleCondType(long dimType, String title, boolean forLimit,
			boolean forPrice, Class<? extends RuleCond> impl) {
		this.dimType = dimType;
		this.title = title;
		this.forLimit = forLimit;
		this.forPrice = forPrice;
		this.impl = impl;
	}

	public static EnumRuleCondType fromName(String name) {
		for (EnumRuleCondType type : values())
			if (type.name().equals(name))
				return type;
		return null;
	}

	static Hashtable<EnumRuleType, Vector<EnumRuleCondType>> HT = new Hashtable<EnumRuleType, Vector<EnumRuleCondType>>();

	public static Vector<EnumRuleCondType> getTypes(EnumRuleType ruleType) {
		if (ruleType == null)
			return null;

		synchronized (HT) {
			Vector<EnumRuleCondType> vec = HT.get(ruleType);

			if (vec != null)
				return vec;

			vec = new Vector<EnumRuleCondType>();

			for (EnumRuleCondType type : EnumRuleCondType.values()) {
				if (ruleType == EnumRuleType.LIMIT && type.forLimit
						|| ruleType == EnumRuleType.USE && type.forPrice)
					vec.add(type);
			}
			HT.put(ruleType, vec);

			return vec;
		}
	}

	public RuleCond newImpl() throws Exception {
		return impl.getConstructor(EnumRuleCondType.class).newInstance(this);
	}

	public String getName() {
		return name();
	}

	public String getTitle() {
		return title;
	}

	public static String toMenuOptions(Vector<EnumRuleCondType> types) {
		StringBuffer sb = new StringBuffer();
		boolean bFirst = true;
		for (EnumRuleCondType type : types) {
			if (bFirst)
				bFirst = false;
			else
				sb.append(",");
			sb.append("{\"type\": \"option\", \"value\": \"" + type.name()
					+ "\", \"label\": \"" + type.title + "\"}");
		}

		return sb.toString();
	}
}
