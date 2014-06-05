package com.wanda.member.countermgt.service.impl;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.wanda.member.countermgt.data.RuleChain;
import com.wanda.member.countermgt.data.TCinema;
import com.wanda.member.countermgt.data.TCinemaExample;
import com.wanda.member.countermgt.data.TCinemaExample.Criteria;
import com.wanda.member.countermgt.data.TCinemaMapper;
import com.wanda.member.countermgt.data.TCinemaTicket;
import com.wanda.member.countermgt.data.TCounterComputeMapper;
import com.wanda.member.countermgt.data.TDConMember;
import com.wanda.member.countermgt.data.TDConMemberExample;
import com.wanda.member.countermgt.data.TDConMemberMapper;
import com.wanda.member.countermgt.data.TMemberInfo;
import com.wanda.member.countermgt.data.TMemberInfoExample;
import com.wanda.member.countermgt.data.TMemberInfoHistory;
import com.wanda.member.countermgt.data.TMemberInfoHistoryMapper;
import com.wanda.member.countermgt.data.TMemberInfoMapper;
import com.wanda.member.countermgt.service.CounterComputeServcie;
import com.wanda.member.exception.NullCinemaException;
import com.wanda.member.exception.NullMemberInfoException;
import com.wanda.member.upgrade.data.TMember;
import com.wanda.member.upgrade.data.TMemberLevelMapper;
import com.wanda.member.upgrade.data.TMemberMapper;
import com.wanda.mms.dao.MyBatisDAO;
@Service("counterComputeServcie")
public class CounterComputeServcieImpl extends MyBatisDAO implements CounterComputeServcie {
	Log log = LogFactory.getLog(CounterComputeServcieImpl.class);
	/**
	 * rule1
	 */
	@Override
	public List<TCinemaTicket> getTicketCountCatalogByClima(BigDecimal memberid) {
		log.debug("getTicketCountCatalogByClima memberKey:"+memberid);
		TCounterComputeMapper mapper = sqlSessionDw.getMapper(TCounterComputeMapper.class);
		return mapper.getTicketsCatalogByCinema(memberid);
	}
	/**
	 * rule2
	 */
	@Override
	public List<TCinemaTicket> getPointExchangeByCinema(BigDecimal memberid) {
		log.debug("getPointExchangeByCinema memberKey:"+memberid);
		TCounterComputeMapper mapper = sqlSessionDw.getMapper(TCounterComputeMapper.class);
		return  mapper.getPointExchangeByCinema(memberid);
	}
	/**
	 * rule3
	 */
	@Override
	public TCinemaTicket getTicketLastBuy(BigDecimal memberid) {
		log.debug("getTicketLastBuy memberKey:"+memberid);
		TCounterComputeMapper mapper = sqlSessionDw.getMapper(TCounterComputeMapper.class);
		return mapper.getTicketLastBuy(memberid);
	}
	/**
	 * rule4
	 */
	@Override
	public TCinemaTicket getLastPointExchangeTicket(BigDecimal memberid) {
		log.debug("getLastPointExchangeTicket memberKey:"+memberid);
		TCounterComputeMapper mapper = sqlSessionDw.getMapper(TCounterComputeMapper.class);
		return mapper.getLastPointExchangeTicket(memberid);
	}
	@Override
	public List<TCinemaTicket> getTicketLastBuys(BigDecimal memberid) {
		log.debug("getTicketLastBuys memberKey:"+memberid);
		TCounterComputeMapper mapper = sqlSessionDw.getMapper(TCounterComputeMapper.class);
		return mapper.getTicketLastBuys(memberid);
	}
	@Override
	public List<TCinemaTicket> getLastPointExchangeTickets(BigDecimal memberid) {
		log.debug("getLastPointExchangeTickets memberKey:"+memberid);
		TCounterComputeMapper mapper = sqlSessionDw.getMapper(TCounterComputeMapper.class);
		return mapper.getLastPointExchangeTickets(memberid);
	}
	@Override
	public int updateMemberInfo(TMember member, RuleChain chain) throws Exception {
		log.debug("updateMemberInfo chain:"+chain.printChain());
		log.debug("updateMemberInfo member:"+ member);
		if(member.getRegistCinemaId() == null||member.getRegistCinemaId().equals(0)){
			log.error("member Cinema is null ,memberId:"+member.getMemberId());
			throw new NullCinemaException();
		}
		//1 获取Mapper
		TCinemaMapper cinemaMapper = sqlSession.getMapper(TCinemaMapper.class);
		TMemberInfoMapper memberInfoMapper = sqlSession.getMapper(TMemberInfoMapper.class);
		TMemberMapper memberMapper = sqlSession.getMapper(TMemberMapper.class);
		TMemberInfoHistoryMapper memberInfoHisMapper = sqlSession.getMapper(TMemberInfoHistoryMapper.class);
		TMemberLevelMapper levelMapper = sqlSession.getMapper(TMemberLevelMapper.class);
		//1.1 获取影城 	select  seqid from t_cinema where inner_code='366'
		TCinemaExample cinemaExample = new TCinemaExample();
		log.debug(member.getRegistCinemaId().toString());
		cinemaExample.createCriteria().andInnerCodeEqualTo(member.getRegistCinemaId().toString());
		List<TCinema> cinemalist = cinemaMapper.selectByExample(cinemaExample);
		TCinema cinema = null;
		if(cinemalist == null||cinemalist.isEmpty()){
			log.error("cinemalist is empty ");
			throw new NullCinemaException();
		}else{
			cinema = cinemalist.get(0);
		}
		//2 获取会员信息
		List<TMemberInfo> list = getMemberInfo(member.getMemberId(), memberInfoMapper);
		if(list == null||list.isEmpty()){
			log.error("member id:"+member.getMemberId()+" have not member info");
			throw new NullMemberInfoException();
		}
		TMember reMember = getMember(member.getMemberNo());
		if(reMember == null){
			log.error("member no:"+member.getMemberNo()+" have not member info");
			throw new NullMemberInfoException();
		}
//		if(isSameManageCinema(list,cinema)){
//			log.info("memeberid:"+reMember.getMemberId() +"is same manage cinema id:");
//			return 0;
//		}
		//3 更新注册影城
		TMemberInfoHistory memberInfoHis = new TMemberInfoHistory();
		if(reMember.getRegistCinemaId().equals(new BigDecimal(0))){
			memberInfoHis.setRegCimemaOldId(new BigDecimal(0));
			memberInfoHis.setRegCimemaNewId(cinema.getSeqid());
			//add by lining 2013-12-16  start for 为了增量推送到dw ，每次计算过后 更改注册时间
			memberInfoHis.setOldRegDate(reMember.getRegistDate());
			Date now = new Date();
			Date yesterday = new Date();
			yesterday.setDate( now.getDate()-1);
			memberInfoHis.setNewRegDate(yesterday);
			reMember.setRegistDate(yesterday);
			//add by lining 2013-12-16 end
			//增加更新时间 start
			reMember.setUpdateDate(yesterday);
			
			//end
			reMember.setRegistCinemaId(cinema.getSeqid());	//用管理影城替换注册影城，	getRegistCinemaId记录的是管理影城	
			memberMapper.updateByPrimaryKey(reMember);
		}else{
			memberInfoHis.setRegCimemaOldId(reMember.getRegistCinemaId());
			memberInfoHis.setRegCimemaNewId(reMember.getRegistCinemaId());
		}
		//4 更新管理影城
		int re = 0;
		log.debug("memberinfo list:"+list);
		for(TMemberInfo record:list){
			//写入 历史  ----
			log.debug(record);	
			memberInfoHis.setSeq(new BigDecimal(levelMapper.getLevelHisSeq()));
			memberInfoHis.setMemberId(record.getMemberId());
			memberInfoHis.setManageCinemaOldId(record.getManageCinemaId());
			memberInfoHis.setManageCinemaNewId(cinema.getSeqid());
			memberInfoHis.setCreateDate(new Date());
			memberInfoHis.setRuleChain(chain.printChain());
			memberInfoHisMapper.insert(memberInfoHis);
			//更新memberinfo
			record.setManageCinemaId(cinema.getSeqid());
			record.setMemberId(member.getMemberId());
			re = memberInfoMapper.updateByPrimaryKeySelective(record);
		}
		return re;
	}
	private boolean isSameManageCinema(List<TMemberInfo> list, TCinema reMember) {
		boolean flag = false;
		for(TMemberInfo info:list){
			if(info.getManageCinemaId().equals(reMember.getSeqid())){
				flag = true;
			}else{
				flag = false;
			}
		}
		return flag;
	}
	private List<TMemberInfo> getMemberInfo(BigDecimal memberId,TMemberInfoMapper memberInfoMapper) {
		TMemberInfoExample example = new TMemberInfoExample();
		log.debug("updateMemberInfo memberid :"+memberId);
		example.createCriteria().andMemberIdEqualTo(memberId);
		List<TMemberInfo> list = memberInfoMapper.selectByExample(example );
		return list;
	}
	public TMember getMember(String memberNo) {
		TMemberMapper memberMapper = sqlSession.getMapper(TMemberMapper.class);
		return memberMapper.selectByPrimaryKey(memberNo);
	}
	public List<TDConMember> getDConMembers(String memberNo) {
		TDConMemberMapper tdconMemberMapper = sqlSessionDw.getMapper(TDConMemberMapper.class);
		TDConMemberExample tdcomexample = new TDConMemberExample();
		tdcomexample.createCriteria().andMemberNoEqualTo(memberNo);	
		List<TDConMember> memberList = tdconMemberMapper.selectByExample(tdcomexample );
		return memberList;
	}

}
