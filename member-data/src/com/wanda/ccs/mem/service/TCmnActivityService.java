package com.wanda.ccs.mem.service;

import java.util.Map;

import com.wanda.ccs.model.TCmnActivity;
import com.xcesys.extras.core.service.ICrudService;

public interface TCmnActivityService extends ICrudService<TCmnActivity> {
	/**
	 * 检测波次名称是否重复
	 * @param name
	 * @param id
	 * @return
	 */
	public boolean checkActivityName(String name, Long id);
	
	/**
	 * 获取波次编码
	 * @param phaseId
	 * @return
	 */
	public String getActivityCode(Long phaseId);

	/**
	 * 更新波次编码
	 * @param activityId
	 */
	public void updateCmnActivityCode(Long activityId);
	
	/**
	 * 获取发送短信参数
	 * @return
	 */
	public Map<String, String> getMemberConfig();

	/**
	 * 获取会员信息
	 * @param mobile
	 * @return
	 */
	public Map<String, ?> getMemberByPhone(String mobile);

}
