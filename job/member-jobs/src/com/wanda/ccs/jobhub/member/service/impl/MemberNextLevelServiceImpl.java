package com.wanda.ccs.jobhub.member.service.impl;

import org.springframework.transaction.annotation.Transactional;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.dao.MemberNextLevelDao;
import com.wanda.ccs.jobhub.member.service.MemberNextLevelService;

@Transactional(rollbackFor = { java.lang.Exception.class })
public class MemberNextLevelServiceImpl implements MemberNextLevelService {

	@InstanceIn(path = "MemberNextLevelDao")
	private MemberNextLevelDao memberNextLevelDao;

	public void clearMemberNextLevel() {
		memberNextLevelDao.clearMemberNextLevel();
	}
	
	public void calculateMemberNextLevel(String yyyy) {
		memberNextLevelDao.calculateMemberNextLevel(yyyy);
	}
	
	public void updateMemberNextLevel() {
		memberNextLevelDao.updateMemberNextLevel();
	}

	public boolean existTempDate() {
		int index = memberNextLevelDao.existTempDate();
		if(index > 0) {
			return true;
		}
		
		return false;
	}
	
}
