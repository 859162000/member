package com.wanda.ccs.auth.service.impl;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.auth.dao.EHREmployeeDao;
import com.wanda.ccs.auth.service.EHREmployeeService;
import com.wanda.ccs.auth.service.TAuthUserService;
import com.wanda.ccs.basemgt.service.TDimTypeDefService;
import com.wanda.ccs.model.EHREmployee;
import com.wanda.ccs.model.IDimType;
import com.wanda.ccs.model.TAuthUser;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.StringUtil;

@Service
public class EHREmployeeServiceImpl extends BaseCrudServiceImpl<EHREmployee> 
		implements EHREmployeeService {
	
	@Autowired
	private EHREmployeeDao dao = null;
	@Autowired
	private TDimTypeDefService dimSvc;
	@Autowired
	private TAuthUserService authUserSvc;
	
	@Override
	public IBaseDao<EHREmployee> getDao() {
		return dao;
	}
	
	@Override
	public PageResult<EHREmployee> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = " select c from EHREmployee c";
		String[] whereBodies = new String[] {
				"c.employeeName like :employeeName",
				"c.rtxName like :rtxName",
				"c.employeeStatus in (:employeeStatus)",
				"c.status in (:status)",
				"c.rtxName is not null"
		};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "";
		}
		PageResult<EHREmployee> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		Map<String,String> areaDimdefs = dimSvc.getDimDefsByTypeId(IDimType.LDIMTYPE_AREA);
		for(EHREmployee ehrUser : result.getContents()){
			String stringDept = "";
			TAuthUser authUser = authUserSvc.getUserByRtx(ehrUser.getRtxName());
			if(authUser != null && authUser.getUserLevel() != null){
				if(authUser.getUserLevel().equals("G")){
					stringDept = "院线";
				}else if(authUser.getUserLevel().equals("R")){
					if(authUser.getRegion() != null)
						stringDept += areaDimdefs.get(authUser.getRegion()) ;
				}else if(authUser.getUserLevel().equals("C")){
					if(authUser.getRegion() != null)
						stringDept += areaDimdefs.get(authUser.getRegion()) ;
					if(authUser.gettCinema() != null)
						stringDept += "\\" + authUser.gettCinema().getShortName();
				}else if(authUser.getUserLevel().equals("T")){
					if(authUser.getRegion() != null)
						stringDept += areaDimdefs.get(authUser.getRegion()) ;
					if(authUser.gettCinema() != null)
						stringDept += "\\" + authUser.gettCinema().getShortName();
					stringDept += "(中心店)";
				}
			}
			ehrUser.setStringDept(stringDept);
		}
		return result;
	}



	public EHREmployee getEhrByRtxName(String rtxName) {
		if(StringUtil.isNullOrBlank(rtxName))
			return null;
		String hql = "from EHREmployee c where c.rtxName = :rtxName";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rtxName", rtxName);
		return dao.queryHqlTopOne(hql, params);
	}
	
}
