package com.wanda.ccs.basemgt.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.basemgt.dao.ITPublisherDao;
import com.wanda.ccs.basemgt.service.TPublisherService;
import com.wanda.ccs.model.TPublisher;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.dao.util.QLUtil;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.StringUtil;

@Service
public class TPublisherServiceImpl extends BaseCrudServiceImpl<TPublisher>
		implements TPublisherService {

	@Autowired
	private ITPublisherDao dao = null;

	@Override
	public IBaseDao<TPublisher> getDao() {
		return dao;
	}

	@Override
	public List<TPublisher> findUnDeletedPublishers() {
		TPublisher publisher = new TPublisher();
//		publisher.setIsdelete((long) 0);
		publisher.setDeleted(Boolean.FALSE);
		return getDao().findExample(publisher);
	}

	@Override
	public List<TPublisher> findByName(String like) {
		if (!StringUtil.isNullOrBlank(like)) {
			like = like.toLowerCase();
		}
		String hql = "from TPublisher where (upper(pincode) like upper(:like) or upper(publishername) like upper(:like)) and deleted = :deleted order by publishername";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("like", "%" + like + "%");
		params.put("deleted", Boolean.FALSE);
		PageResult<TPublisher> result = super.queryHql(hql,
				QLUtil.generateCountQL(hql, null), params, 0, 100);
		return result.getContents();
	}
	
	@Override
	public PageResult<TPublisher> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = "from TPublisher p";
		String[] whereBodies = new String[] { 
				"upper(p.shortname) like upper(:publishername)",
				"upper(p.pincode) like upper(:pincode)", 
				"upper(p.publishercode) like upper(:publishercode)",
				"p.deleted = :deleted"};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "p.pincode";
		}
		query.put("deleted", Boolean.FALSE);
		
		PageResult<TPublisher> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;
	}

}
