package com.wanda.ccs.mem.service;

import com.wanda.ccs.model.TCmnPhase;
import com.xcesys.extras.core.service.ICrudService;

public interface TCmnPhaseService extends ICrudService<TCmnPhase> {
	
	/**
	 * 根据活动id获取阶段编码
	 * @param campaignId
	 * @return
	 */
	public String getCmnPhaseCode(Long campaignId);

	/**
	 * 更新阶段code
	 * @param cmnPhaseId
	 */
	public void updateCmnPhaseCode(Long cmnPhaseId);
	
	/**
	 * 检测阶段名称是否重复
	 * @param name
	 * @param phaseId
	 * @param campaignId
	 * @return
	 */
	public boolean checkPhaseName(String name, Long phaseId, Long campaignId);
	
	/**
	 * 检测阶段开始时间
	 * @param date
	 * @param phaseId
	 * @return
	 */
	public boolean checkPhaseStartDate(String date, Long phaseId);
	
	/**
	 * 检测阶段结束时间
	 * @param date
	 * @param phaseId
	 * @return
	 */
	public boolean checkPhaseEndDate(String date, Long phaseId);
}
