package com.wanda.ccs.mem.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.mem.dao.ITFileAttachDao;
import com.wanda.ccs.mem.service.TFileAttachService;
import com.wanda.ccs.model.TCmnActivity;
import com.wanda.ccs.model.TFileAttach;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.StringUtil;

@Service
public class TFileAttachServiceImpl extends BaseCrudServiceImpl<TFileAttach>
		implements TFileAttachService {

	@Autowired
	private ITFileAttachDao dao;
	
	@Override
	public IBaseDao<TFileAttach> getDao() {
		return dao;
	}
	
//	@Autowired
//	private CreateSchedulerService createSchedulerService;
	
	@Override
	public PageResult<TFileAttach> findByCriteria(
			QueryCriteria<String, Object> query) {
		Vector<String> queryParam = new Vector<String>();
		String fromClause = " from TFileAttach c ";
		queryParam.add("c.status in (:status) ");
		queryParam.add("c.fileName like :filename");
		queryParam.add("c.createdBy =:createdBy");
		queryParam.add("c.refObjectType =:refObjectType");
		String[] whereBodies = queryParam
				.toArray(new String[queryParam.size()]);
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = " c.createdDate desc";
		}
		PageResult<TFileAttach> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;

	}
	
	@Override
	public PageResult<Map<String, ?>> findByCriteriaQuery(
			QueryCriteria<String, Object> query) {
		StringBuffer sql = new StringBuffer();
		sql.append("select fa.*,activity.NAME, activity.CODE, (select count(erl.ABATCH_ERRE_ID) from T_ABATCH_ERRE_LOG erl where erl.FILE_ID = fa.FILE_ATTACH_ID and erl.erre_level = '20') warning ");
		
		StringBuffer countSql = new StringBuffer();
		countSql.append("select count(fa.FILE_ATTACH_ID) ");
		
		StringBuffer fromSql = new StringBuffer();
		fromSql.append(" from T_FILE_ATTACH fa, T_CMN_ACTIVITY activity, T_CAMPAIGN campaign ");
		fromSql.append(" where fa.REF_OBJECT_ID = activity.CMN_ACTIVITY_ID and fa.REF_OBJECT_TYPE = :refObjectType and activity.CAMPAIGN_ID = campaign.CAMPAIGN_ID ");
		if(query.get("userLevel") != null && !StringUtil.isNullOrBlank(query.get("userLevel").toString())){
			fromSql.append(" and campaign.CREATION_LEVEL = :userLevel");
		}
		if(query.get("region") != null && !StringUtil.isNullOrBlank(query.get("region").toString())){
			fromSql.append(" and campaign.CREATION_AREA_ID = :region");
		}
		if(query.get("userCinemaId") != null && !StringUtil.isNullOrBlank(query.get("userCinemaId").toString())){
			fromSql.append(" and campaign.CREATION_CINEMA_ID = :userCinemaId");
		}
		if(query.get("fileName") != null && !StringUtil.isNullOrBlank(query.get("fileName").toString())){
			query.put("likeFileName", "%"+query.get("fileName").toString()+"%");
			fromSql.append(" and fa.FILE_NAME like :likeFileName");
		}
		if(query.get("activityName") != null && !StringUtil.isNullOrBlank(query.get("activityName").toString())){
			query.put("likeActivityName", "%"+query.get("activityName").toString()+"%");
			fromSql.append(" and activity.NAME like :likeActivityName");
		}
		if(query.get("activityCode") != null && !StringUtil.isNullOrBlank(query.get("activityCode").toString())){
			query.put("likeActivityCode", "%"+query.get("activityCode").toString()+"%");
			fromSql.append(" and activity.CODE like :likeActivityCode");
		}
		PageResult<Map<String, ?>> result = getDao().queryNativeSQL(sql.append(fromSql).toString(), countSql.append(fromSql).toString(), query, null, query.getStartIndex(), query.getPageSize());
		return result;

	}
	
	@Override
	public List<TFileAttach> getFileAttachByObject(Long refObjectId,
			String refObjecType,String filename,String status) {
		if(refObjectId == null || StringUtil.isNullOrBlank(refObjecType))
			return null;
		String hql = "from TFileAttach c " +
				"where c.refObjectId = :refObjectId " +" and c.fileName like :filename "+
				"and c.refObjectType = :refObjectType";
		if(status != null){
			hql +=" and c.status =:status " ;
		}
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("refObjectId", refObjectId);
		params.put("refObjectType", refObjecType);
		params.put("filename", filename);
		params.put("status", status);
		
		return dao.queryHql(hql, params);
	}
	
	@Override
	public TFileAttach getFileAttach(Long refObjectId,
			String refObjecType,String filename,String status) {
		List<TFileAttach> list = getFileAttachByObject(refObjectId, refObjecType, filename, status);
		if(list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}

	
	@Override
	public TFileAttach getFileAttach(Long refObjectId, String refObjecType) {
		if(refObjectId == null || StringUtil.isNullOrBlank(refObjecType))
			return null;
		String hql = "from TFileAttach c where c.refObjectId = :refObjectId and c.refObjectType = :refObjectType";		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("refObjectId", refObjectId);
		params.put("refObjectType", refObjecType);
		return getDao().queryHqlTopOne(hql, params);
	}

	@Override
	public void saveFileAttach(TFileAttach fileAttach) {
		if(fileAttach.getRefObjectId() == null){
			String hql = "delete from TFileAttach c where c.refObjectType = :refObjectType and c.refObjectId is null";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("refObjectType", TCmnActivity.class.getSimpleName());
			getDao().updateHql(hql, params);
		}
		if(fileAttach != null)
			getDao().saveOrUpdate(fileAttach);
	}

	@Override
	public void createOrUpdate(TFileAttach fileAttach, String fromSource,CcsUserProfile user) {
		TFileAttach attch = getFileAttach(fileAttach.getRefObjectId(), fileAttach.getRefObjectType(), fileAttach.getFileName(), "E");
		TFileAttach attchTemp = getFileAttach(fileAttach.getRefObjectId(), fileAttach.getRefObjectType(), fileAttach.getFileName(), null);
		if(attch != null){
			super.delete(attch);
		}if(attchTemp !=null){
			super.delete(attchTemp);
		}
		super.createOrUpdate(fileAttach);
		try {
			this.getUniversalDao().getConnection().commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		if(fileAttach.getStatus() != null){
//			Map<String,Object> fileIdMap = new HashMap<String, Object>();
//			fileIdMap.put("FILEID", fileAttach.getId());
//			createSchedulerService.createScheduler(fromSource, fromSource, user, fileIdMap);
//		}
	}

	@Override
	public Long getMemberCountByFileId(Long fileId) {
		if(fileId == null)
			return null;
		String sql = "select count(*) from T_CONTACT_HISTORY_TEMP temp where temp.FILE_ATTACH_ID = :fileId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fileId", fileId);
		getDao().setCacheable(false);
		return Long.valueOf(getDao().queryCountNativeSQL(sql, params));
	}

//	@Override
//	public void createScheduler(String jobName, String triggerName,
//			CcsUserProfile user) {
//		ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"applicationContext-resources.xml"});
//		WsSchedulerService service = (WsSchedulerService) ctx.getBean("wsSchedulerService");
//		String group = "DEFAULT";
//		SimpleTriggerInfo info = new SimpleTriggerInfo();
////		info.setJobName("pointCalculationJob");
//		info.setJobName(jobName);
////		info.setTriggerName("CARD_ORDER_" + System.currentTimeMillis()); //不指定名字会自动产生已 T+ UUID 的随机名称
//		info.setTriggerName(user.getId()+ triggerName + System.currentTimeMillis());
//		info.setTriggerGroup(group);
//		info.setStartTime(new Date(System.currentTimeMillis() + 1000));
//		service.addTrigger(info);
//		
//	}

}
