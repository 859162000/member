package com.wanda.ccs.auth.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.auth.dao.ITAuthUserRgroupDao;
import com.wanda.ccs.auth.service.TAuthUserRgroupService;
import com.wanda.ccs.model.TAuthUserRgroup;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TAuthUserRgroupServiceImpl extends BaseCrudServiceImpl<TAuthUserRgroup> implements
		TAuthUserRgroupService {

	@Autowired
	private ITAuthUserRgroupDao dao = null;

	@Override
	public IBaseDao<TAuthUserRgroup> getDao() {
		return dao;
	}
	
	/** 用户权限组列表 */
	@Override
	public PageResult<TAuthUserRgroup> findByCriteria(QueryCriteria<String, Object> query) {
		
		String fromClause = " select c from TAuthUserRgroup c";
		String[] whereBodies = new String[] {};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			 orderClause = "";
		}
		PageResult<TAuthUserRgroup> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;
	}
}
