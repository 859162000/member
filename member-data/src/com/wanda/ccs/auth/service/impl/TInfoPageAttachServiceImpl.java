package com.wanda.ccs.auth.service.impl;

import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.auth.dao.ITInfoPageAttachDao;
import com.wanda.ccs.auth.service.TInfoPageAttachService;
import com.wanda.ccs.model.TInfoPageAttach;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TInfoPageAttachServiceImpl extends BaseCrudServiceImpl<TInfoPageAttach>
		implements TInfoPageAttachService {

	@Autowired
	private ITInfoPageAttachDao dao = null;

	@Override
	public IBaseDao<TInfoPageAttach> getDao() {
		return dao;
	}

	@Override
	public PageResult<TInfoPageAttach> findByCriteria(
			QueryCriteria<String, Object> query) {
		Vector<String> queryParam = new Vector<String>();

		String fromClause = "select c from TInfoPageAttach c ";
		String joinClause = "";
		String[] whereBodies = queryParam
				.toArray(new String[queryParam.size()]);
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "";
		}
		PageResult<TInfoPageAttach> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;
	}


}
