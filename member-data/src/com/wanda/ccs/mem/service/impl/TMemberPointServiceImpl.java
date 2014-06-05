package com.wanda.ccs.mem.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.mem.dao.ITMemberPointDao;
import com.wanda.ccs.mem.service.TMemberPointService;
import com.wanda.ccs.model.TMemberPoint;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TMemberPointServiceImpl extends BaseCrudServiceImpl<TMemberPoint>
		implements TMemberPointService {

	@Autowired
	private ITMemberPointDao dao;

	@Override
	public IBaseDao<TMemberPoint> getDao() {
		return dao;
	}

	@Override
	public PageResult<TMemberPoint> findByCriteria(
			QueryCriteria<String, Object> query) {
		Vector<String> queryParam = new Vector<String>();
		String fromClause = " from TMemberPoint c ";
		queryParam.add("c.memberId = :memberId ");
		String[] whereBodies = queryParam
				.toArray(new String[queryParam.size()]);
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "";
		}
		PageResult<TMemberPoint> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;
	}

	@Override
	public TMemberPoint getTMemberPointByMemId(Long memgerId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TMemberPoint c where ";
		if (memgerId != null ) {
			hql += " c.memberId=:memberId ";
			params.put("memberId", memgerId);
		} 
		if(getDao().queryHql(hql, params).size()>0){
			return getDao().queryHql(hql, params).get(0);
		}else{
			return null;
		}
	}
	
}
