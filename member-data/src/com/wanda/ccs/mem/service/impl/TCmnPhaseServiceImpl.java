package com.wanda.ccs.mem.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wanda.ccs.mem.service.TCampaignService;
import com.wanda.ccs.mem.service.TCmnPhaseService;
import com.wanda.ccs.model.TCampaign;
import com.wanda.ccs.model.TCmnPhase;
import org.springframework.beans.factory.annotation.Autowired;

import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.DateUtil;
import com.xcesys.extras.core.util.StringUtil;
import com.wanda.ccs.mem.dao.ITCmnPhaseDao;

@Service
public class TCmnPhaseServiceImpl extends BaseCrudServiceImpl<TCmnPhase>
		implements TCmnPhaseService {

	@Autowired
	private ITCmnPhaseDao dao = null;
	
	@Autowired
	private TCampaignService campaignSvc;

	@Override
	public IBaseDao<TCmnPhase> getDao() {
		return dao;
	}

	@Override
	public PageResult<TCmnPhase> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = "from TCmnPhase c";
		String[] whereBodies = new String[] {
				"c.campaignId = :campaignId"
		};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.id desc";
		}

		PageResult<TCmnPhase> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;

	}

	@Override
	public String getCmnPhaseCode(Long campaignId) {
		if(campaignId == null)
			return null;
		StringBuffer code = new StringBuffer();
		code.append("CMP");
		code.append(new SimpleDateFormat("yyyyMMdd").format(DateUtil.getCurrentDate()));
		String sql = "select max(c.code) as maxCode from t_cmn_phase c where c.campaign_id = :id and c.code like :code";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", campaignId);
		params.put("code", code.toString()+"%");
		getDao().setCacheable(false);
		List<Map<String, ?>> list = getDao().queryNativeSQL(sql, params);
		String maxCode = "";
		if(list != null && !list.isEmpty()){
			Map<String, ?> map = list.get(0);
			if(map.get("MAXCODE") != null)
				maxCode = map.get("MAXCODE").toString();
		}
		if(!StringUtil.isNullOrBlank(maxCode)){
			code.append(String.format("%07d", Long.valueOf(maxCode.substring(11))+1));
		}else{
			TCampaign campaign = campaignSvc.getById(campaignId);
			code.append(campaign.getCode().substring(11)).append("001");
		}
		return code.toString();
	}
	
	@Override
	public void updateCmnPhaseCode(Long cmnPhaseId){
		StringBuffer code = new StringBuffer();
		code.append("CMP").append(new SimpleDateFormat("yyyyMMdd").format(DateUtil.getCurrentDate()));
		String sql = "update t_cmn_phase set code = (select '"+code.toString()+"'||lpad(nvl(to_number(substr(max(code),-7)),'0')+1,'7','0') from t_campaign where code like :code) where cmn_phaise_id = :id";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", code.toString());
		params.put("id", cmnPhaseId);
		getDao().updateNativeSQL(sql, params);
	}
	
	@Override
	public void createOrUpdate(TCmnPhase phase){
		if(phase.getId() == null){
			phase.setCode(getCmnPhaseCode(phase.getCampaignId()));
		}
		super.createOrUpdate(phase);
	}

	@Override
	public boolean checkPhaseName(String name, Long phaseId, Long campaignId) {
		if(StringUtil.isNullOrBlank(name))
			return false;
		StringBuffer hql = new StringBuffer("select c from TCmnPhase c where c.name = :name ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		if(phaseId != null && phaseId != 0L){
			hql.append(" and c.id != :id ");
			params.put("id", phaseId);
		}
		if(campaignId != null && campaignId != 0L){
			hql.append(" and c.campaignId = :campaignId");
			params.put("campaignId", campaignId);
		}
		return super.queryCountHql(hql.toString(), params, null) > 0;
	}

	@Override
	public boolean checkPhaseStartDate(String date, Long phaseId) {
		if(phaseId == null || phaseId == 0L || StringUtil.isNullOrBlank(date))
			return false;
		String sql = "select min(start_dtime) as min_date from t_cmn_activity c where c.cmn_phaise_id = :phaseId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("phaseId", phaseId);
		getDao().setCacheable(false);
		List<Map<String, ?>> list = getDao().queryNativeSQL(sql, params);
		if(list != null && !list.isEmpty() && list.get(0) != null && list.get(0).get("MIN_DATE") != null){
			String minDate = list.get(0).get("MIN_DATE").toString();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			try {
				if(format.parse(minDate).before(format1.parse(date))){
					return true;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean checkPhaseEndDate(String date, Long phaseId) {
		if(phaseId == null || phaseId == 0L || StringUtil.isNullOrBlank(date))
			return false;
		String sql = "select max(end_dtime) as max_date from t_cmn_activity c where c.cmn_phaise_id = :phaseId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("phaseId", phaseId);
		getDao().setCacheable(false);
		List<Map<String, ?>> list = getDao().queryNativeSQL(sql, params);
		if(list != null && !list.isEmpty() && list.get(0) != null && list.get(0).get("MAX_DATE") != null){
			String maxDate = list.get(0).get("MAX_DATE").toString();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				if(format.parse(maxDate).after(format.parse(date + " 23:59:59"))){
					return true;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
