package com.wanda.ccs.basemgt.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.basemgt.dao.ITDimTypeDefDao;
import com.wanda.ccs.basemgt.service.TDimTypeDefService;
import com.wanda.ccs.model.TDimDef;
import com.wanda.ccs.model.TDimTypeDef;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.DateUtil;

@Service
public class TDimTypeDefServiceImpl extends BaseCrudServiceImpl<TDimTypeDef>
		implements TDimTypeDefService {

	@Autowired
	private ITDimTypeDefDao dao = null;

	@Override
	public IBaseDao<TDimTypeDef> getDao() {
		return dao;
	}

	@Override
	public PageResult<TDimTypeDef> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = " select c from TDimTypeDef c";
		String[] whereBodies = new String[] { "c.deleted=false",
				"c.code like :code", "c.name like :name" };
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.code,c.name";
		}
		PageResult<TDimTypeDef> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;
	}

	@Override
	public Map<String, String> getDimDefsByTypeId(Long typeId) {
		TDimTypeDef typeDef = super.getById(typeId);
		Map<String, String> map = new LinkedHashMap<String, String>();
		if (typeDef != null) {
			Set<TDimDef> defs = typeDef.gettDimdefs();
			List<TDimDef> list = new ArrayList<TDimDef>();
			list.addAll(defs);
			// sort by code.
			Collections.sort(list);
			if (!list.isEmpty()) {
				for (TDimDef def : list) {
					if (def.getIsdelete() != null && def.getIsdelete() == 1l) {
						// 跳过逻辑删除记录
						continue;
					}
					map.put(def.getCode(), def.getName());
				}
			}
		}

		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Map<String, String>> getAllDimDefs(Boolean... cacheable) {
		Map<String, Map<String, String>> maps = new LinkedHashMap<String, Map<String, String>>();
		if(cacheable == null || cacheable.length == 0)
			super.setQueryCacheable(Boolean.TRUE);
		List<TDimDef> defs = (List<TDimDef>) getUniversalDao().queryHql(
				"from TDimDef c where c.isdelete=0 order by c.typeId, c.code ",
				null);

		Collections.sort(defs);
		if (defs != null && !defs.isEmpty()) {
			for (TDimDef def : defs) {
				String typeId = "" + def.getTypeId();
				Map<String, String> map = maps.get(typeId);
				if (map == null) {
					map = new LinkedHashMap<String, String>();
					maps.put(typeId, map);
				}
				map.put(def.getCode(), def.getName());
			}
		}
		return maps;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Map<String, String>> getAllDimDefsOrderByCode() {
		Map<String, Map<String, String>> maps = new LinkedHashMap<String, Map<String, String>>();
		super.setQueryCacheable(false);
		List<TDimDef> defs = (List<TDimDef>) getUniversalDao().queryHql(
				"from TDimDef c where c.isdelete=0 order by c.typeId, c.code ",
				null);

		if (defs != null && !defs.isEmpty()) {
			for (TDimDef def : defs) {
				String typeId = "" + def.getTypeId();
				Map<String, String> map = maps.get(typeId);
				if (map == null) {
					map = new LinkedHashMap<String, String>();
					maps.put(typeId, map);
				}
				map.put(def.getCode(), def.getName());
			}
		}
		return maps;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String getNameByTypeIdCode(Long typeid, String code) {
		String hql = "select c from TDimDef c join fetch c.tDimtypedef d where c.code = :code and d.id = :typeid";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", code);
		map.put("typeid", typeid);
		List<TDimDef> list = (List<TDimDef>) getUniversalDao().queryHql(hql,
				map);
		if (list != null)
			return list.get(0).getName();
		return null;
	}

	@Override
	public Map<Long, String> getDimDefs() {
		String fromClause = " select c from TDimTypeDef c ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deleted", Boolean.FALSE);
		List<TDimTypeDef> lst = getDao().queryHql(fromClause, params);

		Map<Long, String> dimDefMap = new HashMap<Long, String>();
		for (TDimTypeDef dimType : lst) {
			dimDefMap.put(dimType.getId(), dimType.getTypename());
		}
		return dimDefMap;
	}

	@Override
	public Object[] obtainTDimDefCountAndUpdateDate() {
		Object[] countAndUpd = new Object[2];
		countAndUpd[0] = 0L;
		countAndUpd[1] = DateUtil.getCurrentDate();
		String sql = "select count(*),max(updatetime) as updatetime from T_DIMDEF";
		Connection connection = getUniversalDao().getConnection();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet != null && resultSet.next()) {
				countAndUpd[0] = resultSet.getLong(1);
				Timestamp timestamp = resultSet.getTimestamp(2);
				if (timestamp != null)
					countAndUpd[1] = new java.util.Date(timestamp.getTime());
			}
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return countAndUpd;
	}
	
	@Override
	public Map<String, String> getDimDef() {
		String fromClause = " select c from TDimTypeDef c ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deleted", Boolean.FALSE);
		List<TDimTypeDef> lst = getDao().queryHql(fromClause, params);

		Map<String, String> dimDefMap = new HashMap<String, String>();
		for (TDimTypeDef dimType : lst) {
			dimDefMap.put(dimType.getId().toString(), dimType.getTypename());
		}
		return dimDefMap;
	}
}
