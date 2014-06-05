package com.wanda.ccs.member;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.aggrepoint.adk.form.Input;
import com.aggrepoint.adk.form.Validate;
import com.aggrepoint.adk.form.Validates;
import com.aggrepoint.adk.ui.ValidateResult;
import com.aggrepoint.adk.ui.ValidateResultType;
import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.basemgt.service.TCinemaService;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.mem.service.TExtPointRuleService;
import com.wanda.ccs.mem.service.TMemberService;
import com.wanda.ccs.mem.service.TPointHistoryService;
import com.wanda.ccs.model.TCinema;
import com.wanda.ccs.model.TExtPointRule;
import com.wanda.ccs.model.TPointHistory;
import com.wanda.ccs.service.SpringCommonService;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.ICrudService;
import com.xcesys.extras.util.SpringContextUtil;

public class MemberPointHistoryLet extends BaseCrudWinlet<TPointHistory> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -373645026315385474L;

	public TPointHistoryService service;
	// 积分历史关联的会员的id
	public Long memberId;
	QueryCriteria<String, Object> querythis = new QueryCriteria<String, Object>();
	public boolean showPointByLevel = false;

	public TPointHistory tPointHistory = null;
	public String invoked;
	public boolean isEdit = false;
	public Long pointlong ;
	public Map<String, String> cinemaInnerCodeMaps;
	public Long pointOrderId;//积分操作单号
	public String selectFlag="T";//查看票，卖品  T票  G 卖品
	public String currentSel = "T";
	public Long ruleId;//规则ID
	
	private Map<String,String> pointCinema;
	public Long getPointOrderId() {
		return pointOrderId;
	}
	public void setPointOrderId(Long pointOrderId) {
		this.pointOrderId = pointOrderId;
	}

	public MemberPointHistoryLet memberPointHistoryLet;
	MemberPointHistoryLet getMemberPointHistoryLet(IModuleRequest req) throws Exception {
		if(memberPointHistoryLet == null) {
			memberPointHistoryLet = (MemberPointHistoryLet) req.getWinlet(MemberPointHistoryLet.class.getName());
		}
		return memberPointHistoryLet;
	}
	public String getInvoked() {
		return invoked;
	}

	public void setInvoked(String invoked) {
		this.invoked = invoked;
		this.showPointByLevel = true;
		isEdit = false;
	}

	public Long getMemberId() {
		return memberId;
	}

	// 提供给另一个Winlet使用的对外公开的方法，用于设值memberId
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
		this.pointOrderId = null;
		this.query.put("memberId", memberId);
	}

	public boolean isShowPointByLevel() {
		return showPointByLevel;
	}

	public void setShowPointByLevel(boolean showPointByLevel) {
		this.showPointByLevel = showPointByLevel;
	}

	@Override
	protected ICrudService<TPointHistory> getCrudService() {
		if (service == null) {
			service = SpringContextUtil.getBean(TPointHistoryService.class);
		}
		return service;
	}

	@Override
	public int doCreate(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		tPointHistory = new TPointHistory();
		tPointHistory.setMemberId(memberId);
		tPointHistory.setCreatedBy(((CcsUserProfile)req.getUserProfile()).getId());
		isEdit = true;
		pointlong = new Date().getTime();
		return super.doCreate(req, resp);
	}

	@Override
	public int doSearch(IModuleRequest req, IModuleResponse resp)
	throws Exception {
		id = null;
		query.put("pagePoint", req.getParameter("pagePoint", 1));
//		req.setAttribute(NAME_OF_QUERY, query);
		return RETCODE_OK;
	}
	
	@Override
	public int showEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		CcsUserProfile user = getUser(req);
		//登陆用户是影城级别的用户
		if(cinemaInnerCodeMaps == null || cinemaInnerCodeMaps.isEmpty()){
			if("CINEMA".equals(user.getLevelName())){
				TCinema tCinema = getService(TCinemaService.class).findById(user.getCinemaId());
				cinemaInnerCodeMaps = new HashMap<String, String>();
				cinemaInnerCodeMaps.put(tCinema.getInnerCode(), tCinema.getInnerName());
			}else if("REGION".equals(user.getLevelName())){
				cinemaInnerCodeMaps = SpringCommonService.getAllCinemaMap(user.getRegionCode());
			}else{
				cinemaInnerCodeMaps = SpringCommonService.getAllCinemaMap(null);
			}
		}
		TPointHistory model = null;
		if (id != null && id != 0) {
			// 编辑或查看的场景
			try {
				// 编辑情况
				model = findById(id);
			} catch (Exception e) {
				resp.setUserMessage(e.getLocalizedMessage());
				return RETCODE_ERR;
			}
		} else if (isEdit) {
			// 新增数据的场景
			model = tPointHistory;
		}
		if (model == null) {
			log.debug("#### 隐藏Edit窗口 ####");
			return 8000;
		}
		if (model != null) {
			model.setEditing(editing && canEdit(model));
		}
		Date nowDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String maxDate = format.format(nowDate);
		String nowYear = (String) format.format(nowDate).subSequence(0, 4);
		int intNow = Integer.valueOf(nowYear)-1;
		req.setAttribute("maxDate", maxDate);
		req.setAttribute("minDate", String.valueOf(intNow)+"-01-01");
		req.setAttribute(NAME_OF_EDITING_OBJECT, model);
		req.setAttribute("CINEMAINNERCODEMAP", cinemaInnerCodeMaps);
		return RETCODE_OK;
	}

	@Override
	public int showList(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if (query.get("memberId") != null && showPointByLevel) {
			if(pointCinema == null || pointCinema.isEmpty())
				pointCinema = SpringCommonService.getAllCinemaMap(null);
			req.setAttribute("POINTCINAME", pointCinema);
			req.setAttribute("MEMBERSTATUS", getService(TMemberService.class).findById((Long)query.get("memberId")).getStatus());
			PageResult<TPointHistory> pageTicketResult = SpringContextUtil.getBean(TPointHistoryService.class).findByCriteria(query);
			req.setAttribute("POINTRESULT", buildResult(pageTicketResult));
			req.setAttribute("MEMBERCINEMA", getService(TMemberService.class).findById((Long)query.get("memberId")).gettCinema());
			return RETCODE_OK;
		} else
			return RETCODE_HIDE;
	}

	@Override
	public int saveEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if (req.isValidateField()) {
			if (req.getForm().getValidateField() == null) {
				return 10;
			}
			return req.getForm().getValidateField().isHasError() ? 11 : 10;
		}
		if (req.getForm().hasError())
			return 2;
		String setTime = req.getParameter("setTime", null);
		if(setTime == null || "".equals(setTime)){
			setTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) ;
		}
		TPointHistory  model = null;
		if (tPointHistory != null) {
			model = tPointHistory;
		} else {
			if (id != null) {
				model = super.findById(id);
			} else {
				model = newModel();
			}
		}
		// 从request中把数据populate到编辑对象中
//		populate(model, req);
		tPointHistory.setSetTime(new SimpleDateFormat("yyyy-MM-dd").parse(setTime));
		model.setSetTime(new SimpleDateFormat("yyyy-MM-dd").parse(setTime));
		if(model.getLevelPoint() != null){
			String ss1 = String.valueOf(model.getLevelPoint());
			BigDecimal d5 = new BigDecimal( ss1  );
			String s6 =d5.setScale(0,BigDecimal.ROUND_DOWN).toString();
			long a = Long.valueOf(s6);
			model.setLevelPoint(a);
			tPointHistory.setLevelPoint(a);
		}
		if(model.getActivityPoint() != null){
			String s1 = String.valueOf(model.getActivityPoint());
			BigDecimal dd5 = new BigDecimal( s1  );
			String ss6 =dd5.setScale(0,BigDecimal.ROUND_DOWN).toString();
			long aa = Long.valueOf(ss6);
			model.setActivityPoint(aa);
			tPointHistory.setActivityPoint(aa);
		}
		req.setAttribute(NAME_OF_EDITING_OBJECT, model);
		// 保存前执行数据验证。
		validation(req, resp, model);

		// 初始化安全环境上下文。
		securityContext(req);

		// 执行保存对象操作。
		if (model != null) {
			preSave(null);
			getCrudService().createOrUpdate(model);
			postSave();
			log.debug("#### 执行数据保存方法:saveEdit，保存对象[" + model + "] ####");
		}
	
		// 成功保存后清空变量
		id = null;
		copyFromId = null;
		editing = false;
		tPointHistory = null;
		isEdit = false;
		
		return RETCODE_OK;
	}

	@Validates({
		@Validate(name = "cinemaInnerCode", id = "tne", error = "积分影城不能为空。") })
	public ValidateResult validate(IModuleRequest req, Input input) {
//		activityPoint,levelPoint
//		Form form = input.getForm();
		if (input.getName().equals("activityPoint")
				|| input.getName().equals("levelPoint")) {
//			String ss1 = input.getValue().toString();
//			BigDecimal d5 = new BigDecimal( ss1  );
//			String s6 =d5.setScale(0,BigDecimal.ROUND_DOWN).toString();
//			long activityPoint = Long.valueOf(s6);
//			
//			String s1 = input.getValue().toString();
//			BigDecimal dd5 = new BigDecimal( s1  );
//			String ss6 =dd5.setScale(0,BigDecimal.ROUND_DOWN).toString();
//			long levelPoint = Long.valueOf(ss6);
			long activityPoint = 0l;
			long levelPoint =0l;
			if(tPointHistory.getActivityPoint() !=null){
				activityPoint = tPointHistory.getActivityPoint();
			}
			if(tPointHistory.getLevelPoint()!=null){
				levelPoint = tPointHistory.getLevelPoint();
			}
			
			
			if (getService(TPointHistoryService.class).checkPointBalance(activityPoint, levelPoint,memberId)) {
				return new ValidateResult(
						ValidateResultType.FAILED_SKIP_PROPERTY,
						"非定级积分、定级积分之与会员积分余额之和不能小于零");
			}
			
		}
		String adjResion = "";
		if (input.getName().equals("adjResion")) {
			adjResion = tPointHistory.getAdjResion();
			if(adjResion.length()>200){
				return new ValidateResult(
						ValidateResultType.FAILED_SKIP_PROPERTY,
						"调整原因不能超过100个字");
			}
		}
		return ValidateResult.PASS;
	}

	@Override
	public int cancelEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		isEdit = false;
		return super.cancelEdit(req, resp);
	}
	
	
	public int doPointDataView(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		pointOrderId = req.getParameter("pointOrderId", 0L);
		getMemberPointHistoryLet(req).setPointOrderId(pointOrderId);
		return RETCODE_OK;
	}
	
	public int showPointSaleView(IModuleRequest req, IModuleResponse resp)
			throws Exception {

		if (pointOrderId == null) {
			req.setAttribute("F", true);
			return RETCODE_HIDE;
		} else {
			req.setAttribute("currentSel", currentSel);
			req.setAttribute("F", false);
			return RETCODE_OK;
		}

	}
	
	public int cancelPointSaleView(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		pointOrderId = null;
		return RETCODE_OK;
	}
	
	//查询购票信息
	public int doSearchTicketData(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		// pointOrderId = req.getParameter("pointOrderId", 0L);
		selectFlag = req.getParameter("ticketType", "T");
		ruleId = null;
		return RETCODE_OK;
	}
	
	
	//查询购卖品信息
	public int doSearchConData(IModuleRequest req, IModuleResponse resp)
	throws Exception {
//		pointOrderId = req.getParameter("pointOrderId", 0L);
		selectFlag = req.getParameter("conType", "T");
		ruleId = null;
		return RETCODE_OK;
	}

	//显示购票信息
	public int showTicketView(IModuleRequest req, IModuleResponse resp)
	throws Exception {
		if( !"T".equals(selectFlag)){
			return RETCODE_HIDE;
		}else{
			TPointHistory tPointHistory = getService(TPointHistoryService.class).findById(pointOrderId);
			querythis.put("ORDER_ID", tPointHistory.getPointTransCode());
			querythis.put("CINEMA_INNERCODE", tPointHistory.getCinemaInnerCode());
			PageResult<TPointHistory> pageTicketResult = SpringContextUtil.getBean(TPointHistoryService.class).findByCriteriaForTicket(querythis, null);
			req.setAttribute("TICKETRESULT", buildResult(pageTicketResult));
			currentSel = "T";
			return RETCODE_OK;
		}
		
	}
	
	//显示购卖品信息
	public int showConView(IModuleRequest req, IModuleResponse resp)
	throws Exception {
		if( !"G".equals(selectFlag) ){
			return RETCODE_HIDE;
		}else{
			
			TPointHistory tPointHistory = getService(TPointHistoryService.class).findById(pointOrderId);
			querythis.put("ORDER_ID", tPointHistory.getPointTransCode());
			querythis.put("CINEMA_INNERCODE", tPointHistory.getCinemaInnerCode());
			PageResult<TPointHistory> pageConResult = SpringContextUtil.getBean(TPointHistoryService.class).findByCriteriaForCon(querythis, null);
			// New Displaytag使用的结果集，并将其放到Request中，使JSP可引用。
			req.setAttribute("CONRESULT", buildResult(pageConResult));
			currentSel = "G";
			return RETCODE_OK;
		}
		
	}
	
	//查看积分规则
	public int doRuleView(IModuleRequest req, IModuleResponse resp)
	throws Exception {
		ruleId = req.getParameter("ruleId", 0L);
		return RETCODE_OK;
	}
	
	//显示购卖品信息
	public int showRuleView(IModuleRequest req, IModuleResponse resp)
	throws Exception {
		if( ruleId == null){
			return RETCODE_HIDE;
		}else{
//			TPointHistory tPointHistory = getService(TPointHistoryService.class).findById(pointOrderId);
//			query.put("ORDER_ID", tPointHistory.getPointTransCode());
//			query.put("CINEMA_INNERCODE", tPointHistory.getCinemaInnerCode());
//			PageResult<TPointHistory> pageConResult = SpringContextUtil.getBean(TPointHistoryService.class).findByCriteriaForCon(query, null);
//			// New Displaytag使用的结果集，并将其放到Request中，使JSP可引用。
//			req.setAttribute("CONRESULT", buildResult(pageConResult));
//			currentSel = "G";
			TExtPointRule rule = getService(TExtPointRuleService.class).findById(ruleId);
			req.setAttribute("RULE", rule);
			return RETCODE_OK;
		}
	}
	
	public int cancelRuleView(IModuleRequest req, IModuleResponse resp)
	throws Exception {
		ruleId = null;
		return RETCODE_OK;
	}
}
