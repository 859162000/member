package com.wanda.ccs.mem.service.impl;

import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.mem.dao.ITMemTicketTransOrderDao;
import com.wanda.ccs.mem.service.TMemTicketTransOrderService;
import com.wanda.ccs.model.TTicketTransOrder;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TMemTicketTransOrderServiceImpl extends BaseCrudServiceImpl<TTicketTransOrder>
		implements TMemTicketTransOrderService {

	@Autowired
	private ITMemTicketTransOrderDao dao;

	@Override
	public IBaseDao<TTicketTransOrder> getDao() {
		return dao;
	}

	@Override
	public PageResult<TTicketTransOrder> findByCriteria(
			QueryCriteria<String, Object> query) {
		query.setSort(null);
		if(query.get("pages")!=null){
			
//			String pa = (String)query.get("pages");
			query.setPage(Integer.parseInt(String.valueOf(query.get("pages"))));
		}
		Vector<String> queryParam = new Vector<String>();
		String fromClause = " from TTicketTransOrder c ";
		queryParam.add("c.memberId = :transByMemberId ");
		String[] whereBodies = queryParam
				.toArray(new String[queryParam.size()]);
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = " c.transTime desc , c.orderId  ";
		}
		PageResult<TTicketTransOrder> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;
	}
	
}
