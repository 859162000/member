package com.wanda.ccs.campaign;

import java.util.Map;

import org.displaytag.properties.SortOrderEnum;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.auth.UserLevel;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.file.AbatchErreLogLet;
import com.wanda.ccs.mem.service.TFileAttachService;
import com.wanda.ccs.model.IMemberDimType;
import com.wanda.ccs.model.IMemberRight;
import com.wanda.ccs.model.TCmnActivity;
import com.wanda.ccs.model.TFileAttach;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.webapp.model.DisplayTagPageResult;

public class TargetFileManLet extends BaseCrudWinlet<TFileAttach> implements IMemberDimType, IMemberRight {
	private static final long serialVersionUID = 7118233401373137867L;
	
	public AbatchErreLogLet abatchErreLogLet = new AbatchErreLogLet();

	public AbatchErreLogLet getAbatchErreLogLet(IModuleRequest req)
			throws Exception {
		if (abatchErreLogLet == null) {
			abatchErreLogLet = (AbatchErreLogLet) req
					.getWinlet(AbatchErreLogLet.class.getName());
		}
		return abatchErreLogLet;
	}
	/**
	 * 默认无参构造函数。
	 */
	public TargetFileManLet() {

	}
	
	@Override
	protected TFileAttachService getCrudService() {
		return getService(TFileAttachService.class);
	}
	
	@Override
	protected void initSearch(IModuleRequest req, IModuleResponse resp) {
		super.initSearch(req, resp);
		query.put("refObjectType", TCmnActivity.class.getSimpleName());
		CcsUserProfile user = (CcsUserProfile) req.getUserProfile();
		query.put("userLevel", user.getLevel().toString());
		query.put("region", user.getRegionCode());
		if(user.getLevel() == UserLevel.CINEMA){
			query.put("userCinemaId", user.getCinemaId());
		}
	}
	
	@Override
	protected void prepareQuery(IModuleRequest req) {
		super.prepareQuery(req);
		query.put("activityName", req.getParameter("activityName", null));
		query.put("activityCode", req.getParameter("activityCode", null));
		query.put("fileName", req.getParameter("fileName", null));
	}

	@Override
	public int showList(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		PageResult<Map<String, ?>> pageResult = getCrudService().findByCriteriaQuery(query);
		if (pageResult == null) {
			return RETCODE_HIDE;
		}
		// New Displaytag使用的结果集，并将其放到Request中，使JSP可引用。
		req.setAttribute(NAME_OF_PAGE_RESULT, buildFileResult(pageResult));
		return RETCODE_OK;
	}
	
	/**
	 * 创建使用Displagtag标签库的分页结果集。
	 * <p>
	 * 子类可重载此方法，增加自定义的查询结果处理逻辑。
	 * </p>
	 * 
	 * @param pageResult
	 * @return
	 */
	public DisplayTagPageResult<Map<String, ?>> buildFileResult(PageResult<Map<String, ?>> pageResult) {
		DisplayTagPageResult<Map<String, ?>> result = new DisplayTagPageResult<Map<String, ?>>(pageResult);
		result.setSortDirection(QueryCriteria.DIRECTION_ASC
				.equalsIgnoreCase(query.getDirection()) ? SortOrderEnum.DESCENDING
				: SortOrderEnum.ASCENDING);
		result.setSortCriterion(query.getSort());
		return result;
	}
	
	@Override
	public int doView(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		Long fileId = req.getParameter("id", 0l);
		getAbatchErreLogLet(req).setFileAttachId(fileId);
		return RETCODE_OK;
	}
	
}
