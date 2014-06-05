package com.wanda.mms.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.wanda.mms.dao.MyBatisDAO;
import com.wanda.mms.data.TActResult;
import com.wanda.mms.data.TActResultMapper;
@Service("actResultService")
public class ActResultService extends MyBatisDAO{
	public TActResult getActResult(String actResultId){
		TActResultMapper mapper = sqlSession.getMapper(TActResultMapper.class);
		BigDecimal bd = new BigDecimal(actResultId);
		TActResult re = mapper.selectByPrimaryKey(bd);
		return re;
	}

//	public TActResult getActResultByStatus(String status) {
//		TActResultMapper mapper = sqlSession.getMapper(TActResultMapper.class);
//		TActResult re = mapper.selectByStatus(status);
//		return re;
//	}
}
