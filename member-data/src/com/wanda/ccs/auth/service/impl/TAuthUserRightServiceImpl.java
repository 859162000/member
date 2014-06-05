package com.wanda.ccs.auth.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.auth.dao.ITAuthUserRightDao;
import com.wanda.ccs.auth.service.TAuthUserRightService;
import com.wanda.ccs.model.TAuthUserRight;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TAuthUserRightServiceImpl extends BaseCrudServiceImpl<TAuthUserRight> implements
		TAuthUserRightService {

	@Autowired
	private ITAuthUserRightDao dao = null;

	@Override
	public IBaseDao<TAuthUserRight> getDao() {
		return dao;
	}
	
	/** 用户权限列表 */
	@Override
	public PageResult<TAuthUserRight> findByCriteria(QueryCriteria<String, Object> query) {
		
		String fromClause = " select c from TAuthUserRight c";
		String[] whereBodies = new String[] {};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			 orderClause = "";
		}
		PageResult<TAuthUserRight> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;
	}
}
