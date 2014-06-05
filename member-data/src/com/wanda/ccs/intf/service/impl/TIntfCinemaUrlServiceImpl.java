package com.wanda.ccs.intf.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.intf.dao.ITIntfCinemaUrlDao;
import com.wanda.ccs.intf.service.TIntfCinemaUrlService;
import com.wanda.ccs.model.TIntfCinemaUrl;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TIntfCinemaUrlServiceImpl extends
		BaseCrudServiceImpl<TIntfCinemaUrl> implements TIntfCinemaUrlService {

	@Autowired
	private ITIntfCinemaUrlDao dao = null;
	
	@Override
	public IBaseDao<TIntfCinemaUrl> getDao() {
		return dao;
	}

	@Override
	public PageResult<TIntfCinemaUrl> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = " select c from TIntfCinemaUrl c";
		String[] whereBodies = new String[] { 
				"c.cinemacode = :cinemacode"
		};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "";
		}
		PageResult<TIntfCinemaUrl> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;
	}

	@Override
	public TIntfCinemaUrl getUrlByCinemaId(Long cinemaId) {
		if(cinemaId == null || cinemaId == 0L)
			return null;
		String hql = "from TIntfCinemaUrl c where c.cinemaid = :cinemaId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cinemaId", cinemaId);
		List<TIntfCinemaUrl> list = dao.queryHql(hql, params);
		if(list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}

}
