package com.wanda.ccs.campaign;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONSerializer;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.aggrepoint.adk.form.Form;
import com.aggrepoint.adk.form.Input;
import com.aggrepoint.adk.form.Validate;
import com.aggrepoint.adk.form.Validates;
import com.aggrepoint.adk.ui.ValidateResult;
import com.aggrepoint.adk.ui.ValidateResultType;
import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.auth.UserLevel;
import com.wanda.ccs.auth.service.TAuthUserService;
import com.wanda.ccs.basemgt.service.TCinemaService;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.mem.service.TCampaignService;
import com.wanda.ccs.mem.service.TCmnPhaseService;
import com.wanda.ccs.model.IMemberDimType;
import com.wanda.ccs.model.IMemberRight;
import com.wanda.ccs.model.TAuthUser;
import com.wanda.ccs.model.TCampaign;
import com.wanda.ccs.model.TCampaignCinema;
import com.wanda.ccs.model.TCinema;
import com.wanda.ccs.model.TCmnPhase;
import com.wanda.ccs.service.SpringCommonService;
import com.xcesys.extras.core.model.ValueLabelItem;
import com.xcesys.extras.core.util.DateUtil;
import com.xcesys.extras.core.util.StringUtil;
import com.xcesys.extras.util.ConvertUtil;

public class CampaignManLet extends BaseCrudWinlet<TCampaign> implements IMemberDimType, IMemberRight {
	private static final long serialVersionUID = 7118233401373137867L;
	public PhaseManLet phaseManLet = new PhaseManLet();
	public TCampaign editCampaign;
	public static String editCampaignType = "campaign";
	public static String editPhaseType = "phase";
	public static String editActivityType = "activity";
	
	private boolean hideList = false;
	//区域下的影城列表
	private List<TCinema> cinemaList;
	
	private Long editId = null;
	private Long openId = null;
	
	public Long getOpenId() {
		return openId;
	}

	public void setOpenId(Long openId) {
		this.openId = openId;
	}

	public Long getEditId() {
		return editId;
	}

	public void setEditId(Long editId) {
		this.editId = editId;
	}

	private SelCinemaLet selCinemaLet;
	//获取选择影城Winlet
	public SelCinemaLet getSelCinemaLet(IModuleRequest req)
			throws Exception {
		if (selCinemaLet == null)
			selCinemaLet = (SelCinemaLet) req
					.getWinlet(SelCinemaLet.class.getName());
		return selCinemaLet;
	}
	
	//编辑授权人Winlet
	private SelUserLet selUserLet;
	
	//获取授权人Winlet
	public SelUserLet getSelUserLet(IModuleRequest req)
			throws Exception {
		if (selUserLet == null)
			selUserLet = (SelUserLet) req
					.getWinlet(SelUserLet.class.getName());
		return selUserLet;
	}
	
	//当前编辑对象的类型：活动、阶段、波次 
	private String type;
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 默认无参构造函数。
	 */
	public CampaignManLet() {

	}

	@Override
	protected TCampaignService getCrudService() {
		return getService(TCampaignService.class);
	}
	
	/**
	 * Map<所选择的区域编码, 所选择的区域下影城>
	 */
	private Map<String, List<TCinema>> selMap = new LinkedHashMap<String, List<TCinema>>();
	
	public Map<String, List<TCinema>> getSelMap() {
		return selMap;
	}

	public void setSelMap(Map<String, List<TCinema>> selMap) {
		this.selMap = selMap;
	}

	/**
	 * 营销活动初始化
	 * 
	 * @param conpa
	 */
	public void init(TCampaign campaign) {
		selMap.clear();
		for (TCampaignCinema cc : campaign.gettCampaignCinemas()) {
			if (selMap != null) {
				if (selMap.get(cc.gettCinema().getArea()) == null) {
					selMap.put(cc.gettCinema().getArea(), new ArrayList<TCinema>());
				}
				selMap.get(cc.gettCinema().getArea()).add(cc.gettCinema());
			}
		}
	}

	/**
	 * 获取根节点
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int getRoot(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if(id == null && editCampaign == null)
			return RETCODE_HIDE;
		if(editCampaign == null || (id != null && (editCampaign.getId() == null || id.longValue() != editCampaign.getId())))
			editCampaign = getCrudService().getById(id);
		req.setAttribute("campaign", editCampaign);
		req.setAttribute("type", type);
		req.setAttribute("editId", editId);
		req.setAttribute("openId", openId);
		return RETCODE_OK;
	}
	
	/**
	 * 获取子节点
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int content(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		Long id = req.getParameter("id", 0L);
		String type = req.getParameter("type", "");
		if(id.longValue() != 0){
			if(type.equals(editCampaignType)){
				TCampaign campaign = getCrudService().getById(id);
				if(campaign != null)
					req.setAttribute("phaseList", campaign.gettCmnPhases());
				return 1;
			}else if(type.equals(editPhaseType)){
				TCmnPhase phase = getService(TCmnPhaseService.class).getById(id);
				if(phase != null)
					req.setAttribute("activityList", phase.gettCmnActivities());
				return 2;
			}else if(type.equals(editActivityType)){
				
			}
		}
		return RETCODE_OK;
	}
	/**
	 * 选择节点
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int selectNode(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		Long id = req.getParameter("id", 0L);
		type = req.getParameter("type", "");
		if(id.longValue() != 0){
			if(type.equals("campaign")){
				this.id = id;
			}else if(type.equals("phase")){
				phaseManLet.setId(id);
			}else if(type.equals("activity")){
				phaseManLet.activityManLet.setId(id);
			}
		}
		return RETCODE_OK;
	}
	
	@Override
	protected void initSearch(IModuleRequest req, IModuleResponse resp) {
		super.initSearch(req, resp);
		CcsUserProfile user = (CcsUserProfile) req.getUserProfile();
		if(user.getLevel() == UserLevel.REGION){
			if(cinemaList == null || cinemaList.isEmpty()){
				cinemaList = getService(TCinemaService.class).findCinemasByArea(user.getRegionCode()); 
			}
			req.setAttribute("cinemaList", cinemaList);
		}
	}


	@Override
	protected void prepareQuery(IModuleRequest req) {
		super.prepareQuery(req);
		query.put("code", req.getParameter("code", null));
		query.put("channel", req.getParameter("channel", null));
		query.put("name", req.getParameter("name", null));
		query.put("sStartDate", req.getParameter("sStartDate", null));
		query.put("eStartDate", req.getParameter("eStartDate", null));
		query.put("sEndDate", req.getParameter("sEndDate", null));
		query.put("eEndDate", req.getParameter("eEndDate", null));
		query.put("status", req.getParameter("status", null));
		query.put("type", req.getParameter("type", null));
		
		// 下拉多选时使用，获取所选区域的值
		String[] areas = req.getParameterValues("area");
		String[] cinemas = req.getParameterValues("cinema");
		query.put("areas", areas);
		
		if(cinemas != null){
			query.put("cinemas", ConvertUtil.convertStringArrayToLongArray(cinemas));
		}else{
			query.put("cinemas", null);
		}
		
		query.put("strCinemas", JSONSerializer.toJSON(cinemas));
	}
	
	@Override
	public int showSearch(IModuleRequest req, IModuleResponse resp) throws Exception{
		if(hideList)
			return RETCODE_HIDE;
		return super.showSearch(req, resp);
	}
	
	@Override
	public int showList(IModuleRequest req, IModuleResponse resp) throws Exception{
		if(hideList)
			return RETCODE_HIDE;
//		TSetting setting = getService(TSettingService.class).getByName("CAMPAIGN_SEARCH_ALL");
//		boolean searchAll = true;
//		if(setting != null){
//			searchAll = Boolean.valueOf(setting.getValue());
//		}
		CcsUserProfile user = (CcsUserProfile) req.getUserProfile();
		query.put("userId", user.getId());
//		query.put("searchAll", searchAll);
		query.put("userLevel", user.getLevel().toString());
		query.put("region", user.getRegionCode());
		if(user.getLevel() == UserLevel.CINEMA){
			query.put("userCinemaId", user.getCinemaId());
		}
		return super.showList(req, resp);
	}
	
	@Override
	public int cancelEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		editing = false;
		boolean isNew = false;
		if(editCampaign != null && editCampaign.getId() == null){
			isNew = true;
		}
		int ret = RETCODE_OK;
		editCampaign = null;
		if(isNew){
			doClose(req, resp);
			ret = 1;
		}
		return ret;
	}
	
	/**
	 * 关闭编辑界面
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int doClose(IModuleRequest req, IModuleResponse resp) throws Exception{
		super.cancelEdit(req, resp);
		editCampaign = null;
		phaseManLet = new PhaseManLet();
		type = editCampaignType;
		
		hideList = false;
		return RETCODE_OK;
	}
	@Override
	public int doCreate(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		cancelEdit(req, resp);
		editCampaign = new TCampaign();
		CcsUserProfile user = (CcsUserProfile) req.getUserProfile();
		editCampaign.setCreationCinemaId(user.getCinemaId());
		editCampaign.settCinema(getService(TCinemaService.class).getById(user.getCinemaId()));
		editCampaign.setCreationAreaId(user.getRegionCode());
		editCampaign.setCreationLevel(user.getLevel().toString());
		editCampaign.setStatus(CAMPAINGN_STATUS_PLAN);
		editCampaign.setCreatedBy(user.getId());
		editCampaign.setCreatedDate(DateUtil.getCurrentDate());
		if(user.getLevel() != UserLevel.GROUP){
			editCampaign.setAllCinema(false);
		}
		if(user.getLevel() == UserLevel.CINEMA){
			editCampaign.gettCampaignCinemas().clear();
			TCampaignCinema cc = new TCampaignCinema();
			cc.setCinemaId(user.getCinemaId());
			cc.settCampaign(editCampaign);
			TCinema cinema = getService(TCinemaService.class).getById(user.getCinemaId());
			cinema.getShortName();
			cc.settCinema(cinema);
			editCampaign.gettCampaignCinemas().add(cc);
			
			editCampaign.setSettlementType(CAMPAINGN_SETTLEMENT_TYPE_CINEMA);
		}
		editing = true;
		
		type = "campaign";
		hideList = true;
		return RETCODE_OK;
	}
	
	@Override
	public int doEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		type = "campaign";
		if(!StringUtil.isNullOrBlank(req.getParameter("id", null))){
			cancelEdit(req, resp);
			id = req.getParameter("id", 0L);
		}
		setEditId(id);
		editing = true;
		hideList = true;
		return RETCODE_OK;
	}
	
	@Override
	public int doView(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		cancelEdit(req, resp);
		type = "campaign";
		hideList = true;
		return super.doView(req, resp);
	}
	
	@Override
	public int doDelete(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		doClose(req, resp);
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
		List<TCampaign> campaigns = getCrudService().findByIds(ids);
		for(TCampaign campaign : campaigns){
			if(campaign.gettCmnPhases() != null && !campaign.gettCmnPhases().isEmpty()){
				req.setAttribute("MESSAGE", "活动：" + campaign.getName() + "存在阶段等信息，不能删除。若要删除，请先删除活动下的阶段信息");
				return 100;
			}
		}
		return super.doDelete(req, resp);
	}
	
	@Override
	public int showEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if(id == null && editCampaign == null)
			return RETCODE_HIDE;
		if(editCampaign == null || (id != null && (editCampaign.getId() == null || id.longValue() != editCampaign.getId())))
			editCampaign = getCrudService().getById(id);
		if(editCampaign != null)
			editCampaign.setEditing(editing);
		
		if(editCampaign.getId() != null){
			for(TCampaignCinema cc : editCampaign.gettCampaignCinemas()){
				cc.gettCinema().getShortName();
			}
		}
		init(editCampaign);
		req.setAttribute("date", new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.getCurrentDate()));
		return RETCODE_OK;
	}
	
	/**
	 * 选择适用影城
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int selCinema(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		
		getSelCinemaLet(req).setInvoked(CampaignManLet.class.getName());
		getSelCinemaLet(req).init(editCampaign);
		return RETCODE_OK;
	}
	
	/**
	 * 选择授权人
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int selUser(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		getSelUserLet(req).setInvoked(CampaignManLet.class.getName());
		return RETCODE_OK;
	}
	
	/**
	 * 删除授权人
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int delUser(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		editCampaign.setAllowModifier(null);
		return RETCODE_OK;
	}
	
	/**
	 * 保存选择的影城
	 * @param id
	 */
	public void saveCinema(List<TCinema> list, boolean selectedAll){
		editCampaign.gettCampaignCinemas().clear();
		editCampaign.setAllCinema(selectedAll);
		if(!selectedAll){
			if(list != null && !list.isEmpty()){
				for(TCinema cinema : list){
					TCampaignCinema cc = new TCampaignCinema();
					cc.setCinemaId(cinema.getId());
					cc.settCampaign(editCampaign);
					cc.settCinema(cinema);
					editCampaign.gettCampaignCinemas().add(cc);
				}
			}
		}
	}
	
	/**
	 * 保存选择的授权人
	 * @param id
	 */
	public void saveUser(List<TAuthUser> list){
		if(list != null && !list.isEmpty()){
			TAuthUser user = list.get(0);
			if(user.getFromNc().equals("Y"))
				editCampaign.setAllowModifier(user.getRtx());
			else
				editCampaign.setAllowModifier(user.getLoginId());
		}
	}
	/**
	 * 根据登录id获取人员列表，返回JSON结果。
	 * 
	 * @return
	 */
	public int doGetUser(IModuleRequest req, IModuleResponse resp) {
		List<TAuthUser> list = null;

		String like = req.getParameter("like");
		if(!StringUtil.isNullOrBlank(like)){
			list = getService(TAuthUserService.class).findByLike(like);
		}

		List<ValueLabelItem> items = new ArrayList<ValueLabelItem>();
		if (list != null && !list.isEmpty()) {
			for (TAuthUser user : list) {
				if(user.getFromNc().equals("Y")){
					items.add(new ValueLabelItem(user.getId(), user.getRtx()
							, user.getRtx(), null));
				}else{
					items.add(new ValueLabelItem(user.getId(), user.getLoginId()
							, user.getLoginId(), null));
				}
				
			}
		}
		writeJSON(resp, items);
		return RETCODE_OK;
	}
	
	@Override
	public int saveEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if (req.isValidateField())
			return 2;
		if (req.getForm().hasError())
			return 3;
		if(!editCampaign.getAllCinema() && editCampaign.gettCampaignCinemas().isEmpty()){
			CcsUserProfile user = (CcsUserProfile) req.getUserProfile();
			if(user.getLevel() == UserLevel.GROUP){
				req.setAttribute("MESSAGE", "请选择适用范围或者适用所有影城");
			}else{
				req.setAttribute("MESSAGE", "请选择适用范围");
			}
			return 100;
		}
		// 初始化安全环境上下文。
		securityContext(req);
		if(!StringUtil.isNullOrBlank(editCampaign.getAllowModifier()))
			editCampaign.setAllowModifier(editCampaign.getAllowModifier().trim());
		if(editCampaign.getStatus().equals(CAMPAINGN_STATUS_FINISH)){
			if(editCampaign.getStartDate().after(DateUtil.getCurrentDate())){
				editCampaign.setStatus(CAMPAINGN_STATUS_PLAN);
			}else if(editCampaign.getEndDate().after(DateUtil.getCurrentDate())){
				editCampaign.setStatus(CAMPAINGN_STATUS_EXECUTE);
			}
		}
		getCrudService().createOrUpdate(editCampaign);
		id = editCampaign.getId();
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
			@Validate(name = "channel", id = "tne", error = "请选择活动统计渠道。"),
			@Validate(name = "type", id = "tne", error = "请选择活动类型。"),
			@Validate(name = "settlementType", id = "tne", error = "请选择结算方式。"),
			@Validate(name = "startDate", id = "tne", error = "开始日期不能为空。"),
			@Validate(name = "endDate", id = "tne", error = "结束日期不能为空。")})
	public ValidateResult validate(IModuleRequest req, Input input) {
		Form form = req.getForm();
		if(input.getName().equals("name")){
			if(getCrudService().checkCampaignname(input.getValue().toString(), editCampaign.getId())){
				return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY,"该活动名称已存在，请重新输入");
			}
		}else if(input.getName().equals("allowModifier")){
			if(!StringUtil.isNullOrBlank(input.getValue().toString())){
				if(!getService(TAuthUserService.class).checkUserId(input.getValue().toString().trim())){
					return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY,"该授权人不存在，请重新输入");
				}
			}
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
			if(getCrudService().checkStartDate(editCampaign.getId(), input.getValue().toString()))
				return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "开始日期不能大于活动阶段的最小开始日期");
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
			if(getCrudService().checkEndDate(editCampaign.getId(), input.getValue().toString()))
				return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "结束日期不能小于活动阶段的最大结束日期");
		}
		return ValidateResult.PASS;
	}
	
	
	@Override
	public int doGetCinema(IModuleRequest req, IModuleResponse resp) {
		List<TCinema> list = new ArrayList<TCinema>();
		String areaIds = req.getParameter("param", "");
		
		if(!StringUtil.isNullOrBlank(areaIds)){
			String[] areas = areaIds.split(",");
			if(areas.length > 0)
				for (String area : areas) {
					list.addAll(SpringCommonService.getCinemasByArea(area));
				}
		}
		
		List<ValueLabelItem> items = new ArrayList<ValueLabelItem>();
		if (list != null && !list.isEmpty()) {
			for (TCinema cinema : list) {
				items.add(new ValueLabelItem(cinema.getId(), cinema.getCode(),
						cinema.getInnerName(), cinema.getOutName()));
			}
		}
		writeJSON(resp, items);
		return RETCODE_OK;
	}
}
