package com.wanda.member.upgrade.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wanda.member.upgrade.data.TPointHistory;

public class MemberHelper {
	static Log log = LogFactory.getLog(MemberHelper.class);
	/**
	 * 根据时间区间计算积分总和与购票总和
	 * @param pointHistorys
	 * @return
	 */
	public static TPointHistory getSumPointHis(List<TPointHistory> pointHistorys) {
		long sumLevelPoint = 0;
		int sumTicketCount = 0;
		for (TPointHistory pointHistory:pointHistorys) {
			sumLevelPoint +=pointHistory.getLevelPoint().longValue();
			sumTicketCount += pointHistory.getTicketCount().intValue();
			log.info("sumLevelPoint =="+sumLevelPoint);
			log.info("sumTicketCount =="+sumTicketCount);
		}
		TPointHistory pointHistory = new TPointHistory();
		pointHistory.setLevelPoint(new BigDecimal(sumLevelPoint));
		pointHistory.setTicketCount(new BigDecimal(sumTicketCount));
		return pointHistory;
	}

}
