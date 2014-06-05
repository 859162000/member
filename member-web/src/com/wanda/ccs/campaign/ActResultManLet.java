package com.wanda.ccs.campaign;

import java.util.List;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.aggrepoint.adk.form.Input;
import com.aggrepoint.adk.form.Validates;
import com.aggrepoint.adk.ui.ValidateResult;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.mem.service.TActResultService;
import com.wanda.ccs.mem.service.TCmnActivityService;
import com.wanda.ccs.model.IMemberDimType;
import com.wanda.ccs.model.IMemberRight;
import com.wanda.ccs.model.TActResult;
import com.wanda.ccs.model.TExtPointRule;
import com.wanda.ccs.model.TSegment;
import com.wanda.ccs.model.TVoucherPool;

public class ActResultManLet extends BaseCrudWinlet<TActResult> implements IMemberDimType, IMemberRight {
	private static final long serialVersionUID = -1223324229964591580L;
	
	//编辑的属性名称
	public String editPropertyName;
	public static String targetSegment = "activitySegment";
	public static String resultResSegment = "resultResSegment";
	public static String resultAlterSegment = "resultAlterSegment";
	
	public TActResult editResult;
	
	//活动波次ID
	private Long activityId;
	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		reset();
		this.activityId = activityId;
	}

	
	//选择特殊积分规则winlet
	private SelExtPointRuleLet selExtPointRuleLet;
	//选择券库winlet
	private SelVoucherPoolLet selVoucherPoolLet;
	//选择客群winlet
	private SelSegmentLet selSegmentLet;
	//获取选择积分规则Winlet
	public SelExtPointRuleLet getSelExtPointRuleLet(IModuleRequest req)
			throws Exception {
		if (selExtPointRuleLet == null)
			selExtPointRuleLet = (SelExtPointRuleLet) req
					.getWinlet(SelExtPointRuleLet.class.getName());
		return selExtPointRuleLet;
	}
	
	//获取选择券库Winlet
	public SelVoucherPoolLet getSelVoucherPoolLet(IModuleRequest req)
			throws Exception {
		if (selVoucherPoolLet == null)
			selVoucherPoolLet = (SelVoucherPoolLet) req
					.getWinlet(SelVoucherPoolLet.class.getName());
		return selVoucherPoolLet;
	}
	
	//获取选择券库Winlet
	public SelSegmentLet getSelSegmentLet(IModuleRequest req)
			throws Exception {
		if (selSegmentLet == null)
			selSegmentLet = (SelSegmentLet) req
					.getWinlet(SelSegmentLet.class.getName());
		return selSegmentLet;
	}


	/**
	 * 默认无参构造函数。
	 */
	public ActResultManLet() {

	}
	
	@Override
	public void reset(){
		activityId = null;
		editResult = null;
		id = null;
		editing = false;
	}

	@Override
	protected TActResultService getCrudService() {
		return getService(TActResultService.class);
	}

	

	@Override
	public int showEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if(activityId == null)
			return RETCODE_HIDE;
		if(editResult == null ||  activityId.longValue() != editResult.getCmnActivityId()){
			List<TActResult> list = getCrudService().getActResultsByActivityId(activityId);
			if(list != null && !list.isEmpty()){
				editResult = list.get(0);
			}
			if(editResult == null){
				editResult = new TActResult();
				editResult.setCmnActivityId(activityId);
				editResult.settCmnActivity(getService(TCmnActivityService.class).getById(activityId));
				editResult.setResConfigType(RES_CONFIG_TYPE_OTHER);
				editResult.setStatus(ACTIVITY_ACT_RESULT_EXEXUTE);
				editing = true;
			}else{
				editing = false;
			}
		}
		editResult.setEditing(editing);
		return RETCODE_OK;
	}
	
	@Override
	public int doEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if(editResult == null)
			return RETCODE_OK;
		editing = true;
		editResult.setStatus(ACTIVITY_ACT_RESULT_EXEXUTE);
		return RETCODE_OK;
	}
	
	public int resetCountResult(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if(editResult == null)
			return RETCODE_OK;
		editResult.setStatus(ACTIVITY_ACT_RESULT_EXEXUTE);
		getCrudService().createOrUpdate(editResult);
		cancelEdit(req, resp);
		return RETCODE_OK;
	}
	
	/**
	 * 选择客群
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int selSegment(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		editPropertyName = req.getParameter("propertyName", "");
		getSelSegmentLet(req).setInvoked(ActResultManLet.class.getName());
		return RETCODE_OK;
	}
	
	/**
	 * 选择特殊积分规则
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int selExtPointRule(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		getSelExtPointRuleLet(req).setInvoked(ActResultManLet.class.getName());
		return RETCODE_OK;
	}
	
	/**
	 * 选择券库
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int selVoucherPool(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		getSelVoucherPoolLet(req).setInvoked(ActResultManLet.class.getName());
		return RETCODE_OK;
	}
	
	/**
	 * 保存选择的客群
	 * @param id
	 */
	public void saveSegment(List<TSegment> list){
		if(list != null && !list.isEmpty()){
			TSegment segment = list.get(0);
			segment.getName();
			if (editPropertyName.equals(targetSegment)) {

			} else if (editPropertyName.equals(resultResSegment)) {
				if (editResult.getResSegment() == null
						|| editResult.getResSegmentId().longValue() != segment.getId()) {
					editResult.setResSegment(segment);
					editResult.setResSegmentId(segment.getId());
				}
			} else if (editPropertyName.equals(resultAlterSegment)) {
				if (editResult.getAlterSegment() == null
						|| editResult.getAlterSegmentId().longValue() != segment.getId()) {
					editResult.setAlterSegment(segment);
					editResult.setAlterSegmentId(segment.getId());
				}
			}
		}
	}
	
	/**
	 * 保存选择的券库
	 * @param id
	 */
	public void saveVoucherPool(List<TVoucherPool> list){
		if (list != null && !list.isEmpty()) {
			TVoucherPool voucherPool = list.get(0);
			if (editResult.gettVoucherPool() == null
					|| editResult.gettVoucherPool().getId().longValue() != voucherPool.getId()) {
				voucherPool.getName();
				editResult.settVoucherPool(voucherPool);
			}
		}
	}
	
	/**
	 * 保存选择的特殊积分规则
	 * @param id
	 */
	public void saveExtPointRule(List<TExtPointRule> list){
		if (list != null && !list.isEmpty()) {
			TExtPointRule extPointRule = list.get(0);
			if (editResult.gettExtPointRule() == null
					|| editResult.gettExtPointRule().getId().longValue() != extPointRule
							.getId()) {
				extPointRule.getName();
				editResult.settExtPointRule(extPointRule);
			}
		}
	}
	
	
	
	@Override
	public int saveEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if (req.isValidateField())
			return 2;
		if (req.getForm().hasError())
			return 3;
		if(editResult.getResConfigType().equals(RES_CONFIG_TYPE_OTHER) && editResult.getAlterSegment() == null && editResult.getResSegment() == null){
			req.setAttribute("MESSAGE", "请选择推荐响应或者关联响应客群");
			return 100;
		}
		// 初始化安全环境上下文。
		securityContext(req);
		getCrudService().createOrUpdate(editResult);
		
		cancelEdit(req, resp);
		return RETCODE_OK;
	}
	
	
	@Override
	public int cancelEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		reset();
		return super.cancelEdit(req, resp);
	}
	/**
	 * 编辑页面Ajax输入验证。
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	@Validates({})
	public ValidateResult validate(IModuleRequest req, Input input) {
		return ValidateResult.PASS;
	}

}
