package com.wanda.ccs.schedule.service;

import java.util.List;

import com.wanda.ccs.model.PlanTransData;
import com.wanda.ccs.model.TSchedulePlanB;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 
 * @author Danne
 * 
 */
public interface TSchedulePlanBService extends ICrudService<TSchedulePlanB> {
	/**
	 * 根据planHId查询List<TSchedulePlanB>
	 * 
	 * @param planHId
	 * @param orderClauses
	 * @return
	 */
	public List<TSchedulePlanB> findByPlanHId(Long planHId,
			String... orderClauses);
	
	/**
	 * 获得上传标记
	 * 
	 * @param id
	 * @return
	 */
	public Boolean getBroadcastById(Long id);
	
	/**
	 * 获取接口处数据
	 * @param id
	 * @return
	 */
	public PlanTransData getTransData(Long id);
}
