package com.wanda.ccs.schedule.dao;import com.wanda.ccs.model.TRoundTicketType;import com.xcesys.extras.core.dao.IBaseDao;public interface ITRoundTicketTypeDao extends IBaseDao<TRoundTicketType> {	/**	 * 从二级缓存中清除	 * 	 */	public void evictSecendCache();}