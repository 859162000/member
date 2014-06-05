package com.wanda.ccs.member.segment.service;

import java.util.List;
import java.util.Map;

import javax.mail.search.SearchException;

import com.google.code.pathlet.vo.QueryParamVo;
import com.google.code.pathlet.vo.QueryResultVo;
import com.wanda.ccs.member.ap2in.UserProfile;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;



public interface CompositeQueryService {

	/**
	 * 组合查询影城
	 * @param queryParam
	 * @param criteria
	 * @param user 
	 * @return
	 * @throws SearchException
	 */
	QueryResultVo<Map<String, Object>> queryCinemas(QueryParamVo queryParam, List<ExpressionCriterion> criteria, UserProfile userProfile);
	
	/**
	 * 组合查询营销活动
	 * @param queryParam
	 * @param criteria
	 * @param user 
	 * @return
	 * @throws SearchException
	 */
	QueryResultVo<Map<String, Object>> querySegment(QueryParamVo queryParam, List<ExpressionCriterion> criteria, UserProfile userProfile);
	
	/**
	 * 
	 * @param queryParam
	 * @param criteria
	 * @param userProfile
	 * @return
	 */
	QueryResultVo<Map<String, Object>> queryCampaignSegment(QueryParamVo queryParam, List<ExpressionCriterion> criteria, UserProfile userProfile);
	
	/**
	 * 组合查询活动
	 * @param queryParam
	 * @param criteria
	 * @return
	 * @throws SearchException
	 */
	QueryResultVo<Map<String, Object>> queryActivitys(QueryParamVo queryParam, List<ExpressionCriterion> criteria);
	
	/**
	 * 组合查询卖品品项
	 * @param queryParam
	 * @param criteria
	 * @return
	 * @throws SearchException
	 */
	QueryResultVo<Map<String, Object>> queryConItems(QueryParamVo queryParam, List<ExpressionCriterion> criteria);

	
	/**
	 * 组合查询影片
	 * @param queryParam
	 * @param criteria
	 * @return
	 * @throws SearchException
	 */
	QueryResultVo<Map<String, Object>> queryFilms(QueryParamVo queryParam, List<ExpressionCriterion> criteria);
	
	
	/**
	 * 组合查询用户
	 * @param queryParam
	 * @param criteria
	 * @return
	 * @throws SearchException
	 */
	QueryResultVo<Map<String, Object>> queryAuthUsers(QueryParamVo queryParam, List<ExpressionCriterion> criteria);
	
}