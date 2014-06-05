package com.wanda.member.basic.partitioner;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import com.wanda.member.basic.service.TransMapper;

public class GoodsPartitioner implements Partitioner {
	
	private SqlSessionTemplate dwSqlSession;
	private String bizDate;
	
	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		
		TransMapper transMapper = dwSqlSession.getMapper(TransMapper.class);
		List<String> innerCodeList = transMapper.selectGoodsTransCinema(this.getBizDate());
		Map<String, ExecutionContext> results = new LinkedHashMap<String, ExecutionContext>();
		for(String innerCode : innerCodeList) {
			ExecutionContext context = new ExecutionContext();
			context.put("innerCode", innerCode);
			results.put("partition.goods."+innerCode, context);
		}
		return results;
	}
	public SqlSessionTemplate getDwSqlSession() {
		return dwSqlSession;
	}
	public void setDwSqlSession(SqlSessionTemplate dwSqlSession) {
		this.dwSqlSession = dwSqlSession;
	}
	public String getBizDate() {
		return bizDate;
	}
	public void setBizDate(String bizDate) {
		this.bizDate = bizDate;
	}
	
}
