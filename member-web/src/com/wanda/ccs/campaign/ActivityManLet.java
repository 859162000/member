package com.wanda.ccs.campaign;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.aggrepoint.adk.FileParameter;
import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.aggrepoint.adk.form.Form;
import com.aggrepoint.adk.form.Input;
import com.aggrepoint.adk.form.Validate;
import com.aggrepoint.adk.form.Validates;
import com.aggrepoint.adk.ui.ValidateResult;
import com.aggrepoint.adk.ui.ValidateResultType;
import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.jobhub.client.JobScheduleService;
import com.wanda.ccs.mem.service.TActTargetService;
import com.wanda.ccs.mem.service.TCmnActivityService;
import com.wanda.ccs.mem.service.TFileAttachService;
import com.wanda.ccs.mem.service.TSegmentService;
import com.wanda.ccs.model.IMemberDimType;
import com.wanda.ccs.model.IMemberRight;
import com.wanda.ccs.model.TActOffer;
import com.wanda.ccs.model.TActTarget;
import com.wanda.ccs.model.TCampaign;
import com.wanda.ccs.model.TCmnActivity;
import com.wanda.ccs.model.TCmnPhase;
import com.wanda.ccs.model.TExtPointRule;
import com.wanda.ccs.model.TFileAttach;
import com.wanda.ccs.model.TOffer;
import com.wanda.ccs.model.TSegment;
import com.wanda.ccs.model.TVoucherPool;
import com.xcesys.extras.core.util.DateUtil;
import com.xcesys.extras.core.util.StringUtil;

public class ActivityManLet extends BaseCrudWinlet<TCmnActivity> implements IMemberDimType, IMemberRight {
	private static final long serialVersionUID = 7118233401373137867L;
	
	private TCampaign editCampaign;
	private TCmnPhase editCmnPhase;
	
	private CampaignManLet campaignManLet;
	
	public boolean editTarget;
	
	public CampaignManLet getCampaignManLet(IModuleRequest req)
			throws Exception {
		if (campaignManLet == null)
			campaignManLet = (CampaignManLet) req
					.getWinlet(CampaignManLet.class.getName());
		return campaignManLet;
	}
	
	public ActivityManLet getEditActvityWinLet(IModuleRequest req)
			throws Exception {
		return getCampaignManLet(req).phaseManLet.activityManLet;
	}
	
	public TFileAttach getUploadFile(IModuleRequest req) throws Exception{
		return getCampaignManLet(req).phaseManLet.activityManLet.uploadFile;
	}
	public void setUploadFile(IModuleRequest req, TFileAttach file) throws Exception{
		getCampaignManLet(req).phaseManLet.activityManLet.uploadFile = file;
	}
	public void setId(Long id){
		editing = false;
		this.id = id;
	}
	public TCampaign getEditCampaign() {
		return editCampaign;
	}

	public void setEditCampaign(TCampaign editCampaign) {
		this.editCampaign = editCampaign;
	}

	public TCmnPhase getEditCmnPhase() {
		return editCmnPhase;
	}

	public void setEditCmnPhase(TCmnPhase editCmnPhase) {
		this.editCmnPhase = editCmnPhase;
	}

	//当期的编辑对象
	public TCmnActivity editCmnActivity;
	
	// 编辑的对象所对应的excel文件
	public TFileAttach uploadFile;
	
	//编辑的属性名称
	public String editPropertyName;
	public static String targetSegment = "activitySegment";
	public static String resultResSegment = "resultResSegment";
	public static String resultAlterSegment = "resultAlterSegment";
	
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
	
	//编辑活动响应统计Winlet
	private ActResultManLet actResultManLet;
	
	//获取编辑活动响应统计Winlet
	public ActResultManLet getActResultManLet(IModuleRequest req)
			throws Exception {
		if (actResultManLet == null)
			actResultManLet = (ActResultManLet) req
					.getWinlet(ActResultManLet.class.getName());
		return actResultManLet;
	}
	

	/**
	 * 默认无参构造函数。
	 */
	public ActivityManLet() {

	}

	@Override
	protected TCmnActivityService getCrudService() {
		return getService(TCmnActivityService.class);
	}

	@Override
	public int showList(IModuleRequest req, IModuleResponse resp) throws Exception{
		initSearch(req, resp);
		query.put("cmnPhaseId", editCmnPhase.getId());
		query.put("campaignId", editCampaign.getId());
		
		req.setAttribute("campaign", editCampaign);
		req.setAttribute(
				"phaseIsEnd",
				editCmnPhase.getStatus()
						.equals(CAMPAINGN_STATUS_FINISH)
						|| editCmnPhase.getEndDate().before(
								DateUtil.getCurrentDate()));
		return super.showList(req, resp);
	}
	
	/**
	 * 取消编辑波次目标
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int cancelTargetEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		setUploadFile(req, null);
		getEditActvityWinLet(req).cancelEdit(req, resp);
		return RETCODE_OK;
	}
	
	@Override
	public int cancelEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if(editTarget){
			super.cancelEdit(req, resp);
		}
		editing = false;
		editTarget = false;
		boolean isNew = false;
		if(editCmnActivity != null && editCmnActivity.getId() == null){
			isNew = true;
		}
		int ret = RETCODE_OK;
		editCmnActivity = null;
		if(isNew){
			super.cancelEdit(req, resp);
			getCampaignManLet(req).setType(CampaignManLet.editPhaseType);
			ret = 1;
		}
		return ret;
	}

	@Override
	public int doCreate(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		cancelEdit(req, resp);
		id = null;
		editCmnActivity = new TCmnActivity();
		editCmnActivity.settCampaign(editCampaign);
		editCmnActivity.setCampaignId(editCampaign.getId());
		editCmnActivity.settCmnPhase(editCmnPhase);
		editCmnActivity.setCmnPhaseId(editCmnPhase.getId());
		
		editCmnActivity.setStatus(CAMPAINGN_STATUS_PLAN);
		editCmnActivity.setPromotionType(ACTIVITY_PROMOTION_TYPE_OFFER);
		//通知话术
		TOffer offer = new TOffer();
		offer.setOfferChannel(OFFER_CHANNEL_SHORT_MESSAGE);
		editCmnActivity.settOffer(offer);
		
		TActOffer actOffer = new TActOffer();
		editCmnActivity.settActOffer(actOffer);
		
		getCampaignManLet(req).setType(CampaignManLet.editActivityType);
		return super.doCreate(req, resp);
	}
	
	/**
	 * 上传文件
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int doUpload(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if (!ServletFileUpload.isMultipartContent((HttpServletRequest) req
				.getRequestObject())) {
			throw new IllegalArgumentException(
					"Request is not multipart, please 'multipart/form-data' enctype for your form.");
		}

		FileParameter file = req.getFileParameter("file");
		JSONArray json = new JSONArray();
		
		if(file != null && !file.getFileName().endsWith(".xlsx")  && !file.getFileName().endsWith(".xls")){
			JSONObject jsono = new JSONObject();
			jsono.put("name", "注意：请上传2003版或2007版的Excel.此次操作无效");
			jsono.put("size", 0);
			jsono.put("error", true);
			json.add(jsono);
			if(getUploadFile(req) != null)
				getUploadFile(req).setDelete(true);
		}else if((file.getFileName().endsWith(".xlsx") && file.m_lSize > 150000L) || (file.getFileName().endsWith(".xls") && file.m_lSize > 1100000L)){
			JSONObject jsono = new JSONObject();
			jsono.put("name", "文件内容太大，目标会员文件大小为1万个手机号");
			jsono.put("size", 0);
			jsono.put("error", true);
			json.add(jsono);
			if(getUploadFile(req) != null)
				getUploadFile(req).setDelete(true);
		}else{
			if (getUploadFile(req) == null && file != null)
				setUploadFile(req,  new TFileAttach(TCmnActivity.class.getSimpleName(), "W"));
			getUploadFile(req).setRefObjectId(getEditActvityWinLet(req).editCmnActivity.getId());
			getUploadFile(req).setStatus("W");
			getUploadFile(req).setDelete(false);
			getUploadFile(req).copyFile(file);
			InputStream in = getUploadFile(req).getFileData().getBinaryStream();
			if(file.getFileName().endsWith(".xls")){
				HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(in));
				getEditActvityWinLet(req).editCmnActivity.gettActTarget().setTotalCount(Long.valueOf(wb.getSheetAt(0).getLastRowNum()));
			}else if(file.getFileName().endsWith(".xlsx")){
				XSSFWorkbook wb = new XSSFWorkbook(in);
				getEditActvityWinLet(req).editCmnActivity.gettActTarget().setTotalCount(Long.valueOf(wb.getSheetAt(0).getLastRowNum()));
			}
			
			JSONObject jsono = new JSONObject();
			jsono.put("name", file.getFileName());
			jsono.put("size", file.m_lSize);
			jsono.put("number", getEditActvityWinLet(req).editCmnActivity.gettActTarget().getTotalCount());
			jsono.put("error", false);
			json.add(jsono);
		}
		
		
		resp.setContentType("text/plain; charset=UTF-8");
		PrintWriter writer = resp.getWriter();
		writer.write(json.toString());
		writer.flush();
		return RETCODE_OK;
	}
	
	/**
	 * 下载文件
	 * @param req
	 * @param resp
	 * @return
	 */
	public int doDownload(IModuleRequest req, IModuleResponse resp) throws Exception{
		if(getUploadFile(req) != null){
			HttpServletResponse response = (HttpServletResponse) resp
					.getResponseObject();
			response.setContentType("application/x-download");
			response.setCharacterEncoding("UTF-8");
			try {
				response.addHeader("Content-Disposition", "attachment;filename="
						+ URLEncoder.encode(getUploadFile(req).getFileName(), "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			try {
				IOUtils.copy(getUploadFile(req).getFileData().getBinaryStream(), resp.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return RETCODE_OK;
	}
	
	/**
	 * 移除要上传的文件
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception 
	 */
	public int doUploadDel(IModuleRequest req, IModuleResponse resp) throws Exception {
		getUploadFile(req).setDelete(true);
		getEditActvityWinLet(req).editCmnActivity.gettActTarget().setTotalCount(0L);
		return RETCODE_OK;
	}
	/**
	 * 保存上传的文件
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception 
	 */
	public int doUploadFile(IModuleRequest req, IModuleResponse resp) throws Exception {
		// 初始化安全环境上下文。
		securityContext(req);
		if(getUploadFile(req) != null){
			getService(TFileAttachService.class).saveFileAttach(getUploadFile(req));
			getEditActvityWinLet(req).editCmnActivity.setFileAttach(getUploadFile(req));
			//导入波次目标，创建导入波次目标调度任务
			createImportExcelMemberJob(req, getUploadFile(req));
		}
		return RETCODE_OK;
	}
	
	/**
	 * 创建导入波次目标调度任务
	 * @param req
	 * @throws Exception
	 */
	public void createImportExcelMemberJob(IModuleRequest req, TFileAttach file) throws Exception{
		
		CcsUserProfile user = (CcsUserProfile) req.getUserProfile();
		Map<String, String> params = new HashMap<String, String>();
		params.put("fileAttachId", Long.toString(file.getId()));
		params.put("userId", user.getId());
		
		getService(JobScheduleService.class).scheduleJob("ImportExcelMemberJob", "member-jobs", params);
		
		//调度任务创建成功后,把文件状态改为正在处理
		file.setStatus("P");
		getService(TFileAttachService.class).saveFileAttach(getUploadFile(req));
	}
	@Override
	public int doEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		getCampaignManLet(req).setType(CampaignManLet.editActivityType);
		if(!StringUtil.isNullOrBlank(req.getParameter("id", null))){
			cancelEdit(req, resp);
			id = req.getParameter("id", 0L);
		}
		editing = true;
		getCampaignManLet(req).setEditId(id);
		TCmnActivity activity = getCrudService().getById(id);
		getCampaignManLet(req).setOpenId(activity.getCmnPhaseId());
		return RETCODE_OK;
	}
	
	@Override
	public int doView(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		cancelEdit(req, resp);
		getCampaignManLet(req).setType(CampaignManLet.editActivityType);
		super.doView(req, resp);
		getCampaignManLet(req).setEditId(id);
		TCmnActivity activity = getCrudService().getById(id);
		getCampaignManLet(req).setOpenId(activity.getCmnPhaseId());
		return RETCODE_OK;
	}
	
	/**
	 * 停用波次
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int disabledActivity(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		cancelEdit(req, resp);
		TCmnActivity cmnActivity = getCrudService().getById(req.getParameter("id", 0L));
		if(cmnActivity != null){
			cmnActivity.setStatus(CAMPAINGN_STATUS_INACTIVE);
			getCrudService().createOrUpdate(cmnActivity);
		}
		return RETCODE_OK;
	}
	
	/**
	 * 启用波次
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int enabledActivity(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		cancelEdit(req, resp);
		TCmnActivity cmnActivity = getCrudService().getById(req.getParameter("id", 0L));
		if(cmnActivity != null){
			//启用时，当波次开始时间在当前时间之前，则把波次状态置为执行状态，反之，置为发布状态
			if(cmnActivity.getStartDtime().before(DateUtil.getCurrentDate()))
				cmnActivity.setStatus(CAMPAINGN_STATUS_EXECUTE);
			else
				cmnActivity.setStatus(CAMPAINGN_STATUS_PUBLISH);
			getCrudService().createOrUpdate(cmnActivity);
		}
		return RETCODE_OK;
	}



	@Override
	public int showEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if((id==null && editCmnActivity == null) || editTarget)
			return RETCODE_HIDE;
		if(editCmnActivity == null || (id != null && (editCmnActivity.getId() == null || editCmnActivity.getId() != id.longValue())))
			editCmnActivity = getCrudService().getById(id);
		if(editCmnActivity == null){
			cancelEdit(req, resp);
			return RETCODE_HIDE;
		}
		
		
		if(editCmnActivity.getId() != null && editCmnActivity.getEndDtime() != null){
			editCmnActivity.setCanEnabled(editCmnActivity.getEndDtime().after(DateUtil.getCurrentDate()));
		}
		editCmnActivity.setEditing(editing);
		
		if(editCmnActivity.gettCmnPhase().getStartDate().after(DateUtil.getCurrentDate())){
			req.setAttribute("startDate", new SimpleDateFormat("yyyy-MM-dd").format(editCmnActivity.gettCmnPhase().getStartDate()));
		}else{
			req.setAttribute("startDate", new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.getCurrentDate()));
		}
		req.setAttribute("endDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(editCmnActivity.gettCmnPhase().getEndDate()));
		if(editCampaign == null || editCampaign.getId().longValue() != editCmnActivity.gettCampaign().getId())
			editCampaign = editCmnActivity.gettCampaign();
		req.setAttribute("campaign", editCampaign);
		
		if(editCmnPhase == null || editCmnPhase.getId().longValue() != editCmnActivity.gettCmnPhase().getId())
			editCmnPhase = editCmnActivity.gettCmnPhase();
		req.setAttribute(
				"phaseIsEnd",
				editCmnPhase.getStatus()
						.equals(CAMPAINGN_STATUS_FINISH)
						|| editCmnPhase.getEndDate().before(
								DateUtil.getCurrentDate()));
		
		if(editCmnActivity.gettActOffer() == null){
			TActOffer actOffer = new TActOffer();
			editCmnActivity.settActOffer(actOffer);
		}
		editCmnActivity.gettOffer().getToldTime();
		
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
		getEditActvityWinLet(req).editPropertyName = req.getParameter("propertyName", "");
		getSelSegmentLet(req).setInvoked(ActivityManLet.class.getName());
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
		getSelExtPointRuleLet(req).setInvoked(ActivityManLet.class.getName());
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
		getSelVoucherPoolLet(req).setInvoked(ActivityManLet.class.getName());
		return RETCODE_OK;
	}
	
	/**
	 * 保存选择的客群
	 * @param id
	 */
	public void saveSegment(List<TSegment> list, IModuleRequest req) throws Exception{
		if(list != null && !list.isEmpty()){
			TSegment segment = list.get(0);
			segment.getName();
			if(editPropertyName.equals(targetSegment)){
				getEditActvityWinLet(req).editCmnActivity.gettActTarget().settSegment(segment);
			}
		}
	}
	
	/**
	 * 保存选择的券库
	 * @param id
	 */
	public void saveVoucherPool(List<TVoucherPool> list){
		if(list != null && !list.isEmpty()){
			TVoucherPool voucherPool = list.get(0);
			if (editCmnActivity.gettActOffer().gettVoucherPool() == null
					|| editCmnActivity.gettActOffer().gettVoucherPool()
							.getId().longValue() != voucherPool.getId()) {
				voucherPool.getName();
				editCmnActivity.gettActOffer().settVoucherPool(voucherPool);
			}
		}
	}
	
	/**
	 * 保存选择的特殊积分规则
	 * @param id
	 * @throws Exception 
	 */
	public void saveExtPointRule(List<TExtPointRule> list, IModuleRequest req) throws Exception{
		if(list != null && !list.isEmpty()){
			TExtPointRule extPointRule = list.get(0);
			if (extPointRule != null) {
				extPointRule.getName();
				getEditActvityWinLet(req).editCmnActivity.gettActOffer().settExtPointRule(extPointRule);
			}
		}
	}
	
	
	/**
	 * 显示波次目标编辑页面
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int showTargetEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if (getEditActvityWinLet(req).id == null
				&& getEditActvityWinLet(req).editCmnActivity == null
				&& !getEditActvityWinLet(req).editTarget)
			return RETCODE_HIDE;
		if (getEditActvityWinLet(req).editCmnActivity == null
				|| (getEditActvityWinLet(req).id != null && (getEditActvityWinLet(req).editCmnActivity
						.getId() == null || getEditActvityWinLet(req).editCmnActivity
						.getId() != getEditActvityWinLet(req).id.longValue())))
			getEditActvityWinLet(req).editCmnActivity = getCrudService().getById(getEditActvityWinLet(req).id);
		if(getEditActvityWinLet(req).editCmnActivity == null){
			getEditActvityWinLet(req).cancelEdit(req, resp);
			return RETCODE_HIDE;
		}
		TActTarget actTarget = getEditActvityWinLet(req).editCmnActivity.gettActTarget();
		if(actTarget == null){
			actTarget = new TActTarget();
			actTarget.setTargetType(TARGET_TYPE_SEGMENT);
			actTarget.setStatus(ACTIVITY_ACT_TARGET_PLAN);
			getEditActvityWinLet(req).editCmnActivity.settActTarget(actTarget);
		}else if(StringUtil.isNullOrBlank(actTarget.getStatus())){
			actTarget.setStatus(ACTIVITY_ACT_TARGET_PLAN);
		}
		if(getUploadFile(req) == null){
			TFileAttach file = getService(TFileAttachService.class).getFileAttach(
					getEditActvityWinLet(req).editCmnActivity.getId(), TCmnActivity.class.getSimpleName());
			setUploadFile(req,  file);
			if(file != null && file.getStatus().equals("S") && actTarget.getTargetType().equals(TARGET_TYPE_EXCEL)){
				Long totalCount = getService(TFileAttachService.class).getMemberCountByFileId(file.getId());
				actTarget.setTotalCount(totalCount);
			}
		}
		req.setAttribute("actTarget", actTarget);	
		req.setAttribute("uploadFile", getUploadFile(req));
		return RETCODE_OK;
	}
	/**
	 * 编辑波次目标
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int editTarget(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		cancelTargetEdit(req, resp);
		getEditActvityWinLet(req).id = req.getParameter("id", 0L);
		getEditActvityWinLet(req).editTarget = true;
		return RETCODE_OK;
	}
	
	
	/**
	 * 编辑响应统计方式
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int editResult(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		getEditActvityWinLet(req).cancelEdit(req, resp);
		Long activityId = req.getParameter("id", 0L);
		getActResultManLet(req).setActivityId(activityId);
		return RETCODE_OK;
	}
	
	
	
	/**
	 * 重新计算客群
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int resetSegmentCount(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		
		TSegment segment = getEditActvityWinLet(req).editCmnActivity.gettActTarget().gettSegment();
		segment.setCalCount(-1L);
		getService(TSegmentService.class).createOrUpdate(segment);
		
//		getEditActvityWinLet(req).editCmnActivity.gettActTarget().setTotalCount(-1L);
//		
//		getService(TActTargetService.class).createOrUpdate(editCmnActivity.gettActTarget());
		
		//重新计算客群，创建计算客群数量调度任务
		CcsUserProfile user = (CcsUserProfile) req.getUserProfile();
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", segment.getId().toString());

		getService(JobScheduleService.class).scheduleJob("SegmentCalculateJob", "member-jobs", params);
		
		return RETCODE_OK;
	}
	
	/**
	 * 刷新客群
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int refreshSegment(IModuleRequest req, IModuleResponse resp) throws Exception{
		//获取客群中的实际数量
		if(getEditActvityWinLet(req).editCmnActivity.gettActTarget() != null && getEditActvityWinLet(req).editCmnActivity.gettActTarget().gettSegment() != null){
			getService(TSegmentService.class).setQueryCacheable(false);
			TSegment segment = getService(TSegmentService.class).getById(getEditActvityWinLet(req).editCmnActivity.gettActTarget().gettSegment().getId());
			getEditActvityWinLet(req).editCmnActivity.gettActTarget().settSegment(segment);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("calCount", segment.getCalCount());
			map.put("calCountTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(segment.getCalCountTime()));
			writeJSON(resp, map);
		}
		
		return RETCODE_OK;
	}
	
	/**
	 * 获取上传文件状态
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int getUploadFileStatus(IModuleRequest req, IModuleResponse resp) throws Exception{
		if(getUploadFile(req) != null){
			getService(TFileAttachService.class).setQueryCacheable(false);
			TFileAttach file = getService(TFileAttachService.class).getById(getUploadFile(req).getId());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", file.getStatus());
			if(file.getStatus().equals("S")){
				map.put("calCount", getService(TFileAttachService.class).getMemberCountByFileId(file.getId()));
			}
			getEditActvityWinLet(req).editCmnActivity.setFileAttach(file);
			setUploadFile(req, file);
			writeJSON(resp, map);
		}
		return RETCODE_OK;
	}
	
	/**
	 * 冻结客群
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int lockedSegment(IModuleRequest req, IModuleResponse resp) throws Exception{
		Long maxCount = req.getParameter("maxCount", 0L);
		Long controlCount = req.getParameter("controlCount", 0L);
		getEditActvityWinLet(req).editCmnActivity.gettActTarget().setMaxCount(maxCount);
		getEditActvityWinLet(req).editCmnActivity.gettActTarget().setControlCount(controlCount);
		getEditActvityWinLet(req).editCmnActivity.gettActTarget().setStatus(ACTIVITY_ACT_TARGET_LOCKED);
		
		String targetType = getEditActvityWinLet(req).editCmnActivity.gettActTarget().getTargetType();
		if(targetType.equals(TARGET_TYPE_SEGMENT)){
			//冻结客群时若波次目标类型为客群,则保存客群数量到波次目标中
			TSegment segment = getEditActvityWinLet(req).editCmnActivity.gettActTarget().gettSegment();
			getEditActvityWinLet(req).editCmnActivity.gettActTarget().setTotalCount(segment.getCalCount());
		}
		//冻结客群时,首先保存波次目标,然后创建调度
		getCrudService().createOrUpdate(getEditActvityWinLet(req).editCmnActivity);
		
		//创建冻结客群时，生成联络清单调度任务
		CcsUserProfile user = (CcsUserProfile) req.getUserProfile();
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", getEditActvityWinLet(req).editCmnActivity.gettActTarget().getId().toString());
		params.put("userId", user.getId());
		
		getService(JobScheduleService.class).scheduleJob("CreateContactHistoryJob", "member-jobs", params);

		return RETCODE_OK;
	}
	/**
	 * 解冻客群
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int unlockedSegment(IModuleRequest req, IModuleResponse resp) throws Exception{
		getEditActvityWinLet(req).editCmnActivity.gettActTarget().setStatus(ACTIVITY_ACT_TARGET_PLAN);
		getService(TActTargetService.class).createOrUpdate(getEditActvityWinLet(req).editCmnActivity.gettActTarget());
		return RETCODE_OK;
	}
	
	/**
	 * 设置波次目标类型
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int setTargetType(IModuleRequest req, IModuleResponse resp) throws Exception{
		getEditActvityWinLet(req).editCmnActivity.gettActTarget().setTargetType(req.getParameter("targetType", TARGET_TYPE_SEGMENT));
		return RETCODE_OK;
	}
	
	/**
	 * 保存波次目标
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int saveTargetEdit(IModuleRequest req, IModuleResponse resp) throws Exception{
		if (req.isValidateField())
			return 2;
		if (req.getForm().hasError())
			return 3;
		String targetType = getEditActvityWinLet(req).editCmnActivity.gettActTarget().getTargetType();
		if(targetType.equals(TARGET_TYPE_SEGMENT) && getEditActvityWinLet(req).editCmnActivity.gettActTarget().gettSegment() == null){
			req.setAttribute("MESSAGE", "请选择客群");
			return 100;
		}else if(targetType.equals(TARGET_TYPE_EXCEL) && (getUploadFile(req) == null || getUploadFile(req).isDelete())){
			req.setAttribute("MESSAGE", "请选择导入目标文件");
			return 100;
		}
		if(getEditActvityWinLet(req).editCmnActivity.gettActTarget().getStatus().equals(ACTIVITY_ACT_TARGET_PLAN)){
			getEditActvityWinLet(req).editCmnActivity.gettActTarget().setMaxCount(req.getParameter("maxCount", 0L));
			getEditActvityWinLet(req).editCmnActivity.gettActTarget().setControlCount(req.getParameter("controlCount", 0L));
		}
		if(targetType.equals(TARGET_TYPE_SEGMENT)){
			getEditActvityWinLet(req).editCmnActivity.setFileAttach(null);
		}else if(targetType.equals(TARGET_TYPE_EXCEL)){
			getEditActvityWinLet(req).editCmnActivity.gettActTarget().settSegment(null);
			getEditActvityWinLet(req).editCmnActivity.setFileAttach(getUploadFile(req));
		}
		// 初始化安全环境上下文。
		securityContext(req);
		getCrudService().createOrUpdate(getEditActvityWinLet(req).editCmnActivity);
		if(getUploadFile(req)!=null){
			if("W".equals(getUploadFile(req).getStatus())){
				createImportExcelMemberJob(req, getUploadFile(req));
			}
		}
		cancelTargetEdit(req, resp);
		return RETCODE_OK;
	}
	@Override
	public int saveEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if (req.isValidateField())
			return 2;
		if (req.getForm().hasError())
			return 3;
		if(!StringUtil.isNullOrBlank(editCmnActivity.gettActOffer().getOfferType())){
			if (editCmnActivity.gettActOffer().getOfferType()
					.equals(ACT_OFFER_TYPE_INTEGRATE)) {
				TExtPointRule extPointRule = editCmnActivity.gettActOffer().gettExtPointRule();
				if (extPointRule == null) {
					req.setAttribute("MESSAGE", "请选择特殊积分规则");
					return 100;
				} else if (editCmnActivity.getStartDtime().before(
						extPointRule.getStartDtime())
						|| editCmnActivity.getEndDtime().after(
								extPointRule.getEndDtime())) {
					req.setAttribute("MESSAGE", "波次的起始时间应该在特殊积分规则的起始时间之内,请修改特殊积分规则或波次的起始时间");
					return 200;
				}
			} else if (editCmnActivity.gettActOffer().getOfferType()
					.equals(ACT_OFFER_TYPE_VOUCHER)
					&& editCmnActivity.gettActOffer().gettVoucherPool() == null) {
				req.setAttribute("MESSAGE", "请选择券库");
				return 100;
			}
		}
		String status = req.getParameter("status","");
		if (status.equals(CAMPAINGN_STATUS_PLAN) || status.equals(CAMPAINGN_STATUS_PUBLISH)) {
			if (!StringUtil.isNullOrBlank(editCmnActivity.gettActOffer().getOfferType())) {
				if (editCmnActivity.gettActOffer().getOfferType()
						.equals(ACT_OFFER_TYPE_INTEGRATE)) {
					editCmnActivity.gettActOffer().settVoucherPool(null);
				} else if (editCmnActivity.gettActOffer().getOfferType()
						.equals(ACT_OFFER_TYPE_VOUCHER)) {
					editCmnActivity.gettActOffer().settExtPointRule(null);
				}
			}
		}
		if(status.equals(CAMPAINGN_STATUS_PUBLISH) && editCmnActivity.gettOffer().getToldTime().before(DateUtil.getCurrentDate())){
			req.setAttribute("MESSAGE", "短信通知时间应该在波次发布时间之前,请修改通知时间后再重新发布");
			return 300;
		}
		editCmnActivity.setStatus(status);
		// 初始化安全环境上下文。
		securityContext(req);
		getCrudService().createOrUpdate(editCmnActivity);
		
		id = editCmnActivity.getId();
		cancelEdit(req, resp);
		return RETCODE_OK;
	}
	
	/**
	 * 发布波次
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int publishActivity(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		id = req.getParameter("id", 0L);
		int ret = RETCODE_OK;
		TCmnActivity activity = getCrudService().getById(id);
		if (activity.getStatus().equals(CAMPAINGN_STATUS_PLAN)
				&& activity.gettActTarget() != null
				&& activity.gettActTarget().getStatus()
						.equals(ACTIVITY_ACT_TARGET_LOCKED)) {
			if(activity.gettOffer().getToldTime().before(DateUtil.getCurrentDate())){
				req.setAttribute("MESSAGE", "短信通知时间应该在波次发布时间之前,请修改通知时间后再重新发布");
				return 200;
			}
			activity.setStatus(CAMPAINGN_STATUS_PUBLISH);
			getCrudService().createOrUpdate(activity);
		}else{
			req.setAttribute("MESSAGE", "该波次不是计划状态或者目标没有冻结,不能发布");
			ret = 100;
		}
		cancelEdit(req, resp);
		return ret;
	}
	
	/**
	 * 取消发布波次
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int cancelPublishActivity(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		id = req.getParameter("id", 0L);
		int ret = RETCODE_OK;
		TCmnActivity activity = getCrudService().getById(id);
		if (activity.getStatus().equals(CAMPAINGN_STATUS_PUBLISH)) {
			activity.setStatus(CAMPAINGN_STATUS_PLAN);
			getCrudService().createOrUpdate(activity);
		}else{
			req.setAttribute("MESSAGE", "该波次不为发布状态,不能取消发布,请检查状态");
			ret = 100;
		}
		cancelEdit(req, resp);
		return ret;
	}
	
	/**
	 * 导出波次模板选择模板
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int exptExcl(IModuleRequest req, IModuleResponse resp) throws Exception{
		HttpServletResponse httprep = (HttpServletResponse) resp
				.getResponseObject();
		httprep.setContentType("application/vnd.ms-excel;charset=UTF-8");
		httprep.addHeader(
				"Content-Disposition",
				"attachment;filename="
						+ URLEncoder.encode("波次目标选择模板"+ ".xlsx", "UTF-8"));
		InputStream tmpl = ActivityManLet.class
				.getResourceAsStream("/tmpl/target.xlsx");
		byte[] bytes = new byte[1024];
		BufferedInputStream bis = new BufferedInputStream(tmpl,1024);
		while(bis.read(bytes) != -1){
			httprep.getOutputStream().write(bytes);
		}
		httprep.getOutputStream().flush();
		httprep.getOutputStream().close();
		return RETCODE_OK;
	}
	
	/**
	 * 发送短息
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int sendMessage(IModuleRequest req, IModuleResponse resp) throws Exception{
		String mobileNo = req.getParameter("mobileNo", "");
		
		if(StringUtil.isNullOrBlank(mobileNo)){
			return 100;
		}
		Map<String, ?> memberMap = getCrudService().getMemberByPhone(mobileNo);
		if(memberMap == null || memberMap.isEmpty()){
			req.setAttribute("MESSAGE", "该手机为非会员,请重新输入");
			return 100;
		}
		
		String msgContent = req.getParameter("msgContent", "");
		
		msgContent = replaceOffer(msgContent, memberMap, mobileNo);
		Map<String, String> map = getCrudService().getMemberConfig();
		if(map == null || map.isEmpty()){
			req.setAttribute("MESSAGE", "发送短息配置错误,请联系院线IT");
			return 200;
		}
		String msgSvcIp = map.get("MSG_MQ_IP") == null ? "" : map.get("MSG_MQ_IP").toString();
		String msgChannelId = map.get("MSG_CHANNEL_ID") == null ? "" : map.get("MSG_CHANNEL_ID").toString();
		if(StringUtil.isNullOrBlank(msgSvcIp) || StringUtil.isNullOrBlank(msgChannelId)){
			req.setAttribute("MESSAGE", "发送短息配置错误,请联系院线IT");
			return 200;
		}
		String systemId = memberMap.get("INNER_CODE") == null
				|| StringUtil.isNullOrBlank(memberMap.get("INNER_CODE")
						.toString()) ? "001" : memberMap.get("INNER_CODE")
				.toString();
//		systemId = "001";
		SendTestMsg.sendMsgCheckCode(msgSvcIp, msgChannelId, mobileNo, systemId, msgContent);
		return RETCODE_OK;
	}
	
	public String replaceOffer(String content, Map<String, ?> memberMap, String mobileNo){
		content = content.replace("${name}", memberMap.get("NAME") == null?"":memberMap.get("NAME").toString()).
		replace("${birthday}", memberMap.get("BIRTHDAY") == null?"":memberMap.get("BIRTHDAY").toString()).
		replace("${regdate}", memberMap.get("REGIST_DATE") == null?"":memberMap.get("REGIST_DATE").toString()).
		replace("${expirepo}", memberMap.get("EXG_EXPIRE_POINT_BALANCE") == null?"":memberMap.get("EXG_EXPIRE_POINT_BALANCE").toString()).
		replace("${curpoint}", memberMap.get("EXG_POINT_BALANCE") == null?"":memberMap.get("EXG_POINT_BALANCE").toString());
		
		if(memberMap.get("MEM_LEVEL") == null || memberMap.get("MEM_LEVEL").toString().equals("") || memberMap.get("MEM_LEVEL").toString().equals("1")){
			content = content.replace("${level}", "一星");
		}else if(memberMap.get("MEM_LEVEL").toString().equals("2")){
			content = content.replace("${level}", "二星");
		}else if(memberMap.get("MEM_LEVEL").toString().equals("3")){
			content = content.replace("${level}", "三星");
		}else if(memberMap.get("MEM_LEVEL").toString().equals("4")){
			content = content.replace("${level}", "四星");
		}
		
		if(memberMap.get("TARGET_LEVEL") == null || memberMap.get("TARGET_LEVEL").toString().equals("") || memberMap.get("TARGET_LEVEL").toString().equals("1")){
			content = content.replace("${tgtlevel}", "一星");
		}else if(memberMap.get("TARGET_LEVEL").toString().equals("2")){
			content = content.replace("${tgtlevel}", "二星");
		}else if(memberMap.get("TARGET_LEVEL").toString().equals("3")){
			content = content.replace("${tgtlevel}", "三星");
		}else if(memberMap.get("TARGET_LEVEL").toString().equals("4")){
			content = content.replace("${tgtlevel}", "四星");
		}
		
		if(memberMap.get("GENDER") == null || memberMap.get("GENDER").toString().equals("") || memberMap.get("GENDER").toString().equals("O")){
			content = content.replace("${gender}", "未指定");
		}else if(memberMap.get("GENDER").toString().equals("M")){
			content = content.replace("${gender}", "男");
		}else if(memberMap.get("GENDER").toString().equals("F")){
			content = content.replace("${gender}", "女");
		}
		
		if(!StringUtil.isNullOrBlank(mobileNo)){
			if(mobileNo.length() >= 4){
				content = content.replace("${mobile4}", mobileNo.substring(mobileNo.length() - 4));
			}
		}
		return content;
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
			@Validate(name = "content", id = "maxlen", args = { "1000" }, error = "话术内容长度不能超过1000个字符"),
			@Validate(name = "priority", id = "tne", error = "优先级不能为空。"),
			@Validate(name = "startDtime", id = "tne", error = "开始时间不能为空。"),
			@Validate(name = "endDtime", id = "tne", error = "结束时间不能为空。"),
			@Validate(name = "startTime", id = "tne", error = "通知开始时间不能为空。"),
			@Validate(name = "endTime", id = "tne", error = "通知结束时间不能为空。"),
			@Validate(name = "toldTime", id = "tne", error = "通知时间不能为空。"),
			@Validate(name = "maxCount", id = "tne", error = "受众数量不能为空。"),
			@Validate(name = "controlCount", id = "tne", error = "控制组数量不能为空。"),
			@Validate(name = "content", id = "tne", error = "话术内容不能为空。")})
	public ValidateResult validate(IModuleRequest req, Input input) {
		Form form = req.getForm();
		if(input.getName().equals("name")){
			if(getCrudService().checkActivityName(input.getValue().toString(), editCmnActivity.getId()))
				return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "波次名已存在，请重新输入");
		}else if(input.getName().equals("startDtime")){
			String startDtime = input.getValue() ==null ? "" : input.getValue().toString();
			String endDtime = form.getInputByName("endDtime") == null
					|| form.getInputByName("endDtime").getValue() == null ? ""
					: form.getInputByName("endDtime").getValue().toString();
			String toldTime = form.getInputByName("toldTime") == null
					|| form.getInputByName("toldTime").getValue() == null ? ""
					: form.getInputByName("toldTime").getValue().toString();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(!StringUtil.isNullOrBlank(startDtime)&& !StringUtil.isNullOrBlank(endDtime)){
				try {
					if(format.parse(startDtime).after(format.parse(endDtime))){
						return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "开始时间不能大于结束时间");
					}else if(startDtime.equals(endDtime)){
						return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "开始时间不能等于结束时间");
					}else{
						form.setDisabled("endDtime", true);
						form.setDisabled("endDtime", false);
					}
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if(!StringUtil.isNullOrBlank(startDtime)&& !StringUtil.isNullOrBlank(toldTime)){
				try {
					
					if(format.parse(startDtime).after(format.parse(toldTime))){
						form.setDisabled("toldTime", true);
						form.setDisabled("toldTime", false);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if(!StringUtil.isNullOrBlank(startDtime)){
				try {
					if(format.parse(startDtime).before(editCmnActivity.gettCmnPhase().getStartDate())){
						return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "开始时间不能小于阶段的开始时间");
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}else if(input.getName().equals("endDtime")){
			String startDtime = form.getInputByName("startDtime") == null
					|| form.getInputByName("startDtime").getValue() == null ? ""
					: form.getInputByName("startDtime").getValue().toString();
			String endDtime = input.getValue() == null ? "" : input.getValue().toString();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(!StringUtil.isNullOrBlank(startDtime)&& !StringUtil.isNullOrBlank(endDtime)){
				try {
					if(format.parse(startDtime).after(format.parse(endDtime))){
						return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "结束时间不能小于开始时间");
					}else if(startDtime.equals(endDtime)){
						return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "结束时间不能等于开始时间");
					}else{
						form.setDisabled("startDtime", true);
						form.setDisabled("startDtime", false);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if(!StringUtil.isNullOrBlank(endDtime)){
				try {
					if(format.parse(endDtime).after(editCmnActivity.gettCmnPhase().getEndDate())){
						return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "结束时间不能大于阶段的结束时间");
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}else if(input.getName().equals("toldTime")){
			String toldTime = input.getValue() == null ? "" : input.getValue().toString();
			String startDtime = form.getInputByName("startDtime") == null
					|| form.getInputByName("startDtime").getValue() == null ? ""
					: form.getInputByName("startDtime").getValue().toString();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(!StringUtil.isNullOrBlank(toldTime) && !StringUtil.isNullOrBlank(startDtime)){
				try {
					if(format.parse(toldTime).after(format.parse(startDtime))){
						return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "通知时间不能大于活动的开始时间");
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else if(input.getName().equals("maxCount")){
			Long maxCount = 0L;
			if(input.getValue() != null && !input.getValue().toString().equals(""))
				maxCount = Long.valueOf(input.getValue().toString());
			if (maxCount <= 0L){
				return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "受众数量应该大于0");
			}
		}else if(input.getName().equals("controlCount")){
			Long controlCount = 0L;
			if(input.getValue() != null && !input.getValue().toString().equals(""))
				controlCount = Long.valueOf(input.getValue().toString());
			if (controlCount < 0L){
				return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "控制组数量不能小于0");
			}
		}
//		else if(input.getName().equals("maxCount")){
//			Long maxCount = 0L;
//			if(input.getValue() != null && !input.getValue().toString().equals(""))
//				maxCount = Long.valueOf(input.getValue().toString());
//			if(editCmnActivity.gettActTarget().getTotalCount() != null && maxCount.longValue() > editCmnActivity.gettActTarget().getTotalCount())
//				return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "受众数量应该小于等于总数量");
//			Long controlCount = 0L;
//			if(form.getInputByName("controlCount").getValue() != null && !form.getInputByName("controlCount").getValue().toString().equals(""))
//				controlCount = Long.valueOf(form.getInputByName("controlCount").getValue().toString());
//			if(maxCount <= controlCount)
//				return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "受众数量应该大于控制组数量");
//			else{
//				form.setDisabled("controlCount", true);
//				form.setDisabled("controlCount", false);
//			}
//		}else if(input.getName().equals("controlCount")){
//			Long controlCount = 0L;
//			if(input.getValue() != null && !input.getValue().toString().equals(""))
//				controlCount = Long.valueOf(input.getValue().toString());
//			Long maxCount = 0L;
//			if(form.getInputByName("maxCount").getValue() != null && !form.getInputByName("maxCount").getValue().toString().equals(""))
//				maxCount = Long.valueOf(form.getInputByName("maxCount").getValue().toString());
//			if(controlCount >= maxCount)
//				return new ValidateResult(ValidateResultType.FAILED_SKIP_PROPERTY, "控制组数量应该小于受众数量");
//			else{
//				form.setDisabled("maxCount", true);
//				form.setDisabled("maxCount", false);
//			}
//		}
		return ValidateResult.PASS;
	}

}
