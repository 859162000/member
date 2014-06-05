package com.wanda.ccs.schedule.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.wanda.ccs.model.TScheduleGuideH;
import com.wanda.ccs.schedule.dao.ITScheduleGuideHDao;
import com.wanda.ccs.schedule.service.TScheduleGuideHService;
import com.wanda.ccs.service.impl.BaseStateUVServiceImpl;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;

/**
 * 排片指导service
 * 
 * @author Benjamin
 * @date 2011-10-21
 */
@Service
public class TScheduleGuideHServiceImpl extends
		BaseStateUVServiceImpl<TScheduleGuideH> implements TScheduleGuideHService {

	@Autowired
	private ITScheduleGuideHDao dao;

	@Override
	public PageResult<TScheduleGuideH> findByCriteria(
			QueryCriteria<String, Object> criteria) {
		String fromClause = " select c from TScheduleGuideH c";
		String[] whereBodies = new String[] { 
				"c.issue = :issue",
				"c.ver= :ver",
				"c.status in (:status)", 
				"c.suitRegion =:suitRegion",
				"c.suitStartDate >= :suitStartDate",
				"c.suitEndDate <= :suitEndDate" };
		String joinClause = "";
		String orderClause = null;
		if (criteria.getSort() == null
				|| criteria.getSort().trim().length() == 0) {
			orderClause = "c.status desc,c.issue desc,c.ver desc";
		}
		PageResult<TScheduleGuideH> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, criteria);
		return result;
	}

	@Override
	public IBaseDao<TScheduleGuideH> getDao() {
		return dao;
	}

	@Override
	public Map<String, String> findAllGuideH(String... regions) {
		Map<String, String> map = new HashMap<String, String>();
		Map<String, Object> params = new HashMap<String,Object>();

		String fromClause = " select c from TScheduleGuideH c";
		
		if(regions != null && regions.length > 0){
			if(regions.length == 1){
				fromClause += " where c.suitRegion = :regions";
			}else{
				fromClause += " where c.suitRegion in (:regions)";
			}
			params.put("regions", regions);
		}

		setQueryCacheable(false);
		List<TScheduleGuideH> lst = getDao().queryHql(fromClause, params);

		for (TScheduleGuideH gh : lst) {
			map.put(gh.getIssue(), gh.getIssue());
		}
		return map;
	}

	@Override
	public List<TScheduleGuideH> findByIssue(String issue){

		Assert.isTrue(StringUtils.isNotBlank(issue),"期数不可为空");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("issue", issue);
		String hql = "from TScheduleGuideH h where h.issue=:issue order by h.ver desc";
		return super.queryHql(hql, params);
	}

	@Override
	public int obtainLatelyVer(String issue) {
		int ver = 1;
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TScheduleGuideH h where h.issue=:issue order by h.ver desc";
		params.put("issue", issue);
		List<TScheduleGuideH> list = dao.queryHql(hql, params);
		if (list != null && list.size() > 0) {
			ver = NumberUtils.toInt(list.get(0).getVer()) + 1;
		}
		return ver;

	}

}
