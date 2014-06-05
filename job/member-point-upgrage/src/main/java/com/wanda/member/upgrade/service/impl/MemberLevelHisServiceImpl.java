package com.wanda.member.upgrade.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.wanda.member.upgrade.data.TLevelHistory;
import com.wanda.member.upgrade.data.TLevelHistoryMapper;
import com.wanda.member.upgrade.service.MemberLevelHisService;
import com.wanda.mms.dao.MyBatisDAO;

@Service("memberLevelHisService")
public class MemberLevelHisServiceImpl extends MyBatisDAO implements MemberLevelHisService {

	@Override
	public void doInsertMemberLevelHis(TLevelHistory record) {
		TLevelHistoryMapper mapper = sqlSession.getMapper(TLevelHistoryMapper.class);
		mapper.insert(record);
	}

	@Override
	public void doDeleteMemberLevelHis(long seq) {
		TLevelHistoryMapper mapper = sqlSession.getMapper(TLevelHistoryMapper.class);
		mapper.deleteByPrimaryKey(new BigDecimal(seq));
	}

}
