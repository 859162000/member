package com.wanda.ccs.auth.service;



import com.wanda.ccs.model.TAuthRgroupRight;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 
 * 权限组权限service
 * @author Yang
 * @date 2011-11-30
 */
public interface TAuthRgroupRightService extends ICrudService<TAuthRgroupRight> {
	
	/**
	 * 保存权限组权限
	 * @param id
	 * @param rightIds
	 */
	public void saveRgroupRight(Long groupId, Long[] rightIds);
	
}
