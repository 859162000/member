package com.wanda.ccs.mem.service;

import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.model.TMackDaddyCardOrder;
import com.xcesys.extras.core.service.ICrudService;

public interface TMackDaddyCardOrderService extends
		ICrudService<TMackDaddyCardOrder> {
	/**
	 * 创建或审批制卡申请单
	 * @param user
	 * @param order
	 * @param action
	 * @param comments
	 */
	public void createOrUpdateRequest(CcsUserProfile user, TMackDaddyCardOrder order, String action, String comments);
	
}
