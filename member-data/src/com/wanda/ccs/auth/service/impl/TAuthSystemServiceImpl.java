package com.wanda.ccs.auth.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.auth.dao.ITAuthSystemDao;
import com.wanda.ccs.auth.service.TAuthSystemService;
import com.wanda.ccs.model.TAuthSystem;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TAuthSystemServiceImpl extends BaseCrudServiceImpl<TAuthSystem> implements
		TAuthSystemService {

	@Autowired
	private ITAuthSystemDao dao = null;

	@Override
	public IBaseDao<TAuthSystem> getDao() {
		return dao;
	}
	
	@Override
	public PageResult<TAuthSystem> findByCriteria(QueryCriteria<String, Object> query) {
		
		String fromClause = " select c from TAuthSystem c";
		String[] whereBodies = new String[] {};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			 orderClause = "";
		}
		PageResult<TAuthSystem> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;
	}

	@Override
	public TAuthSystem getSystemBySystemId(String systemId) {
		if(systemId == null || systemId.equals(""))
			return null;
		String hql = "select c from TAuthSystem c where c.systemId = :systemId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("systemId", systemId);
		List<TAuthSystem> systemList = dao.queryHql(hql, params);
		if(systemList != null && !systemList.isEmpty()){
			TAuthSystem system = systemList.get(0);
			return system;
		}
		return null;
	}

	@Override
	public boolean checkSystemId(Long id,String systemId) {
		if(systemId == null || systemId.equals(""))
			return false;
		String hql = "select c from TAuthSystem c where c.systemId = :systemId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("systemId", systemId);
		if(id != null && id != 0L){
			hql += " and c.id != :id";
			params.put("id", id);
		}
		if(super.queryCountHql(hql, params, null)>0){
			return true;
		}
		return false;
	}

}
