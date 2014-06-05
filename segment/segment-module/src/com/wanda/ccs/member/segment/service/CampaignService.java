package com.wanda.ccs.member.segment.service;

import java.util.List;
import java.util.Map;

import com.google.code.pathlet.vo.QueryParamVo;
import com.google.code.pathlet.vo.QueryResultVo;
import com.wanda.ccs.member.ap2in.UserProfile;
import com.wanda.ccs.member.segment.vo.CampaignCriteriaVo;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;

public interface CampaignService {
	
	public QueryResultVo<Map<String, Object>> queryList(QueryParamVo parameters, List<ExpressionCriterion> criterionList, UserProfile userinfo);
	
	public QueryResultVo<Map<String, Object>> queryReportList(QueryParamVo queryParam, List<ExpressionCriterion> criteria, UserProfile userinfo);
	
	public boolean hasSameName(String name, Long selfCriteriaId);
	
	public void insert(CampaignCriteriaVo campaign);
	
	public void update(CampaignCriteriaVo campaign);
	
	public void delete(int campaignId);
	
	public CampaignCriteriaVo queryById(int campaignId);
	
	public void updateStatus(int campaignId, String status) throws Exception;
	
	public void updateTime(CampaignCriteriaVo campaign);
	
}