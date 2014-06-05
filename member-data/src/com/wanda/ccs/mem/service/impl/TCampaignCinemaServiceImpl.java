package com.wanda.ccs.mem.service.impl;

import org.springframework.stereotype.Service;
import com.wanda.ccs.mem.service.TCampaignCinemaService;
import com.wanda.ccs.model.TCampaignCinema;
import org.springframework.beans.factory.annotation.Autowired;

import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.wanda.ccs.mem.dao.ITCampaignCinemaDao;

@Service
public class TCampaignCinemaServiceImpl extends
		BaseCrudServiceImpl<TCampaignCinema> implements TCampaignCinemaService {

	@Autowired
	private ITCampaignCinemaDao dao = null;

	@Override
	public IBaseDao<TCampaignCinema> getDao() {
		return dao;
	}

	@Override
	public PageResult<TCampaignCinema> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = "from TCampaignCinema c";
		String[] whereBodies = new String[] {};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.id desc";
		}

		PageResult<TCampaignCinema> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;

	}
}
