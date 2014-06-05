package com.wanda.ccs.campaign;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.mem.service.TMemberVoucherPoolService;
import com.wanda.ccs.model.IMemberDimType;
import com.wanda.ccs.model.TVoucherPool;
import com.xcesys.extras.util.ConvertUtil;

public class SelVoucherPoolLet extends BaseCrudWinlet<TVoucherPool> implements
		IMemberDimType {
	private static final long serialVersionUID = 6408411508935505004L;

	/**
	 * 波次中选择特殊积分规则
	 */
	private ActivityManLet activityManLet;

	public ActivityManLet getCampaignManLet(IModuleRequest req)
			throws Exception {
		if (activityManLet == null)
			activityManLet = (ActivityManLet) req
					.getWinlet(ActivityManLet.class.getName());
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
	public SelVoucherPoolLet() {

	}

	@Override
	protected TMemberVoucherPoolService getCrudService() {
		return getService(TMemberVoucherPoolService.class);
	}

	@Override
	protected void initSearch(IModuleRequest req, IModuleResponse resp) {
		super.initSearch(req, resp);
	}

	@Override
	protected void prepareQuery(IModuleRequest req) {
		super.prepareQuery(req);
	}

	@Override
	public int showSearch(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if (hide)
			return RETCODE_HIDE;
		return super.showSearch(req, resp);
	}

	/**
	 * 保存选择的券库
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int saveSelVoucherPool(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		String[] oids = req.getParameterValues("ids");
		Long[] ids = ConvertUtil.convertStringArrayToLongArray(oids);
		int ret = 0;
		if (ids != null && ids.length > 0) {
			if (getInvoked().equals(CampaignManLet.class.getName())) {
				getCampaignManLet(req).saveVoucherPool(
						getCrudService().findByIds(ids));
				ret = 1;
			}
		}
		hide = true;
		return ret;
	}

	/**
	 * 关闭选择券库
	 * 
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
