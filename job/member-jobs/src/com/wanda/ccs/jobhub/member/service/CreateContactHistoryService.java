package com.wanda.ccs.jobhub.member.service;

import com.wanda.ccs.jobhub.member.vo.TargetVo;

public interface CreateContactHistoryService {

	/**
	 * 创建联络清单
	 * @param params
	 */
	void saveContactHistory(TargetVo targetVo, String userId);
	
	public TargetVo getTargetVo(Long actTargetId);

}