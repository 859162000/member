package com.wanda.ccs.schedule.service;

import java.util.List;

import com.wanda.ccs.model.TRoundFilm;
import com.wanda.ccs.model.TSubRoundTicketType;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 子场次(TRoundFilm)票类详细
 * 
 * @author xiaofeng
 *
 */
public interface TSubRoundTicketTypeService extends ICrudService<TSubRoundTicketType> {
	public List<TSubRoundTicketType> findAndOrderByMyCinemaSelfDefining(TRoundFilm rf);
	
	
	/**
	 * 获取一个票类在一个连场的所有子场次中的个数和价格之和
	 * 
	 * @param planBId
	 * @param ticketTypeId
	 * @return
	 */
	public int[] getTicketTypeCountAndPrice(long planBId , long ticketTypeId);
}
