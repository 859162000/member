package com.wanda.member.upgrade.service.impl;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wanda.member.upgrade.data.TMemberLevel;
import com.wanda.member.upgrade.data.TMemberLevelMapper;
import com.wanda.member.upgrade.service.MemberLevelService;
import com.wanda.mms.dao.MyBatisDAO;
@Service("memberLevelService")
@Scope("prototype")
public class MemberLevelServiceImpl extends MyBatisDAO implements MemberLevelService {
	Log loger =LogFactory.getLog(MemberLevelServiceImpl.class);
	public TMemberLevel getMemberLevel(BigDecimal memberId) {
		TMemberLevelMapper memberLevelmapper = sqlSession.getMapper(TMemberLevelMapper.class);
		loger.info("get member level ,member id is"+memberId);
		TMemberLevel memberLevel = memberLevelmapper.selectByPrimaryKey(memberId);
		loger.info("member level "+memberLevel);
		return memberLevel;
	}

	@Override
	public void doUpdateMemberLevel(TMemberLevel memberLevel) {
		TMemberLevelMapper memberLevelmapper = sqlSession.getMapper(TMemberLevelMapper.class);
		if(memberLevel.getMemberId()==null){
			return;
		}
		memberLevelmapper.updateByPrimaryKeySelective(memberLevel);
	}
	
}
