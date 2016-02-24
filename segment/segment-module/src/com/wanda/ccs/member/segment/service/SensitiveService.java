package com.wanda.ccs.member.segment.service;

import java.util.List;
import java.util.Map;

import com.google.code.pathlet.vo.QueryParamVo;
import com.google.code.pathlet.vo.QueryResultVo;
import com.wanda.ccs.member.ap2in.UserProfile;
import com.wanda.ccs.member.segment.vo.SensitiveWordVo;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;

public interface SensitiveService {
	void insert(SensitiveWordVo sensitive);
	public SensitiveWordVo get(Long wordId) ;
	QueryResultVo<Map<String, Object>> queryList(QueryParamVo parameters, List<ExpressionCriterion> criterionList, UserProfile userProfile);
	public CriteriaQueryResult getSegmentQuery(long wordId);
	public void update(SensitiveWordVo sensitive) ;
	public void logicDelete(long  wordId) ;
	public boolean hasSameName(String segmentName, Long selfSegmentId);
}
