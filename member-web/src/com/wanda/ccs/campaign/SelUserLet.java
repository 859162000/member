package com.wanda.ccs.campaign;

import java.util.List;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.wanda.ccs.auth.service.TAuthUserService;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.model.IMemberDimType;
import com.wanda.ccs.model.TAuthUser;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.util.ConvertUtil;

public class SelUserLet extends BaseCrudWinlet<TAuthUser> implements
		IMemberDimType {
	private static final long serialVersionUID = 6408411508935505004L;
	
	//活动中选择授权人
	private CampaignManLet campaignManLet;
	public CampaignManLet getCampaignManLet(IModuleRequest req) throws Exception{
		if(campaignManLet == null)
			campaignManLet = (CampaignManLet) req.getWinlet(CampaignManLet.class.getName());
		return campaignManLet;
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
	public SelUserLet() {

	}

	@Override
	protected TAuthUserService getCrudService() {
		return getService(TAuthUserService.class);
	}

	@Override
	protected void initSearch(IModuleRequest req, IModuleResponse resp) {
		super.initSearch(req, resp);
		query.put("status", "E");
	}

	@Override
	protected void prepareQuery(IModuleRequest req) {
		super.prepareQuery(req);
		query.put("loginId", req.getParameter("loginId", null));
	}

	@Override
	public int showSearch(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if (hide)
			return RETCODE_HIDE;
		return super.showSearch(req, resp);
	}
	
	/**
	 * 显示列表窗口内容，分页查询结果为null时则隐藏该窗口。
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	@Override
	public int showList(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		PageResult<TAuthUser> pageResult = getCrudService().findByQuery(query);
		if (pageResult == null) {
			return RETCODE_HIDE;
		}
		// New Displaytag使用的结果集，并将其放到Request中，使JSP可引用。
		req.setAttribute(NAME_OF_PAGE_RESULT, buildResult(pageResult));
		return RETCODE_OK;
	}
	
	
	/**
	 * 保存选择的人员
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int saveSelUser(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		String[] oids = req.getParameterValues("ids");
		Long[] ids = ConvertUtil.convertStringArrayToLongArray(oids);
		int ret = 0;
		if(ids != null && ids.length > 0){
			if(getInvoked().equals(CampaignManLet.class.getName())){
				List<TAuthUser> list = getCrudService().findByIds(ids);
				getCampaignManLet(req).saveUser(list);
				ret = 1;
			}
		}
		hide = true;
		return ret;
	}
	
	/**
	 * 关闭选择人员
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
