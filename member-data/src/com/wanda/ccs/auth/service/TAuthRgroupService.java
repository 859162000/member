package com.wanda.ccs.auth.service;

import com.wanda.ccs.model.TAuthRgroup;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 
 * 权限组相关业务service
 * @author Yang
 * @date 2011-11-30
 */
public interface TAuthRgroupService extends ICrudService<TAuthRgroup> {
	
	/**
	 * 通过权限组名称判断权限组是否存在
	 * @param groupName
	 * @return
	 */
	public boolean chackRGroupByName(String groupName);
	
	public void saveRgroup(TAuthRgroup rgroup);
}
