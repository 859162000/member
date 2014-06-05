package com.wanda.ccs.member.segment.service;

import java.util.List;
import java.util.Map;

import com.wanda.ccs.sqlasm.ClauseResult;
import com.wanda.ccs.sqlasm.CriteriaResult;
import com.wanda.ccs.sqlasm.DataType;

public class CriteriaQueryResult implements CriteriaResult {
	
	private String orderBySql;
	
	private CriteriaResult criteriaResult;
	
	public CriteriaQueryResult(CriteriaResult criteriaResult, String orderBySql) {
		this.criteriaResult = criteriaResult;
		this.orderBySql = orderBySql;
	}
	
	public String getOrderBySql() {
		return orderBySql;
	}

	public List<Object> getParameters() {
		return criteriaResult.getParameters();
	}

	public List<DataType> getParameterTypes() {
		return criteriaResult.getParameterTypes();
	}

	public String getComposedText() {
		return criteriaResult.getComposedText();
	}

	public String getParameterizeText() {
		return criteriaResult.getParameterizeText();
	}

	public Map<String, List<ClauseResult>> getParagraphedResults() {
		return criteriaResult.getParagraphedResults();
	}

	public boolean isEmpty() {
		return criteriaResult.isEmpty();
	}
	
	
	

}
