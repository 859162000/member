package com.wanda.ccs.film.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.film.dao.ITFcAttachDao;
import com.wanda.ccs.film.service.TFcAttachService;
import com.wanda.ccs.model.TFcAttach;
import com.wanda.ccs.model.TFilm;
import com.wanda.ccs.model.TFilmContract;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.StringUtil;

@Service
public class TFcAttachServiceImpl extends
		BaseCrudServiceImpl<TFcAttach> implements TFcAttachService {

	@Autowired
	private ITFcAttachDao dao = null;
	
	@Override
	public IBaseDao<TFcAttach> getDao() {
		return dao;
	}

	@Override
	public PageResult<TFcAttach> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = " select c from TFcAttach c";
		String[] whereBodies = new String[] { "c.isdelete=0"
				};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.contractNo,c.contractNo";
		}
		PageResult<TFcAttach> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;
	}

	@Override
	public boolean checkExisted(Long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TFilmContract c where ";
		int count = super.queryCountHql(hql, params, null);
		return count > 0;
	}

}
