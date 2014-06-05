package com.wanda.ccs.auth.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.auth.dao.ITJMonOfficealJobsDao;
import com.wanda.ccs.auth.service.EHROrgnizationService;
import com.wanda.ccs.auth.service.TJMonOfficealJobsService;
import com.wanda.ccs.basemgt.service.TSettingService;
import com.wanda.ccs.model.EHROrgnization;
import com.wanda.ccs.model.TJMonOfficialJobs;
import com.wanda.ccs.model.UtilType;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.util.SpringContextUtil;

@Service
public class TJMonOfficealJobsServiceImpl extends BaseCrudServiceImpl<TJMonOfficialJobs> 
		implements TJMonOfficealJobsService {
	
	@Autowired
	private ITJMonOfficealJobsDao dao = null;
	
	@Override
	public IBaseDao<TJMonOfficialJobs> getDao() {
		return dao;
	}
	
	@Override
	public PageResult<TJMonOfficialJobs> findByCriteria(
			QueryCriteria<String, Object> query) {
		Vector<String> queryParam = new Vector<String>();
		Long[] areaId = (Long[])query.get("area");
		Long areaDer = Long.valueOf(SpringContextUtil.getBean(TSettingService.class).getByName(UtilType.EHR_DER_P).getValue());
		if(null==areaId){
			query.put("areaIds", null);
		}else{
			List<Long> areaList = new ArrayList<Long>();
			for(Long ad:areaId){
				if(!ad.equals(areaDer)){
					areaList.add(ad);
				}
				if(ad.equals(areaDer)){
					List<EHROrgnization> list = SpringContextUtil.getBean(EHROrgnizationService.class).getParentID(areaDer.toString());
					for(EHROrgnization ehrOrg : list){
						areaList.add(ehrOrg.getId());
					}
				}
			}
			Long[] areaIds = new Long[areaList.size()];
			for(int i=0;i<areaList.size();i++){
				areaIds[i]=areaList.get(i);
			}
			query.put("areaIds", areaIds);
		}
		
		String fromClause = "select c from TJMonOfficialJobs c ";
		String joinClause = "";
		if(null!=query.get("cinema")){
			queryParam.add("c.cinemaOrgId in(:cinema)  ");
		}
		if(null!=query.get("area")){
			queryParam.add("c.regionOrgId in(:areaIds)  ");
		}
		queryParam.add("c.empName like:employeeName ");
		if(query.get("jobName") == null || "".equals(((String[])query.get("jobName"))[0])){
			queryParam.add("c.jobName in (:jobs) ");
		}else{queryParam.add("c.jobName in(:jobName) ");}
		String[] whereBodies = queryParam
		.toArray(new String[queryParam.size()]);
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.regionOrgId,c.cinemaOrgId ,c.jobName ";
		}
		PageResult<TJMonOfficialJobs> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;
	}

	@Override
	public TJMonOfficialJobs getJMonOJByRTX(String rtxName) {
		String hql = " select c from TJMonOfficialJobs c where c.rtxName =:rtxName";
		Map<String, Object> params = new HashMap<String, Object>();
		if(!"".equals(rtxName)){
			params.put("rtxName", rtxName);
		}
		return getDao().queryHqlTopOne(hql, params);
	}

	@Override
	public List<TJMonOfficialJobs> checkRTX(String rtxName) {
		String hql = " select c from TJMonOfficialJobs c where c.rtxName =:rtxName";
		Map<String, Object> params = new HashMap<String, Object>();
		if(!"".equals(rtxName)){
			params.put("rtxName", rtxName);
		}
		
		return super.queryHql(hql, params);
		
	}
	
}
