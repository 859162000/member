package com.wanda.ccs.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.auth.dao.ITAuthUserAccessDao;
import com.wanda.ccs.auth.service.TAuthUserAccessService;
import com.wanda.ccs.model.TAuthUserAccess;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TAuthUserAccessServiceImpl extends BaseCrudServiceImpl<TAuthUserAccess>
		implements TAuthUserAccessService {

	@Autowired
	private ITAuthUserAccessDao dao = null;

	@Override
	public IBaseDao<TAuthUserAccess> getDao() {
		return dao;
	}

	/** 日志信息列表 */
	@Override
	public PageResult<TAuthUserAccess> findByCriteria(
			QueryCriteria<String, Object> query) {

		String fromClause = " select c from TAuthUserAccess c ";
		String[] whereBodies = new String[] {
				"c.userName like :userName",
				"c.requestTime >= to_date(:start,'yyyy-mm-dd')",
				"c.requestTime < to_date(:end,'yyyy-mm-dd') + 1",
				"c.requestUri like :requestUri"
		};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "";
		}
		PageResult<TAuthUserAccess> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;
	}

}
