package com.wanda.ccs.data.price;

import java.util.StringTokenizer;
import java.util.Vector;

/**
 * 规则中的价格部分
 * 
 * @author Jim
 * 
 */
public class RulePrice {
	static final String AUTO_EXTRA = "A";

	String type;
	EnumPriceMode mode;
	/** 仅用于兑换规则，为true表示iExtra = max[最低票价 - N x 单次价值, 0] */
	boolean bAutoExtra = false;
	int iPercent = 100;
	int iDeduct;
	int iFixed;
	int iExchange = 1;
	int iExchangePoint = 1000;
	int iExtra;

	public RulePrice(String type) {
		this.type = type;
		mode = EnumPriceMode.getForType(type).elementAt(0);
	}

	public RulePrice copy(RulePrice price) {
		this.type = price.type;
		this.mode = price.mode;
		this.bAutoExtra = price.bAutoExtra;
		this.iPercent = price.iPercent;
		this.iDeduct = price.iDeduct;
		this.iFixed = price.iFixed;
		this.iExchange = price.iExchange;
		this.iExtra = price.iExtra;
		this.iExchangePoint = price.iExchangePoint;
		return this;
	}

	public String getType() {
		return type;
	}

	public Vector<EnumPriceMode> getModes() {
		return EnumPriceMode.getForType(type);
	}

	public EnumPriceMode getMode() {
		return mode;
	}

	public String getModeName() {
		return mode.getName();
	}

	public void setModeName(String name) {
		mode = EnumPriceMode.fromName(name);
	}

	public boolean isAutoExtra() {
		return bAutoExtra;
	}

	public String getAutoExtraFlag() {
		return bAutoExtra ? "T" : "F";
	}

	public void setAutoExtraFlag(String str) {
		bAutoExtra = "T".equalsIgnoreCase(str);
	}

	public void setPercent(int pct) {
		if (pct > 100)
			iPercent = 100;
		else if (pct < 0)
			iPercent = 0;
		else
			iPercent = pct;
	}

	public int getPercent() {
		return iPercent;
	}

	public void setDeduct(int val) {
		iDeduct = val;
	}

	public int getDeduct() {
		return iDeduct;
	}

	public void setFixed(int val) {
		iFixed = val;
	}

	public int getFixed() {
		return iFixed;
	}

	public void setExchange(int val) {
		iExchange = val;
	}

	public int getExchange() {
		return iExchange;
	}

	public void setExtra(int val) {
		iExtra = val;
	}

	public int getExtra() {
		return iExtra;
	}

	public int getExchangePoint() {
		return iExchangePoint;
	}

	public void setExchangePoint(int val) {
		this.iExchangePoint = val;
	}

	void parseExtra(String str) {
		if (AUTO_EXTRA.equals(str)) {
			bAutoExtra = true;
			iExtra = 0;
		} else {
			bAutoExtra = false;
			iExtra = Integer.parseInt(str);
		}
	}

	public void parseRule(String rule) {
		try {
			StringTokenizer st = new StringTokenizer(rule, "!");

			type = st.nextToken();
			mode = EnumPriceMode.fromName(st.nextToken());
			switch (mode) {
			case PERCENT:
				iPercent = Integer.parseInt(st.nextToken());
				break;
			case DEDUCT:
				iDeduct = Integer.parseInt(st.nextToken());
				break;
			case FIXED:
				iFixed = Integer.parseInt(st.nextToken());
				break;
			case STANDARD:
				break;
			case LOWEST:
				break;
			case EXCHANGE:
			case EXCHANGE_STANDARD:
			case EXCHANGE_LOWEST:
				iExchange = Integer.parseInt(st.nextToken());
				parseExtra(st.nextToken());
				break;
			case EXCHANGE_PERCENT:
				iExchange = Integer.parseInt(st.nextToken());
				iPercent = Integer.parseInt(st.nextToken());
				parseExtra(st.nextToken());
				break;
			case EXCHANGE_DEDUCT:
				iExchange = Integer.parseInt(st.nextToken());
				iDeduct = Integer.parseInt(st.nextToken());
				parseExtra(st.nextToken());
				break;
			case EXCHANGE_FIXED:
				iExchange = Integer.parseInt(st.nextToken());
				iFixed = Integer.parseInt(st.nextToken());
				parseExtra(st.nextToken());
			case EXCHANGE_POINT:
			case EXCHANGE_POINT_STANDARD:
				iExchangePoint = Integer.parseInt(st.nextToken());
				break;
			case EXCHANGE_POINT_PERCENT:
				iExchangePoint = Integer.parseInt(st.nextToken());
				iPercent = Integer.parseInt(st.nextToken());
				break;
			case EXCHANGE_POINT_DEDUCT:
				iExchangePoint = Integer.parseInt(st.nextToken());
				iDeduct = Integer.parseInt(st.nextToken());
				break;
			case EXCHANGE_POINT_FIXED:
				iExchangePoint = Integer.parseInt(st.nextToken());
				iFixed = Integer.parseInt(st.nextToken());
				break;
			}
		} catch (Exception e) {
		}
	}

	public String getRule() {
		StringBuffer sb = new StringBuffer();

		sb.append(type).append("!").append(mode.name());
		switch (mode) {
		case PERCENT:
			sb.append("!").append(iPercent);
			break;
		case DEDUCT:
			sb.append("!").append(iDeduct);
			break;
		case FIXED:
			sb.append("!").append(iFixed);
			break;
		case STANDARD:
			break;
		case LOWEST:
			break;
		case EXCHANGE:
		case EXCHANGE_STANDARD:
		case EXCHANGE_LOWEST:
			sb.append("!").append(iExchange).append("!")
					.append(bAutoExtra ? AUTO_EXTRA : iExtra);
			break;
		case EXCHANGE_PERCENT:
			sb.append("!").append(iExchange).append("!").append(iPercent)
					.append("!").append(bAutoExtra ? AUTO_EXTRA : iExtra);
			break;
		case EXCHANGE_DEDUCT:
			sb.append("!").append(iExchange).append("!").append(iDeduct)
					.append("!").append(bAutoExtra ? AUTO_EXTRA : iExtra);
			break;
		case EXCHANGE_FIXED:
			sb.append("!").append(iExchange).append("!").append(iFixed)
					.append("!").append(bAutoExtra ? AUTO_EXTRA : iExtra);
			break;
		case EXCHANGE_POINT:
		case EXCHANGE_POINT_STANDARD:
			sb.append("!").append(iExchangePoint);
			break;
		case EXCHANGE_POINT_PERCENT:
			sb.append("!").append(iExchangePoint).append("!").append(iPercent);
			break;
		case EXCHANGE_POINT_DEDUCT:
			sb.append("!").append(iExchangePoint).append("!").append(iDeduct);
			break;
		case EXCHANGE_POINT_FIXED:
			sb.append("!").append(iExchangePoint).append("!").append(iFixed);
			break;
		}

		return sb.toString();
	}

	public String getDesc() {
		StringBuffer sb = new StringBuffer();

		if (mode != null)
			switch (mode) {
			case PERCENT:
				sb.append("按标准票价折扣").append(iPercent).append("%");
				break;
			case DEDUCT:
				sb.append("按标准票价扣减").append(iDeduct).append("元");
				break;
			case FIXED:
				sb.append("指定票价").append(iFixed).append("元");
				break;
			case STANDARD:
				sb.append("按标准票价");
				break;
			case LOWEST:
				sb.append("按最低票价");
				break;
			case EXCHANGE:
			case EXCHANGE_STANDARD:
			case EXCHANGE_PERCENT:
			case EXCHANGE_DEDUCT:
			case EXCHANGE_FIXED:
			case EXCHANGE_LOWEST:
				sb.append(iExchange).append("兑1");
				if (bAutoExtra)
					sb.append("自动计算补差");
				else if (iExtra != 0)
					sb.append("补").append(iExtra).append("元");

				switch (mode) {
				case EXCHANGE_STANDARD:
					sb.append("(票房价格按标准票价)");
					break;
				case EXCHANGE_PERCENT:
					sb.append("(票房价格按标准票价折扣").append(iPercent).append("%)");
					break;
				case EXCHANGE_DEDUCT:
					sb.append("(票房价格按标准票价扣减").append(iDeduct).append("元)");
					break;
				case EXCHANGE_FIXED:
					sb.append("(票房价格指定票价").append(iFixed).append("元)");
					break;
				case EXCHANGE_LOWEST:
					sb.append("(票房价格按最低票价)");
					break;
				}
				break;
			case EXCHANGE_POINT:
			case EXCHANGE_POINT_PERCENT:
			case EXCHANGE_POINT_DEDUCT:
			case EXCHANGE_POINT_FIXED:
			case EXCHANGE_POINT_STANDARD:
				sb.append(iExchangePoint).append("分兑1");

				switch (mode) {
				case EXCHANGE_POINT_PERCENT:
					sb.append("(票房价格按标准票价折扣").append(iPercent).append("%)");
					break;
				case EXCHANGE_POINT_DEDUCT:
					sb.append("(票房价格按标准票价扣减").append(iDeduct).append("元)");
					break;
				case EXCHANGE_POINT_FIXED:
					sb.append("(票房价格指定票价").append(iFixed).append("元)");
					break;
				case EXCHANGE_POINT_STANDARD:
					sb.append("(票房价格按标准票价)");
					break;
				}
				break;
			}

		return sb.toString();
	}
}
