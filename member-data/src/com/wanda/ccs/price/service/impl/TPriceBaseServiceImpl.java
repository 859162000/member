package com.wanda.ccs.price.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.model.TPriceBase;
import com.wanda.ccs.price.dao.ITPriceBaseDao;
import com.wanda.ccs.price.service.TPriceBaseService;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.StringUtil;

@Service
public class TPriceBaseServiceImpl extends BaseCrudServiceImpl<TPriceBase>
		implements TPriceBaseService {

	@Autowired
	private ITPriceBaseDao dao = null;

	@Override
	public IBaseDao<TPriceBase> getDao() {
		return dao;
	}

	@Override
	public PageResult<TPriceBase> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = " select c from TPriceBase c";
		String[] whereBodies = new String[] { "c.code like :code",
				"c.name like :name" };
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.code,c.name";
		}
		PageResult<TPriceBase> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;
	}

	@Override
	public boolean checkExisted(String code, String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TPriceBase c where ";
		if (!StringUtil.isNullOrBlank(code)) {
			hql += " c.code=:code";
			params.put("code", code);
		} else if (!StringUtil.isNullOrBlank(name)) {
			hql += " c.name=:name ";
			params.put("name", name);
		} else {
			return false;
		}
		int count = super.queryCountHql(hql, params, null);
		return count > 0;
	}

	@Override
	public List<TPriceBase> getPriceBaseList() {
		// TPriceBase price = new TPriceBase();
		return getDao().findAll();
	}

	/**
	 * 获取最接近指定价格的的基础价格
	 * 
	 * @param price
	 * @return
	 */
	public TPriceBase getClosestPriceBase(int price) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("price", price);

		List<TPriceBase> lst = getDao()
				.queryHql(
						" select c from TPriceBase c where c.price >= :price order by c.price asc",
						params);
		if (lst.size() > 0)
			return lst.get(0);

		lst = getDao()
				.queryHql(
						" select c from TPriceBase c where c.price <= :price order by c.price desc",
						params);
		if (lst.size() > 0)
			return lst.get(0);

		return null;
	}

	/**
	 * 获得最大的code用于新增
	 */
	public String getMaxCodeThatBeCreated() {
		String mCode = "1";
		String hql = "from TPriceBase order by code desc";
		List<TPriceBase> list = getDao().queryHql(hql, null);
		List<Integer> listIte = new ArrayList<Integer>();
		if (list != null && !list.isEmpty()) {
			for (TPriceBase o : list) {
				Integer ite = NumberUtils.toInt(o.getCode());
				listIte.add(ite);
			}
			Collections.sort(listIte);
			mCode = "" + (listIte.get(listIte.size() - 1) + 1);
		}
		mCode = StringUtils.leftPad(mCode, 5, '0');
		return mCode;
	}

}
