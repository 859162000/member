package com.wanda.ccs.mem.service.impl;

import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.mem.dao.ITMemGoodsTransOrderDao;
import com.wanda.ccs.mem.service.TMemGoodsTransOrderService;
import com.wanda.ccs.model.TGoodsTransOrder;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TMemGoodsTransOrderServiceImpl extends BaseCrudServiceImpl<TGoodsTransOrder>
		implements TMemGoodsTransOrderService {

	@Autowired
	private ITMemGoodsTransOrderDao dao;

	@Override
	public IBaseDao<TGoodsTransOrder> getDao() {
		return dao;
	}

	@Override
	public PageResult<TGoodsTransOrder> findByCriteria(
			QueryCriteria<String, Object> query) {
		query.setSort(null);
		if(query.get("pages")!=null){
			
//			String pa = (String)query.get("pages");
			query.setPage(Integer.parseInt(String.valueOf(query.get("pages"))));
		}
		Vector<String> queryParam = new Vector<String>();
		String fromClause = " from TGoodsTransOrder c ";
		queryParam.add("c.memberId = :transByMemberId ");
		String[] whereBodies = queryParam
				.toArray(new String[queryParam.size()]);
		String joinClause = "";
		String orderClause = null;
		query.setSort(null);
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = " c.transTime desc ,c.orderId ";
		}
		PageResult<TGoodsTransOrder> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;
	}
	
}
