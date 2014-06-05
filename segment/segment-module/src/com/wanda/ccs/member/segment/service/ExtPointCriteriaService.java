package com.wanda.ccs.member.segment.service;

import java.util.List;
import java.util.Map;

import com.google.code.pathlet.vo.QueryParamVo;
import com.google.code.pathlet.vo.QueryResultVo;
import com.wanda.ccs.member.ap2in.UserProfile;
import com.wanda.ccs.member.segment.vo.ExtPointCriteriaVo;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;



public interface ExtPointCriteriaService {

	ExtPointCriteriaVo get(Long segmentId);
	
	ExtPointCriteriaVo getByCode(String code);

	QueryResultVo<Map<String, Object>> queryList(QueryParamVo parameters, List<ExpressionCriterion> criterionList, UserProfile userinfo);

	void insert(ExtPointCriteriaVo segment);
	
	void update(ExtPointCriteriaVo segment);
	
	void delete(String[] usernames);
	
	void logicDelete(String[] extPointCriteriaIds);
	
	boolean hasSameName(String name, Long selfCriteriaId);
	
	boolean hasReference(Long criteriaId);
	
}