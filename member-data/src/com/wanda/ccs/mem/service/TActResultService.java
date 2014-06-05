package com.wanda.ccs.mem.service;

import java.util.List;

import com.wanda.ccs.model.TActResult;
import com.xcesys.extras.core.service.ICrudService;

public interface TActResultService extends ICrudService<TActResult> {
	/**
	 * 根据波次id获取响应统计执行结果
	 * @param activityId
	 * @return
	 */
	public List<TActResult> getActResultsByActivityId(Long activityId);
}
