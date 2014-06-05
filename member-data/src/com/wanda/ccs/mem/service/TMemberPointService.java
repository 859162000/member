package com.wanda.ccs.mem.service;

import com.wanda.ccs.model.TMemberPoint;
import com.xcesys.extras.core.service.ICrudService;

public interface TMemberPointService extends ICrudService<TMemberPoint> {
	/**
	 * 根据会员ID
	 * @param memgerId
	 * @return
	 */
	public TMemberPoint getTMemberPointByMemId(Long memgerId);
}
