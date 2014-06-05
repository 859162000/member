package com.wanda.ccs.auth.service;

import java.util.List;

import com.wanda.ccs.model.TJMonOfficialJobs;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 员工Service.
 * 
 * @author Chenxm
 * 
 */

public interface TJMonOfficealJobsService extends
		ICrudService<TJMonOfficialJobs> {
	/**
	 * 根据rtxName查询到员工的信息
	 * @param rtxName
	 * @return
	 */
	public TJMonOfficialJobs getJMonOJByRTX(String rtxName);
	/**
	 * 校验RTX是否已存在
	 * @param rtxName
	 * @return
	 */
	public List<TJMonOfficialJobs> checkRTX(String rtxName);
}
