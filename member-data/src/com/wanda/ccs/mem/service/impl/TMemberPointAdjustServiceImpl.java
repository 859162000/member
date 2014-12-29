package com.wanda.ccs.mem.service.impl;

import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.mem.dao.ITMemberPointAdjustDao;
import com.wanda.ccs.mem.service.TMemberPointAdjustService;
import com.wanda.ccs.model.TMemberPointAdjust;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TMemberPointAdjustServiceImpl extends  BaseCrudServiceImpl<TMemberPointAdjust> implements TMemberPointAdjustService{
	
	@Autowired
	private ITMemberPointAdjustDao dao;

	public IBaseDao<TMemberPointAdjust> getDao() {
		return dao;
	}
	
	public PageResult<TMemberPointAdjust> findByCriteria(
			QueryCriteria<String, Object> query) {
		Vector<String> queryParam = new Vector<String>();
		String hql = " select c from TMemberPointAdjust c ";
		queryParam.add("c.tMember.mobile = :mobile");
		queryParam.add("c.tMember.memberNo = :memberNo");
		queryParam.add("c.approve = :approve");
		String[] whereBodies = queryParam.toArray(new String[queryParam.size()]);
		String joinClause = "";
		String orderClause = "";
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = " c.createdDate desc";
		}
		PageResult<TMemberPointAdjust> result = getDao().queryQueryCriteria(hql,
				joinClause, orderClause, whereBodies, query);
		return result;
	}

}
