package com.wanda.member.basic.service;

import java.util.Map;

public interface FlagMapper {
	public void updateStatus(Map<String, Object> param);
	public int checkStatus(Map<String, Object> param);
}
