package com.wanda.ccs.member;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.wanda.ccs.mem.service.TExtPointCriteriaService;
import com.wanda.ccs.mem.service.TExtPointRuleService;
import com.wanda.ccs.model.IMemberDimType;
import com.wanda.ccs.model.IMemberRight;
import com.wanda.ccs.model.TExtPointCriteria;
import com.wanda.ccs.model.TExtPointRule;
import com.xcesys.extras.core.util.StringUtil;
import com.xcesys.extras.util.SpringContextUtil;

/**
 * 积分规则管理
 * @author admin
 *
 */
public class ExtPointRuleLet extends BaseCrudWinlet<TExtPointRule> implements
		IMemberDimType,IMemberRight {
	
	private static final long serialVersionUID = 1975483350689805095L;

	// 积分
	public TExtPointRule rule;

	private SelExtPointCriteriaLet selExtPointCriteriaLet;
	
	public Long getId() {
		return id;
	}

	// 无参数的构造方法
	public ExtPointRuleLet() {

	}
	
	//获取选择客群Winlet
	public SelExtPointCriteriaLet getSelExtPointCriteriaLet(IModuleRequest req)throws Exception {
		if(selExtPointCriteriaLet == null) {
			selExtPointCriteriaLet = (SelExtPointCriteriaLet) req.getWinlet(SelExtPointCriteriaLet.class.getName());
		}
		return selExtPointCriteriaLet;
	}
	
	@Override
	protected TExtPointRuleService getCrudService() {
		return SpringContextUtil.getBean(TExtPointRuleService.class);
	}
	
	protected TExtPointCriteriaService getCriteriaService() {
		return SpringContextUtil.getBean(TExtPointCriteriaService.class);
	}

	@Override
	protected void prepareQuery(IModuleRequest req) {
		super.prepareQuery(req);
		//将String类型转换成日期类
		query.put("sStartDate", req.getParameter("sStartDate", null));
		query.put("eStartDate", req.getParameter("eStartDate", null));
		query.put("sEndDate", req.getParameter("sEndDate", null));
		query.put("eEndDate", req.getParameter("eEndDate", null));
		query.put("code", req.getParameter("code", null));
		query.put("name", req.getParameter("name", null));
		query.put("status", req.getParameter("status", null));
	}
	
	//特殊积分条件
	public void saveExtPointCriteria(List<TExtPointCriteria> list) throws Exception{
		if(list != null && !list.isEmpty()){
			TExtPointCriteria extPointCriteria = list.get(0); 
			extPointCriteria.getName();
			if(extPointCriteria != null) { 
				rule.settExtPointCriteria(extPointCriteria);
			}
		}
	}
	
	/**
	 * 选择客群
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int selExtPointCriteria(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		getSelExtPointCriteriaLet(req).setInvoked(ExtPointRuleLet.class.getName());
		return RETCODE_OK;
	}

	@Override
	protected void initSearch(IModuleRequest req, IModuleResponse resp) {
		super.initSearch(req, resp);
	}

	@Override
	public int showSearch(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		return super.showSearch(req, resp);
	}

	@Override
	public int showList(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		return super.showList(req, resp);
	}

	@Override
	public int doSearch(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		return super.doSearch(req, resp);
	}

	@Override
	public int doView(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		cancelEdit(req, resp);
		return super.doView(req, resp);
	}

	@Override
	public int doCreate(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		cancelEdit(req, resp);
		rule = new TExtPointRule();
		rule.setStatus(CAMPAINGN_STATUS_PLAN);
		rule.setAdditionType("P");
		
		return super.doCreate(req, resp);
	}

	@Override
	public int doEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		cancelEdit(req, resp);
		return super.doEdit(req, resp);
	}
	
	@Override
	public int cancelEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		rule = null;
		return super.cancelEdit(req, resp);
	}

	
	//停用方法
	public int doStop(IModuleRequest req, IModuleResponse resp) throws Exception {
		id = req.getParameter("id", 0L);
		if(id != 0) {
			log.debug("#### 执行停用方法:doStop, 停用对象ID[" + id + "] ####");
			rule = getCrudService().findById(id);
			rule.setStatus(CAMPAINGN_STATUS_INACTIVE);
			getCrudService().update(rule);
		}
		cancelEdit(req, resp);
		return RETCODE_OK;
	}
	
	//启用方法
	public int doStart(IModuleRequest req, IModuleResponse resp) throws Exception {
		id = req.getParameter("id", 0L);
		if(id != 0) {
			log.debug("#### 执行启用方法:doStop, 启用对象ID[" + id + "] ####");
			rule = getCrudService().findById(id);
			if(new Date().getTime() >= rule.getEndDtime().getTime()) {
				rule.setStatus(CAMPAINGN_STATUS_INACTIVE);
			}
			if(new Date().getTime() < rule.getStartDtime().getTime()) {
				rule.setStatus(CAMPAINGN_STATUS_PUBLISH);
			}
			if(new Date().getTime() >= rule.getStartDtime().getTime()) {
				rule.setStatus(CAMPAINGN_STATUS_EXECUTE);
			}
			getCrudService().update(rule);
		}
		cancelEdit(req, resp);
		return RETCODE_OK;
	}
	
	@Override
	public int saveEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if(req.isValidateField()) {
			if(req.getForm().getValidateField() == null) {
				return 10;
			}
			return req.getForm().getValidateField().isHasError() ? 11 : 10;
		}
		if(req.getForm().hasError()) {
			if(rule.getAdditionType() == null || ("P".equals(rule.getAdditionType()) && rule.getAdditionCode() == null)){
				req.setAttribute("MESSAGE", "请选择可兑换积分类型");
				return 100; 
			}
			return 2;
		} 
		
		if((rule.getAdditionCode() == null && rule.getAdditionPercent() == null) || StringUtil.isNullOrBlank(rule.getAdditionType())){
			req.setAttribute("MESSAGE", "请选择可兑换积分类型");
			return 100; 
		}
		
		if(rule.gettExtPointCriteria() == null){
			req.setAttribute("MESSAGE", "请选择积分条件");
			return 100;
		}
			
		if(rule.getAdditionType().equals("P")){
			rule.setAdditionCode(null);
		}else if(rule.getAdditionType().equals("C")){
			rule.setAdditionPercent(null);
		}
		securityContext(req);
		String status = req.getParameter("status", null);
		if(!StringUtil.isNullOrBlank(status)){
			rule.setStatus(status);
		}
		getCrudService().createOrUpdate(rule);
		
		cancelEdit(req, resp);
		log.debug("#### 执行数据保存方法:saveEdit，保存对象[" + rule + "] ####");
		
		return RETCODE_OK;
	}
	
	//发布方法
	public int doReplace(IModuleRequest req, IModuleResponse resp) throws Exception{
		if(req.isValidateField()) {
			if(req.getForm().getValidateField() == null) {
				return 10;
			}
			return req.getForm().getValidateField().isHasError() ? 11 : 10;
		}
		if(req.getForm().hasError()) {
			return 2;
		}
		securityContext(req);
		rule.setStatus(CAMPAINGN_STATUS_PUBLISH);
		this.getCrudService().createOrUpdate(rule);
		this.cancelEdit(req, resp);
		log.debug("#### 执行数据发布方法:doReplace, 发布对象[" + rule + "] ####");
		return RETCODE_OK;
	}
	
	@Override
	public int showEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if(id == null && rule == null)
			return RETCODE_HIDE;
		if(rule == null || (id != null && (rule.getId() == null || id.longValue() != rule.getId())))
			rule = getCrudService().getById(id);
		
		if(rule != null && rule.gettExtPointCriteria() != null) {
			String type = getCriteriaService().getCriteriaScheme(rule.gettExtPointCriteria().getId());
			if(type == null) {
				throw new Exception("积分规则类型解析失败！");
			} else {
				int t = Integer.parseInt(type);
				if("P".equals(rule.getAdditionType()) && t == 1) {
					rule.setAdditionType(null);
					rule.setAdditionCode(null);
					rule.setAdditionPercent(null);
				}
//				if(t == 1) { 
//					rule.setAdditionType("C");
//				}
				rule.gettExtPointCriteria().setType(t);
			}
		}
		
		rule.setEditing(editing);
		return RETCODE_OK;
	}
	
	/**
	 * 编辑页面Ajax输入验证。
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	@Validates({
		@Validate(name = "name", id = "tne", error = "积分规则名称不能为空。"),
		@Validate(name = "name", id = "maxlen", args = { "20" }, error = "名称长度不能超过20个字符"),
		@Validate(name = "strStartDtime", id = "tne", error = "开始时间不能为空。"),
		@Validate(name = "additionPercent", id = "tne", error = "请输入额外积分(百分比)。"),
		@Validate(name = "additionCode", id = "tne", error = "请输入额外积分(积分值)。"),
		@Validate(name = "strEndDtime", id = "tne", error = "结束时间不能为空。")
	})
	public ValidateResult validate(IModuleRequest req, Input input) {
		Form form = req.getForm();
		if(input == form.getValidateField()){
			if(input.getName().equals("additionType")){
				String value = input.getValue().toString();
				if(value.equals("P")){
					form.setDisabled("additionCode", true);
					form.setDisabled("additionPercent", false);
				}else if(value.equals("C")){
					form.setDisabled("additionCode", false);
					form.setDisabled("additionPercent", true);
				}
			}
		}
		if(input.getName().equals("name")){
			if(getCrudService().checkRuleName(input.getValue().toString(), rule.getId())){
				return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "名称已存在");
			}
		}else if(input.getName().equals("strStartDtime")){
			String strStartDtime = input.getValue().toString();
			String strEndDtime = form.getInputByName("strEndDtime").getValue() == null ? "" : form.getInputByName("strEndDtime").getValue().toString();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			if(!StringUtil.isNullOrBlank(strStartDtime)&& !StringUtil.isNullOrBlank(strEndDtime)){
				try {
					if(format.parse(strStartDtime).after(format.parse(strEndDtime))){
						return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "开始时间不能大于结束时间");
					}else{
						form.setDisabled("strEndDtime", true);
						form.setDisabled("strEndDtime", false);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}else if(input.getName().equals("strEndDtime")){
			String strStartDtime = form.getInputByName("strStartDtime").getValue() == null ? "" : form.getInputByName("strStartDtime").getValue().toString();
			String strEndDtime = input.getValue().toString();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			if(!StringUtil.isNullOrBlank(strStartDtime)&& !StringUtil.isNullOrBlank(strEndDtime)){
				try {
					if(format.parse(strStartDtime).after(format.parse(strEndDtime))){
						return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "结束时间不能小于开始时间");
					}else{
						form.setDisabled("strStartDtime", true);
						form.setDisabled("strStartDtime", false);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}else if(input.getName().equals("additionPercent")){
			String additionPercent = input.getValue() == null ? "" : input.getValue().toString();
			if(!StringUtil.isNullOrBlank(additionPercent)){
				if(Long.valueOf(additionPercent) <= 0){
					return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "额外积分(百分比)应该大于0");
				}
			}
		}else if(input.getName().equals("additionCode")){
			String additionCode = input.getValue() == null ? "" : input.getValue().toString();
			if(!StringUtil.isNullOrBlank(additionCode)){
				if(Long.valueOf(additionCode) <= 0){
					return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "额外积分(积分值)应该大于0");
				}
			}
		}
		return ValidateResult.PASS;
	}
	
	protected TExtPointRule newModel() {
		return new TExtPointRule();
	}

	public TExtPointRule getRule() {
		return rule;
	}

	public void setRule(TExtPointRule rule) {
		this.rule = rule;
	}
}
