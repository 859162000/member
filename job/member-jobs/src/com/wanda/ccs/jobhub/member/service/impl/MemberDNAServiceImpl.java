package com.wanda.ccs.jobhub.member.service.impl;

import org.springframework.transaction.annotation.Transactional;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.dao.MemberDNADao;
import com.wanda.ccs.jobhub.member.dao.MemberDNADao.DnaSql;
import com.wanda.ccs.jobhub.member.service.MemberDNAService;

@Transactional(rollbackFor = { java.lang.Exception.class })
public class MemberDNAServiceImpl implements MemberDNAService {

	@InstanceIn(path = "MemberDNADao")
	private MemberDNADao memberDNADao;
	
	public void calculateMemberByHuangNiu() throws Exception {
		memberDNADao.calculateSql(null, DnaSql.ACX_HUANGNIU);
	}

	public void calculateMemberTicket(String date) throws Exception {
		memberDNADao.calculateSql(date, DnaSql.ACX_BEHAVIOR_FILM_TKT);
	}

	public void calculateMemberBehaviorBase(String date) throws Exception {
		memberDNADao.calculateSql(date, DnaSql.ACX_BEHAVIOR_BASE);
	}

	public void calculateMemberBehaviorIndex() throws Exception {
		memberDNADao.calculateSql(null, DnaSql.ACX_BEHAVIOR_INDEX);
	}

	public void calculateMemberBehaviorStand() throws Exception {
		memberDNADao.calculateSql(null, DnaSql.ACX_BEHAVIOR_STAND);
	}

	public void updateMemberBehaviorStand() throws Exception {
		memberDNADao.calculateSql(null, DnaSql.ACX_BEHAVIOR_STAND_UPDATE);
	}

	public void calculateMemberBehaviorDistance() throws Exception {
		memberDNADao.calculateSql(null, DnaSql.ACX_BEHAVIOR_DISTANCE);
	}

	public void calculateMemberBehaviorSegment(String date) throws Exception {
		memberDNADao.calculateSql(date, DnaSql.ACX_BEHAVIOR_SEGMENT);
	}
	
	public void cleanAllDNA() throws Exception {
		memberDNADao.cleanAllDNA();
	}

}
