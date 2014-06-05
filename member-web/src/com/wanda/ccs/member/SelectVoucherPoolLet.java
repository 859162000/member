package com.wanda.ccs.member;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.mem.service.TMemberVoucherPoolService;
import com.wanda.ccs.model.IMemberDimType;
import com.wanda.ccs.model.TVoucherPool;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.service.ICrudService;
import com.xcesys.extras.util.ConvertUtil;

public class SelectVoucherPoolLet extends BaseCrudWinlet<TVoucherPool> implements
		IMemberDimType {


	private static final long serialVersionUID = 6940241325357772450L;

	private TMemVoucherRuleLet tMemVoucherRulelet;
	public TMemVoucherRuleLet gettMemVoucherRulelet(IModuleRequest req) throws Exception {
		if(tMemVoucherRulelet == null) {
			tMemVoucherRulelet = (TMemVoucherRuleLet) req.getWinlet(TMemVoucherRuleLet.class.getName());
		}
		return tMemVoucherRulelet;
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
	public SelectVoucherPoolLet() {

	}
	
	@Override
	protected ICrudService<TVoucherPool> getCrudService() {
		
		return this.getService(TMemberVoucherPoolService.class);
	}


	@Override
	protected void initSearch(IModuleRequest req, IModuleResponse resp) {
		super.initSearch(req, resp);
	}

	@Override
	protected void prepareQuery(IModuleRequest req) {
		super.prepareQuery(req);
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
	 * 保存选择的券库
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int saveVoucherPool(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		String[] oids = req.getParameterValues("ids");
		Long[] ids = ConvertUtil.convertStringArrayToLongArray(oids);
		int ret = 0;
		if(ids != null && ids.length > 0){
			if(getInvoked().equals(TMemVoucherRuleLet.class.getName())){
				this.gettMemVoucherRulelet(req).saveVoucherPool(getCrudService().findByIds(ids));
				ret = 1;
			}
		}
		hide = true;
		return ret;
	}
	
	/**
	 * 关闭选择的券库
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int closeVoucherPool(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		hide = true;
		return RETCODE_OK;
	}

	@Override
	public int showList(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		PageResult<TVoucherPool> pageResult = this.getCrudService().findByCriteria(query);
		if (pageResult == null) {
			return RETCODE_HIDE;
		}
		// New Displaytag使用的结果集，并将其放到Request中，使JSP可引用。
		req.setAttribute(NAME_OF_PAGE_RESULT, buildResult(pageResult));
		return RETCODE_OK;
	}
}
