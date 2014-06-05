package com.wanda.ccs.auth.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.auth.dao.ITAuthRgroupDao;
import com.wanda.ccs.auth.service.TAuthRgroupRightService;
import com.wanda.ccs.auth.service.TAuthRgroupService;
import com.wanda.ccs.model.TAuthRgroup;
import com.wanda.ccs.model.TAuthRgroupRight;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.StringUtil;

@Service
public class TAuthRgropuServiceImpl extends BaseCrudServiceImpl<TAuthRgroup> implements
		TAuthRgroupService {

	@Autowired
	private ITAuthRgroupDao dao = null;
	@Autowired
	TAuthRgroupRightService rgroupRightSvc;
	@Override
	public IBaseDao<TAuthRgroup> getDao() {
		return dao;
	}
	
	/** 可授予用户的权限的列表 */
	@Override
	public PageResult<TAuthRgroup> findByCriteria(QueryCriteria<String, Object> query) {
		
		String fromClause = " select c from TAuthRgroup c";
		String[] whereBodies = new String[] {};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			 orderClause = "";
		}
		PageResult<TAuthRgroup> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;
	}
	
	@Override
	public boolean chackRGroupByName(String groupName){
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select c from TAuthRgroup c where 1=1 ";
		if(!StringUtil.isNullOrBlank(groupName)){
			hql += "and c.groupName = :groupName";
			params.put("groupName", groupName);
		}
		if(super.queryCountHql(hql, params, null)>0)
			return true;
		return false;
	}

	@Override
	public void saveRgroup(TAuthRgroup rgroup) {
		for(TAuthRgroupRight rgroupRight : rgroup.gettAuthRgroupRights()){
			if(rgroupRight.getDelete()){
				rgroupRightSvc.delete(rgroupRight);
			}else{
				rgroupRightSvc.createOrUpdate(rgroupRight);
			}
		}
		
	}


	
}
