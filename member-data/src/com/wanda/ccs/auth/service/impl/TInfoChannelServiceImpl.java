package com.wanda.ccs.auth.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.auth.dao.ITInfoChannelDao;
import com.wanda.ccs.auth.service.TInfoChannelService;
import com.wanda.ccs.model.TInfoChannel;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TInfoChannelServiceImpl extends BaseCrudServiceImpl<TInfoChannel>
		implements TInfoChannelService {

	@Autowired
	private ITInfoChannelDao dao = null;

	@Override
	public IBaseDao<TInfoChannel> getDao() {
		return dao;
	}

	@Override
	public PageResult<TInfoChannel> findByCriteria(
			QueryCriteria<String, Object> query) {
		Vector<String> queryParam = new Vector<String>();

		String fromClause = "select c from TInfoChannel c ";
		if(query.get("channelName") != null){
			queryParam.add(" c.channelName like :channelName");
		}
			
		String joinClause = "";
		String[] whereBodies = queryParam
				.toArray(new String[queryParam.size()]);
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "";
		}
		PageResult<TInfoChannel> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;
	}

	@Override
	public int checkChannelName(String channelName) {
		String hql = " select c from TInfoChannel c where c.channelName =:channelName";
		Map<String, Object> params = new HashMap<String, Object>();
		if(!"".equals(channelName)){
			params.put("channelName", channelName);
		}
		
		return super.queryCountHql(hql, params, null);
	}

	@Override
	public int checkHomeUrl(String homeUrl) {
		String hql = " select c from TInfoChannel c where c.homeUrl =:homeUrl";
		Map<String, Object> params = new HashMap<String, Object>();
		if(!"".equals(homeUrl)){
			params.put("homeUrl", homeUrl);
		}
		return super.queryCountHql(hql, params, null);
	}

	@Override
	public List<TInfoChannel> findAllChannels() {
		String hql = " select c from TInfoChannel c order by c.channelName";
		return super.queryHql(hql, null);
	}


}
