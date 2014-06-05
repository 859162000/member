package com.wanda.ccs.mackdaddycard;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.aggrepoint.adk.form.Input;
import com.aggrepoint.adk.form.Validate;
import com.aggrepoint.adk.form.Validates;
import com.aggrepoint.adk.ui.ValidateResult;
import com.aggrepoint.adk.ui.ValidateResultType;
import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.auth.UserLevel;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.mem.service.TMackDaddyCardOrderService;
import com.wanda.ccs.model.IMemberDimType;
import com.wanda.ccs.model.IMemberRight;
import com.wanda.ccs.model.TMackDaddyCardOrder;

public class MackDaddyCardOrderLet extends BaseCrudWinlet<TMackDaddyCardOrder> implements IMemberDimType,IMemberRight {
	private static final long serialVersionUID = 5778777394524178958L;
	
	public TMackDaddyCardOrder editOrder;
	
	public String status = DIMTYPE_CARD_ORDER_STATUS_A;
	
	public RequestLogLet logLet = new RequestLogLet();
	/**
	 * 默认无参构造函数。
	 */
	public MackDaddyCardOrderLet() {

	}

	@Override
	protected TMackDaddyCardOrderService getCrudService() {
		return getService(TMackDaddyCardOrderService.class);
	}
	
	/**
	 * 显示主界面
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int showMain(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		query.put("status", status);
		return RETCODE_OK;
	}
	
	/**
	 * 选择状态
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int selStatus(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		status = req.getParameter("status", DIMTYPE_CARD_ORDER_STATUS_A);
		return RETCODE_OK;
	}

	@Override
	protected void initSearch(IModuleRequest req, IModuleResponse resp) {
		super.initSearch(req, resp);
		CcsUserProfile user = (CcsUserProfile) req.getUserProfile();
		query.put("region", user.getRegionCode());
		if(user.getLevel() == UserLevel.CINEMA)
			query.put("cinemaId", user.getCinemaId());
	}


	@Override
	protected void prepareQuery(IModuleRequest req) {
		super.prepareQuery(req);
		query.put("submitBy", req.getParameter("submitBy",null));
	}


	@Override
	public int cancelEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		editOrder = null;
		logLet = new RequestLogLet();
		return super.cancelEdit(req, resp);
	}

	@Override
	public int doCreate(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		cancelEdit(req, resp);
		editOrder = new TMackDaddyCardOrder();
		CcsUserProfile user = (CcsUserProfile) req.getUserProfile();
		editOrder.setCinemaId(user.getCinemaId());
		editOrder.setRegionCode(user.getRegionCode());
		editOrder.setSubmitBy(user.getId());
		editOrder.setDealStatus("W");
		return super.doCreate(req, resp);
	}
	
	@Override
	public int doEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		cancelEdit(req, resp);
		return super.doEdit(req, resp);
	}
	
	@Override
	public int doView(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		cancelEdit(req, resp);
		return super.doView(req, resp);
	}
	


	@Override
	public int showEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if(id == null && editOrder == null)
			return RETCODE_HIDE;
		if(editOrder == null || (id != null && editOrder.getId() != id))
			editOrder = getCrudService().getById(id);
		
		editOrder.setEditing(editing);
		logLet.setMackDaddyCardOrderId(editOrder.getId());
		if(editOrder.getId() != null){
			editOrder.gettCinema().getInnerCode();
		}
		return RETCODE_OK;
	}
	
	@Override
	public int saveEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if (req.isValidateField())
			return 2;
		if (req.getForm().hasError())
			return 3;
		// 初始化安全环境上下文。
		securityContext(req);
		CcsUserProfile user = (CcsUserProfile) req.getUserProfile();
		getCrudService().createOrUpdateRequest(user, editOrder, req.getParameter("status",null), req.getParameter("comments",null));
		cancelEdit(req, resp);
		return RETCODE_OK;
	}
	

	/**
	 * 编辑页面Ajax输入验证。
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	@Validates({
			@Validate(name = "numberOfCards", id = "tne", error = "请输入申请数量。"),
			@Validate(name = "comments", id = "tne", error = "审批拒绝时，审批意见不能为空。", after = true)})
	public ValidateResult validate(IModuleRequest req, Input input) {
		if(input.getName().equals("comments")){
			if(!req.getParameter("status", "").equals(DIMTYPE_CARD_ORDER_STATUS_F)){
				return ValidateResult.PASS_SKIP_PROPERTY;
			}
		}
		if (input.getName().equals("numberOfCards")) {
			if (input.getValue() != null
					&& Long.valueOf((String) input.getValue()) <= 0 ) {
				return new ValidateResult(
						ValidateResultType.FAILED_SKIP_PROPERTY, "申请数量必须大于0");
			}
		}
		return ValidateResult.PASS;
	}

}
