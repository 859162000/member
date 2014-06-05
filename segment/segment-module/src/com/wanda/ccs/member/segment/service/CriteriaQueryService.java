package com.wanda.ccs.member.segment.service;

import java.util.List;

import com.wanda.ccs.sqlasm.CriteriaParseException;
import com.wanda.ccs.sqlasm.CriteriaResult;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;

public interface CriteriaQueryService {
	
	/**
	 * 
	 * @param criteriaJson 可全查询定义JSON信息，来自表 T_SEGMENT.criteria_scheme字段
	 * @param sortName     客群排序名称（dimdef中的code值）,如果为空表示不输出结果对象中的querySqlWithoutOrderBy属性,来自表 T_SEGMENT.sort_name 字段
	 * @param sortOrder    客群排序的次序，'desc'为倒序， 'asc'为正序，如果为空表示不输出结果对象中的querySqlWithoutOrderBy属性，  来自表 T_SEGMENT.sort_order 字段
	 * @return
	 */
	CriteriaQueryResult getSegmentQuery(String criteriaJson, String sortNameCode, String sortOrder) throws CriteriaParseException;
	
	/**
	 * 得到特殊积分条件中，卖品交易的查询的条件。
	 * @param criteriaJson
	 * @param sortNameCode
	 * @return  返回对象中只有 parameter和querySql属性需要使用。 orderBySql不需要使用，为null.
	 * @throws CriteriaParseException
	 */
	CriteriaResult getExtPointConSaleQuery(List<ExpressionCriterion> criteria) throws CriteriaParseException;
	
	
	/**
	 * 得到特殊积分条件中，票交易查询的条件。
	 * @param criteriaJson
	 * @param sortNameCode
	 * @return
	 * @throws CriteriaParseException
	 */
	CriteriaResult getExtPointTicketQuery(List<ExpressionCriterion> criteria) throws CriteriaParseException;
	
	
	/**
	 * 得到特殊积分条件中，会员查询的条件
	 * @param criteriaJson
	 * @param sortNameCode
	 * @return
	 * @throws CriteriaParseException
	 */
	CriteriaResult getExtPointMemberQuery(List<ExpressionCriterion> criteria) throws CriteriaParseException;
	
}
