package com.wanda.ccs.mackdaddycard;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.mem.service.TMackDaddyCardOrderLogService;
import com.wanda.ccs.model.IMemberDimType;
import com.wanda.ccs.model.TMackDaddyCardOrderLog;

public class RequestLogLet extends BaseCrudWinlet<TMackDaddyCardOrderLog> implements IMemberDimType {
	private static final long serialVersionUID = 3877361019542357044L;
	
	private Long mackDaddyCardOrderId;
	

	public Long getMackDaddyCardOrderId() {
		return mackDaddyCardOrderId;
	}

	public void setMackDaddyCardOrderId(Long mackDaddyCardOrderId) {
		this.mackDaddyCardOrderId = mackDaddyCardOrderId;
	}

	/**
	 * 默认无参构造函数。
	 */
	public RequestLogLet() {

	}

	@Override
	protected TMackDaddyCardOrderLogService getCrudService() {
		return getService(TMackDaddyCardOrderLogService.class);
	}
	
	@Override
	protected void initSearch(IModuleRequest req, IModuleResponse resp) {
		super.initSearch(req, resp);
	}


	@Override
	protected void prepareQuery(IModuleRequest req) {
		super.prepareQuery(req);
	}
	
	public int showList(IModuleRequest req, IModuleResponse resp) throws Exception{
		if(mackDaddyCardOrderId == null)
			return RETCODE_HIDE;
		query.put("mackDaddyCardOrderId", mackDaddyCardOrderId);
		return super.showList(req, resp);
	}

}
