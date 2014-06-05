package com.wanda.ccs.auth.service;

import java.util.List;
import java.util.Map;

import com.wanda.ccs.model.TAuthUser;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 
 * 用户管理service
 * @author Yang
 * @date 2011-12-1
 */
public interface TAuthUserService extends ICrudService<TAuthUser> {
	/**
	 * 检查登录账号是否存在
	 * @param loginId
	 * @return
	 */
	public boolean checkLoginId(String loginId);
	
	/** 
	 * 用户登录，通过账号和密码获得用户
	 * @param loginId
	 * @param loginPwd
	 * @return
	 */
	public TAuthUser getUserByLogin(String loginId,String loginPwd);
	
	/**
	 * 删除账号，把账号状态改为已删除
	 * @param ids
	 */
	public void deleteUser(Long[] ids);
	
	/**
	 * 保存用户，包括用户权限
	 * @param authUser
	 */
	public void saveUser(TAuthUser authUser);
	
	/**
	 * 根据nc主键获得用户
	 * @param pkDeptdoc
	 * @param psncode
	 * @return
	 */
	public TAuthUser getUserByPsnbasdoc(String psnbasdoc);
	
	/**
	 * 通过序列号serialNumber获得用户
	 * @param serialNumber
	 * @return
	 */
	public TAuthUser getUserBySerialNumber(Long serialNumber);
	
	/**
	 * 检测用户密码是否正确
	 * @param loginId
	 * @param loginPwd
	 * @return
	 */
	public boolean checkUserByLoginIdAndPwd(String loginId,String loginPwd);
	
	/**
	 * 检测用户密码是否正确
	 * @param loginId
	 * @param loginPwd
	 * @return
	 */
	public void updateUserPwdByLoginId(String loginId,String loginPwd);
	
	/**
	 * 根据序列号获取用户姓名
	 * @param serialNumber
	 * @return
	 */
	public Map<String,String> getUserNameBySerialNumber(Long serialNumber,Long authUserId);
	
	/**
	 * 根据序列号获取用户姓名
	 * @param serialNumber
	 * @return
	 */
	public Map<String,String> getEhrUserNameBySerialNum(Long serialNumber,Long authUserId);
	
	/**
	 * 根据EHR员工号获取用户
	 * @param pkDeptdoc
	 * @param psncode
	 * @return
	 */
	public TAuthUser getUserByRtx(String rtx);
	
	/**
	 * 根据rtx或loginid获取员工
	 * @param like
	 * @return
	 */
	public List<TAuthUser> findByLike(String like);

	/**
	 * 检查员工号是否存在
	 * @param userId
	 * @return
	 */
	public boolean checkUserId(String userId);
	
	/**
	 * 根据查询条件获取人员列表(会员中使用)
	 * @param query
	 * @return
	 */
	public PageResult<TAuthUser> findByQuery(QueryCriteria<String, Object> query);
	
}
