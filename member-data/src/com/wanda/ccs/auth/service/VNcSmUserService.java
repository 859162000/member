package com.wanda.ccs.auth.service;

import com.wanda.ccs.model.VNcSmUser;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 
 * 用户登录视图service
 * 
 * @author Yang
 * @date 2012-02-14
 */
public interface VNcSmUserService extends ICrudService<VNcSmUser> {
	/**
	 * 通过用户账户和密码获得用户
	 * 
	 * @param userCode
	 * @param userPassword
	 * @return
	 */
	public VNcSmUser getUserByCodeAndPwd(String userCode, String userPassword);

	/**
	 * 通过用户账户获得用户
	 * 
	 * @param userCode
	 * @return
	 */
	public VNcSmUser getUserByCode(String userCode);
}
