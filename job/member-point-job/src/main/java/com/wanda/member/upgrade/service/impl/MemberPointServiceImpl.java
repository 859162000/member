package com.wanda.member.upgrade.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wanda.member.upgrade.data.TPointHistory;
import com.wanda.member.upgrade.data.TPointHistoryExample;
import com.wanda.member.upgrade.data.TPointHistoryMapper;
import com.wanda.member.upgrade.service.MemberHelper;
import com.wanda.member.upgrade.service.MemberPointService;
import com.wanda.mms.dao.MyBatisDAO;
@Service("memberPointService")
@Scope("prototype")
public class MemberPointServiceImpl extends MyBatisDAO implements MemberPointService{
	Log logger = LogFactory.getLog(MemberPointServiceImpl.class);
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	public TPointHistory getThisYearPointHistyByMemberId(String year, BigDecimal memeberid) {
		String startdate = year+"-01-01";
		String enddate = year+"-12-31";
		Date dStartDate = null;
		Date dEnddate = null;
		try {
			logger.debug("startDate =="+startdate);
			logger.debug("enddate =="+enddate);
			System.out.println("startDate =="+startdate);
			System.out.println("enddate =="+enddate);
			dStartDate = format.parse(startdate);
			dEnddate = format.parse(enddate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}		
		TPointHistoryExample example = new TPointHistoryExample();
		example.createCriteria().andMemberIdEqualTo(memeberid).
		andSetTimeBetween(dStartDate, dEnddate)
		.andIsHistoryNotEqualTo("1")
		.andIsdeleteEqualTo(new BigDecimal("0"));
		return queryPointHistory(example);
	}
	/**
	 * 用于1升2 的计算
	 * @param year
	 * @param memeberid
	 * @return
	 */
	public TPointHistory getAllYearPointHistyByMemberId(String year, BigDecimal memberId){
		
		TPointHistoryExample example = new TPointHistoryExample();
		example.createCriteria().andMemberIdEqualTo(memberId).andIsHistoryNotEqualTo("1")
		.andIsdeleteEqualTo(new BigDecimal("0"));//0的才有效
		return queryPointHistory( example);
	}
	private TPointHistory queryPointHistory( TPointHistoryExample example) {
		TPointHistoryMapper pointHistoryMapper = sqlSession.getMapper(TPointHistoryMapper.class);
		List<TPointHistory> pointHistorys = pointHistoryMapper.selectByExample(example);
		return MemberHelper.getSumPointHis(pointHistorys);
	}
}
