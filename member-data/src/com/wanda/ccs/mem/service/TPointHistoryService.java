package com.wanda.ccs.mem.service;

import java.util.Map;

import com.wanda.ccs.model.TPointHistory;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.ICrudService;

public interface TPointHistoryService extends ICrudService<TPointHistory> {
	/**
	 * 校验可兑换余额是否小于零
	 * @param activityPoint
	 * @param levelPoint
	 * @return
	 */
	public boolean checkPointBalance(Long activityPoint,Long levelPoint,Long memberId);
	
	public PageResult<TPointHistory> findByCriteriaForCon(
			QueryCriteria<String, Object> query, Map<String, Object> map);
	
	public PageResult<TPointHistory> findByCriteriaForTicket(
			QueryCriteria<String, Object> query,  Map<String, Object> map);
}
