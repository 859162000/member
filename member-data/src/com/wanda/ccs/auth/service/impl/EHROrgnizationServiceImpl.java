package com.wanda.ccs.auth.service.impl;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.auth.dao.EHROrgnizationDao;
import com.wanda.ccs.auth.service.EHROrgnizationService;
import com.wanda.ccs.model.EHROrgnization;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class EHROrgnizationServiceImpl extends BaseCrudServiceImpl<EHROrgnization> 
		implements EHROrgnizationService {
	
	@Autowired
	private EHROrgnizationDao dao = null;
	
	@Override
	public IBaseDao<EHROrgnization> getDao() {
		return dao;
	}

	@SuppressWarnings("unchecked")
	public Map<String,String> getParentUnitID(String parentUnitID) {
		Map<String,Object> params = new HashMap<String, Object>();
		Map<String,  String> maps = new LinkedHashMap<String, String>();
		params.put("parentUnitId", Long.valueOf(parentUnitID));
		List<EHROrgnization> orgs = (List<EHROrgnization>) getUniversalDao().queryHql(
				"from EHROrgnization c where c.parentUnitId =:parentUnitId and c.shortName not like '%区域本部%' order by c.shortName",
				params);
		if (orgs != null && !orgs.isEmpty()) {
			for (EHROrgnization org : orgs) {
				maps.put(org.getId().toString(), org.getShortName());
			}
		}
		return maps;
	}

	@SuppressWarnings("unchecked")
	public List<EHROrgnization> getParentID(String parentUnitID) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("parentUnitId", Long.valueOf(parentUnitID));
		List<EHROrgnization> orgs = (List<EHROrgnization>) getUniversalDao().queryHql(
				"from EHROrgnization c where c.parentUnitId =:parentUnitId and c.shortName not like '%区域本部%' order by c.shortName",
				params);
		
		return orgs;
	}

	
//	@Override
//	public PageResult<EHREmployee> findByCriteria(
//			QueryCriteria<String, Object> query) {
//		Vector<String> queryParam = new Vector<String>();
//		String fromClause = "select c from EHREmployee c ";
//		queryParam.add("c.eName like:eName ");
//		String[] whereBodies = queryParam
//				.toArray(new String[queryParam.size()]);
//		String joinClause = "";
//		String orderClause = null;
//		if (query.getSort() == null || query.getSort().trim().length() == 0) {
//			orderClause = "c.unitId asc,jobName asc";
//		}
//		PageResult<EHREmployee> result = getDao().queryQueryCriteria(
//				fromClause, joinClause, orderClause, whereBodies, query);
//		return result;
//	}
	
//	@SuppressWarnings("unchecked")
//	@Override
//	public Map<String,String> getParentIDs(Long[] parentIDs) {
//		Map<String,Object> params = new HashMap<String, Object>();
//		Map<String,  String> maps = new LinkedHashMap<String, String>();
//		params.put("parentIDs", parentIDs);
//		List<EHROrgnization> orgs = (List<EHROrgnization>) getUniversalDao().queryHql(
//				"from EHROrgnization c where c.parentUnitId in(:parentIDs)",
//				params);
//		if (orgs != null && !orgs.isEmpty()) {
//			for (EHROrgnization org : orgs) {
//				maps.put(org.getId().toString(), org.getShortName());
//			}
//		}
//		return maps;
//	}
	
	@SuppressWarnings("unchecked")
	public List<EHROrgnization> getParentIDs(Long[] parentIDs) {
		Map<String,Object> params = new HashMap<String, Object>();
//		Map<String,  String> maps = new LinkedHashMap<String, String>();
		params.put("parentIDs", parentIDs);
		List<EHROrgnization> orgs = (List<EHROrgnization>) getUniversalDao().queryHql(
				"from EHROrgnization c where c.parentUnitId in(:parentIDs) and c.shortName not like '%区域本部%' order by c.shortName",
				params);
//		if (orgs != null && !orgs.isEmpty()) {
//			for (EHROrgnization org : orgs) {
//				maps.put(org.getId().toString(), org.getShortName());
//			}
//		}
		return orgs;
	}
	
}
