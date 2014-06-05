package com.wanda.ccs.data.price;

import java.util.Map;

public class RuleCondTicketPrice extends RuleCond {
	static final String OPTION_LOWER = "L";
	static final String OPTION_HIGHER = "H";
	static final String OPTION_BETWEEN = "M";
	static final String OPTION_NO = "N";

	private int lowerPrice; // 标准票价的低于多少
	private int highPrice; // //标准票价的高于多少
	private int lprice; // 标准票价的从多少
	private int hprice; // 标准票价的到多少
	private String priceChoose; // 判断选择的RADIO

	public RuleCondTicketPrice(EnumRuleCondType dim) {
		super(dim);
		priceChoose = OPTION_LOWER;
	}

	@Override
	public boolean hasValue() {
		return priceChoose != null && !OPTION_NO.equals(priceChoose);
	}

	public int getLowerPrice() {
		return lowerPrice;
	}

	public void setLowerPrice(int lowerPrice) {
		this.lowerPrice = lowerPrice;
	}

	public int getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(int highPrice) {
		this.highPrice = highPrice;
	}

	public int getLprice() {
		return lprice;
	}

	public void setLprice(int lprice) {
		this.lprice = lprice;
	}

	public int getHprice() {
		return hprice;
	}

	public void setHprice(int hprice) {
		this.hprice = hprice;
	}

	public String getPriceChoose() {
		return priceChoose;
	}

	public void setPriceChoose(String priceChoose) {
		this.priceChoose = priceChoose;
	}

	@Override
	public boolean parseRule(String rule) {
		if (rule == null)
			return false;
		if (!rule.startsWith(dim.name() + DELIMITER))
			return false;
		rule = rule.substring(dim.name().length() + DELIMITER.length());
		if (rule.startsWith(OPTION_LOWER)) {
			priceChoose = rule.substring(0, 1);
			rule = rule.substring(1 + DELIMITER.length());
			String[] list = rule.split(DELIMITER);
			lowerPrice = Integer.parseInt(list[0]);
		}
		if (rule.startsWith(OPTION_HIGHER)) {
			priceChoose = rule.substring(0, 1);
			rule = rule.substring(1 + DELIMITER.length());
			String[] list = rule.split(DELIMITER);
			highPrice = Integer.parseInt(list[0]);
		}
		if (rule.startsWith(OPTION_BETWEEN)) {
			priceChoose = rule.substring(0, 1);
			rule = rule.substring(1 + DELIMITER.length());
			String[] list = rule.split(DELIMITER);
			lprice = Integer.parseInt(list[0]);
			hprice = Integer.parseInt(list[1]);
		}
		return true;
	}

	@Override
	public String genRule() {
		StringBuffer sb = new StringBuffer();
		sb.append(dim.name());
		sb.append(DELIMITER).append(priceChoose);
		if (OPTION_LOWER.equals(priceChoose)) {
			sb.append(DELIMITER).append(lowerPrice);
		}
		if (OPTION_HIGHER.equals(priceChoose)) {
			sb.append(DELIMITER).append(highPrice);
		}
		if (OPTION_BETWEEN.equals(priceChoose)) {
			sb.append(DELIMITER).append(lprice);
			sb.append(DELIMITER).append(hprice);
		}
		return sb.toString();
	}

	@Override
	public String genDesc() {
		StringBuffer sb = new StringBuffer();
		if (OPTION_LOWER.equals(priceChoose)) {
			sb.append("标准票价 < ");
			sb.append(lowerPrice);
			sb.append("元");
		} else if (OPTION_HIGHER.equals(priceChoose)) {
			sb.append("标准票价 >= ");
			sb.append(highPrice);
			sb.append("元");
		} else if (OPTION_BETWEEN.equals(priceChoose)) {
			sb.append(lprice);
			sb.append("元");
			sb.append(" <= 标准票价 < ");
			sb.append(hprice);
			sb.append("元");
		}
		return sb.toString();
	}

	@Override
	public boolean matches(Map<String, Object> params) {
		Integer price = (Integer) params.get(RuleParamTypes.STD_PRICE);
		if (price == null)
			return false;

		if (OPTION_LOWER.equals(priceChoose))
			return price < lowerPrice;
		else if (OPTION_HIGHER.equals(priceChoose))
			return price >= highPrice;
		else if (OPTION_BETWEEN.equals(priceChoose))
			return price >= lprice && price < hprice;

		return false;
	}
}
