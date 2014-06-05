package com.wanda.ccs.auth.service;

import com.wanda.ccs.model.EHREmployee;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 员工Service.
 * 
 * @author Chenxm
 * 
 */

public interface EHREmployeeService extends ICrudService<EHREmployee>{
	
	/**
	 * 根据员工号，获取ehr人员
	 * @param employeeCode
	 * @return
	 */
	public EHREmployee getEhrByRtxName(String rtxName);
}
