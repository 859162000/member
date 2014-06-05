package com.wanda.ccs.auth.service.impl;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.auth.dao.ITAuthRgroupRightDao;
import com.wanda.ccs.auth.service.TAuthRgroupRightService;
import com.wanda.ccs.auth.service.TAuthRgroupService;
import com.wanda.ccs.auth.service.TAuthRightService;
import com.wanda.ccs.model.TAuthRgroupRight;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TAuthRgroupRightServiceImpl extends
		BaseCrudServiceImpl<TAuthRgroupRight> implements
		TAuthRgroupRightService {

	@Autowired
	private ITAuthRgroupRightDao dao = null;

	/** 可授予用户权限service */
	@Autowired
	TAuthRightService rightService;

	/** 权限组service */
	@Autowired
	TAuthRgroupService rgroupService;

	@Override
	public IBaseDao<TAuthRgroupRight> getDao() {
		return dao;
	}

	/** 权限组权限的列表 */
	@Override
	public PageResult<TAuthRgroupRight> findByCriteria(
			QueryCriteria<String, Object> query) {

		String fromClause = " select c from TAuthRgroupRight c";
		String[] whereBodies = new String[] {};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "";
		}
		PageResult<TAuthRgroupRight> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;
	}

	@Override
	public void saveRgroupRight(Long groupId, Long[] rightIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "delete  from TAuthRgroupRight c where c.tAuthRgroup.id = :groupId";
		params.put("groupId", groupId);
		getDao().updateHql(hql, params);
		if (rightIds != null)
			for (Long rightId : rightIds) {
				TAuthRgroupRight rgroup = new TAuthRgroupRight();
				rgroup.settAuthRgroup(rgroupService.getById(groupId));
				rgroup.settAuthRight(rightService.getById(rightId));
				super.create(rgroup);
			}
	}
}
