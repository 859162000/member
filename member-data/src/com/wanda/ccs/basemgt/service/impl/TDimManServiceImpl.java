package com.wanda.ccs.basemgt.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.basemgt.dao.ITDimManDao;
import com.wanda.ccs.basemgt.service.TDimManService;
import com.wanda.ccs.model.TDimDef;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.DateUtil;
import com.xcesys.extras.core.util.StringUtil;

@Service
public class TDimManServiceImpl extends BaseCrudServiceImpl<TDimDef>
		implements TDimManService {

	@Autowired
	private ITDimManDao dao = null;

	@Override
	public IBaseDao<TDimDef> getDao() {
		return dao;
	}

	@Override
	public PageResult<TDimDef> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = " select c from TDimDef c ";
		String[] whereBodies = new String[] {

		"c.name like :name" ,"c.code =:code","c.isdelete =:isDelete" ,"c.typeId =:typeId"};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.code,c.name";
		}
		PageResult<TDimDef> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;
	}

	@Override
	public List<TDimDef> findUnDeletedDim() {
		TDimDef dimDef = new TDimDef();
		dimDef.setIsdelete((long) 0);
		return getDao().findExample(dimDef);
	}


	@Override
	public boolean checkExistedByName(String name,Long typeId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TDimDef c where ";
		if (!StringUtil.isNullOrBlank(name)) {
			hql += " c.name=:name and c.typeId =:typeId and c.isdelete = 0";
			params.put("name", name);
			params.put("typeId", typeId);
		} else {
			return false;
		}
		int count = super.queryCountHql(hql, params, null);
		return count > 0;
	}

	@Override
	public boolean checkExistedByCode(String code,Long typeId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TDimDef c where ";
		if (!StringUtil.isNullOrBlank(code)) {
			hql += " c.code=:code and c.typeId =:typeId and c.isdelete = 0";
			params.put("code", code);
			params.put("typeId", typeId);
		} else {
			return false;
		}
		int count = super.queryCountHql(hql, params, null);
		return count > 0;
	}

	@Override
	public void deleteDims(Long[] ids) {
		for(int i=0;i<ids.length;i++){
			Long id = ids[i];
			TDimDef dim = getDao().findById(id);
			dim.setIsdelete(1L);
			dim.setUpdatetime(DateUtil.getCurrentDate());
			getDao().update(dim);
		}
	}

	@Override
	public List<TDimDef> findByTypeId(Long typeId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TDimDef c where typeId = :typeId and isdelete = 0 order by code asc";
		params.put("typeId", typeId);
		
		List<TDimDef> list = super.queryHql(hql, params);
		Collections.sort(list, new Comparator<TDimDef>(){
			@Override
			public int compare(TDimDef o1, TDimDef o2) {
				Integer code1 = Integer.parseInt(o1.getCode());
				Integer code2 = Integer.parseInt(o2.getCode());
				return code1.compareTo(code2);
			}
		});
		return list;
	}

	@Override
	public void createOrUpdate(TDimDef dim) {
		if(dim.isNuw()){
			String hql = "from TDimDef c where c.code=:code and c.typeId =:typeId";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", dim.getCode());
			map.put("typeId", dim.getTypeId());
			TDimDef d = getDao().queryHqlTopOne(hql, map);
			if(d != null){
				d.setIsdelete(0L);
				d.setName(dim.getName());
				d.setUpdatetime(DateUtil.getCurrentDate());
				super.update(d);
			}else{
				super.create(dim);
			}
		}else{
			super.update(dim);
		}
	}
}
