package com.wanda.ccs.data.price;

public enum EnumRuleType {
	LIMIT(false), USE(true);

	boolean bHasPrice;

	EnumRuleType(boolean hasPrice) {
		bHasPrice = hasPrice;
	}

	public boolean isHasPrice() {
		return bHasPrice;
	}
}
