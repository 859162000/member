package com.wanda.ccs.member;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.aggrepoint.adk.form.Input;
import com.aggrepoint.adk.form.Validates;
import com.aggrepoint.adk.ui.ValidateResult;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.mem.service.TLevelHistoryService;
import com.wanda.ccs.model.TLevelHistory;
import com.xcesys.extras.core.service.ICrudService;
import com.xcesys.extras.util.SpringContextUtil;

public class MemberLevelHistoryLet extends BaseCrudWinlet<TLevelHistory> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -373645026315385474L;

	public TLevelHistoryService service;
	// 积分历史关联的会员的id
	public Long memberId;
	public String invoked;
	public boolean showLevel = false;
	public boolean isEdit = false;

	public TLevelHistory tLevelHistory = null;

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
	protected ICrudService<TLevelHistory> getCrudService() {
		if (service == null) {
			service = SpringContextUtil.getBean(TLevelHistoryService.class);
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

	@Override
	public int doCreate(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		tLevelHistory = new TLevelHistory();
		isEdit = true;
		return super.doCreate(req, resp);
	}

	@Override
	public int showEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		TLevelHistory model = null;
		if (id != null && id != 0) {
			// 编辑或查看的场景
			try {
				// 编辑情况
				model = findById(id);
			} catch (Exception e) {
				resp.setUserMessage(e.getLocalizedMessage());
				return RETCODE_ERR;
			}
		} else if (isEdit) {
			// 新增数据的场景
			model = tLevelHistory;
		}
		if (model == null) {
			log.debug("#### 隐藏Edit窗口 ####");
			return 8000;
		}
		if (model != null) {
			model.setEditing(editing && canEdit(model));
		}
		req.setAttribute(NAME_OF_EDITING_OBJECT, model);
		return RETCODE_OK;
	}

	@Validates({})
	public ValidateResult validate(IModuleRequest req, Input input) {
		return ValidateResult.PASS;
	}
}
