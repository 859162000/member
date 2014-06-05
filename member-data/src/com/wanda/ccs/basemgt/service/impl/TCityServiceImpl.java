package com.wanda.ccs.basemgt.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.basemgt.dao.ITCityDao;
import com.wanda.ccs.basemgt.service.TCityService;
import com.wanda.ccs.model.TCity;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.StringUtil;

@Service
public class TCityServiceImpl extends BaseCrudServiceImpl<TCity> implements
		TCityService {

	@Autowired
	private ITCityDao dao = null;

	@Override
	public IBaseDao<TCity> getDao() {
		return dao;
	}

	@Override
	public List<TCity> findUnDeletedCity() {
		TCity city = new TCity();
		city.setIsdelete((long) 0);
		return getDao().findExample(city);
	}

	@Override
	public List<TCity> findByProvinceId(Long provinceId) {
		String hql = " select c from TCity c  ";
		Map<String, Object> params = new HashMap<String, Object>();

		if (!StringUtil.isNullOrBlank(String.valueOf(provinceId))) {
			hql += " where c.tProvince.id =:provinceId and c.isdelete =:isdelete";
			params.put("provinceId", provinceId);
			params.put("isdelete", 0L);
		}

		return getDao().queryHql(hql, params);
	}

	@Override
	public List<TCity> findByArea(String area) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TCity c";
		if (!StringUtil.isNullOrBlank(area)) {
			hql += " where c.area =:area and c.isdelete =:isdelete";
			params.put("area", area);
			params.put("isdelete", 0L);
		}
		return super.queryHql(hql, params);
	}

	@Override
	public List<TCity> findByNames(String[] names) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TCity c";
		if (names != null && names.length > 0) {
			hql += " where c.isdelete =:isdelete and c.name in :names ";
			params.put("names", names);
			params.put("isdelete", 0L);
		}
		return super.queryHql(hql, params);
	}

}
