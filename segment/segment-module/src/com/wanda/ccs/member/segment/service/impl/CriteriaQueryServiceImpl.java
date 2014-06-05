package com.wanda.ccs.member.segment.service.impl;

import static com.wanda.ccs.member.segment.defimpl.ExtPointCriteriaDef.GROUP_ID_CON_ITEM;
import static com.wanda.ccs.member.segment.defimpl.ExtPointCriteriaDef.GROUP_ID_MEMBER;
import static com.wanda.ccs.member.segment.defimpl.ExtPointCriteriaDef.GROUP_ID_TICKET;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.wanda.ccs.member.segment.defimpl.ExtPointCriteriaDef;
import com.wanda.ccs.member.segment.defimpl.SegmentCriteriaDef;
import com.wanda.ccs.member.segment.service.CriteriaQueryResult;
import com.wanda.ccs.member.segment.service.CriteriaQueryService;
import com.wanda.ccs.sqlasm.CriteriaParseException;
import com.wanda.ccs.sqlasm.CriteriaResult;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;
import com.wanda.ccs.sqlasm.expression.JsonCriteriaHelper;

public class CriteriaQueryServiceImpl implements CriteriaQueryService {
	
	private SegmentCriteriaDef segmentCriteriaDef = new SegmentCriteriaDef();
	
	private ExtPointCriteriaDef extPointCriteriaDef = new ExtPointCriteriaDef();
	
	public CriteriaQueryResult getSegmentQuery(String criteriaJson, String sortNameCode, String sortOrder)
			throws CriteriaParseException {
		
		List<ExpressionCriterion> criteria = JsonCriteriaHelper.parse(criteriaJson);
		CriteriaResult result = segmentCriteriaDef.getParser().parse(criteria);
		
		//拼装order by语句
		String orderBySql = null;
		String orderByCol = segmentCriteriaDef.getOrderByMap().get(sortNameCode);
		if(orderByCol != null) {
			StringBuilder buf = new StringBuilder();
			buf.append("order by ").append(orderByCol).append(" ").append(sortOrder);
			orderBySql = buf.toString();
		}
		
		
		return new CriteriaQueryResult(result, orderBySql);
	}

	
	public CriteriaResult getExtPointConSaleQuery(List<ExpressionCriterion> criteria) throws CriteriaParseException {
		//List<ExpressionCriterion> criteria = JsonCriteriaHelper.parse(criteriaJson);
		Set<String> groupIds = getGroupIdSet(criteria);
		
		//存在卖品交易组条件， 或仅会员基本组条件时才返回结果集
		if(groupIds.contains(GROUP_ID_CON_ITEM)
			|| (groupIds.contains(GROUP_ID_MEMBER) && groupIds.contains(GROUP_ID_TICKET) == false && groupIds.contains(GROUP_ID_CON_ITEM) == false)) {
			return extPointCriteriaDef.getConSaleParser().parse(criteria);
		}
		else {
			return null;
		}

	} 

	public CriteriaResult getExtPointTicketQuery(List<ExpressionCriterion> criteria) throws CriteriaParseException {
		//List<ExpressionCriterion> criteria = JsonCriteriaHelper.parse(criteriaJson);
		Set<String> groupIds = getGroupIdSet(criteria);
		
		//存在卖品交易组条件， 或仅会员基本组条件时才返回结果集
		if(groupIds.contains(GROUP_ID_TICKET)
				|| (groupIds.contains(GROUP_ID_MEMBER) && groupIds.contains(GROUP_ID_TICKET) == false && groupIds.contains(GROUP_ID_CON_ITEM) == false)) {
			return extPointCriteriaDef.getTicketSaleParser().parse(criteria);
		}
		else {
			return null;
		}
	}
	
	public CriteriaResult getExtPointMemberQuery(List<ExpressionCriterion> criteria) throws CriteriaParseException {
		//List<ExpressionCriterion> criteria = JsonCriteriaHelper.parse(criteriaJson);
		Set<String> groupIds = getGroupIdSet(criteria);
		
		//存在卖品交易组条件， 或仅会员基本组条件时才返回结果集
		if(groupIds.contains(GROUP_ID_TICKET)
				|| (groupIds.contains(GROUP_ID_MEMBER) && groupIds.contains(GROUP_ID_TICKET) == false && groupIds.contains(GROUP_ID_CON_ITEM) == false)) {
			return extPointCriteriaDef.getMemberParser().parse(criteria);
		}
		else {
			return null;
		}
	}
	
	private Set<String> getGroupIdSet(List<ExpressionCriterion> criteria) {
		HashSet<String> groupIds = new HashSet<String>();
		
		for(ExpressionCriterion cri : criteria) {
			String groupId = cri.getGroupId();
			if(groupIds.contains(groupId) == false) {
				groupIds.add(groupId);
			}
		}
		
		return groupIds;
	}

//	private boolean hasValueDepends(CriteriaResult result) {
//		boolean foundValueDepends = false;
//		Map<String, List<ClauseResult>> groupdResult = result.getParagraphedResults();
//		for(List<ClauseResult> group : groupdResult.values()) {
//			for(ClauseResult cr : group) {
//				if(!(cr.getSrcClause() instanceof SqlClause)) {
//					foundValueDepends = true;
//				}
//			}
//		}
//		
//		return foundValueDepends;
//	}
	
}
