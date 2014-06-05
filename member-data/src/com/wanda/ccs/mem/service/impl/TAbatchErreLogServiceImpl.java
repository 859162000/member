package com.wanda.ccs.mem.service.impl;

import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.mem.dao.ITAbatchErreLogDao;
import com.wanda.ccs.mem.service.TAbatchErreLogService;
import com.wanda.ccs.model.TAbatchErreLog;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TAbatchErreLogServiceImpl extends BaseCrudServiceImpl<TAbatchErreLog>
		implements TAbatchErreLogService {

	@Autowired
	private ITAbatchErreLogDao dao;

	@Override
	public IBaseDao<TAbatchErreLog> getDao() {
		return dao;
	}

	@Override
	public PageResult<TAbatchErreLog> findByCriteria(
			QueryCriteria<String, Object> query) {
		Vector<String> queryParam = new Vector<String>();
		if (query.get("pageerr") != null) {
			query.setPage(Integer.parseInt(String.valueOf(query.get("pageerr"))));
		}
		String fromClause = " from TAbatchErreLog c ";
		queryParam.add(" c.fileAttachId = :fileAttachId ");
		String[] whereBodies = queryParam
				.toArray(new String[queryParam.size()]);
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "";
		}
		PageResult<TAbatchErreLog> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;
	}
	
}
