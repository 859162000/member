package com.wanda.ccs.auth.service;



import java.util.Map;

import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.model.TAuthOrgAssign;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 
 * 权限组权限service
 * @author Yang
 * @date 2011-11-30
 */
public interface TAuthOrgAssignService extends ICrudService<TAuthOrgAssign> {
	
	/**
	 * 获取院线的组织关系
	 * @return
	 */
	public TAuthOrgAssign getGroupOrgAssign();
	
	/**
	 * 获取区域的组织关系
	 * @return
	 */
	public Map<String, TAuthOrgAssign> getRegionOrgAssign();
	
	/**
	 * 获取影院的组织关系
	 * @return
	 */
	public Map<String, Map<String, TAuthOrgAssign>> getCinemaOrgAssign();
	
	/**
	 * 获取用户在组织结构中的位置
	 * @param user
	 * @return
	 */
	public TAuthOrgAssign getOrgAssignByUser(CcsUserProfile user);
	
	/**
	 * 获取用户在组织结构中的位置
	 * @param user
	 * @return
	 */
	public TAuthOrgAssign getOrgAssignByRegion(String region);
	
	/**
	 * 获取用户在组织结构中的位置
	 * @param user
	 * @return
	 */
	public TAuthOrgAssign getOrgAssignByCinema(Long cinemaId);
	
	/**
	 * 根据corp获取组织结构关系
	 * @param pkCorp
	 * @return
	 */
	public TAuthOrgAssign getOrgAssignByCorp(String pkCorp);
	
}
