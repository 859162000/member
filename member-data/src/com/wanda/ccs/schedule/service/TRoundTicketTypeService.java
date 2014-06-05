package com.wanda.ccs.schedule.service;

import java.util.List;

import com.wanda.ccs.model.TRoundTicketType;
import com.wanda.ccs.model.TSchedulePlanB;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 
 * @author Danne
 * 
 */
public interface TRoundTicketTypeService extends ICrudService<TRoundTicketType> {
	/**
	 * 按票类和场次删除
	 * 
	 * @param ticketTypeId
	 * @param planBId
	 */
	void deleteByTicketTypeAndPlanB(Long ticketTypeId, Long planBId);

	/**
	 * 按票类和场次加载
	 * 
	 * @param ticketTypeId
	 * @param planBId
	 * @return
	 */
	TRoundTicketType findByTicketTypeAndPlanB(Long ticketTypeId, Long planBId);
	
	/**
	 * 检测票类是否用于了排片
	 * @param ticketTypeId
	 * @return
	 */
	public boolean checkRoundTickettype(Long ticketTypeId);
	
	/**
	 * 获得场次下票类，并按"票类排序"功能中设定的顺序排列
	 * @param cinemaId
	 * @param planBId
	 * @return
	 */
	public List<TRoundTicketType> findAndOrderByMyCinemaSelfDefining(TSchedulePlanB planB);
	/**
	 * 从二级缓存中清除
	 * 
	 * @param id
	 */
	public void evictSecendCache();
}
