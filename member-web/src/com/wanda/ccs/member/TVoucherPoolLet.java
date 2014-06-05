package com.wanda.ccs.member;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.mem.service.TFileAttachService;
import com.wanda.ccs.mem.service.TMackDaddyCardOrderService;
import com.wanda.ccs.mem.service.TMemberVoucherPoolService;
import com.wanda.ccs.model.IDimType;
import com.wanda.ccs.model.TFileAttach;
import com.wanda.ccs.model.TVoucherPool;
import com.wanda.ccs.model.TVoucherPoolDetail;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.service.ICrudService;
import com.xcesys.extras.util.SpringContextUtil;

/**
 * 券库管理
 * @author admin
 *
 */
public class TVoucherPoolLet extends BaseCrudWinlet<TVoucherPool> implements
		IDimType {

	private static final long serialVersionUID = 2566904688581079984L;
	
	public TVoucherPool tVoucherPool;
	
	public VoucherPoolDetailLet voucherPoolDetailLet = new VoucherPoolDetailLet();

	VoucherPoolDetailLet getVoucherPoolDetailLet(IModuleRequest req)
			throws Exception {
		if (voucherPoolDetailLet == null) {
			voucherPoolDetailLet = (VoucherPoolDetailLet) req
					.getWinlet(VoucherPoolDetailLet.class.getName());
		}
		return voucherPoolDetailLet;
	}
	
	public TFileAttach uploadFile;
	
	public boolean importOk;
	
	private PageResult<TVoucherPoolDetail> result;
	
	public Long getId() {
		return id;
	}
	
	private TMemberVoucherPoolService service;
	
	public TVoucherPoolLet() {
		
	}
	
	protected TVoucherPool newModel() {
		return new TVoucherPool();
	}

	
	protected ICrudService<TVoucherPool> getCrudService() {
		if (service == null) {
			service = SpringContextUtil
					.getBean(TMemberVoucherPoolService.class);
		}
		return service;
	}
	

	protected void prepareQuery(IModuleRequest req) {
		super.prepareQuery(req);
		query.put("name", req.getParameter("name", ""));
		query.put("id", req.getParameter("id", ""));
	}
	
	protected void initSearch(IModuleRequest req, IModuleResponse resp) {
		super.initSearch(req, resp);
	}
	
	public int showList(IModuleRequest req, IModuleResponse resp)
		throws Exception {
		PageResult<TVoucherPool> pageResult = this.getCrudService().findByCriteria(query);
		if (pageResult == null) {
			return RETCODE_HIDE;
		}
		// New Displaytag使用的结果集，并将其放到Request中，使JSP可引用。
		req.setAttribute(NAME_OF_PAGE_RESULT, buildResult(pageResult));
		return RETCODE_OK;
	}
		
	public int showSearch(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		return super.showSearch(req, resp);
	}
	
	public int doView(IModuleRequest req, IModuleResponse resp)
		throws Exception {
			return super.doView(req, resp);
	}
	
	public int cancelEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		tVoucherPool = null;
		uploadFile = null;
		return super.cancelEdit(req, resp);
	}
	
	public int doSearch(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		return super.doSearch(req, resp);
	}

	public int doCreate(IModuleRequest req, IModuleResponse resp)
	throws Exception {
		tVoucherPool = new TVoucherPool();
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
				}
			}else {
				this.getService(TMemberVoucherPoolService.class).delete(ids);
			}
		}
		id = null;
		ids = null;
		editing = false;
		return RETCODE_OK;
	}
	
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
			uploadFile.setRefObjectId(tVoucherPool.getId());
			uploadFile.setRefObjectType(tVoucherPool.getClass().getName());
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
	
	
	public int doUploadDel(IModuleRequest req, IModuleResponse resp)throws Exception {
		uploadFile = null;
		return RETCODE_OK;
	}
	
	public int saveUpLoadFile(IModuleRequest req, IModuleResponse resp)throws Exception {
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
				//this.getService(TMackDaddyCardOrderService.class).createScheduler("voucherPoolExecutionJob", "POOL_JOB", user);
			}
			uploadFile = null;
			this.importOk = true;
			if(this.getVoucherPoolDetailLet(req) != null) {
				this.getVoucherPoolDetailLet(req).setVoucherPoolId(tVoucherPool.getId());
				this.getVoucherPoolDetailLet(req).setInvoked(VoucherPoolDetailLet.class.getName());
				this.getVoucherPoolDetailLet(req).showTicketList(req, resp);
			}
		}catch(Exception e) {
			log.error(e);
			resp.setUserMessage("保存数据时发生异常:" + e.getLocalizedMessage());
			return RETCODE_ERR;
		}
		return RETCODE_OK;
	}

	public int doEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		tVoucherPool = null;
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
		this.getCrudService().createOrUpdate(tVoucherPool);
		log.debug("#### 执行数据保存方法:saveEdit，保存对象[" + tVoucherPool + "] ####");
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
			tVoucherPool.setHasImport("0");
			tVoucherPool.setPoolType("0");
			tVoucherPool.setSendLock("0");
			this.getCrudService().createOrUpdate(tVoucherPool);
			this.cancelEdit(req, resp);
			this.getVoucherPoolDetailLet(req).cancelEdit(req, resp);
			//this.getVoucherPoolDetailLet(req).setHide(true);
			log.debug("#### 执行数据保存方法:saveEdit，保存对象[" + tVoucherPool + "] ####");
			return RETCODE_OK;
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
		response.setHeader("Content-disposition", "attachment;   filename=\"" + "voucher_pool_"+ now + ".xls\"");
		
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
		sheet.setColumnWidth(num1++, 4000);
		
		num1=0;
		setCellValue(row,num1++,"券在订单中序列号",sheetStyle);
		setCellValue(row,num1++,"到期日期",sheetStyle);
		setCellValue(row,num1++,"条码",sheetStyle);
		
		workbook.write(stream);// 把相应的Excel工作薄存盘
		stream.flush();
		stream.close(); // 操作结束，关闭文件
		
		return RETCODE_OK;
	}
	
	//私有的模板下载方法
	private void setCellValue(HSSFRow row,int i,Object value,HSSFCellStyle sheetStyle){
		HSSFCell cell = row.createCell(i);
		cell.setCellStyle(sheetStyle);
		cell.setCellValue(TVoucherPoolLet.nullToEmpty(value));
	}
	
	//私有的模板下载方法
	private static String nullToEmpty(Object obj){
    	if(obj == null){
    		return "";
    	}
    	return String.valueOf(obj);
	}
	
	public int doImport(IModuleRequest req, IModuleResponse resp) {
		System.out.println("2222");
		return RETCODE_OK;
	}

	public int showEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		int retNo = super.showEdit(req, resp);
		if(retNo == RETCODE_OK) {
			if(id != null && id != 0) {
				try {
					tVoucherPool = this.findById(id);
					return 2;
				}catch (Exception e){
					resp.setUserMessage(e.getLocalizedMessage());
					return RETCODE_ERR;
				}
			}
			if(tVoucherPool == null) {
				log.debug("#### 隐藏Edit窗口 ####");
				return RETCODE_HIDE;
			}
			if(tVoucherPool != null) {
				tVoucherPool.setEditing(editing && canEdit(tVoucherPool));
				if(tVoucherPool.getId() != null) {
					return 1;
				}
			}
			req.setAttribute(NAME_OF_EDITING_OBJECT, tVoucherPool);
		}
		return retNo;
	}
	
	@Validates({
		@Validate(name = "name", id = "tne", error = "券库名称不能为空。")
	})
	public ValidateResult validate(IModuleRequest req, Input input) {
		return ValidateResult.PASS;
	}

	public TMemberVoucherPoolService getService() {
		return service;
	}

	public void setService(TMemberVoucherPoolService service) {
		this.service = service;
	}

	public TVoucherPool gettVoucherPool() {
		return tVoucherPool;
	}

	public void settVoucherPool(TVoucherPool tVoucherPool) {
		this.tVoucherPool = tVoucherPool;
	}

	public TFileAttach getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(TFileAttach uploadFile) {
		this.uploadFile = uploadFile;
	}

	public PageResult<TVoucherPoolDetail> getResult() {
		return result;
	}

	public void setResult(PageResult<TVoucherPoolDetail> result) {
		this.result = result;
	}

	public VoucherPoolDetailLet getVoucherPoolDetailLet() {
		return voucherPoolDetailLet;
	}

	public void setVoucherPoolDetailLet(VoucherPoolDetailLet voucherPoolDetailLet) {
		this.voucherPoolDetailLet = voucherPoolDetailLet;
	}

	public boolean isImportOk() {
		return importOk;
	}

	public void setImportOk(boolean importOk) {
		this.importOk = importOk;
	}
}
