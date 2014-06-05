package com.wanda.ccs.basemgt.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.basemgt.dao.ITSettingDao;
import com.wanda.ccs.basemgt.service.TSettingService;
import com.wanda.ccs.model.TSetting;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.StringUtil;

@Service
public class TSettingServiceImpl extends BaseCrudServiceImpl<TSetting>
		implements TSettingService {

	@Autowired
	private ITSettingDao dao = null;

	@Override
	public IBaseDao<TSetting> getDao() {
		return dao;
	}

	@Override
	public PageResult<TSetting> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = " select c from TSetting c";
		String[] whereBodies = new String[] {

		"c.name like :name", "c.valueType = :valueType" };
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.name";
		}
		PageResult<TSetting> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;
	}

	@Override
	public Map<String, List<String>> getAllTSettings() {
		List<TSetting> tSetting = super.findAll();
		Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();

		if (tSetting.size() > 0 && tSetting != null) {
			for (TSetting setting : tSetting) {
				List<String> list = new ArrayList<String>();
				String name = setting.getName();
				String[] values = setting.getValue().split(",");
				for (int i = 0; i < values.length; i++) {
					list.add(values[i]);
				}
				map.put(name, list);
			}
		}
		return map;
	}

	@Override
	public TSetting getByName(String name) {
		String fromClause = "from TSetting c where c.name = :name";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("name", name);
		List<TSetting> lst = getDao().queryHql(fromClause, param);
		if (lst.size() > 0) {
			return lst.get(0);
		}
		return null;
	}

	@Override
	public boolean checkExisted(String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TSetting c where ";
		if (!StringUtil.isNullOrBlank(name)) {
			hql += " c.name=:name";
			params.put("name", name);
		} else {
			return false;
		}
		int count = super.queryCountHql(hql, params, null);
		return count > 0;
	}

}
