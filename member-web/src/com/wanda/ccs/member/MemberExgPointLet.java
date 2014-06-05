package com.wanda.ccs.member;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.aggrepoint.adk.form.Input;
import com.aggrepoint.adk.form.Validates;
import com.aggrepoint.adk.ui.ValidateResult;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.mem.service.TMemberExgPointService;
import com.wanda.ccs.model.TMemberExgPoint;
import com.xcesys.extras.core.service.ICrudService;
import com.xcesys.extras.util.SpringContextUtil;

public class MemberExgPointLet extends BaseCrudWinlet<TMemberExgPoint> {

	private static final long serialVersionUID = -373645026315385474L;

	public TMemberExgPointService service;
	// 积分历史关联的会员的id
	public Long memberId;
	public String invoked;
	public boolean showLevel = false;
	public boolean isEdit = false;

	public TMemberExgPoint tLevelHistory = null;

	public String getInvoked() {
		return invoked;
	}

	public void setInvoked(String invoked) {
		this.invoked = invoked;
		this.showLevel = true;
	}

	public Long getMemberId() {
		return memberId;
	}

	// 提供给另一个Winlet使用的对外公开的方法，用于设值memberId
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
		this.query.put("memberId", memberId);
	}

	@Override
	protected ICrudService<TMemberExgPoint> getCrudService() {
		if (service == null) {
			service = SpringContextUtil.getBean(TMemberExgPointService.class);
		}
		return service;
	}

	@Override
	public int showList(IModuleRequest req, IModuleResponse resp)
			throws Exception {

		if (query.get("memberId") != null && showLevel) {
			return super.showList(req, resp);
		} else
			return RETCODE_HIDE;
	}


	@Validates({})
	public ValidateResult validate(IModuleRequest req, Input input) {
		return ValidateResult.PASS;
	}
	
}
