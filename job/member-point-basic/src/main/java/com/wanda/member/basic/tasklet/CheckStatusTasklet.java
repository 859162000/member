package com.wanda.member.basic.tasklet;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.wanda.member.basic.service.FlagMapper;

public class CheckStatusTasklet implements Tasklet {
	private static final Log logger = LogFactory.getLog(CheckStatusTasklet.class);
	private SqlSessionTemplate sqlSession;
	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		String checkStatus = (String)chunkContext.getStepContext().getJobParameters().get("check_status");
		if(!StringUtils.isEmpty(checkStatus) && "on".equals(checkStatus)){
			FlagMapper mapper = sqlSession.getMapper(FlagMapper.class);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("bizDate", chunkContext.getStepContext().getJobParameters().get("biz_date"));
			int count = mapper.checkStatus(param);
			if(count == 0){
				contribution.setExitStatus(ExitStatus.FAILED);
				logger.info("营业日："+param.get("bizDate")+",状态表T_SYS_DATA_JOB中未达到积分计算开始条件。");
			}
		}
		return RepeatStatus.FINISHED;
	}
	public SqlSessionTemplate getSqlSession() {
		return sqlSession;
	}
	public void setSqlSession(SqlSessionTemplate sqlSession) {
		this.sqlSession = sqlSession;
	}
}
