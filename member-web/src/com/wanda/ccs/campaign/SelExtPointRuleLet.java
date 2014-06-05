package com.wanda.ccs.campaign;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.mem.service.TExtPointRuleService;
import com.wanda.ccs.model.IMemberDimType;
import com.wanda.ccs.model.TExtPointRule;
import com.xcesys.extras.util.ConvertUtil;

public class SelExtPointRuleLet extends BaseCrudWinlet<TExtPointRule> implements
		IMemberDimType {
	private static final long serialVersionUID = 6408411508935505004L;
	
	/**
	 * 波次中选择特殊积分规则
	 */
	private ActivityManLet activityManLet;
	public ActivityManLet getActivityManLet(IModuleRequest req) throws Exception{
		if(activityManLet == null)
			activityManLet = ((CampaignManLet) req.getWinlet(CampaignManLet.class.getName())).phaseManLet.activityManLet;
		return activityManLet;
	}
	

	/**
	 * 是否隐藏做为被调用且弹出显示的本winlet
	 */
	private boolean hide = true;

	/**
	 * 该winlet调用者类名
	 */
	private String invoked;

	public String getInvoked() {
		return invoked;
	}

	public void setInvoked(String invoked) {
		this.invoked = invoked;
		this.hide = false;
		this.query.setPage(1);
	}

	/**
	 * 默认无参构造函数。
	 */
	public SelExtPointRuleLet() {

	}

	@Override
	protected TExtPointRuleService getCrudService() {
		return getService(TExtPointRuleService.class);
	}

	@Override
	protected void initSearch(IModuleRequest req, IModuleResponse resp) {
		super.initSearch(req, resp);
//		query.put("status", CAMPAINGN_STATUS_EXECUTE);
		query.put("inStatus", new String[]{CAMPAINGN_STATUS_PUBLISH, CAMPAINGN_STATUS_EXECUTE});
	}

	@Override
	protected void prepareQuery(IModuleRequest req) {
		super.prepareQuery(req);
		query.put("sStartDate", req.getParameter("sStartDate", null));
		query.put("eStartDate", req.getParameter("eStartDate", null));
		query.put("sEndDate", req.getParameter("sEndDate", null));
		query.put("eEndDate", req.getParameter("eEndDate", null));
		query.put("code", req.getParameter("code", null));
		query.put("name", req.getParameter("name", null));
	}

	@Override
	public int showSearch(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if (hide)
			return RETCODE_HIDE;
		return super.showSearch(req, resp);
	}
	
	/**
	 * 保存选择的特殊积分规则
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int saveSelExtPointRule(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		String[] oids = req.getParameterValues("ids");
		Long[] ids = ConvertUtil.convertStringArrayToLongArray(oids);
		int ret = 0;
		if(ids != null && ids.length > 0){
			if(getInvoked().equals(ActivityManLet.class.getName())){
				getActivityManLet(req).saveExtPointRule(getCrudService().findByIds(ids), req);
				ret = 1;
			}
		}
		hide = true;
		return ret;
	}
	
	/**
	 * 关闭选择特殊积分规则
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int closeWinlet(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		hide = true;
		return RETCODE_OK;
	}
}
