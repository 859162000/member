package com.wanda.ccs.basemgt.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.basemgt.dao.ITCinemaContactDao;
import com.wanda.ccs.basemgt.service.TCinemaContactService;
import com.wanda.ccs.model.TCinemaContact;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.StringUtil;

@Service
public class TCinemaContactServiceImpl extends BaseCrudServiceImpl<TCinemaContact>
		implements TCinemaContactService {

	@Autowired
	private ITCinemaContactDao dao = null;

	@Override
	public IBaseDao<TCinemaContact> getDao() {
		return dao;
	}
	@Override
	public PageResult<TCinemaContact> findByCriteria(QueryCriteria<String, Object> query) {
		String fromClause = " select c from TCinemaContact c  where c.cinemaId = :id";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.name";
		}
		PageResult<TCinemaContact> result = getDao().queryQueryCriteria(fromClause,
				null, orderClause, null, query);
		return result;
	}

	@Override
	public boolean checkExisted(String code,String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TCinemaContact c where ";
		if (!StringUtil.isNullOrBlank(code)) {
			hql += " c.code=:code";
			params.put("code", code);
		} else {
			return false;
		}
		int count = super.queryCountHql(hql, params, null);
		return false;
	}
	
	@Override
	public Map<Long, String> findUnDeletedCinemas() {
//		Map<Long, String> map = new HashMap<Long, String>();
//
//		String fromClause = " select c from TCinemaContact c where c.isdelete = :isdelete";
//
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("isdelete", 0l);
//		List<TCinemaContact> lst = getDao().queryHql(fromClause, params);
//
//		for (TCinemaContact gh : lst) {
//			map.put(gh.getId(), gh.getName());
//		}
		return null;
	}
	@Override
	public List<TCinemaContact> findCinemaContacts(Long cinemaId) {
		String fromClause = " select c from TCinemaContact c where c.cinemaId = :cinemaId ";
		
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("cinemaId", cinemaId);
				List<TCinemaContact> lst = getDao().queryHql(fromClause, params);
		return lst;
	
	}
	
}
