package com.wanda.ccs.auth.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.auth.UserLevel;
import com.wanda.ccs.auth.dao.ITAuthOrgAssignDao;
import com.wanda.ccs.auth.service.TAuthOrgAssignService;
import com.wanda.ccs.model.TAuthOrgAssign;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TAuthOrgAssignServiceImpl extends BaseCrudServiceImpl<TAuthOrgAssign> implements
		TAuthOrgAssignService {

	@Autowired
	private ITAuthOrgAssignDao dao = null;

	@Override
	public IBaseDao<TAuthOrgAssign> getDao() {
		return dao;
	}
	
	@Override
	public PageResult<TAuthOrgAssign> findByCriteria(QueryCriteria<String, Object> query) {
		
		String fromClause = " select c from TAuthOrgAssign c";
		String[] whereBodies = new String[] {};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			 orderClause = "";
		}
		PageResult<TAuthOrgAssign> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;
	}
	
	@Override
	public TAuthOrgAssign getGroupOrgAssign() {
		List<TAuthOrgAssign> defs =  super.queryHql("from TAuthOrgAssign c where c.region is null and c.cinemaId is null",
				null);
		if(defs != null && !defs.isEmpty())
			return defs.get(0);
		return null;
	}
	
	@Override
	public Map<String, TAuthOrgAssign> getRegionOrgAssign() {
		Map<String,TAuthOrgAssign> map = new HashMap<String, TAuthOrgAssign>();

		List<TAuthOrgAssign> defs = super.queryHql("from TAuthOrgAssign c where c.region is not null and c.cinemaId is null",
				null);
		if (defs != null && !defs.isEmpty()) {
			for (TAuthOrgAssign def : defs) {
				map.put(def.getRegion(), def);
			}
		}
		return map;
	}
	
	
	@Override
	public Map<String, Map<String, TAuthOrgAssign>> getCinemaOrgAssign() {
		Map<String, Map<String, TAuthOrgAssign>> maps = new LinkedHashMap<String, Map<String, TAuthOrgAssign>>();

		List<TAuthOrgAssign> defs = (List<TAuthOrgAssign>) super.queryHql("from TAuthOrgAssign c where c.region is not null and c.cinemaId is not null",
				null);
		if (defs != null && !defs.isEmpty()) {
			for (TAuthOrgAssign def : defs) {
				String regionCode = def.getRegion();
				Map<String, TAuthOrgAssign> map = maps.get(regionCode);
				if (map == null) {
					map = new LinkedHashMap<String, TAuthOrgAssign>();
					maps.put(regionCode, map);
				}
				map.put(def.getCinemaId()+"", def);
			}
		}
		return maps;
	}
	
	/**
	 * 获取用户在组织结构中的位置
	 * @param user
	 * @return
	 */
	@Override
	public TAuthOrgAssign getOrgAssignByUser(CcsUserProfile user) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TAuthOrgAssign c where 1=1 ";
		if(user.getLevel() == UserLevel.REGION && user.getRegionCode() != null && !user.getRegionCode().equals("")){
			params.put("region", user.getRegionCode());
			hql += " and c.region = :region ";
		}else{
			hql += " and c.region is null ";
		}
		if(user.getLevel() == UserLevel.CINEMA && user.getCinemaId() != 0 ){
			params.put("cinemaId", user.getCinemaId());
			hql += " and c.cinemaId = :cinemaId";
		}else{
			hql += " and c.cinemaId is null ";
		}
		List<TAuthOrgAssign> list = super.queryHql(hql, params);
		if(list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}
	
	/**
	 * 获取用户在组织结构中的位置
	 * @param user
	 * @return
	 */
	@Override
	public TAuthOrgAssign getOrgAssignByRegion(String region) {
		if(region == null || region.equals(""))
			return null;
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TAuthOrgAssign c where c.region = :region and c.cinemaId is null";
		params.put("region", region);
		List<TAuthOrgAssign> list = super.queryHql(hql, params);
		if(list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}
	
	/**
	 * 获取用户在组织结构中的位置
	 * @param user
	 * @return
	 */
	@Override
	public TAuthOrgAssign getOrgAssignByCinema(Long cinemaId) {
		if(cinemaId == 0L)
			return null;
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TAuthOrgAssign c where c.cinemaId = :cinemaId ";
		params.put("cinemaId", cinemaId);
		List<TAuthOrgAssign> list = super.queryHql(hql, params);
		if(list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}

	@Override
	public TAuthOrgAssign getOrgAssignByCorp(String pkCorp) {
		if(pkCorp == null || pkCorp.equals(""))
			return null;
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TAuthOrgAssign c where c.pkCorp = :pkCorp";
		params.put("pkCorp", pkCorp);
		List<TAuthOrgAssign> list = super.queryHql(hql, params);
		if(list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}
	
}
