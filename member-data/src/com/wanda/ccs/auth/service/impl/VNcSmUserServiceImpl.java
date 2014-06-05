package com.wanda.ccs.auth.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.auth.dao.IVNcSmUserDao;
import com.wanda.ccs.auth.service.VNcSmUserService;
import com.wanda.ccs.model.VNcSmUser;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class VNcSmUserServiceImpl extends BaseCrudServiceImpl<VNcSmUser>
		implements VNcSmUserService {

	@Autowired
	private IVNcSmUserDao dao = null;

	@Override
	public IBaseDao<VNcSmUser> getDao() {
		return dao;
	}

	/** 权限组权限的列表 */
	@Override
	public PageResult<VNcSmUser> findByCriteria(
			QueryCriteria<String, Object> query) {

		String fromClause = " select c from VNcSmUser c";
		String[] whereBodies = new String[] {};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "";
		}
		PageResult<VNcSmUser> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;
	}

	@Override
	public VNcSmUser getUserByCodeAndPwd(String userCode, String userPassword) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select c from VNcSmUser c where c.userCode = :userCode and c.userPassword =:userPassword";
		params.put("userCode", userCode);
		params.put("userPassword", userPassword);
		List<VNcSmUser> list = super.queryHql(hql, params);
		if (list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}

	@Override
	public VNcSmUser getUserByCode(String userCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select c from VNcSmUser c where c.userCode = :userCode";
		params.put("userCode", userCode);
		List<VNcSmUser> list = super.queryHql(hql, params);
		if (list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}
}
