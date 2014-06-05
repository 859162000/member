package com.wanda.ccs.auth.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.auth.dao.IVNcUsersDao;
import com.wanda.ccs.auth.service.TAuthOrgAssignService;
import com.wanda.ccs.auth.service.TAuthUserService;
import com.wanda.ccs.auth.service.VNcUsersService;
import com.wanda.ccs.basemgt.service.TDimTypeDefService;
import com.wanda.ccs.model.IDimType;
import com.wanda.ccs.model.TAuthOrgAssign;
import com.wanda.ccs.model.TAuthUser;
import com.wanda.ccs.model.TNcBdDeptdoc;
import com.wanda.ccs.model.VNcUsers;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.dao.IUniversalDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class VNcUsersServiceImpl extends BaseCrudServiceImpl<VNcUsers>
		implements VNcUsersService {

	@Autowired
	private IVNcUsersDao dao = null;
	
	@Autowired
	private IUniversalDao universlaDao;
	
	@Autowired
	private TAuthOrgAssignService orgAssignSvc;
	
	@Autowired
	private TDimTypeDefService dimSvc;
	
	@Autowired
	private TAuthUserService authUserSvc;

	@Override
	public IBaseDao<VNcUsers> getDao() {
		return dao;
	}

	/** nc用户列表 */
	@Override
	public PageResult<VNcUsers> findByCriteria(
			QueryCriteria<String, Object> query) {

		String fromClause = " select c from VNcUsers c";
		String[] whereBodies = new String[] {
				"c.pkCorp = :pkCorp",
				"c.pkDeptdoc = :pkDeptdoc",
				"c.psnname like :psnname",
				query.get("pkCorp") == null || query.get("pkCorp").equals("") ? "1 != 1":"1=1"
		};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "";
		}
		PageResult<VNcUsers> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		List<VNcUsers> list = result.getContents();
		for(VNcUsers ncUser : list){
			String stringDept = "";
			Map<String, Object> orgParams = new HashMap<String, Object>();
			String orgSql = "select c from TAuthOrgAssign c where c.pkCorp = :pkCorp";
			orgParams.put("pkCorp", query.get("pkCorp"));
			List<TAuthOrgAssign> orgList = orgAssignSvc.queryHql(orgSql, orgParams);
			if(orgList != null && !orgList.isEmpty()){
				if(orgList.get(0).getCinemaId() != null){
					stringDept +=dimSvc.getNameByTypeIdCode(104L, orgList.get(0).gettCinema().getArea())+"\\"+ orgList.get(0).gettCinema().getShortName()+"\\";
				}else if(orgList.get(0).getRegion() != null){
					stringDept +=dimSvc.getNameByTypeIdCode(104L, orgList.get(0).getRegion())+"\\";
				}else {
					stringDept += "院线\\";
				}
			}
			Map<String, Object> deptParams = new HashMap<String, Object>();
			String deptSql = "select * from T_NC_BD_DEPTDOC d start with d.pk_deptdoc = :pkDeptdoc  connect by prior  d.pk_fathedept = d.pk_deptdoc";
			deptParams.put("pkDeptdoc", ncUser.getPkDeptdoc());
			List<TNcBdDeptdoc> deptList = universlaDao.queryNativeSQL(TNcBdDeptdoc.class, deptSql,
					deptParams, null);
			int i=0;
			for(TNcBdDeptdoc deptdoc : deptList){
				stringDept += deptdoc.getDeptname() ;
				i++;
				if(i<deptList.size()){
					stringDept += "\\";
				}
				
			}
			ncUser.setStringDept(stringDept);
		}
		return result;
	}
	
	/** nc用户列表 */
	@Override
	public PageResult<VNcUsers> findAllByCriteria(
			QueryCriteria<String, Object> query) {

		String fromClause = " select c from VNcUsers c";
		String[] whereBodies = new String[] {
				"c.psnname like :psnname",
		};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.showorder";
		}
		PageResult<VNcUsers> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		List<VNcUsers> list = result.getContents();
		Map<String,Map<String,String>> dims = dimSvc.getAllDimDefs();
		for(VNcUsers ncUser : list){
			String stringDept = "";
			TAuthUser authUser = authUserSvc.getUserByPsnbasdoc(ncUser.getPkPsnbasdoc());
			if(authUser != null && authUser.getUserLevel() != null){
				if(authUser.getUserLevel().equals("G")){
					stringDept = "院线";
				}else if(authUser.getUserLevel().equals("R")){
					if(authUser.getRegion() != null)
						stringDept += dims.get(IDimType.DIMTYPE_AREA).get(authUser.getRegion()) ;
				}else if(authUser.getUserLevel().equals("C")){
					if(authUser.getRegion() != null)
						stringDept += dims.get(IDimType.DIMTYPE_AREA).get(authUser.getRegion()) ;
					if(authUser.gettCinema() != null)
						stringDept += "\\" + authUser.gettCinema().getShortName();
				}else if(authUser.getUserLevel().equals("T")){
					if(authUser.getRegion() != null)
						stringDept += dims.get(IDimType.DIMTYPE_AREA).get(authUser.getRegion()) ;
					if(authUser.gettCinema() != null)
						stringDept += "\\" + authUser.gettCinema().getShortName();
					stringDept += "(中心店)";
				}
			}
			ncUser.setStringDept(stringDept);
		}
		return result;
	}

	@Override
	public VNcUsers getVNcUserByPsndoc(String psndoc) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select c from VNcUsers c where c.pkPsndoc = :pkPsndoc";
		params.put("pkPsndoc", psndoc);
		List<VNcUsers> list = super.queryHql(hql, params);
		if (list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}

	@Override
	public VNcUsers getVNcUserByPsnbasdoc(String psnbasdoc) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select c from VNcUsers c where c.pkPsnbasdoc = :pkPsnbasdoc";
		params.put("pkPsnbasdoc", psnbasdoc);
		List<VNcUsers> list = super.queryHql(hql, params);
		if (list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}


}
