package com.wanda.ccs.auth;

public enum UserLevel {
	GROUP("院线"), REGION("区域"), CINEMA("影城");

	String levelName;

	UserLevel(String name) {
		levelName = name;
	}

	public String getLevelName() {
		return levelName;
	}

	public static UserLevel fromName(String name) {
		for (UserLevel l : UserLevel.values())
			if (l.name().equalsIgnoreCase(name))
				return l;
		return null;
	}
}
