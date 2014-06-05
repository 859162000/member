package com.wanda.ccs.mem.service;

import com.wanda.ccs.model.TSegment;
import com.xcesys.extras.core.service.ICrudService;

public interface TSegmentService extends ICrudService<TSegment> {
	/**
	 * 根据客群id,获取客群实际数量
	 * @param segmentId
	 * @return
	 */
	public Long getCalCountById(Long segmentId);
}
