package com.wanda.member.activity.processor;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemProcessor;

import com.wanda.member.activity.model.ActivityPointMember;
import com.wanda.member.activity.service.TPointHistoryMapper;


public class MemberProcessor implements ItemProcessor<ActivityPointMember, ActivityPointMember>{
	private static final Log logger = LogFactory.getLog(MemberProcessor.class);
	private int pointAdditionCode;
	private int extPointRuleId;
	private int extPointCriteriaId;
	private String bizDate;
	
	private SqlSessionTemplate mbrSqlSession;
	@Override
	public ActivityPointMember process(ActivityPointMember member) throws Exception {
		member.setPointAdditionCode(pointAdditionCode);
		member.setExtPointRuleId(extPointRuleId);
		member.setExtPointCriteriaId(extPointCriteriaId);
		member.setBizDate(bizDate);
		
		TPointHistoryMapper tpMapper = mbrSqlSession.getMapper(TPointHistoryMapper.class);
		int count = tpMapper.countActivityPoint(member);
		if(count > 0){
			return null;
		}
		return member;
	}
	public int getPointAdditionCode() {
		return pointAdditionCode;
	}
	public void setPointAdditionCode(int pointAdditionCode) {
		this.pointAdditionCode = pointAdditionCode;
	}
	public int getExtPointRuleId() {
		return extPointRuleId;
	}
	public void setExtPointRuleId(int extPointRuleId) {
		this.extPointRuleId = extPointRuleId;
	}
	public int getExtPointCriteriaId() {
		return extPointCriteriaId;
	}
	public void setExtPointCriteriaId(int extPointCriteriaId) {
		this.extPointCriteriaId = extPointCriteriaId;
	}
	public String getBizDate() {
		return bizDate;
	}
	public void setBizDate(String bizDate) {
		this.bizDate = bizDate;
	}
	
	public SqlSessionTemplate getMbrSqlSession() {
		return mbrSqlSession;
	}
	public void setMbrSqlSession(SqlSessionTemplate mbrSqlSession) {
		this.mbrSqlSession = mbrSqlSession;
	}
	@AfterStep
	public void afterStep(StepExecution stepExecution){
		long endTime = new Date().getTime();
		long startTime = stepExecution.getStartTime().getTime();
		long runTime = endTime - startTime;
		logger.info("会员记录，BIZ_DATE:"+this.getBizDate()+",规则编号："+this.getExtPointRuleId()+",需要处理的会员记录数量:"+stepExecution.getWriteCount()+",用时:"+runTime+" ms");
	}
}
