package com.wanda.ccs.auth.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.auth.dao.ITAuthModuleDao;
import com.wanda.ccs.auth.service.TAuthModuleService;
import com.wanda.ccs.auth.service.TAuthSystemService;
import com.wanda.ccs.model.TAuthModule;
import com.wanda.ccs.model.TAuthSystem;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TAuthModuleServiceImpl extends BaseCrudServiceImpl<TAuthModule> implements
		TAuthModuleService {

	@Autowired
	private ITAuthModuleDao dao = null;
	@Autowired
	private TAuthSystemService systemSvc;

	@Override
	public IBaseDao<TAuthModule> getDao() {
		return dao;
	}
	
	@Override
	public PageResult<TAuthModule> findByCriteria(QueryCriteria<String, Object> query) {
		
		String fromClause = " select c from TAuthModule c";
		String[] whereBodies = new String[] {
				"c.authSystemId = :systemId"
		};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			 orderClause = "";
		}
		PageResult<TAuthModule> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;
	}

	@Override
	public List<TAuthModule> findAll(){
		String hql = " select c from TAuthModule c order by c.authSystemId";
		return dao.queryHql(hql, null);
	}

	@Override
	public Map<Long, Map<Long, String>> getSystemModule() {
		Map<Long, Map<Long, String>> map = new HashMap<Long, Map<Long,String>>();
		List<TAuthSystem> list = systemSvc.findAll();
		String hql = "select c from TAuthModule c where 1=1";
		hql += " and c.authSystemId = :systemId";
		Map<String, Object> params = new HashMap<String, Object>();
		if(list != null && !list.isEmpty()){
			for(TAuthSystem system : list){
				params.put("systemId", system.getId());
				List<TAuthModule> moduleList = dao.queryHql(hql, params);
				if(moduleList != null && !moduleList.isEmpty()){
					Map<Long, String> moduleMap = new HashMap<Long, String>();
					for(TAuthModule module : moduleList){
						moduleMap.put(module.getId(), module.getModuleName());
					}
					map.put(system.getId(), moduleMap);
				}
			}
		}
		return map;
	}

	@Override
	public List<TAuthModule> getModuleBySystemId(Long systemId) {
		String hql = "select c from TAuthModule c where c.authSystemId = :systemId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("systemId", systemId);
		List<TAuthModule> moduleList = dao.queryHql(hql, params);
		return moduleList;
	}


	
}
