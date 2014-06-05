package com.wanda.ccs.file;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.mem.service.TAbatchErreLogService;
import com.wanda.ccs.model.TAbatchErreLog;
import com.xcesys.extras.core.service.ICrudService;
import com.xcesys.extras.util.SpringContextUtil;

public class AbatchErreLogLet extends BaseCrudWinlet<TAbatchErreLog> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1636088108260958614L;
	
	private TAbatchErreLogService service;

	public Long fileAttachId;

	public Long getFileAttachId() {
		return fileAttachId;
	}

	@Override
	public int doSearch(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		query.put("pageerr", req.getParameter("pageerr", 1));
		return super.doSearch(req, resp);
	}

	public void setFileAttachId(Long fileAttachId) {
		this.fileAttachId = fileAttachId;
	}

	@Override
	protected ICrudService<TAbatchErreLog> getCrudService() {
		if (service == null) {
			service = SpringContextUtil.getBean(TAbatchErreLogService.class);
		}
		return service;
	}

	public int doCloseList(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		fileAttachId = null;
		return RETCODE_OK;
	}

	@Override
	public int showList(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if (fileAttachId == null) {
			return RETCODE_HIDE;
		} else {
			query.put("fileAttachId", fileAttachId);
			return super.showList(req, resp);
		}
	}
}