package com.wanda.ccs.campaign;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.aggrepoint.adk.form.Form;
import com.aggrepoint.adk.form.Input;
import com.aggrepoint.adk.form.Validate;
import com.aggrepoint.adk.form.Validates;
import com.aggrepoint.adk.ui.ValidateResult;
import com.aggrepoint.adk.ui.ValidateResultType;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.mem.service.TCmnPhaseService;
import com.wanda.ccs.model.IMemberDimType;
import com.wanda.ccs.model.IMemberRight;
import com.wanda.ccs.model.TCampaign;
import com.wanda.ccs.model.TCmnPhase;
import com.xcesys.extras.core.util.DateUtil;
import com.xcesys.extras.core.util.StringUtil;
import com.xcesys.extras.util.ConvertUtil;

public class PhaseManLet extends BaseCrudWinlet<TCmnPhase> implements IMemberDimType, IMemberRight {
	private static final long serialVersionUID = 7118233401373137867L;
	public TCmnPhase editPhase;
	
	public ActivityManLet activityManLet = new ActivityManLet();
	
	
	private CampaignManLet campaignManLet;
	
	public CampaignManLet getCampaignManLet(IModuleRequest req)
			throws Exception {
		if (campaignManLet == null)
			campaignManLet = (CampaignManLet) req
					.getWinlet(CampaignManLet.class.getName());
		return campaignManLet;
	}

	public TCampaign getEditCampaign(IModuleRequest req) throws Exception {
		return getCampaignManLet(req).editCampaign;
	}

	public void setId(Long id){
		editing = false;
		this.id = id;
	}
	/**
	 * 默认无参构造函数。
	 */
	public PhaseManLet() {

	}

	@Override
	protected TCmnPhaseService getCrudService() {
		return getService(TCmnPhaseService.class);
	}
	
	@Override
	public int showList(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		initSearch(req, resp);
		query.put("campaignId", getEditCampaign(req).getId());
		req.setAttribute("campaign", getEditCampaign(req));
		req.setAttribute(
				"campaignIsEnd",
				getEditCampaign(req).getStatus()
						.equals(CAMPAINGN_STATUS_FINISH)
						|| getEditCampaign(req).getEndDate().before(
								DateUtil.getCurrentDate()));
		return super.showList(req, resp);
	}
	
	@Override
	public int cancelEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		editing = false;
		boolean isNew = false;
		if(editPhase != null && editPhase.getId() == null){
			isNew = true;
		}
		int ret = RETCODE_OK;
		editPhase = null;
		if(isNew){
			activityManLet = new ActivityManLet();
			super.cancelEdit(req, resp);
			getCampaignManLet(req).setType(CampaignManLet.editCampaignType);
			ret = 1;
		}
		return ret;
	}

	@Override
	public int doCreate(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		cancelEdit(req, resp);
		id = null;
		editPhase = new TCmnPhase();
		editPhase.setStatus(CAMPAINGN_STATUS_PLAN);
		editPhase.settCampaign(getEditCampaign(req));
		editPhase.setCampaignId(getEditCampaign(req).getId());
		editing = true;
		
		getCampaignManLet(req).setType(CampaignManLet.editPhaseType);
		
		return RETCODE_OK;
	}
	
	@Override
	public int doEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		getCampaignManLet(req).setType(CampaignManLet.editPhaseType);
		if(!StringUtil.isNullOrBlank(req.getParameter("id", null))){
			cancelEdit(req, resp);
			id = req.getParameter("id", 0L);
		}
		getCampaignManLet(req).setEditId(id);
		TCmnPhase phase = getCrudService().getById(id);
		getCampaignManLet(req).setOpenId(phase.getCampaignId());
		editing = true;
		return RETCODE_OK;
	}
	
	@Override
	public int doView(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		cancelEdit(req, resp);
		getCampaignManLet(req).setType(CampaignManLet.editPhaseType);
		super.doView(req, resp);
		getCampaignManLet(req).setEditId(id);
		TCmnPhase phase = getCrudService().getById(id);
		getCampaignManLet(req).setOpenId(phase.getCampaignId());
		return RETCODE_OK;
	}
	@Override
	public int doDelete(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		id = req.getParameter("id", 0l);
		if (id != 0) {
			log.debug("#### 执行删除方法:doDelete，删除对象ID[" + id + "] ####");
			ids = new Long[] { id };
		} else {
			String[] oids = req.getParameterValues("ids");
			ids = ConvertUtil.convertStringArrayToLongArray(oids);
			log.debug("#### 执行删除方法:doDelete，删除对象ID[" + ids + "] ####");
		}
		if(ids == null || ids.length == 0){
			return 1;
		}
		List<TCmnPhase> phases = getCrudService().findByIds(ids);
		for(TCmnPhase phase : phases){
			if(phase.gettCmnActivities() != null && !phase.gettCmnActivities().isEmpty()){
				req.setAttribute("MESSAGE", "活动：" + phase.getName() + "存在波次信息，不能删除。若要删除，请先删除阶段下的波次信息");
				return 100;
			}
		}
		return super.doDelete(req, resp);
	}
	
	@Override
	public int showEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if(id == null && editPhase == null)
			return RETCODE_HIDE;
		if(editPhase == null || (id != null && (editPhase.getId() == null || id.longValue() != editPhase.getId())))
			editPhase = getCrudService().getById(id);
		editPhase.setEditing(editing);
		
		activityManLet.setEditCampaign(getEditCampaign(req));
		activityManLet.setEditCmnPhase(editPhase);
		
		if(editPhase.gettCampaign().getStartDate().after(DateUtil.getCurrentDate())){
			req.setAttribute("startDate", new SimpleDateFormat("yyyy-MM-dd").format(editPhase.gettCampaign().getStartDate()));
		}else{
			req.setAttribute("startDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.getCurrentDate()));
		}
		req.setAttribute("endDate", new SimpleDateFormat("yyyy-MM-dd").format(editPhase.gettCampaign().getEndDate()));
		
		req.setAttribute("campaign", getEditCampaign(req));
		req.setAttribute(
				"campaignIsEnd",
				getEditCampaign(req).getStatus()
						.equals(CAMPAINGN_STATUS_FINISH)
						|| getEditCampaign(req).getEndDate().before(
								DateUtil.getCurrentDate()));
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
		if(editPhase.getStatus().equals(CAMPAINGN_STATUS_FINISH)){
			if(editPhase.getStartDate().after(DateUtil.getCurrentDate())){
				editPhase.setStatus(CAMPAINGN_STATUS_PLAN);
			}else if(editPhase.getEndDate().after(DateUtil.getCurrentDate())){
				editPhase.setStatus(CAMPAINGN_STATUS_EXECUTE);
			}
		}
		getCrudService().createOrUpdate(editPhase);
		id = editPhase.getId();
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
			@Validate(name = "name", id = "tne", error = "名称不能为空。"),
			@Validate(name = "name", id = "maxlen", args = { "20" }, error = "名称长度不能超过20个字符"),
			@Validate(name = "description", id = "maxlen", args = { "1000" }, error = "描述长度不能超过1000个字符"),
			@Validate(name = "startDate", id = "tne", error = "开始日期不能为空。"),
			@Validate(name = "endDate", id = "tne", error = "结束日期不能为空。")})
	public ValidateResult validate(IModuleRequest req, Input input) {
		Form form = req.getForm();
		if(input.getName().equals("name")){
			if(getCrudService().checkPhaseName(input.getValue().toString(), editPhase.getId(), editPhase.getCampaignId()))
				return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "该阶段名称已存在，请重新输入");
		}else if(input.getName().equals("startDate")){
			String startDate = input.getValue() == null ? "" : input.getValue().toString();
			String endDate = form.getInputByName("endDate").getValue() == null ? "" : form.getInputByName("endDate").getValue().toString();
			if(!StringUtil.isNullOrBlank(startDate) && !StringUtil.isNullOrBlank(endDate)){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				try {
					if(format.parse(startDate).after(format.parse(endDate))){
						return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "开始时间不能小于结束时间");
					}else{
						form.setDisabled("endDate", true);
						form.setDisabled("endDate", false);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} 
			if(getCrudService().checkPhaseStartDate(input.getValue().toString(), editPhase.getId()))
				return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "开始时间不能大于活动波次的最小开始时间");
		}else if(input.getName().equals("endDate")){
			String startDate = form.getInputByName("startDate").getValue() == null ? "" : form.getInputByName("startDate").getValue().toString();
			String endDate = input.getValue() == null ? "" : input.getValue().toString();
			if(!StringUtil.isNullOrBlank(startDate) && !StringUtil.isNullOrBlank(endDate)){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				try {
					if(format.parse(startDate).after(format.parse(endDate))){
						return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "结束时间不能大于开始时间");
					}else{
						form.setDisabled("startDate", true);
						form.setDisabled("startDate", false);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} 
			if(getCrudService().checkPhaseEndDate(input.getValue().toString(), editPhase.getId()))
				return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "结束时间不能小于活动波次的最大结束时间");
		}
		return ValidateResult.PASS;
	}

}
