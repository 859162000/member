package com.wanda.ccs.kpi;

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
import org.springframework.beans.factory.annotation.Autowired;

import com.aggrepoint.adk.FileParameter;
import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.aggrepoint.adk.form.Input;
import com.aggrepoint.adk.form.Validates;
import com.aggrepoint.adk.ui.ValidateResult;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.mem.service.TMemberKPIFileService;
import com.wanda.ccs.model.IMemberDimType;
import com.wanda.ccs.model.TFileAttach;
import com.wanda.ccs.model.TMemberKPI;
import com.xcesys.extras.core.service.ICrudService;
import com.xcesys.extras.util.SpringContextUtil;

public class KpiImportLet extends BaseCrudWinlet<TFileAttach> implements IMemberDimType{

	private static final long serialVersionUID = 2129180834037682949L;
	
	@Autowired
	private TMemberKPIFileService service;
	
	private boolean isUpLoad = false;
	public String status;
	public String refObjectType;
	// 编辑的对象所对应的实体类
	public TFileAttach uploadFile;
	
	public KpiErrorLogLet kpiErrorLoglet = new KpiErrorLogLet();
	
	public KpiErrorLogLet getKpiErrorLoglet(IModuleRequest req)throws Exception {
		if(kpiErrorLoglet == null) {
			kpiErrorLoglet = (KpiErrorLogLet) req.getWinlet(KpiErrorLogLet.class.getName());
		}
		return kpiErrorLoglet;
	}

	protected TFileAttach newModel() {
		return new TFileAttach();
	}

	@Override
	protected ICrudService<TFileAttach> getCrudService() {
		if (service == null) {
			service = SpringContextUtil.getBean(TMemberKPIFileService.class);
		}
		return service;
	}

	@Override
	protected void prepareQuery(IModuleRequest req) {
		super.prepareQuery(req);
		query.put("fileName", req.getParameter("fileName", ""));
		//refObjectType = TMemberKPI.class.getName();
	}
	
	public static void main(String[] args) {
		System.out.println(TMemberKPI.class.getName())	;
	}

	@Override
	public int doSearch(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		this.getKpiErrorLoglet(req).setFileAttachId(null);
		return super.doSearch(req, resp);
	}

	@Override
	public int showList(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		query.put("refObjectType", TMemberKPI.class.getName());
		query.put("createdBy", getUser(req).getId());
		String[] status = { "E", "W" };
		query.put("status", status);
		return super.showList(req, resp);
	}

	public int doCreatUpLoad(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		isUpLoad = true;
		//refObjectType = TMemberKPI.class.getName();
		return RETCODE_OK;
	}

	public int showUpLoadView(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if (isUpLoad) {
			return RETCODE_OK;
		} else {
			return RETCODE_HIDE;
		}

	}

/*	*//**
	 * 上传批量导入kpi模板
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int doUpLoadTemp(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		isUpLoad = true;
		status = req.getParameter("status", null);
		refObjectType = "模板";
		return RETCODE_OK;
	}

	/**
	 * 下载批量导入kpi模板
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
		response.setHeader("Content-disposition", "attachment;   fileName=\"" + "member_kpi"+ now + ".xls\"");
		
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
		setCellValue(row,num1++,"kpi导入年份",sheetStyle);
		setCellValue(row,num1++,"kpi导入影城",sheetStyle);
		setCellValue(row,num1++,"kpi导入数量",sheetStyle);
		
		workbook.write(stream);// 把相应的Excel工作薄存盘
		stream.flush();
		stream.close(); // 操作结束，关闭文件
		return RETCODE_OK;
	}
	
	//私有的模板下载方法
	private void setCellValue(HSSFRow row,int i,Object value,HSSFCellStyle sheetStyle){
		HSSFCell cell = row.createCell(i);
		cell.setCellStyle(sheetStyle);
		cell.setCellValue(KpiImportLet.nullToEmpty(value));
	}
	
	//私有的模板下载方法
	private static String nullToEmpty(Object obj){
    	if(obj == null){
    		return "";
    	}
    	return String.valueOf(obj);
	}

	/**
	 * 删除文件
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int doUploadDel(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		uploadFile = null;
		return RETCODE_OK;
	}

	@Validates({})
	public ValidateResult validate(IModuleRequest req, Input input) {
		return ValidateResult.PASS;
	}

	public int doUpload(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if (!ServletFileUpload.isMultipartContent((HttpServletRequest) req
				.getRequestObject())) {
			throw new IllegalArgumentException(
					"Request is not multipart, please 'multipart/form-data' enctype for your form.");
		}

		FileParameter[] files = req.getFileParameters("files[]");
		JSONArray json = new JSONArray();
		if (uploadFile == null && files.length > 0)
			uploadFile = new TFileAttach(refObjectType, status);
		uploadFile.setRefObjectId(0l);
		uploadFile.setStatus("W");
		for (FileParameter item : files) {
			uploadFile.copyFile(item);
			JSONObject jsono = new JSONObject();
			jsono.put("name", item.getFileName());
			jsono.put("size", item.m_lSize);
			json.add(jsono);
		}
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter writer = resp.getWriter();
		writer.write(json.toString());
		writer.flush();
		return RETCODE_OK;
	}

	@Override
	public int saveEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if (req.isValidateField())// ajax数据校验
			return 2;
		if (req.getForm().hasError())// 数据校验失败
			return 3;
		try {
			// 初始化安全环境上下文。
			securityContext(req);
			// 执行保存对象操作。
			// CcsUserProfile user = (CcsUserProfile) req.getUserProfile();
			if (uploadFile != null) {
				service.createOrUpdate(uploadFile);
			}

		} catch (Exception e) {
			log.error(e);
			resp.setUserMessage("保存数据时发生异常:" + e.getLocalizedMessage());
			return RETCODE_ERR;
		} finally {
			cancelEdit(req, resp);
		}
		return RETCODE_OK;
	}

	@Override
	public int cancelEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		isUpLoad = false;
		uploadFile = null;
		refObjectType = TMemberKPI.class.getName();
		status = "";
		return RETCODE_OK;
	}

	@Override
	public int doView(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		Long fileId = req.getParameter("id", 0l);
		this.getKpiErrorLoglet(req).setFileAttachId(fileId);
		return RETCODE_OK;
	}

	public TFileAttach getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(TFileAttach uploadFile) {
		this.uploadFile = uploadFile;
	}

	public boolean isUpLoad() {
		return isUpLoad;
	}

	public void setUpLoad(boolean isUpLoad) {
		this.isUpLoad = isUpLoad;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRefObjectType() {
		return refObjectType;
	}

	public void setRefObjectType(String refObjectType) {
		this.refObjectType = refObjectType;
	}
}
