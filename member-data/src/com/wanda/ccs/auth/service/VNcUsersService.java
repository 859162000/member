package com.wanda.ccs.auth.service;



import com.wanda.ccs.model.VNcUsers;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 
 * 用户列表视图service
 * @author Yang
 * @date 2012-02-14
 */
public interface VNcUsersService extends ICrudService<VNcUsers> {
	
	/**
	 * 通过psndoc获得人员
	 * @param psndoc
	 * @return
	 */
	public VNcUsers getVNcUserByPsndoc(String psndoc);
	
	/**
	 * 获取所有人员列表
	 * @param query
	 * @return
	 */
	public PageResult<VNcUsers> findAllByCriteria(
			QueryCriteria<String, Object> query);
	
	/**
	 * 通过psnbasdoc获取nc人员
	 * @param psnbasdoc
	 * @return
	 */
	public VNcUsers getVNcUserByPsnbasdoc(String psnbasdoc);
}
