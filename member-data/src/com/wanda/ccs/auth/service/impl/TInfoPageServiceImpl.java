package com.wanda.ccs.auth.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aggrepoint.adk.IModuleRequest;
import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.auth.dao.ITInfoPageDao;
import com.wanda.ccs.auth.service.TInfoPageService;
import com.wanda.ccs.model.TInfoPage;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TInfoPageServiceImpl extends BaseCrudServiceImpl<TInfoPage>
		implements TInfoPageService {

	@Autowired
	private ITInfoPageDao dao = null;

	@Override
	public IBaseDao<TInfoPage> getDao() {
		return dao;
	}

	@Override
	public PageResult<TInfoPage> findByCriteria(
			QueryCriteria<String, Object> query) {
		Vector<String> queryParam = new Vector<String>();

		String fromClause = "select c from TInfoPage c ";
		String joinClause = "";
		if(query.get("channelId") != null){
			queryParam.add(" c.infoChannelId =:channelId");
		}
		if(query.get("title") != null){
			queryParam.add(" c.title like :title");
		}
		if(query.get("status") != null){
			queryParam.add(" c.status = :status");
		} 
		String[] whereBodies = queryParam
				.toArray(new String[queryParam.size()]);
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = " c.id";
		}
		PageResult<TInfoPage> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;
	}

	@Override
	public void recallInfoPage(TInfoPage infoPage,IModuleRequest req) {
		CcsUserProfile user = (CcsUserProfile) req.getUserProfile();
		infoPage.setStatus("H");
		infoPage.setUpdatedBy(user.getName());
		infoPage.setUpdatedDate(new Date());
		super.update(infoPage);
	}

	@Override
	public void publishInfoPage(TInfoPage infoPage,IModuleRequest req) {
		CcsUserProfile user = (CcsUserProfile) req.getUserProfile();
		infoPage.setStatus("P");
		infoPage.setUpdatedBy(user.getName());
		infoPage.setUpdatedDate(new Date());
		infoPage.setPublishDate(new Date());
		super.update(infoPage);
	}

	@Override
	public Boolean checkTitle(String title) {
		String hql = " select c from TInfoPage c where c.title =:title";
		Map<String, Object> params = new HashMap<String, Object>();
		if(!"".equals(title)){
			params.put("title", title);
		}
		return super.queryCountHql(hql, params, null)>0?true:false;
	}
	
	@Override
	public List<TInfoPage> getMessage(Long infoChannelId) {
		
		
		String hql = "from TInfoPage c  where c.infoChannelId=:infoChannelId and c.status='P' order by c.publishDate";
		Map<String, Object> params = new HashMap<String, Object>();
		if(!"".equals(infoChannelId)){
			params.put("infoChannelId", infoChannelId);
		}
		
		
		
		List<TInfoPage> list = dao.queryHql(hql, null);
//		List<TInfoPage> l = new ArrayList<TInfoPage>();
//		l.add(list.get(0));
//		l.add(list.get(1));
//		l.add(list.get(2));
//		l.add(list.get(3));
//		l.add(list.get(4));

		return list;
	}


}
