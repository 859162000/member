package com.wanda.ccs.auth.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.auth.dao.ITAuthRightDao;
import com.wanda.ccs.auth.service.TAuthModuleService;
import com.wanda.ccs.auth.service.TAuthRightService;
import com.wanda.ccs.model.TAuthModule;
import com.wanda.ccs.model.TAuthRight;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.dao.util.QLUtil;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.StringUtil;

@Service
public class TAuthRightServiceImpl extends BaseCrudServiceImpl<TAuthRight>
		implements TAuthRightService {

	@Autowired
	private ITAuthRightDao dao = null;
	@Autowired
	private TAuthModuleService moduleSvc;

	@Override
	public IBaseDao<TAuthRight> getDao() {
		return dao;
	}

	/** 可授予用户的权限的列表 */
	@Override
	public PageResult<TAuthRight> findByCriteria(
			QueryCriteria<String, Object> query) {

		String fromClause = " select c from TAuthRight c";
		String[] whereBodies = new String[] { 
				"c.forGroup = :forGroup",
				"c.forRegion = :forRegion",
				"c.forCinema = :forCinema",
				"c.authModuleId = :moduleId",
				"c.authSystemId = :systemId"};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.tAuthModule";
		}
		PageResult<TAuthRight> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;
	}

	/** 可授予用户的权限的列表 */
	@Override
	public PageResult<TAuthRight> findRightByCriteria(
			QueryCriteria<String, Object> query) {

		String hql = "select c from TAuthRight c where 1 != 1 ";
		if (!StringUtil.isNullOrBlank((String) query.get("forGroup"))
				&& ((String) query.get("forGroup")).equals("Y")) {
			hql += " or c.forGroup = :forGroup";
		}
		if (!StringUtil.isNullOrBlank((String) query.get("forRegion"))
				&& ((String) query.get("forRegion")).equals("Y")) {
			hql += " or c.forRegion = :forRegion";
		}
		if (!StringUtil.isNullOrBlank((String) query.get("forCinema"))
				&& ((String) query.get("forCinema")).equals("Y")) {
			hql += " or c.forCinema = :forCinema";
		}
		hql += " order by c.authSystemId";
		PageResult<TAuthRight> result = super.queryHql(hql,
				QLUtil.generateCountQL(hql, null), query,
				query.getStartIndex(), query.getPageSize());
		return result;
	}

	@Override
	public Map<Long, List<TAuthRight>> getRightsByRight(TAuthRight right) {
		Map<Long, List<TAuthRight>> maps = new LinkedHashMap<Long, List<TAuthRight>>();
		Map<String, Object> params = new HashMap<String, Object>();
		List<TAuthModule> authModuleList = moduleSvc.findAll();
		for (TAuthModule module : authModuleList) {
			String hql = "select c from TAuthRight c where 1 = 1 ";
			hql += " and c.tAuthModule = :module ";
			params.put("module", module);
			if (right.getForGroup() != null && !right.getForGroup().equals("")) {
				hql += " and c.forGroup = :forGroup ";
				params.put("forGroup", right.getForGroup());
			}
			if (right.getForRegion() != null
					&& !right.getForRegion().equals("")) {
				hql += " and c.forRegion = :forRegion ";
				params.put("forRegion", right.getForRegion());
			}
			if (right.getForCinema() != null
					&& !right.getForCinema().equals("")) {
				hql += " and c.forCinema = :forCinema";
				params.put("forCinema", right.getForCinema());
			}
			List<TAuthRight> authRightList = super.queryHql(hql, params);
			maps.put(module.getId(), authRightList);

		}
		return maps;
	}

	@Override
	public TAuthRight getRightByCode(String rightCode) {
		if(rightCode == null || rightCode.equals(""))
			return null;
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select c from TAuthRight c where c.rightCode = :rightCode ";
		params.put("rightCode", rightCode);
		List<TAuthRight> rightList = dao.queryHql(hql, params);
		if(rightList != null && !rightList.isEmpty())
			return rightList.get(0);
		return null;
	}

	@Override
	public boolean checkRightCode(Long id, String rightCode) {
		if(rightCode == null || rightCode.equals(""))
			return false;
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select c from TAuthRight c where c.rightCode = :rightCode ";
		params.put("rightCode", rightCode);
		if(id != null && id != 0L){
			hql += " and c.id != id";
			params.put("id", id);
		}
		if(super.queryCountHql(hql, params, null) > 0)
			return true;
		return false;
	}

}
