package com.wanda.member.activity.reader;

import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.resource.ListPreparedStatementSetter;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JdbcCursorItemReader;

import com.wanda.member.activity.conts.RuleTypeConts;
import com.wanda.member.activity.model.ActivityPointRuleSql;

public class MemberTransReader<T> extends JdbcCursorItemReader<T>{
	private final String CONTEXT_KEY_SUFFIX = "_ACTIVITY_POINT_SQL_LIST";
	
	@BeforeStep
    public void retrieveInterstepData(StepExecution stepExecution) {
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        List<ActivityPointRuleSql> ticketSqlList = (List<ActivityPointRuleSql>)jobContext.get(RuleTypeConts.MEMBER_RULE + CONTEXT_KEY_SUFFIX);
        int sqlIndex = (Integer)stepExecution.getExecutionContext().get("sql_index");
        ActivityPointRuleSql ruleSql = ticketSqlList.get(sqlIndex);
        this.setSql(ruleSql.getSqlStr());
        
        ListPreparedStatementSetter setter = new ListPreparedStatementSetter();
        setter.setParameters(ruleSql.getSqlParams());
        this.setPreparedStatementSetter(setter);
    }
	@Override
	public void afterPropertiesSet() throws Exception {
		this.setSql("Nothing...");
		super.afterPropertiesSet();
	}
}
