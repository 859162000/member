package com.wanda.ccs.member;


import java.util.List;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.mem.service.TExtPointCriteriaService;
import com.wanda.ccs.model.IMemberDimType;
import com.wanda.ccs.model.TExtPointCriteria;
import com.xcesys.extras.util.ConvertUtil;

public class SelExtPointCriteriaLet extends BaseCrudWinlet<TExtPointCriteria> implements
		IMemberDimType {

	private static final long serialVersionUID = 1975483350689805095L;

	private ExtPointRuleLet extPointRuleLet;
	public ExtPointRuleLet getExtPointRuleLet(IModuleRequest req) throws Exception {
		if(extPointRuleLet == null) {
			extPointRuleLet = (ExtPointRuleLet) req.getWinlet(ExtPointRuleLet.class.getName());
		}
		return extPointRuleLet;
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
	public SelExtPointCriteriaLet() {

	}
	
	protected TExtPointCriteriaService getCrudService() {
		return this.getService(TExtPointCriteriaService.class);
	}
	
	@Override
	protected void initSearch(IModuleRequest req, IModuleResponse resp) {
		super.initSearch(req, resp);
	}

	@Override
	protected void prepareQuery(IModuleRequest req) {
		super.prepareQuery(req);
		query.put("name", req.getParameter("name", ""));
		query.put("code", req.getParameter("code", null));
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
	public int saveCriterial(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		String[] oids = req.getParameterValues("ids");
		Long[] ids = ConvertUtil.convertStringArrayToLongArray(oids);
		int ret = 0;
		if(ids != null && ids.length > 0){
			if(getInvoked().equals(ExtPointRuleLet.class.getName())){
				List<TExtPointCriteria> list = getCrudService().findByIds(ids);
				
				if(list != null && !list.isEmpty()){
					TExtPointCriteria extPointCriteria = list.get(0);
					if(extPointCriteria != null) {
						String type = getCrudService().getCriteriaScheme(extPointCriteria.getId());
						if(type == null) {
							throw new Exception("积分规则类型解析失败！");
						} else {
							int t = Integer.parseInt(type);
							extPointCriteria.setType(t);
						}
					}
				}
				this.getExtPointRuleLet(req).saveExtPointCriteria(list);
				ret = 1;
			}
		}
		hide = true;
		return ret;
	}
	
	/**
	 * 关闭选择的特殊积分规则
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int closeSelCriterial(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		hide = true;
		return RETCODE_OK;
	}
}
