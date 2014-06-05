package com.wanda.member.upgrade.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wanda.member.upgrade.data.TLevelHistory;
import com.wanda.member.upgrade.data.TMemberLevel;
import com.wanda.member.upgrade.data.TMemberLevelMapper;
import com.wanda.member.upgrade.data.TMemberPoint;
import com.wanda.member.upgrade.data.TMemberPointMapper;
import com.wanda.member.upgrade.data.TPointHistory;
import com.wanda.member.util.DateUtil;
import com.wanda.mms.control.stream.Dimdef;
import com.wanda.mms.dao.MyBatisDAO;
@Service
@Scope("prototype")
public abstract class AbstractMemberUpgradeStratrey extends MyBatisDAO  implements MemberUpgradeStrategy  {
	Log logger = LogFactory.getLog(AbstractMemberUpgradeStratrey.class);
	private static final String MEMBER_UP = "up";
	private static final String MEMBER_SYS = "member_sys";
	private static SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Resource
	protected MemberPointService memberPointService;
	@Resource
	protected MemberLevelHisService memberLevelHisService;
	@Resource
	protected MemberLevelService memberLevelService;
	@Override
	public long compute(String year,TMemberLevel memberLevel) {
		if( memberLevel ==null){
			logger.warn(" memberLevel is null");
			return 0;
		}
		if(memberLevel.getMemLevel().equals(String.valueOf(getMemberLevel()))){
			return 0;
		}
		TPointHistory pointHistory = getPointHistory(year, memberLevel);
		if( pointHistory ==null){
			logger.warn(" pointHistory is null");
			return 0;
		}
		int level_point = pointHistory.getLevelPoint().intValue();
		int ticket_count= pointHistory.getTicketCount().intValue();//有效观影张数
		logger.info("level_point:"+level_point);
		logger.info("ticket_count:"+ticket_count);
		/**
		 * •12个月内(自然年, 01/01-12/31), 
		 *  有效定级积分500分/年或有效观影12张/年,可以立即晋级二星会员
		  •对于新注册的会员, 以上升级的有效期到注册年的次年12/31
		 */
		boolean isNeedUpgrade = isNeedUpgrade(level_point, ticket_count);
		logger.info("isNeedUpgrade:"+isNeedUpgrade);
		if(isNeedUpgrade){
			//1 扣减积分
			long remainPoint =getRemainPoint(level_point);//剩余积分
			//2 扣减张数
			long remainTicket = getRemainTicketCount(ticket_count);
			TMemberLevelMapper memberLevelmapper = sqlSession.getMapper(TMemberLevelMapper.class);
			long seq = memberLevelmapper.getLevelHisSeq();
			//3 更新 级别 扣减积分 扣减张数
			updateMemberPoint(memberLevel.getMemberId());
			//4 写入更新事件
			insertEventUpgrade(seq,memberLevel.getExpireDate(),memberLevel.getMemberId(),new BigDecimal(remainPoint),new BigDecimal(remainTicket));
			
			//5 更新会员级别
			updateMemberLevel(seq,memberLevel.getMemberId(),remainPoint,remainTicket);
			logger.info("[member-upgrade-success]="+memberLevel.getMemberId()+"is upgrade to "+getMemberLevel()+ "seq:"+seq);
			return seq;
		}else{
			logger.info("member id :"+ memberLevel.getMemberId()+" is not upgrade");
			return 0;
		}
	}

	protected abstract TPointHistory getPointHistory(String year, TMemberLevel memberLevel) ;
	
	protected final long getRemainTicketCount(int ticket_count) {
		return minus(getTicketLevelCount(),ticket_count);
	}
	protected final long getRemainPoint(int level_point) {
		return minus(getLevelPoint(),level_point);
	}
	protected abstract int getTicketLevelCount() ;
	public abstract int getLevelPoint();

	protected void updateMemberLevel(long seq,BigDecimal memberId, long remainPoint, long remainTicket) {
		
		Date expireDate = getLastDayOfNextYear();  
		TMemberLevel record = new TMemberLevel();
		Date now = new Date();
		record.setExpireDate(expireDate);
		record.setMemberId(memberId);
		record.setOrgLevel(String.valueOf(getMemberLevel()-1));
		record.setMemLevel(String.valueOf(getMemberLevel()));
		record.setTargetLevel(String.valueOf(getMemberLevel()+1));
		record.setSetTime(now);
		record.setUpdateDate(now);
		record.setLevelPointOffset(new BigDecimal(remainPoint));
		record.setTicketOffset(new BigDecimal(remainTicket));
		record.setMemberLevelHistoryId(new BigDecimal(seq));
		memberLevelService.doUpdateMemberLevel(record);
	}
	protected abstract boolean isNeedUpgrade(int level_point, int ticket_count);
	protected abstract int getMemberLevel();
	protected Date getLastDayOfNextYear() {
		int year = DateUtil.getCurrentYear();
		Date expireDate = DateUtil.getLastDayOfYear(year+1);
		try {
			return format.parse(format.format(expireDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	

	protected void insertEventUpgrade(long seq, Date expdate,BigDecimal memberId,BigDecimal remainPoint,BigDecimal remainTicketCount) {
		Date now = new Date();
		TLevelHistory record = new TLevelHistory();
		record.setLevelHistoryId(new BigDecimal(seq));
		record.setMemLevel(String.valueOf(getMemberLevel()));
		record.setExpireDate(getLastDayOfNextYear());
		record.setOrgMemLevel(String.valueOf(getMemberLevel()-1));
		record.setOrgExpireDate(DateUtil.getDate(expdate));
		record.setSetTime(now);
		record.setResonType(Dimdef.RESON_TYPE_QT);
		record.setReason(MEMBER_SYS);
		record.setChgType(MEMBER_UP);
		record.setMemberId(memberId);
		record.setLevelPoint(remainPoint);
		record.setTicketCount(remainTicketCount);
		record.setIsdelete(new BigDecimal("0"));//
		record.setCreateBy(MEMBER_SYS);
		record.setCreateDate(now);
		record.setUpdateBy(MEMBER_SYS);
		record.setUpdateDate(now);
		record.setVersion(new BigDecimal(1));
		memberLevelHisService.doInsertMemberLevelHis(record);
	}


	protected void updateMemberPoint(BigDecimal memberid) {
		logger.info("member id ="+memberid);
		TMemberPointMapper mapper = sqlSession.getMapper(TMemberPointMapper.class);
		TMemberPoint record = new TMemberPoint();
		record.setIsLevel("1");//1 代表什么意思？
		record.setMemberId(memberid);
		mapper.updateByPrimaryKeySelective(record);
	}

	protected long minus(long limit, int value) {
		long remain = limit - value;
		if (remain >0){
			return remain;
		}else{
			return 0;
		}
	}

	
}
