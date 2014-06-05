package com.wanda.ccs.mem.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.mem.dao.ITExtPointCriteriaDao;
import com.wanda.ccs.mem.service.TExtPointCriteriaService;
import com.wanda.ccs.model.TExtPointCriteria;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TExtPointCriteriaServiceImpl extends BaseCrudServiceImpl<TExtPointCriteria>
		implements TExtPointCriteriaService {

	@Autowired
	private ITExtPointCriteriaDao dao = null;

	@Override
	public IBaseDao<TExtPointCriteria> getDao() {
		return dao;
	}

	@Override
	public PageResult<TExtPointCriteria> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = "from TExtPointCriteria c";
		String[] whereBodies = new String[] {
				"c.name like :name",
				"c.code like :code"
		};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.id desc";
		}

		PageResult<TExtPointCriteria> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;

	}

	@Override
	public String getCriteriaScheme(long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		List<Map<String, ?>> result = getDao().queryNativeSQL("select t.criteria_scheme from T_EXT_POINT_CRITERIA t where t.ext_point_criteria_id = :id", params);
		if(result != null && result.size() > 0) {
			Map<String, ?> map = result.get(0);
			Object obj = map.get("CRITERIA_SCHEME");
			if(obj != null) {
				return parseType(obj.toString());
			}
		}
		
		return null;
	}
	
	private String parseType(String scheme) {
		int type = 1;
		
		if(scheme.indexOf("tsale") > 0 || scheme.indexOf("conSale") > 0) {
			type = 2;
		}
		/*else if(scheme.indexOf("memberLevel") > 0) {
			type = 1;
		}*/
		
		return String.valueOf(type);
	}
}
