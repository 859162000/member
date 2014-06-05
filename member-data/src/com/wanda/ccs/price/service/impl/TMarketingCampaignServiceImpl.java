package com.wanda.ccs.price.service.impl;

import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.model.TMarketingCampaign;
import com.wanda.ccs.price.dao.ITMarketingCompaignDao;
import com.wanda.ccs.price.service.TMarketingCampaignService;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TMarketingCampaignServiceImpl extends BaseCrudServiceImpl<TMarketingCampaign>
		implements TMarketingCampaignService{
	
	@Autowired
	private ITMarketingCompaignDao dao = null;

	@Override
	public IBaseDao<TMarketingCampaign> getDao() {
		return dao;
	}
	
	@Override
	public PageResult<TMarketingCampaign> findByCriteria(
			QueryCriteria<String, Object> query) {
		Vector<String> queryParam = new Vector<String>();
		String fromClause = "select c from TMarketingCampaign c ";
		queryParam.add("c.status <> 'X' ");
		if ("YES".equals(query.get("isTicket"))){
			queryParam.add("c.status <> 'D' and c.startDate <= sysdate and c.endDate >= sysdate ");
			if(("CINEMA").equals(query.get("level"))){
				if((!"".equals(query.get("campaignId"))) && (null != query.get("campaignId"))) 
					queryParam.add("(c.ownerRegion =:region and c.campaignLevel = 'R') or (c.tCinema.id =:cinema and c.campaignLevel = 'C') or (c.campaignLevel = 'G') or (c.id =:campaignId )");
				else
					queryParam.add("(c.ownerRegion =:region and c.campaignLevel = 'R') or (c.tCinema.id =:cinema and c.campaignLevel = 'C') or (c.campaignLevel = 'G') ");
			}else if(("REGION").equals(query.get("level"))){
				if((!"".equals(query.get("campaignId"))) && (null != query.get("campaignId"))) {
					queryParam.add("(c.ownerRegion =:region and c.campaignLevel = 'R') or (c.campaignLevel = 'G') or (c.id =:campaignId )");
				}
				else
					queryParam.add("(c.ownerRegion =:region and c.campaignLevel = 'R') or (c.campaignLevel = 'G') ");
			}else{
				if((!"".equals(query.get("campaignId"))) && (null != query.get("campaignId"))) 
					queryParam.add(" (c.campaignLevel = 'G') or (c.id =:campaignId )");
				else
					queryParam.add(" c.campaignLevel = 'G' ");
			}
		}else{
			if (("CINEMA").equals(query.get("level"))) {
				if(("ALL").equals(query.get("symbol")))
					queryParam.add("c.ownerRegion = :queryString ");
				else
					queryParam.add("c.campaignLevel = 'C' and c.tCinema.id = :queryString ");
			} else if (("REGION").equals(query.get("level"))) {
				queryParam.add("c.campaignLevel = 'R' and c.ownerRegion = :queryString ");
			} else {
				queryParam.add("c.campaignLevel = 'G' ");
			}
			if ((null != query.get("campaignName"))
					&& (!("").equals(query.get("campaignName")))) {
				queryParam.add("c.campaignName like :campaignName");
			}
			if ((null != query.get("status"))
					&& (!("").equals(query.get("status")))) {
				queryParam.add("c.status like :status");
			}
		}
		String[] whereBodies = queryParam
				.toArray(new String[queryParam.size()]);
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.startDate";
		}
//		String ql = QLUtil.generateQL(fromClause, joinClause, orderClause, whereBodies, null, null, query);
//		
		PageResult<TMarketingCampaign> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;
	}

}
