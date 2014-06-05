package com.wanda.ccs.mem.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wanda.ccs.mem.dao.ITMemberKPIFileDao;
import com.wanda.ccs.mem.service.TMemberKPIFileService;
import com.wanda.ccs.model.TFileAttach;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.StringUtil;

@Service
public class TMemberKPIFileServiceImpl extends  BaseCrudServiceImpl<TFileAttach> implements TMemberKPIFileService{
	
	@Autowired
	private ITMemberKPIFileDao dao;

	public IBaseDao<TFileAttach> getDao() {
		return dao;
	}
	
	@SuppressWarnings("all")
	public PageResult<TFileAttach> findByCriteria(
			QueryCriteria query) {
		Vector<String> queryParam = new Vector<String>();
		String hql = " select attach from TFileAttach attach ";
		queryParam.add("attach.status in (:status) ");
		queryParam.add("attach.fileName like :fileName");
		queryParam.add("attach.createdBy = :createdBy");
		queryParam.add("attach.refObjectType = :refObjectType");
		String[] whereBodies = queryParam.toArray(new String[queryParam.size()]);
		String joinClause = "";
		String orderClause = "";
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = " attach.createdDate desc";
		}
		PageResult<TFileAttach> result = getDao().queryQueryCriteria(hql,
				joinClause, orderClause, whereBodies, query);
		return result;
	}
	
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
	
	public TFileAttach getFileAttach(Long refObjectId,
			String refObjecType,String filename,String status) {
		List<TFileAttach> list = getFileAttachByObject(refObjectId, refObjecType, filename, status);
		if(list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}

	public void createOrUpdate(TFileAttach tFileAttach) {
		TFileAttach attch = getFileAttach(0L, tFileAttach.getRefObjectType(), tFileAttach.getFileName(), "E");
		if(attch != null){
			super.delete(attch);
		}
		super.createOrUpdate(tFileAttach);
	}

	public void setDao(ITMemberKPIFileDao dao) {
		this.dao = dao;
	}

}
