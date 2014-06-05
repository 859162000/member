package com.wanda.ccs.file;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.aggrepoint.adk.FileParameter;
import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.aggrepoint.adk.form.Input;
import com.aggrepoint.adk.form.Validates;
import com.aggrepoint.adk.ui.ValidateResult;
import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.jobhub.client.JobScheduleService;
import com.wanda.ccs.mem.service.TFileAttachService;
import com.wanda.ccs.model.TFileAttach;
import com.wanda.ccs.model.TMember;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.service.ICrudService;
import com.xcesys.extras.util.SpringContextUtil;

public class AbatchMemberLet extends BaseCrudWinlet<TFileAttach> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4468920361957032020L;
	private TFileAttachService service;

	private boolean isUpLoad = false;
	public String status;
	private String refObjectType;
	// 编辑的对象所对应的图片
	public TFileAttach uploadFile;
	
	public AbatchErreLogLet abatchErreLogLet = new AbatchErreLogLet();

	AbatchErreLogLet getAbatchErreLogLet(IModuleRequest req)
			throws Exception {
		if (abatchErreLogLet == null) {
			abatchErreLogLet = (AbatchErreLogLet) req
					.getWinlet(AbatchErreLogLet.class.getName());
		}
		return abatchErreLogLet;
	}

	protected TFileAttach newModel() {
		return new TFileAttach();
	}

	@Override
	protected ICrudService<TFileAttach> getCrudService() {
		if (service == null) {
			service = SpringContextUtil.getBean(TFileAttachService.class);
		}
		return service;
	}

	@Override
	protected void prepareQuery(IModuleRequest req) {
		super.prepareQuery(req);
		query.put("filename", req.getParameter("filename", ""));
		refObjectType = TMember.class.getName();
	}

	@Override
	protected void initSearch(IModuleRequest req, IModuleResponse resp) {
		if(refObjectType == null)
			refObjectType = TMember.class.getName();
		super.initSearch(req, resp);
	}

	@Override
	public int doSearch(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		getAbatchErreLogLet(req).setFileAttachId(null);
		return super.doSearch(req, resp);
	}

	@Override
	public int showList(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		query.put("refObjectType", refObjectType);
		query.put("createdBy", getUser(req).getId());
		String[] status = { "E", "W" ,"S"};
		query.put("status", status);
		PageResult<TFileAttach> pageResult = getService(
				TFileAttachService.class).findByCriteria(query);
		if (pageResult == null) {
			return RETCODE_HIDE;
		}
		// New Displaytag使用的结果集，并将其放到Request中，使JSP可引用。
		req.setAttribute("MEMBERABATCHRESULT", buildResult(pageResult));
		return 0;
	}

	public int doCreatUpLoad(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		isUpLoad = true;
		status = req.getParameter("status", "P");
		refObjectType = TMember.class.getName();
		uploadFile = null;
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

	/**
	 * 上传批量导入会员模板
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
	 * 下载批量导入会员模板
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int doDownLoadTemp(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		uploadFile = getService(TFileAttachService.class).getFileAttach(0l, "模板", "%批量导入会员模板%", null);
		if (uploadFile != null) {
			HttpServletResponse response = (HttpServletResponse) resp
					.getResponseObject();
			response.setContentType("application/x-download");
			response.setCharacterEncoding("UTF-8");
			try {
				response.addHeader(
						"Content-Disposition",
						"attachment;filename="
								+ URLEncoder.encode(uploadFile.getFileName(),
										"UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			try {
				IOUtils.copy(uploadFile.getFileData().getBinaryStream(),
						resp.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		uploadFile = null;
		return RETCODE_OK;
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
		for (FileParameter item : files) {
			if(!item.m_strContentType.contains("excel") && !item.m_strContentType.contains("download")){
				JSONObject jsono = new JSONObject();
				jsono.put("name", "注意：请上传2003版的Excel.此次操作无效");
				jsono.put("size", 0);
				json.add(jsono);
			}else{
			uploadFile.copyFile(item);
			JSONObject jsono = new JSONObject();
			jsono.put("name", item.getFileName());
			jsono.put("size", item.m_lSize);
			json.add(jsono);
			}
		}
		resp.setContentType("text/plain;charset=utf-8");
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
				if(uploadFile.getContentType() == null){
					resp.setUserMessage("上传文件有误，请上传2003版Excel文件.");
					return RETCODE_ERR;
				}else{
					getService(TFileAttachService.class).createOrUpdate(uploadFile,"memberJob",(CcsUserProfile)req.getUserProfile());
					if(uploadFile.getStatus() != null && uploadFile.getStatus().equals("W")){
						Map<String, String> params = new HashMap<String, String>();
						params.put("fileId", uploadFile.getId().toString());
						
						getService(JobScheduleService.class).scheduleJob("MemberImportJob", "member-jobs", params);
//						getService(CreateSchedulerService.class).createScheduler("memberJob", "memberJob", (CcsUserProfile)req.getUserProfile(), fileIdMap);
					}
				}
			}

		} catch (Exception e) {
			log.error(e);
			resp.setUserMessage("保存数据时发生异常:" + e.getLocalizedMessage());
			return RETCODE_ERR;
		} finally {
			cancelEdit(req, resp);
		}
		resp.setUserMessage("保存成功之后请稍等片刻,然后刷新(F5)或者重新查询,查看相关信息.");
		return RETCODE_OK;
	}

	@Override
	public int cancelEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		isUpLoad = false;
		uploadFile = null;
		refObjectType = TMember.class.getName();
		status = "";
		return RETCODE_OK;
	}

	@Override
	public int doView(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		Long fileId = req.getParameter("id", 0l);
		getAbatchErreLogLet(req).setFileAttachId(fileId);
		return RETCODE_OK;
	}
	
	

}