package com.wanda.ccs.auth.service;

import java.util.List;

import com.aggrepoint.adk.IModuleRequest;
import com.wanda.ccs.model.TJMonChanges;
import com.wanda.ccs.model.TJMonOfficialJobs;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 员工Service.
 * 
 * @author Chenxm
 * 
 */

public interface TJMonChangesService extends ICrudService<TJMonChanges>{
	/**
	 * 确认操作
	 * @param ids
	 * @param req
	 */
	public void doApprove(String strs,IModuleRequest req);
	/**
	 * 根据确认状态查询
	 * @param req
	 * @param resp
	 * @return
	 */
	public List<TJMonChanges> doFindTJMonChangesList(String approveFlag);
	/**
	 * 改变人员信息时，生成日志保存在TJMonChanges
	 * @param oldJMonOJ
	 * @param newJMonOJ
	 * @param req
	 */
	public void doChanges(TJMonOfficialJobs oldJMonOJ,TJMonOfficialJobs newJMonOJ,IModuleRequest req);
	/**
	 * 校验人员信息
	 * @param rtxName
	 */
	public boolean checkRTX(String rtxName);
	
	/**
	 * 查询五天内做过确认的信息
	 * @return
	 */
	public List<TJMonChanges> doStickieList();
}
