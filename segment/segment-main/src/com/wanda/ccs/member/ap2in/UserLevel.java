package com.wanda.ccs.member.ap2in;

public enum UserLevel {
	GROUP, REGION, CINEMA;

	public static UserLevel fromName(String name) {
		for (UserLevel l : UserLevel.values())
			if (l.name().equalsIgnoreCase(name))
				return l;
		return null;
	}
}
