package com.wanda.member.basic.tasklet;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.wanda.member.basic.service.FlagMapper;

public class UpdataStatusTasklet implements Tasklet {
	private SqlSessionTemplate sqlSession;
	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		FlagMapper mapper = sqlSession.getMapper(FlagMapper.class);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("bizDate", chunkContext.getStepContext().getJobParameters().get("biz_date"));
		mapper.updateStatus(param);
		return RepeatStatus.FINISHED;
	}
	public SqlSessionTemplate getSqlSession() {
		return sqlSession;
	}
	public void setSqlSession(SqlSessionTemplate sqlSession) {
		this.sqlSession = sqlSession;
	}
}
