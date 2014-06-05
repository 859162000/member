package com.wanda.member.basic.service;

import java.util.Map;

public interface PointTsMapper {
	public void insertPointHistory(Map<String, Object> param);
	public void insertMemberKpi(Map<String, Object> param);
	public void delPointHistory(Map<String, Object> param);
	public void delMemberKpi(Map<String, Object> param);
}
