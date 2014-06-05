package com.wanda.ccs.auth.service;


import com.wanda.ccs.model.TAuthSystem;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 
 * 系统service
 * @author Yang
 * @date 2012-3-8
 */
public interface TAuthSystemService extends ICrudService<TAuthSystem> {
	/**
	 * 通过systemId获得system
	 * @param systemId
	 * @return
	 */
	public TAuthSystem getSystemBySystemId(String systemId);
	
	/**
	 * 检验systemid是否存在
	 * @param systemId
	 * @return
	 */
	public boolean checkSystemId(Long id,String systemId);
}
