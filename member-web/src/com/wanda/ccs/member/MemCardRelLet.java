package com.wanda.ccs.member;

import java.util.Map;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.mem.service.TMemCardRelService;
import com.wanda.ccs.model.IDimType;
import com.wanda.ccs.model.TMemCardRel;
import com.wanda.ccs.service.SpringCommonService;
import com.xcesys.extras.core.service.ICrudService;
import com.xcesys.extras.util.SpringContextUtil;

public class MemCardRelLet extends BaseCrudWinlet<TMemCardRel>
		implements IDimType {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6417653479352305073L;
	public Long memberId;
	public String invoked;
	public boolean showCardRel = false;
	Map<String,String> cinemaMap;
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
		this.query.put("memberId", memberId);
	}

	public String getInvoked() {
		return invoked;
	}

	public void setInvoked(String invoked) {
		this.invoked = invoked;
		this.showCardRel = true;
	}

	private TMemCardRelService service;

	public MemCardRelLet() {

	}

	protected TMemCardRel newModel() {
		return new TMemCardRel();
	}

	protected ICrudService<TMemCardRel> getCrudService() {
		if (service == null) {
			service = SpringContextUtil
					.getBean(TMemCardRelService.class);
		}
		return service;
	}

	public int showList(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if (query.get("memberId") != null && showCardRel) {
			if(cinemaMap != null && cinemaMap.isEmpty())
				cinemaMap = SpringCommonService.getAllCodeAndInnerNameCinemaMap();
			req.setAttribute("CARDCINEMA", cinemaMap);
			return super.showList(req, resp);
		} else
			return RETCODE_HIDE;
	}

}
