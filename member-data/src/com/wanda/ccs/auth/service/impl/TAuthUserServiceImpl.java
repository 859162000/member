package com.wanda.ccs.auth.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.auth.dao.ITAuthUserDao;
import com.wanda.ccs.auth.service.EHREmployeeService;
import com.wanda.ccs.auth.service.TAuthRgroupService;
import com.wanda.ccs.auth.service.TAuthRightService;
import com.wanda.ccs.auth.service.TAuthUserRgroupService;
import com.wanda.ccs.auth.service.TAuthUserRightService;
import com.wanda.ccs.auth.service.TAuthUserService;
import com.wanda.ccs.auth.service.VNcUsersService;
import com.wanda.ccs.model.EHREmployee;
import com.wanda.ccs.model.TAuthUser;
import com.wanda.ccs.model.TAuthUserRgroup;
import com.wanda.ccs.model.TAuthUserRight;
import com.wanda.ccs.model.VNcUsers;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.StringUtil;

@Service
public class TAuthUserServiceImpl extends BaseCrudServiceImpl<TAuthUser>
		implements TAuthUserService {

	@Autowired
	private ITAuthUserDao dao = null;

	/** 用户权限组service */
	@Autowired
	TAuthUserRgroupService userRgroupService;
	/** 用户权限service */
	@Autowired
	TAuthUserRightService userRightService;
	/** 权限组service */
	@Autowired
	TAuthRgroupService rgroupService;
	/** 权限service */
	@Autowired
	TAuthRightService rightService;
	
	@Autowired
	VNcUsersService ncUserService;
	@Autowired
	EHREmployeeService ehrUserService;

	@Override
	public IBaseDao<TAuthUser> getDao() {
		return dao;
	}

	/** 用户列表 */
	@Override
	public PageResult<TAuthUser> findByCriteria(
			QueryCriteria<String, Object> query) {

		String fromClause = " select c from TAuthUser c";
		String[] whereBodies = new String[] { "c.userLevel in (:userLevel)",
				"c.loginId like :loginId", "c.userName like :userName",
				"c.region = :region", "c.tCinema.id = :cinemaId",
				"c.status <> :status", "c.pkDeptdoc = :pkDeptdoc",
				"c.fromNc = :fromNc" };
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "";
		}
		PageResult<TAuthUser> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;
	}
	
	/** 用户列表 */
	@Override
	public PageResult<TAuthUser> findByQuery(
			QueryCriteria<String, Object> query) {

		String fromClause = " select c from TAuthUser c";
		String[] whereBodies = new String[] {
				"c.loginId like :loginId or c.rtx like :loginId", 
				"c.userName like :userName",
				"c.status = :status",
				"(c.fromNc = 'N') or (c.fromNc = 'Y' and c.rtx is not null)"};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "";
		}
		PageResult<TAuthUser> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;
	}
	
	@Override
	public boolean checkLoginId(String loginId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select c from TAuthUser c where c.loginId = :loginId";
		params.put("loginId", loginId);
		if (super.queryCountHql(hql, params, null) > 0)
			return true;
		return false;
	}

	@Override
	public TAuthUser getUserByLogin(String loginId, String loginPwd) {
		if (StringUtil.isNullOrBlank(loginId)
				|| StringUtil.isNullOrBlank(loginPwd))
			return null;
		Map<String, Object> params = new HashMap<String, Object>();

		String hql = "select c from TAuthUser c where c.fromNc = :fromNc ";
		params.put("fromNc", "N");
		if (!StringUtil.isNullOrBlank(loginId)) {
			hql += " and c.loginId = :loginId ";
			params.put("loginId", loginId);
		}
		if (!StringUtil.isNullOrBlank(loginPwd)) {
			hql += " and c.loginPwd = :loginPwd";
			params.put("loginPwd", loginPwd);
		}
		if (super.queryCountHql(hql, params, null) > 0) {
			List<TAuthUser> list = super.queryHql(hql, params);
			return list.get(0);
		}
		return null;
	}

	@Override
	public void deleteUser(Long[] ids) {
		for (Long id : ids) {
			TAuthUser user = super.getById(id);
			user.setStatus("X");
			super.update(user);
		}
	}

	@Override
	public void saveUser(TAuthUser authUser) {
		super.createOrUpdate(authUser);
		for (TAuthUserRight userRight : authUser.gettAuthUserRights()) {
			if(userRight.getDelete()){//需要删除
				if(userRight.getId() != null)
					userRightService.delete(userRight);
			}else {//需要创建
				if(userRight.getId() == null){
					userRight.settAuthUser(authUser);
					userRightService.create(userRight);
				}
			}
		}
		for (TAuthUserRgroup userRgroup : authUser.gettAuthUserRgroups()) {
			if (userRgroup.getId() != null && userRgroup.getDelete()) {// 需要删除
				userRgroupService.delete(userRgroup);
			} else { // 需要创建
				userRgroup.settAuthUser(authUser);
				userRgroupService.create(userRgroup);
			}
		}
	}

	@Override
	public TAuthUser getUserByPsnbasdoc(String psnbasdoc) {
		if (StringUtil.isNullOrBlank(psnbasdoc))
			return null;
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select c from TAuthUser c where c.fromNc = 'Y' ";
		if (!StringUtil.isNullOrBlank(psnbasdoc)) {
			hql += " and c.pkPsnbasdoc = :pkPsnbasdoc ";
			params.put("pkPsnbasdoc", psnbasdoc);
		}
		List<TAuthUser> list = super.queryHql(hql, params);
		if (list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}

	@Override
	public TAuthUser getUserBySerialNumber(Long serialNumber) {
		if (serialNumber == null || serialNumber == 0)
			return null;
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select c from TAuthUser c where c.serialNumber = :serialNumber";
		params.put("serialNumber", serialNumber);
		List<TAuthUser> list = super.queryHql(hql, params);
		if (list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}

	@Override
	public boolean checkUserByLoginIdAndPwd(String loginId, String loginPwd) {
		if (StringUtil.isNullOrBlank(loginId)
				|| StringUtil.isNullOrBlank(loginPwd))
			return false;
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select c from TAuthUser c where c.fromNc = :fromNc ";
		params.put("fromNc", "N");
		if (!StringUtil.isNullOrBlank(loginId)) {
			hql += " and c.loginId = :loginId ";
			params.put("loginId", loginId);
		}
		if (!StringUtil.isNullOrBlank(loginPwd)) {
			hql += " and c.loginPwd = :loginPwd";
			params.put("loginPwd", loginPwd);
		}
		if (super.queryCountHql(hql, params, null) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public void updateUserPwdByLoginId(String loginId, String loginPwd) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select c from TAuthUser c where c.fromNc = :fromNc ";
		params.put("fromNc", "N");
		if (!StringUtil.isNullOrBlank(loginId)) {
			hql += " and c.loginId = :loginId ";
			params.put("loginId", loginId);
		}
		List<TAuthUser> list = super.queryHql(hql, params);
		if (list != null && !list.isEmpty()){
			TAuthUser user = list.get(0);
			user.setLoginPwd(loginPwd);
			dao.createOrUpdate(user);
		}

	}

	@Override
	public Map<String,String> getUserNameBySerialNumber(Long serialNumber, Long authUserId) {
		Map<String, String> map = new HashMap<String, String>();
 		if (serialNumber == null || serialNumber == 0)
			return null;
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select c from TAuthUser c where c.serialNumber = :serialNumber";
		params.put("serialNumber", serialNumber);
		if(authUserId != null && authUserId != 0L){
			hql += " and c.id != :id";
			params.put("id", authUserId);
		}
		List<TAuthUser> list = super.queryHql(hql, params);
		if(list != null && !list.isEmpty()){
			TAuthUser user = list.get(0);
			if(user.getFromNc() != null && user.getFromNc().equals("Y")){
				map.put("fromNc", "Y");
				VNcUsers ncUser = ncUserService.getVNcUserByPsnbasdoc(user.getPkPsnbasdoc());
				if(ncUser != null){
					map.put("userName", ncUser.getPsnname());
				}
			}else {
				map.put("fromNc", "N");
				map.put("userName", user.getUserName());
			}
		}else{
			return null;
		}
			
		return map;
	}

	@Override
	public Map<String, String> getEhrUserNameBySerialNum(Long serialNumber,
			Long authUserId) {
		Map<String, String> map = new HashMap<String, String>();
 		if (serialNumber == null || serialNumber == 0)
			return null;
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select c from TAuthUser c where c.serialNumber = :serialNumber";
		params.put("serialNumber", serialNumber);
		if(authUserId != null && authUserId != 0L){
			hql += " and c.id != :id";
			params.put("id", authUserId);
		}
		TAuthUser user = dao.queryHqlTopOne(hql, params);
		if(user != null){
			if(user.getFromNc() != null && user.getFromNc().equals("Y")){
				map.put("fromNc", "Y");
				EHREmployee ehrUser = ehrUserService.getEhrByRtxName(user.getRtx());
				if(ehrUser != null){
					map.put("userName", ehrUser.getEmployeeName());
				}
			}else {
				map.put("fromNc", "N");
				map.put("userName", user.getUserName());
			}
		}else{
			return null;
		}
		return map;
	}

	@Override
	public TAuthUser getUserByRtx(String rtx) {
		if (StringUtil.isNullOrBlank(rtx))
			return null;
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select c from TAuthUser c where c.fromNc = 'Y' ";
		hql += " and c.rtx = :rtx ";
		params.put("rtx", rtx);
		return dao.queryHqlTopOne(hql, params);
	}

	@Override
	public List<TAuthUser> findByLike(String like) {
		if(StringUtil.isNullOrBlank(like))
			return null;
		String hql = "select c from TAuthUser c where c.loginId like :userId or c.rtx like :userId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", "%"+like+"%");
		return getDao().queryHql(hql, params);
	}
	
	@Override
	public boolean checkUserId(String userId) {
		if(StringUtil.isNullOrBlank(userId))
			return false;
		String hql = "select c from TAuthUser c where c.loginId = :userId or c.rtx = :userId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		return super.queryCountHql(hql, params, null) > 0;
	}
}