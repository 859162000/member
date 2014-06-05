package com.wanda.member.countermgt.service.impl;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wanda.member.countermgt.data.RuleChain;
import com.wanda.member.countermgt.data.TMemberRepair;
import com.wanda.member.countermgt.data.TMemberRepairExample;
import com.wanda.member.countermgt.data.TMemberRepairMapper;
import com.wanda.member.countermgt.service.CounterComputeFacade;
import com.wanda.member.countermgt.service.CounterComputeRule;
import com.wanda.member.upgrade.data.TMember;
import com.wanda.member.upgrade.data.TMemberMapper;
import com.wanda.mms.dao.MyBatisDAO;
@Service("counterComputeFacade")
@Scope("prototype")
public class CounterComputeFacadeImpl extends MyBatisDAO implements CounterComputeFacade {
	@Resource(name="rule1")
	CounterComputeRule rule1 = null;
	@Resource(name="setCounterRule")
	CounterComputeRule setCounterRule = null;
	Log log = LogFactory.getLog(CounterComputeFacadeImpl.class);
	@Override
	public void computeByMember(String memberId) {
		log.info("memberid:"+memberId);
		TMemberMapper mapper = sqlSession.getMapper(TMemberMapper.class);
		TMember re = mapper.selectByPrimaryKeyByMemberId(new BigDecimal(memberId));
		computeByMember(re);
	}

	@Override
	public void computeByMember(TMember member) {
		if(member == null||member.getMemberId() ==null||member.getMemberNo()==null){
			log.error(member);
			return;
		}
		RuleChain chain = new RuleChain();
		try {
			log.debug(member);
			rule1.compute(member , chain );
			log.debug(chain.printChain());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		
	}

	@Override
	public void computeCounterbyMemberSet() {
		TMemberRepairMapper repairMapper = sqlSession.getMapper(TMemberRepairMapper.class);
		TMemberRepairExample example = new TMemberRepairExample();
		example.createCriteria().andIsRepairedEqualTo((short)0);
		List<TMemberRepair> list = repairMapper.selectByExample(example);
		log.info("list===:"+list.size());
		for (TMemberRepair tMemberRepair : list) {
			tMemberRepair.setIsRepaired((short)1);
			TMemberRepairExample example1 = new TMemberRepairExample();
			example1.createCriteria().andMemberIdEqualTo(tMemberRepair.getMemberId());
			repairMapper.updateByExample(tMemberRepair, example1 );
			computeByMember(tMemberRepair.getMemberId().toString());
			tMemberRepair.setIsRepaired((short)2);
			example1.createCriteria().andMemberIdEqualTo(tMemberRepair.getMemberId());
			repairMapper.updateByExample(tMemberRepair, example1 );
			
		}
		list = repairMapper.selectByExample(example);
		log.info("list===:"+list.size());
	}

	@Override
	public void setCounter(String memberid, String cinemaId) {
		TMemberMapper mapper = sqlSession.getMapper(TMemberMapper.class);
		TMember re = mapper.selectByPrimaryKeyByMemberId(new BigDecimal(memberid));
		RuleChain chain = new RuleChain();
		try {
			re.setRegistCinemaId(new BigDecimal(cinemaId));
			setCounterRule.compute(re, chain );
		} catch (Exception e) {
			log.error(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
