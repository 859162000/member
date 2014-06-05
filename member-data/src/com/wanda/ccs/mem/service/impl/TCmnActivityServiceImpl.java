package com.wanda.ccs.mem.service.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.mem.dao.ITCmnActivityDao;
import com.wanda.ccs.mem.service.TCmnActivityService;
import com.wanda.ccs.mem.service.TCmnPhaseService;
import com.wanda.ccs.mem.service.TFileAttachService;
import com.wanda.ccs.model.TCmnActivity;
import com.wanda.ccs.model.TCmnPhase;
import com.wanda.ccs.model.TFileAttach;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.DateUtil;
import com.xcesys.extras.core.util.StringUtil;

@Service
public class TCmnActivityServiceImpl extends BaseCrudServiceImpl<TCmnActivity>
		implements TCmnActivityService {

	@Autowired
	private ITCmnActivityDao dao = null;
	
	
	@Autowired
	private TCmnPhaseService cmnPhaseSvc;
	
	@Autowired
	private TFileAttachService  fileAttachSvc;
	
	@Override
	public IBaseDao<TCmnActivity> getDao() {
		return dao;
	}

	@Override
	public PageResult<TCmnActivity> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = "select c from TCmnActivity c";
		String[] whereBodies = new String[] {
				"c.name like :name",
				"c.code like :code",
				"c.campaignId = :campaignId",
				"c.cmnPhaseId = :cmnPhaseId"
		};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.id desc";
		}

		PageResult<TCmnActivity> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		for(TCmnActivity activity : result.getContents()){
			activity.setCanEnabled(activity.getEndDtime().after(DateUtil.getCurrentDate()));
		}
		return result;

	}
	
	@Override
	public void createOrUpdate(TCmnActivity cmnActivity){
		if(cmnActivity.getId() == null){
			cmnActivity.setCode(this.getActivityCode(cmnActivity.getCmnPhaseId()));
		}
		super.createOrUpdate(cmnActivity);
		
		
		TFileAttach fileAttach = cmnActivity.getFileAttach();
		if(fileAttach == null || fileAttach.isDelete() ){//当上传文件为空时，删除历史上传文件
			String hql = "delete from TFileAttach c where c.refObjectId = :refObjectId and c.refObjectType = :refObjectType";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("refObjectId", cmnActivity.getId());
			params.put("refObjectType", TCmnActivity.class.getName());
			getDao().updateHql(hql, params);
		}else if(fileAttach.getId() == null){//当文件没有存入数据库时，保存文件
			fileAttach.setRefObjectId(cmnActivity.getId());
			fileAttachSvc.createOrUpdate(fileAttach);
		}else if(fileAttach.getRefObjectId() == null){//当文件中的请求数据id为空时，更新请求数据id
			String hql = "update TFileAttach c set c.refObjectId = :refObjectId where c.id = :id";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("refObjectId", cmnActivity.getId());
			params.put("id", fileAttach.getId());
			getDao().updateHql(hql, params);
		}else if(fileAttach.getStatus().equals("W")){
			fileAttachSvc.createOrUpdate(fileAttach);
		}
		
	}

	@Override
	public boolean checkActivityName(String name, Long id) {
		if(StringUtil.isNullOrBlank(name))
			return false;
		StringBuffer hql = new StringBuffer("select c from TCmnActivity c where c.name = :name");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		if(id != null){
			hql.append(" and c.id <> :id");
			params.put("id", id);
		}
		return super.queryCountHql(hql.toString(), params, null) > 0;
	}

	@Override
	public String getActivityCode(Long phaseId) {
		if(phaseId == null)
			return null;
		StringBuffer code =  new StringBuffer();
		code.append("CMB").append(new SimpleDateFormat("yyyyMMdd").format(DateUtil.getCurrentDate()));
		String sql = "select max(c.code) as maxCode from t_cmn_activity c where c.cmn_phaise_id = :id and c.code like :code";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", phaseId);
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
			code.append(String.format("%010d", Long.valueOf(maxCode.substring(11))+1));
		}else{
			TCmnPhase phase = cmnPhaseSvc.getById(phaseId);
			code.append(phase.getCode().substring(11)).append("001");
		}
		return code.toString();
	}
	
	@Override
	public void updateCmnActivityCode(Long activityId){
		StringBuffer code = new StringBuffer();
		code.append("CMB").append(new SimpleDateFormat("yyyyMMdd").format(DateUtil.getCurrentDate()));
		String sql = "update t_cmn_activity set code = (select '"+code.toString()+"'||lpad(nvl(to_number(substr(max(code),-10)),'0')+1,'10','0') from t_campaign where code like :code) where cmn_activity_id = :id";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", code.toString());
		params.put("id", activityId);
		getDao().updateNativeSQL(sql, params);
	}

	@Override
	public Map<String, String> getMemberConfig() {
		String sql = "select PARAMETER_NAME,PARAMETER_VALUE from T_MEMBER_CONFIG c where c.PARAMETER_NAME in (:parameterNames)";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("parameterNames", new String[]{"MSG_MQ_IP","MSG_CHANNEL_ID"});
		getDao().setCacheable(false);
		List<Map<String, ?>> list = getDao().queryNativeSQL(sql, params);
		Map<String, String> memberConfigMap = new HashMap<String, String>();
		for(Map<String, ?> map : list){
			if(map != null && !map.isEmpty())
				memberConfigMap.put(map.get("PARAMETER_NAME").toString(), map.get("PARAMETER_VALUE").toString());
		}
		return memberConfigMap;
	}
	
	@Override
	public Map<String, ?> getMemberByPhone(String mobile){
		if(StringUtil.isNullOrBlank(mobile))
			return null;
		String sql = "select m.mobile,c.inner_code,m.NAME,m.gender,to_char(m.BIRTHDAY,'yyyy-MM-dd') as BIRTHDAY,tml.MEM_LEVEL,to_char(m.REGIST_DATE,'yyyy-MM-dd') as REGIST_DATE, mp.EXG_EXPIRE_POINT_BALANCE,tml.TARGET_LEVEL,mp.EXG_POINT_BALANCE "
					+" from t_member m,t_member_level tml,t_member_point mp,t_member_info mi,t_cinema c "
					+" where m.member_id = tml.member_id and m.member_id = mp.member_id and m.member_id = mi.member_id and mi.manage_cinema_id = c.seqid"
					+" and m.mobile = :mobile";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobile", mobile);
		getDao().setCacheable(false);
		List<Map<String, ?>> list = getDao().queryNativeSQL(sql, params);
		if(list != null && !list.isEmpty())
			return list.get(0);
		return null;

	}
}
