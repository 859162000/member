package com.wanda.ccs.member;


import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.aggrepoint.adk.FileParameter;
import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.aggrepoint.adk.form.Input;
import com.aggrepoint.adk.form.Validate;
import com.aggrepoint.adk.form.Validates;
import com.aggrepoint.adk.ui.ValidateResult;
import com.aggrepoint.adk.ui.ValidateResultType;
import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.campaign.SelSegmentLet;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.mem.service.TFileAttachService;
import com.wanda.ccs.mem.service.TMackDaddyCardOrderService;
import com.wanda.ccs.mem.service.TMemberVoucherPoolService;
import com.wanda.ccs.mem.service.TMemberVoucherRuleService;
import com.wanda.ccs.model.IMemberDimType;
import com.wanda.ccs.model.TFileAttach;
import com.wanda.ccs.model.TMemVoucherRule;
import com.wanda.ccs.model.TSegment;
import com.wanda.ccs.model.TVoucherPool;
import com.xcesys.extras.core.service.ICrudService;
import com.xcesys.extras.util.SpringContextUtil;

/**
 * 券发放规则管理
 * @author admin
 *
 */
public class TMemVoucherRuleLet extends BaseCrudWinlet<TMemVoucherRule> implements IMemberDimType{

	private static final long serialVersionUID = -5884972360889290831L;
	
	private TMemberVoucherRuleService service;
	
	public TMemVoucherRule rule;
	
	public boolean saveOrUpdate;
	
	private SelSegmentLet selSegmentLet;
	
	private SelectVoucherPoolLet selectVoucherPoolLet;
	
	//上传的文件
	public TFileAttach uploadFile;
	
	public Long getId() {
		return id;
	}
	
	public TMemVoucherRuleLet() {
		
	}
	
	//获取选择客群Winlet
	public SelSegmentLet getSelSegmentLet(IModuleRequest req)throws Exception {
		if(selSegmentLet == null) {
			selSegmentLet = (SelSegmentLet) req.getWinlet(SelSegmentLet.class.getName());
		}
		return selSegmentLet;
	}

	//获取选择券库Winlet
	public SelectVoucherPoolLet getSelectVoucherPoolLet(IModuleRequest req)throws Exception {
		if(selectVoucherPoolLet == null) {
			selectVoucherPoolLet = (SelectVoucherPoolLet) req.getWinlet(SelectVoucherPoolLet.class.getName());
		}
		return selectVoucherPoolLet;
	}
	
	protected ICrudService<TMemVoucherRule> getCrudService() {
		if(service == null) {
			service = SpringContextUtil.getBean(TMemberVoucherRuleService.class);
		}
		return service;
	}
	
	
	protected TMemVoucherRule newModel() {
		return new TMemVoucherRule();
	}
	
	//存客群
	public void saveSegment(List<TSegment> list) throws Exception{
		if(list != null && !list.isEmpty()){
			TSegment segment = list.get(0);
			segment.getName();
			if(segment != null) {
				rule.settSegment(segment);
			}
		}
		this.saveOrUpdate = true;
	}
	
	//存券库
	public void saveVoucherPool(List<TVoucherPool> list) throws Exception{
		if(list != null && !list.isEmpty()){
			TVoucherPool tVoucherPool = list.get(0);
			tVoucherPool.getName();
			if(tVoucherPool != null) {
				rule.settVoucherPool(tVoucherPool);
			}
		}
		this.saveOrUpdate = true;
	}
	
	/**
	 * 选择客群
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int selectSegment(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		getSelSegmentLet(req).setInvoked(TMemVoucherRuleLet.class.getName());
		return RETCODE_OK;
	}
	
	/**
	 * 选择券库
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int selectVoucherPool(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		getSelectVoucherPoolLet(req).setInvoked(TMemVoucherRuleLet.class.getName());
		return RETCODE_OK;
	}
	
	protected void prepareQuery(IModuleRequest req) {
		super.prepareQuery(req);
		query.put("code", req.getParameter("code", ""));
		query.put("name", req.getParameter("name", ""));
		query.put("status", req.getParameter("status", ""));
	}
	
	/**
	 * 下载导入模板
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int doDownLoadTemp(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = format.format(new Date());
		
		HttpServletResponse response = (HttpServletResponse) resp
		.getResponseObject();
		OutputStream stream = response.getOutputStream();
		response.reset();
		
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.ms-excel;utf-8");
		response.setHeader("Content-disposition", "attachment;   filename=\"" + "voucher_rule_"+ now + ".xls\"");
		
		HSSFWorkbook workbook = new HSSFWorkbook(); // 创建新的Excel工作薄

		HSSFSheet sheet = workbook.createSheet("JSP"); // 在Excel工作薄中建工作表，名为缺省
		HSSFRow row =  sheet.createRow(0);// 在索引0的位置建行（最顶端的行）
		row.createCell(0);// 在索引0的位置建单元格
		HSSFCellStyle sheetStyle = workbook.createCellStyle();
		//sheetStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体
		sheetStyle.setFillForegroundColor(HSSFColor.ORANGE.index);
		HSSFFont headfont = workbook.createFont();
		// headfont.setFontName("黑体");
		headfont.setFontHeightInPoints((short) 10);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		sheetStyle.setFont(headfont);
		sheetStyle.setWrapText(true);
		
		int num1 = 0;
		sheet.setColumnWidth(num1++, 4000);
		sheet.setColumnWidth(num1++, 4000);
		
		num1=0;
		setCellValue(row,num1++,"会员编码",sheetStyle);
		setCellValue(row,num1++,"券在订单中序列号",sheetStyle);
		
		workbook.write(stream);// 把相应的Excel工作薄存盘
		stream.flush();
		stream.close(); // 操作结束，关闭文件
		
		return RETCODE_OK;
	}
	
	//私有的模板下载方法
	private void setCellValue(HSSFRow row,int i,Object value,HSSFCellStyle sheetStyle){
		HSSFCell cell = row.createCell(i);
		cell.setCellStyle(sheetStyle);
		cell.setCellValue(TMemVoucherRuleLet.nullToEmpty(value));
	}
	
	//私有的模板下载方法
	private static String nullToEmpty(Object obj){
    	if(obj == null){
    		return "";
    	}
    	return String.valueOf(obj);
	}
	
	//上传文件验证
	public int doUpload(IModuleRequest req, IModuleResponse resp) throws Exception {
		if (!ServletFileUpload.isMultipartContent((HttpServletRequest) req
				.getRequestObject())) {
			throw new IllegalArgumentException(
					"Request is not multipart, please 'multipart/form-data' enctype for your form.");
		}
		FileParameter[] files = req.getFileParameters("files[]");
		JSONArray array = new JSONArray();
		if(uploadFile == null && files.length > 0) {
			uploadFile = new TFileAttach();
			uploadFile.setRefObjectId(rule.getId());
			uploadFile.setRefObjectType(rule.getClass().getName());
			uploadFile.setStatus("W");
			for(FileParameter item : files) {
				uploadFile.copyFile(item);
				JSONObject json = new JSONObject();
				json.put("name", item.getFileName());
				json.put("size", item.m_lSize);
				array.add(json);
			}
		}
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter writer = resp.getWriter();
		writer.write(array.toString());
		writer.flush();
		return RETCODE_OK;
	}
	
	//删除上传的文件
	public int doUploadDel(IModuleRequest req, IModuleResponse resp) throws Exception {
		uploadFile = null;
		
		return RETCODE_OK;
	}
	
	public int saveUpLoadFile(IModuleRequest req, IModuleResponse resp) throws Exception {
		if(req.isValidateField()) {
			if(req.getForm().getValidateField() == null) {
				return 10;
			}
			return req.getForm().getValidateField().isHasError() ? 11 : 10;
		}
		if(req.getForm().hasError()) {
			return 2;
		}
		try {
			securityContext(req);
			if(uploadFile != null) {
				//fileService.createOrUpdate(uploadFile);
				this.getService(TFileAttachService.class).createOrUpdate(uploadFile);
				//CcsUserProfile user = (CcsUserProfile) req.getUserProfile();
				//this.getService(TMackDaddyCardOrderService.class).createScheduler("voucherRuleExecutionJob", "RULE_JOB", user);
			}
			uploadFile = null;
		}catch(Exception e) {
			log.error(e);
			resp.setUserMessage("保存数据时发生异常:" + e.getLocalizedMessage());
			return RETCODE_ERR;
		}finally {
		}
		return RETCODE_OK;
	}
	
	protected void initSearch(IModuleRequest req, IModuleResponse resp) {
		super.initSearch(req, resp);
	}

	public int showList(IModuleRequest req, IModuleResponse resp)
		throws Exception {
		return super.showList(req, resp);
	}
	
	public int doSearch(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		return super.doSearch(req, resp);
	}
	
	public int doView(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		return super.doView(req, resp);
	}

	public int showSearch(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		return super.showSearch(req, resp);
	}
	
	public int showEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		int retNo = super.showEdit(req, resp);
		if(retNo == RETCODE_OK) {
			if(id != null && id != 0) {
				try {
					if(this.saveOrUpdate == false) {
						rule = this.findById(id);
					}
					if(rule.getSourceType() != null) {
						if(rule.getSourceType().equals(VOUCHER_TYPE_RULE)){
							return 3;
						}
						if(rule.getSourceType().equals(VOUCHER_TYPE_FILE)) {
							return 4;
						}
					}
				}catch(Exception e) {
					resp.setUserMessage(e.getLocalizedMessage());
					return RETCODE_ERR;
				}
			}
			if(rule == null) {
				log.debug("#### 隐藏Edit窗口 ####");
				return RETCODE_HIDE;
			}
			if(rule != null) {
				rule.setEditing(editing && canEdit(rule));
				if(rule.getId() != null && rule.getSourceType() != null){
					if(rule.getSourceType().equals(VOUCHER_TYPE_RULE)){
						return 1;
					}else if(rule.getSourceType().equals(VOUCHER_TYPE_FILE)){
						return 2;
					}
//					if(uploadFile != null) {
//						uploadFile.setRefObjectId(rule.getId());
//						getService(TFileAttachService.class).update(uploadFile);
//						uploadFile = null;
//					}
				}
			}
			req.setAttribute(NAME_OF_EDITING_OBJECT, rule);
			
		}
		return retNo;
	}
	
	public int doCreate(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		
		rule = new TMemVoucherRule();
		rule.setStatus(CAMPAINGN_STATUS_PLAN);
		String sendOrder = req.getParameter("sendOrder", "");
		rule.setSendOrder(sendOrder);
		return super.doCreate(req, resp);
	}

	public int doDelete(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		
		id = req.getParameter("id", 0L);
		if(id != 0) {
			log.debug("#### 执行删除方法:doDelete，删除对象ID[" + id + "] ####");
			ids = new Long[]{id};
			if(ids != null && ids.length > 0) {
				if(service != null) {
					service.delete(ids);
				}else {
					this.getService(TMemberVoucherRuleService.class).delete(ids);
				}
			}
		}
		id = null;
		ids = null;
		editing = false;
		return RETCODE_OK;
	}
	
	//停用方法
	public int doStop(IModuleRequest req, IModuleResponse resp) throws Exception {
		id = req.getParameter("id", 0L);
		if(id != 0) {
			rule = this.findById(id);
			rule.setStatus(CAMPAINGN_STATUS_INACTIVE);
			log.debug("#### 执行停用方法:doStop，停用对象ID[" + id + "] ####");
			if(service != null) {
				this.getCrudService().update(rule);
			}
		}
		id = null;
		editing = false;
		return RETCODE_OK;
	}
	
	//启用方法
	public int doStart(IModuleRequest req, IModuleResponse resp) throws Exception {
		id = req.getParameter("id", 0L);
		if(id != 0) {
			rule = this.findById(id);
			if(new Date().getTime() >= rule.getEndDtime().getTime()) {
				rule.setStatus(CAMPAINGN_STATUS_INACTIVE);
			}
			if(new Date().getTime() < rule.getStartDtime().getTime()) {
				rule.setStatus(CAMPAINGN_STATUS_PUBLISH);
			}
			if(new Date().getTime() >= rule.getStartDtime().getTime()) {
				rule.setStatus(CAMPAINGN_STATUS_EXECUTE);
			}
			log.debug("#### 执行启用方法:doStart，启用对象ID[" + id + "] ####");
			if(service != null) {
				this.getCrudService().update(rule);
			}
		}
		id = null;
		editing = false;
		return RETCODE_OK;
	}
	
	//发布方法
	public int doReplace(IModuleRequest req, IModuleResponse resp) throws Exception {
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
			if(rule.gettVoucherPool() != null) {
				if(rule.gettVoucherPool().getSendLock().toString().equals("0")) {
					rule.gettVoucherPool().setSendLock("1");
					this.getService(TMemberVoucherPoolService.class).update(rule.gettVoucherPool());
				}
			}
			this.cancelEdit(req, resp);
			log.debug("#### 执行数据发布方法:doReplace，发布对象[" + rule + "] ####");
		return RETCODE_OK;
	}
	
	public int cancelEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		rule = null;
		uploadFile = null;
		this.saveOrUpdate = false;
		return super.cancelEdit(req, resp);
	}
	
	public int doEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		rule = null;
		return super.doEdit(req, resp);
	}
	
	public int nextStep(IModuleRequest req, IModuleResponse resp) {
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
		this.getCrudService().createOrUpdate(rule);
		log.debug("#### 执行数据保存方法:saveEdit，保存对象[" + rule + "] ####");
		return RETCODE_OK;
	}
	
	public int saveEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		
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
			this.getCrudService().createOrUpdate(rule);
			this.cancelEdit(req, resp);
			log.debug("#### 执行数据保存方法:saveEdit，保存对象[" + rule + "] ####");
			
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
		@Validate(name = "name", id = "tne", error = "券发放名称不能为空。"),
		@Validate(name = "strStartDtime", id = "tne", error = "开始时间不能为空。"),
		@Validate(name = "strEndDtime", id = "tne", error = "结束时间不能为空。"),
		@Validate(name = "sourceType", id = "tne", error = "请选择指定方式。" )
	})
	public ValidateResult validate(IModuleRequest req, Input input) {
		return ValidateResult.PASS;
	}
	
	public TMemberVoucherRuleService getService() {
		return service;
	}

	public void setService(TMemberVoucherRuleService service) {
		this.service = service;
	}

	public TMemVoucherRule getRule() {
		return rule;
	}

	public void setRule(TMemVoucherRule rule) {
		this.rule = rule;
	}

	public TFileAttach getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(TFileAttach uploadFile) {
		this.uploadFile = uploadFile;
	}

	public boolean isSaveOrUpdate() {
		return saveOrUpdate;
	}

	public void setSaveOrUpdate(boolean saveOrUpdate) {
		this.saveOrUpdate = saveOrUpdate;
	}
}
