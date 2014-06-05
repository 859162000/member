package com.wanda.ccs.mem.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.basemgt.service.TDimTypeDefService;
import com.wanda.ccs.mem.dao.ITCampaignDao;
import com.wanda.ccs.mem.service.TCampaignService;
import com.wanda.ccs.model.IDimType;
import com.wanda.ccs.model.IMemberDimType;
import com.wanda.ccs.model.TCampaign;
import com.wanda.ccs.model.TCampaignCinema;
import com.wanda.ccs.model.TCmnActivity;
import com.wanda.ccs.model.TCmnPhase;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.DateUtil;
import com.xcesys.extras.core.util.StringUtil;

@Service
public class TCampaignServiceImpl extends BaseCrudServiceImpl<TCampaign>
		implements TCampaignService {

	@Autowired
	private ITCampaignDao dao = null;
	
	@Autowired
	private TDimTypeDefService dimSvc; 

	@Override
	public IBaseDao<TCampaign> getDao() {
		return dao;
	}

	@Override
	public PageResult<TCampaign> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = "select c from TCampaign c";
		String[] whereBodies = new String[] {
				"c.code like :code or (exists(select p from TCmnPhase p where p.campaignId = c.id and p.code like :code)) or (exists(select a from TCmnActivity a where a.campaignId = c.id and a.code like :code))",
				"c.name like :name or (exists(select p from TCmnPhase p where p.campaignId = c.id and p.name like :name)) or (exists(select a from TCmnActivity a where a.campaignId = c.id and a.name like :name))",
				"c.status = :status or (exists(select p from TCmnPhase p where p.campaignId = c.id and p.status = :status)) or (exists(select a from TCmnActivity a where a.campaignId = c.id and a.status = :status))",
				"c.channel = :channel",
				"c.startDate >= to_date(:sStartDate,'yyyy-mm-dd')",
				"c.startDate < to_date(:eStartDate,'yyyy-mm-dd')+1",
				"c.endDate >= to_date(:sEndDate,'yyyy-mm-dd')",
				"c.endDate < to_date(:eEndDate,'yyyy-mm-dd') + 1",
				"c.type = :type",
				"c.allCinema = true or (exists(select cc from TCampaignCinema cc where cc.tCampaign.id = c.id and cc.tCinema.area in (:areas)))",
				"c.allCinema = true or (exists(select cc from TCampaignCinema cc where cc.tCampaign.id = c.id and cc.tCinema.id in (:cinemas)))",
				"c.allCinema = true or (exists(select cc from TCampaignCinema cc where cc.tCampaign.id = c.id and cc.tCinema.area = :region))",
				"c.allCinema = true or (exists(select cc from TCampaignCinema cc where cc.tCampaign.id = c.id and cc.tCinema.id = :userCinemaId))"
		};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.id desc";
		}

		PageResult<TCampaign> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		Map<String, String> areas = dimSvc.getDimDefsByTypeId(IDimType.LDIMTYPE_AREA);
		Map<String, String> status = dimSvc.getDimDefsByTypeId(IMemberDimType.LDIMTYPE_CAMPAIGN_STATUS);
		for(TCampaign campaign : result.getContents()){
			StringBuffer cinemas = new StringBuffer();
			if(campaign.getAllCinema()){
				cinemas.append("所有影城");
			}else{
				for(String key : areas.keySet()){
					StringBuffer areaCinemas = new StringBuffer();
					for(TCampaignCinema cc : campaign.gettCampaignCinemas()){
						if(cc.gettCinema().getArea().equals(key)){
							areaCinemas.append(cc.gettCinema().getShortName()).append("、");
						}
					}
					if(!StringUtil.isNullOrBlank(areaCinemas.toString())){
						areaCinemas.insert(0, areas.get(key)+":");
						areaCinemas.substring(0, areaCinemas.length()-1);
						cinemas.append(areaCinemas.substring(0, areaCinemas.length()-1)+";\n");
					}
				}
			}
			campaign.setCinemas(cinemas.toString());
			
			StringBuffer phaseAndActivity = new StringBuffer();
			for(TCmnPhase phase : campaign.gettCmnPhases()){
				StringBuffer phases = new StringBuffer();
				phases.append(phase.toString()).append("|").append(status.get(phase.getStatus()));
				if(phase.gettCmnActivities() != null && !phase.gettCmnActivities().isEmpty()){
					phases.append(" : ");
				}
				for(TCmnActivity activity : phase.gettCmnActivities()){
					phases.append(activity.toString()).append("|").append(status.get(activity.getStatus()));
					phases.append("、");
				}
				if(phases.lastIndexOf("、") == -1){
					phaseAndActivity.append(phases);
				}else{
					phaseAndActivity.append(phases.substring(0, phases.length()-1));
				}
				phaseAndActivity.append(";\n");
			}
			campaign.setPhaseAndActivitys(phaseAndActivity.toString());
		}
		return result;

	}

	@Override
	public String getCampainCode() {
		StringBuffer code = new StringBuffer();
		code.append("CMN").append(new SimpleDateFormat("yyyyMMdd").format(DateUtil.getCurrentDate()));
		String hql = "select max(code) as maxCode from t_campaign c where c.code like :code";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", code.toString()+"%");
		getDao().setCacheable(false);
		List<Map<String, ?>> list = getDao().queryNativeSQL(hql, params);
		String maxCode = "";
		if(list != null && !list.isEmpty()){
			Map<String, ?> map = list.get(0);
			if(map.get("MAXCODE") != null)
				maxCode = map.get("MAXCODE").toString();
		}
		if(!StringUtil.isNullOrBlank(maxCode)){
			code.append(String.format("%04d", Long.valueOf(maxCode.substring(11))+1)); 
		}else{
			code.append("0001");
		}
		return code.toString();
	}
	
	@Override
	public void updateCampaignCode(Long campaignId){
		StringBuffer code = new StringBuffer();
		code.append("CMN").append(new SimpleDateFormat("yyyyMMdd").format(DateUtil.getCurrentDate()));
		String sql = "update t_campaign set code = (select '"+code.toString()+"'||lpad(nvl(to_number(substr(max(code),-4)),'0')+1,'4','0') from t_campaign where code like :code) where campaign_id = :id";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", code.toString());
		params.put("id", campaignId);
		getDao().updateNativeSQL(sql, params);
	}
	
	@Override
	public void createOrUpdate(TCampaign campaign){
		if(campaign.getId() == null){
			campaign.setCode(getCampainCode());
		}
		super.createOrUpdate(campaign);
	}

	@Override
	public boolean checkCampaignname(String name, Long campaignId) {
		if(StringUtil.isNullOrBlank(name))
			return false;
		StringBuffer hql = new StringBuffer("select c from TCampaign c where c.name = :name ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		if(campaignId != null && campaignId != 0L){
			hql.append(" and c.id <> :id");
			params.put("id", campaignId);
		}
		return super.queryCountHql(hql.toString(), params, null) > 0;
	}

	@Override
	public boolean checkStartDate(Long campaignId, String date) {
		if(campaignId == null || campaignId == 0L || StringUtil.isNullOrBlank(date))
			return false;
		String sql = "select min(start_date) as min_date from t_cmn_phase c where c.campaign_id = :campaignId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("campaignId", campaignId);
		getDao().setCacheable(false);
		List<Map<String, ?>> list = getDao().queryNativeSQL(sql, params);
		if(list != null && !list.isEmpty() && list.get(0) != null && list.get(0).get("MIN_DATE") != null){
			String minDate = list.get(0).get("MIN_DATE").toString();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				if(format.parse(minDate).before(format.parse(date))){
					return true;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean checkEndDate(Long campaignId, String date) {
		if(campaignId == null || campaignId == 0L || StringUtil.isNullOrBlank(date))
			return false;
		String sql = "select max(end_date) as max_date from t_cmn_phase c where c.campaign_id = :campaignId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("campaignId", campaignId);
		getDao().setCacheable(false);
		List<Map<String, ?>> list = getDao().queryNativeSQL(sql, params);
		if(list != null && !list.isEmpty() && list.get(0) != null && list.get(0).get("MAX_DATE") != null){
			String maxDate = list.get(0).get("MAX_DATE").toString();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				if(format.parse(maxDate).after(format.parse(date))){
					return true;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
