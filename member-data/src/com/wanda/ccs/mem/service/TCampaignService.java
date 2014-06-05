package com.wanda.ccs.mem.service;

import com.wanda.ccs.model.TCampaign;
import com.xcesys.extras.core.service.ICrudService;

public interface TCampaignService extends ICrudService<TCampaign> {
	
	/**
	 * 获取活动编码
	 * @return
	 */
	public String getCampainCode();

	/**
	 * 更新活动编码
	 * @param campaignId
	 */
	public void updateCampaignCode(Long campaignId);
	
	/**
	 * 检测活动名称是否存在
	 * @param name
	 * @param campaignId
	 * @return
	 */
	public boolean checkCampaignname(String name, Long campaignId);
	
	/**
	 * 检测活动开始时间
	 * @param campaignId
	 * @param date
	 * @return
	 */
	public boolean checkStartDate(Long campaignId, String date);
	
	/**
	 * 检测活动结束时间
	 * @param campaignId
	 * @param date
	 * @return
	 */
	public boolean checkEndDate(Long campaignId, String date);
}
