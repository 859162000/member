package com.wanda.ccs.mem.service;

import java.util.List;
import java.util.Map;

import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.model.TFileAttach;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.ICrudService;

public interface TFileAttachService extends ICrudService<TFileAttach> {
	
	public void createOrUpdate(TFileAttach fileAttach,String fromSource ,CcsUserProfile user);
	/**
	 * 根据查询条件查询文件
	 */
	public List<TFileAttach> getFileAttachByObject(Long refObjectId,
			String refObjecType,String filename,String status);
	
	public TFileAttach getFileAttach(Long refObjectId,
			String refObjecType,String filename,String status);
	
	/**
	 * 获取文件
	 * @param refObjectId
	 * @param refObjecType
	 * @return
	 */
	public TFileAttach getFileAttach(Long refObjectId, String refObjecType);
	
	/**
	 * 上传文件
	 * @param fileAttach
	 */
	public void saveFileAttach(TFileAttach fileAttach);
	
	/**
	 * 获取文件中的实际会员数
	 * @param fileId
	 * @return
	 */
	public Long getMemberCountByFileId(Long fileId);
	
	public PageResult<Map<String, ?>> findByCriteriaQuery(
			QueryCriteria<String, Object> query);
	
	
//	/**
//	 * 创建调度
//	 * @param jobName
//	 * 
//	 * @param triggerName
//	 * 
//	 * @param user
//	 */
//	public void createScheduler(String jobName, String triggerName, CcsUserProfile user);
	
}
