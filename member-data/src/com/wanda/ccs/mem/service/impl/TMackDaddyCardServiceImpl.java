package com.wanda.ccs.mem.service.impl;

import org.springframework.stereotype.Service;
import com.wanda.ccs.mem.service.TMackDaddyCardService;
import com.wanda.ccs.model.TMackDaddyCard;
import org.springframework.beans.factory.annotation.Autowired;

import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.wanda.ccs.mem.dao.ITMackDaddyCardDao;

@Service
public class TMackDaddyCardServiceImpl extends
		BaseCrudServiceImpl<TMackDaddyCard> implements TMackDaddyCardService {

	@Autowired
	private ITMackDaddyCardDao dao = null;

	@Override
	public IBaseDao<TMackDaddyCard> getDao() {
		return dao;
	}

	@Override
	public PageResult<TMackDaddyCard> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = "from TMackDaddyCard c";
		String[] whereBodies = new String[] {};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.id desc";
		}

		PageResult<TMackDaddyCard> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;

	}
}
