package com.wanda.ccs.data.price;

import java.util.Hashtable;
import java.util.Vector;

import com.wanda.ccs.model.IDimType;

public enum EnumPriceMode implements IDimType {
	/** */
	STANDARD(true, false, true, false),
	/** */
	PERCENT(true, false, true, false),
	/** */
	DEDUCT(true, false, true, false),
	/** */
	FIXED(true, false, true, false),
	/** */
	LOWEST(true, false, true, false),
	/** */
	EXCHANGE(false, true, false, false),
	/** */
	EXCHANGE_STANDARD(false, true, false, false),
	/** */
	EXCHANGE_PERCENT(false, true, false, false),
	/** */
	EXCHANGE_DEDUCT(false, true, false, false),
	/** */
	EXCHANGE_FIXED(false, true, false, false),
	/** */
	EXCHANGE_LOWEST(false, true, false, false),
	/**针对积分票添加的枚举类型 */
	EXCHANGE_POINT(false, false, false, true),
	/** */
	EXCHANGE_POINT_PERCENT(false, false, false, true),
	/** */
	EXCHANGE_POINT_DEDUCT(false, false, false, true),
	/** */
	EXCHANGE_POINT_FIXED(false, false, false, true),
	/** */
	EXCHANGE_POINT_STANDARD(false, false, false, true);

	boolean bForMoney;
	boolean bForTime;
	boolean bForDiscount;
	boolean bForPoint;

	EnumPriceMode(boolean forMoney, boolean forTime, boolean forDiscount, boolean forPoint) {
		bForMoney = forMoney;
		bForTime = forTime;
		bForDiscount = forDiscount;
		bForPoint = forPoint;
	}

	public static EnumPriceMode fromName(String name) {
		for (EnumPriceMode mode : values())
			if (mode.name().equals(name))
				return mode;

		return null;
	}

	public String getName() {
		return name();
	}

	static Hashtable<String, Vector<EnumPriceMode>> HT = new Hashtable<String, Vector<EnumPriceMode>>();

	public static Vector<EnumPriceMode> getForType(String type) {
		if (type == null || !type.equals("M") && !type.equals("T")
				&& !type.equals("D") && !type.equals("P"))
			return null;

		synchronized (HT) {
			Vector<EnumPriceMode> vec = HT.get(type);
			if (vec != null)
				return vec;

			vec = new Vector<EnumPriceMode>();
			for (EnumPriceMode mode : values()) {
				if (type.equals("M") && mode.bForMoney || type.equals("T")
						&& mode.bForTime || type.equals("D")
						&& mode.bForDiscount || type.equals("P") && mode.bForPoint)
					vec.add(mode);
			}

			HT.put(type, vec);
			return vec;
		}

	}
}
