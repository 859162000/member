package com.wanda.ccs.auth.service;

import java.util.List;
import java.util.Map;

import com.wanda.ccs.model.EHROrgnization;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 员工Service.
 * 
 * @author Chenxm
 * 
 */

public interface EHROrgnizationService extends ICrudService<EHROrgnization>{
	/**
	 * 根据父节点查询其子目录的下的组织
	 * @param parentUnitID
	 * @return
	 */
	public Map<String,String> getParentUnitID(String parentUnitID);
	/**
	 * 根据父节点获取子节点所有组织内容信息
	 * @param parentUnitID
	 * @return
	 */
	List<EHROrgnization> getParentID(String parentUnitID);
	/**
	 *多选下拉联动
	 * @param parentIDs
	 * @return
	 */
	public List<EHROrgnization> getParentIDs(Long[] parentIDs);
	
//	public Map<String,String> getParentIDs(Long[] parentIDs);
	
}
