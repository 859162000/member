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
import com.wanda.ccs.mem.service.TMemberPointAdjustService;
import com.wanda.ccs.mem.service.TMemberService;
import com.wanda.ccs.mem.service.TPointHistoryService;
import com.wanda.ccs.model.TCinema;
import com.wanda.ccs.model.TMember;
import com.wanda.ccs.model.TMemberPointAdjust;
import com.wanda.ccs.model.TPointHistory;
import com.wanda.ccs.service.SpringCommonService;
import com.xcesys.extras.core.service.ICrudService;
import com.xcesys.extras.util.ConvertUtil;
import com.xcesys.extras.util.SpringContextUtil;

public class MemberPointAdjustApproveLet extends BaseCrudWinlet<TMemberPointAdjust> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -373645026315325474L;
	public TMemberPointAdjustService service;
//	// 积分历史关联的会员的id
	public Long memberId;
	public long resetFlag = 0;
	public Map<String, String> cinemaInnerCodeMaps;
	public TMemberPointAdjust tMemberPointAdjust = null;
	boolean isEdit = false;


	@Override
	protected ICrudService<TMemberPointAdjust> getCrudService() {
		if (service == null) {
			service = SpringContextUtil.getBean(TMemberPointAdjustService.class);
		}
		return service;
	}

	@Override
	public int doCreate(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		resetFlag= new Date().getTime();
		tMemberPointAdjust = new TMemberPointAdjust();
//		tMemberPointAdjust.setMemberId(memberId);
		tMemberPointAdjust.setApprove("W");//待审批
		tMemberPointAdjust.setCreatedBy(((CcsUserProfile)req.getUserProfile()).getId());
		isEdit = true;
		return super.doCreate(req, resp);
	}

	@Override
	public int doEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		resetFlag= new Date().getTime();
		return super.doEdit(req, resp);
	}
	
	@Override
	public int doSearch(IModuleRequest req, IModuleResponse resp)
	throws Exception {
		id = null;
		return super.doSearch(req, resp);
	}
	@Override
	protected void prepareQuery(IModuleRequest req) {
		super.prepareQuery(req);
		query.put("mobile", req.getParameter("mobile", null));
		query.put("memberNo", req.getParameter("memberNo", null));
		query.put("approve", req.getParameter("approve", null));
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
		TMemberPointAdjust model = null;
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
			model = tMemberPointAdjust;
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
			req.setAttribute("POINTCINAME", SpringCommonService.getAllCinemaMap(null));
			return super.showList(req, resp);
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
		TMemberPointAdjust  model = null;
		if (tMemberPointAdjust != null) {
			model = tMemberPointAdjust;
		} else {
			if (id != null) {
				model = super.findById(id);
			} else {
				model = newModel();
			}
		}
		// 从request中把数据populate到编辑对象中
		tMemberPointAdjust.setSetTime(new SimpleDateFormat("yyyy-MM-dd").parse(setTime));
		model.setSetTime(new SimpleDateFormat("yyyy-MM-dd").parse(setTime));
		if(model.getLevelPoint() != null){
			String ss1 = String.valueOf(model.getLevelPoint());
			BigDecimal d5 = new BigDecimal( ss1  );
			String s6 =d5.setScale(0,BigDecimal.ROUND_DOWN).toString();
			long a = Long.valueOf(s6);
			model.setLevelPoint(a);
			tMemberPointAdjust.setLevelPoint(a);
		}
		if(model.getActivityPoint() != null){
			String s1 = String.valueOf(model.getActivityPoint());
			BigDecimal dd5 = new BigDecimal( s1  );
			String ss6 =dd5.setScale(0,BigDecimal.ROUND_DOWN).toString();
			long aa = Long.valueOf(ss6);
			model.setActivityPoint(aa);
			tMemberPointAdjust.setActivityPoint(aa);
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
		tMemberPointAdjust = null;
		isEdit = false;
		
		return RETCODE_OK;
	}

	@Validates({
		@Validate(name = "cinemaInnerCode", id = "tne", error = "积分影城不能为空。"),
		@Validate(name = "adjReasonType", id = "tne", error = "调整类型不能为空。"),
		@Validate(name = "mobile", id = "regexp", args = { "(^[\\d]{0,0}$)|(^[1][3-8]+\\d{9})" }, error = "请输入正确的手机号."),
		@Validate(name = "mobile", id = "tne", error = "会员手机号不能为空。") })
	public ValidateResult validate(IModuleRequest req, Input input) {
		if (input.getName().equals("mobile")) {
			if ("mobile".equals(input.getName())) {
				TMember tmember = getService(TMemberService.class).queryMemByMobile(input.getValue().toString());
				if (tmember==null) {
					return new ValidateResult(
							ValidateResultType.FAILED_SKIP_PROPERTY,
							"此会员不存在");
				}else{
					memberId = tmember.getId();
					tMemberPointAdjust.setMemberId(memberId);
				}
			}
		}
		if (input.getName().equals("activityPoint")
				|| input.getName().equals("levelPoint")) {
			long activityPoint = 0l;
			long levelPoint =0l;
			if(tMemberPointAdjust.getActivityPoint() !=null){
				activityPoint = tMemberPointAdjust.getActivityPoint();
			}
			if(tMemberPointAdjust.getLevelPoint()!=null){
				levelPoint = tMemberPointAdjust.getLevelPoint();
			}
			if(memberId!=null&&memberId>0){
				if (getService(TPointHistoryService.class).checkPointBalance(activityPoint, levelPoint,memberId)) {
					return new ValidateResult(
							ValidateResultType.FAILED_SKIP_PROPERTY,
							"非定级积分、定级积分之与会员积分余额之和不能小于零");
				}
			}
		}
		String adjResion = "";
		if (input.getName().equals("adjResion")) {
			adjResion = input.getValue().toString().trim();
			if(adjResion.length()>200){
				return new ValidateResult(
						ValidateResultType.FAILED_SKIP_PROPERTY,
						"调整原因不能超过100个字");
			}
		}
		if (input.getName().equals("adjReasonType")) {
			String adjReasonType = input.getValue().toString();
			if(adjReasonType.length()<=0){
				return new ValidateResult(
						ValidateResultType.FAILED_SKIP_PROPERTY,
						"调整l类型不能为空");
			}
		}
		return ValidateResult.PASS;
	}
	/**
	 * 审批
	 */
	public int doApproves(IModuleRequest req, IModuleResponse resp) throws Exception {
		securityContext(req);
		id = req.getParameter("id", 0l);
		if (id != 0) {
			TMemberPointAdjust tpa = null;
			if (service != null) {
				 tpa =  service.findById(id);
			} else {
				 tpa = getService(TMemberPointAdjustService.class).findById(id);
			}
			if(tpa!=null){
				TPointHistory tph = new TPointHistory();
				tph.setActivityPoint(tpa.getActivityPoint());
				tph.setAdjReasonType(tpa.getAdjReasonType());
				tph.setAdjResion(tpa.getAdjResion());
				tph.setCinemaInnerCode(tpa.getCinemaInnerCode());
				tph.setCreatedBy(tpa.getCreatedBy());
				tph.setCreatedDate(tpa.getCreatedDate());
				tph.setUpdatedBy(tpa.getUpdatedBy());
				tph.setUpdatedDate(tpa.getUpdatedDate());
				tph.setLevelPoint(tpa.getLevelPoint());
				tph.setSetTime(tpa.getSetTime());
				tph.settMember(tpa.gettMember());
				tph.setMemberId(tpa.getMemberId());
				 getService(TPointHistoryService.class).createOrUpdate(tph);
				 tpa.setApprove("Y");
				 getService(TMemberPointAdjustService.class).createOrUpdate(tpa);
			}
		}
		
		id = null;
		ids = null;
		editing = false;
		return RETCODE_OK;
	}
	
	
	/**
	 * 审批未通过
	 */
	public int doUnApproves(IModuleRequest req, IModuleResponse resp) throws Exception {
		securityContext(req);
		id = req.getParameter("id", 0l);
		if (id != 0) {
			TMemberPointAdjust tpa = null;
			if (service != null) {
				 tpa =  service.findById(id);
			} else {
				 tpa = getService(TMemberPointAdjustService.class).findById(id);
			}
			if(tpa!=null){
				 tpa.setApprove("N");
				 getService(TMemberPointAdjustService.class).createOrUpdate(tpa);
			}
		}
		
		id = null;
		ids = null;
		editing = false;
		return RETCODE_OK;
	}
	
	public int cancelEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		id=null;
		isEdit = false;
		return 0;
	}
}
