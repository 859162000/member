package com.wanda.ccs.campaign;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.auth.UserLevel;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.mem.service.TSegmentService;
import com.wanda.ccs.model.IMemberDimType;
import com.wanda.ccs.model.TSegment;
import com.xcesys.extras.util.ConvertUtil;

public class SelSegmentLet extends BaseCrudWinlet<TSegment> implements
		IMemberDimType {
	private static final long serialVersionUID = 6408411508935505004L;
	
	//波次中选择客群
	private ActivityManLet activityManLet;
	public ActivityManLet getActivityManLet(IModuleRequest req) throws Exception{
		if(activityManLet == null)
			activityManLet = ((CampaignManLet) req.getWinlet(CampaignManLet.class.getName())).phaseManLet.activityManLet;
		return activityManLet;
	}
	
	//波次响应统计方式中选择客群
	private ActResultManLet actResultManLet;
	public ActResultManLet getActResultManLet(IModuleRequest req) throws Exception{
		if(actResultManLet == null)
			actResultManLet = (ActResultManLet) req.getWinlet(ActResultManLet.class.getName());
		return actResultManLet;
	}

	//是否隐藏做为被调用且弹出显示的本winlet
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
	public SelSegmentLet() {

	}

	@Override
	protected TSegmentService getCrudService() {
		return getService(TSegmentService.class);
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
		query.put("code", req.getParameter("code", ""));
		query.put("name", req.getParameter("name", ""));
	}

	@Override
	public int showSearch(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if (hide)
			return RETCODE_HIDE;
		return super.showSearch(req, resp);
	}
	
	/**
	 * 保存选择的客群
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int saveSelSegment(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		String[] oids = req.getParameterValues("ids");
		Long[] ids = ConvertUtil.convertStringArrayToLongArray(oids);
		int ret = 0;
		if(ids != null && ids.length > 0){
			if(getInvoked().equals(ActivityManLet.class.getName())){
				getActivityManLet(req).saveSegment(getCrudService().findByIds(ids), req);
				ret = 1;
				if(getActivityManLet(req).editTarget){
					ret = 2;
				}
			}else if(getInvoked().equals(ActResultManLet.class.getName())){
				getActResultManLet(req).saveSegment(getCrudService().findByIds(ids));
				ret = 3;
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
