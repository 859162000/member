package com.wanda.ccs.basemgt.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.basemgt.dao.ITProvinceDao;
import com.wanda.ccs.basemgt.service.TProvinceService;
import com.wanda.ccs.model.TCinema;
import com.wanda.ccs.model.TProvince;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.StringUtil;

@Service
public class TProvinceServiceImpl extends BaseCrudServiceImpl<TProvince>
		implements TProvinceService {

	@Autowired
	private ITProvinceDao dao = null;

	@Override
	public IBaseDao<TProvince> getDao() {
		return dao;
	}

	@Override
	public PageResult<TProvince> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = " select c from TProvince c ";
		String[] whereBodies = new String[] {

		"c.name like :name", "c.area = :area" };
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.code,c.name";
		}
		PageResult<TProvince> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;
	}

	@Override
	public List<TProvince> findUnDeletedProvince() {
		TProvince province = new TProvince();
		province.setIsdelete((long) 0);
		return getDao().findExample(province);
	}

	@Override
	public List<TProvince> findByArea(String areaCode) {
		String hql = " select c from TProvince c  ";
		Map<String, Object> params = new HashMap<String, Object>();

		if (!StringUtil.isNullOrBlank(areaCode)) {
			hql += " where c.area =:areaCode and c.isdelete =:isdelete";
			params.put("areaCode", areaCode);
			params.put("isdelete", 0L);
		}

		return getDao().queryHql(hql, params);
	}

	@Override
	public boolean checkExistedByName(String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TProvince c where ";
		if (!StringUtil.isNullOrBlank(name)) {
			hql += " c.name=:name";
			params.put("name", name);
		} else {
			return false;
		}
		int count = super.queryCountHql(hql, params, null);
		return count > 0;
	}

	@Override
	public boolean checkExistedByCode(String code) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TProvince c where ";
		if (!StringUtil.isNullOrBlank(code)) {
			hql += " c.code=:code";
			params.put("code", code);
		} else {
			return false;
		}
		int count = super.queryCountHql(hql, params, null);
		return count > 0;
	}

}
