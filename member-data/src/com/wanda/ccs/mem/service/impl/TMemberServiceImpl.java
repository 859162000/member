package com.wanda.ccs.mem.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aggrepoint.adk.IModuleRequest;
import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.basemgt.service.TCinemaService;
import com.wanda.ccs.mem.dao.ITMemberDao;
import com.wanda.ccs.mem.service.TMemberAddrService;
import com.wanda.ccs.mem.service.TMemberFavContactsService;
import com.wanda.ccs.mem.service.TMemberFavFilmTypeService;
import com.wanda.ccs.mem.service.TMemberInfoService;
import com.wanda.ccs.mem.service.TMemberLogService;
import com.wanda.ccs.mem.service.TMemberService;
import com.wanda.ccs.model.TCinema;
import com.wanda.ccs.model.TMember;
import com.wanda.ccs.model.TMemberLog;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.DateUtil;
import com.xcesys.extras.core.util.StringUtil;
import com.xcesys.extras.util.SpringContextUtil;

@Service
public class TMemberServiceImpl extends BaseCrudServiceImpl<TMember> implements
		TMemberService {

	@Autowired
	private ITMemberDao dao;

	@Autowired
	private TMemberAddrService tMemberAddrService;

	@Autowired
	private TMemberInfoService tMemberInfoService;

	@Autowired
	private TMemberFavContactsService facContactService;

	@Autowired
	private TMemberFavFilmTypeService facFilmTypeService;

	@Autowired
	private TMemberLogService tMemberLogService;

	@Override
	public IBaseDao<TMember> getDao() {
		return dao;
	}

	@Override
	public PageResult<TMember> findByCriteria(
			QueryCriteria<String, Object> query) {
		Vector<String> queryParam = new Vector<String>();
		if (query.get("pagemember") != null) {
			query.setPage(Integer.parseInt(String.valueOf(query.get("pagemember"))));
		}
		String fromClause = " from TMember c ";
		//queryParam.add("c.isDeleted = :memberisdeleted ");
		queryParam.add("c.mobile =:membermobile");
		queryParam.add("c.name like :name");
		queryParam.add("c.tMackDaddyCard.cardNumber =:tMackDaddyCardNo");//会员万人迷卡卡号
		queryParam.add("c.email =:email");
		queryParam.add("c.registCinemaId =:registCinemaId");
		queryParam.add("c.tMemberInfo.manageCinema =:manageCinema");
		queryParam.add("c.tMemberInfo.idCardType =:idCardType");
		queryParam.add("c.tMemberInfo.idCardNo =:idCardNo");
		//queryParam.add("c.status = :memberstatus");
		
//		if(query.get("query_start") != null && !"".equals(query.get("query_start"))){
//			query.put("startTime", query.get("query_start").toString()+" 00:00:00");
//		}
//		if(query.get("query_end") != null && !"".equals(query.get("query_end"))){
//			query.put("endTime", query.get("query_end").toString()+" 23:59:59");
//		}
		queryParam.add("c.registDate >= to_date(:query_start,'yyyy-mm-dd hh24:mi:ss')");
		queryParam.add("c.registDate <= to_date(:query_end,'yyyy-mm-dd hh24:mi:ss')");
		
		if(query.get("minExgPointBalance") != null && !"".equals(query.get("minExgPointBalance"))){
			query.put("minExgPointBalance", Long.parseLong((String)query.get("minExgPointBalance").toString()));
		}
		if(query.get("maxExgPointBalance") != null && !"".equals(query.get("maxExgPointBalance"))){
			query.put("maxExgPointBalance", Long.parseLong((String)query.get("maxExgPointBalance").toString()));
		}
		queryParam.add("c.tMemberPoint.exgPointBalance >= :minExgPointBalance");
		queryParam.add("c.tMemberPoint.exgPointBalance <= :maxExgPointBalance");
		
		
		if(query.get("channelId") != null && !"".equals(query.get("channelId"))){
			query.put("channelId", query.get("channelId"));
		}
		queryParam.add("c.channelId =:channelId");
		
		if("F".equals(query.get("hasResult"))){
			queryParam.add(" 1 !=1 ");
		}
		//解除登陆用户查询数据的限制
//		if(query.get("cinemaIds") != null && (query.get("registCinemaId")==null || "".equals(query.get("registCinemaId")))){
//			queryParam.add("c.registCinemaId in(:cinemaIds)");
//		}
		String[] whereBodies = queryParam
				.toArray(new String[queryParam.size()]);
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = " c.memberNo ,c.mobile";
		}
		PageResult<TMember> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;

	}

	@Override
	public void createOrUpdate(TMember tMember) {
		super.createOrUpdate(tMember);
		
		if (tMember.gettMemberAddrs() != null) {
			tMember.gettMemberAddrs().setMemberId(tMember.getId());
			tMemberAddrService.createOrUpdate(tMember.gettMemberAddrs());
		}
		if (tMember.gettMemberInfo() != null) {
			tMember.gettMemberInfo().setMemberId(tMember.getId());
			tMemberInfoService.createOrUpdate(tMember.gettMemberInfo());
		}
		if (tMember.gettMemFavContacts() != null) {
			tMember.gettMemFavContacts().setMemberId(tMember.getId());
			facContactService.createOrUpdate(tMember.gettMemFavContacts());
		}
		if (tMember.gettMemFavFilmtypes() != null) {
			tMember.gettMemFavFilmtypes().setMemberId(tMember.getId());
			facFilmTypeService.createOrUpdate(tMember.gettMemFavFilmtypes());
		}
//		if (tMemberPointService.getTMemberPointByMemId(tMember.getId()) == null) {
//			IntegralInitialization integral = new IntegralInitialization();
//			integral.addMemberPointByID(super.getUniversalDao().getConnection(), tMember.getId(), tMember.getCreatedBy(), 0l);
//		}
		super.createOrUpdate(tMember);
		
		TMemberLog tMemberLog = new TMemberLog();
		tMemberLog.setChangedBy(tMember.getUpdatedBy());
		tMemberLog.setChangedDate(new Date());
		tMemberLog.setMeberId(tMember.getId());
		tMemberLog.setMemberMobile(tMember.getMobile());
		if(tMember.getIsDeleted()!= null){
			tMemberLog.setMemberDeleted(tMember.getIsDeleted().toString());
		}else{
			tMemberLog.setMemberDeleted("0");
		}
		if(tMember.getStatus()!=null){
			tMemberLog.setMemberStatus(tMember.getStatus().toString());
		}else{
			tMemberLog.setMemberStatus("1");
		}
		
		tMemberLog.setBirthday(tMember.getBirthday());
		tMemberLog.setRegistCinemaId(tMember.getRegistCinemaId());
		tMemberLog.setIsContactable(tMember.getContactable().toString());
		tMemberLogService.createOrUpdate(tMemberLog);
//		initMemberPointAndLevel(tMember);
	}

	@Override
	public void deleteTMembers(Long[] ids) {
		for (int i = 0; i < ids.length; i++) {
			Long id = ids[i];
			TMember tMem = getDao().findById(id);
			tMem.setIsDeleted(1l);
			tMem.setMobile(tMem.getMobile()+"_"+new Date().getTime());
			tMem.setUpdatedDate(DateUtil.getCurrentDate());
			getDao().update(tMem);
			TMemberLog tMemberLog = new TMemberLog();
			tMemberLog.setChangedBy(tMem.getUpdatedBy());
			tMemberLog.setChangedDate(new Date());
			tMemberLog.setMeberId(tMem.getId());
			tMemberLog.setMemberMobile(tMem.getMobile());
			tMemberLog.setMemberDeleted(tMem.getIsDeleted().toString());
			tMemberLog.setMemberStatus(tMem.getStatus().toString());
			tMemberLog.setRegistCinemaId(tMem.getRegistCinemaId());
			tMemberLog.setIsContactable(tMem.getContactable().toString());
			tMemberLog.setBirthday(tMem.getBirthday());
			tMemberLogService.createOrUpdate(tMemberLog);
		}
	}

	@Override
	public boolean checkMobile(String mobileNo,Long status) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TMember c where ";
		if (!StringUtil.isNullOrBlank(mobileNo)) {
			hql += " c.mobile=:mobileNo and c.status = :status";
			params.put("mobileNo", mobileNo);
			params.put("status", status);
		} else {
			return false;
		}
		int count = super.queryCountHql(hql, params, null);
		return count > 0;
	}

	@Override
	public String getMemNumTop(Long cinemaId) {

		String hql = "select max(cast(SUBSTR(c.MEMBER_NO,5) as int)) as MAXNUM from T_MEMBER c  ";
		String memNum = null;
		Map<String, Object> params = new HashMap<String, Object>();
		if (cinemaId > 0 && cinemaId != null) {
			TCinema tCinema = SpringContextUtil.getBean(TCinemaService.class)
					.findById(cinemaId);
			params.put("memberNo", "%C" + tCinema.getInnerCode() + "%");
			hql += "  where c.MEMBER_NO like :memberNo ";

		}
		List<Map<String,?>> list = getDao().queryNativeSQL(hql, params);
		if(list != null && !list.isEmpty()&& list.get(0) != null && !list.get(0).isEmpty()
				&& list.get(0).get("MAXNUM") != null){
			memNum = String.valueOf(list.get(0)
					.get("MAXNUM"));
		}
		
		return memNum;
	}

	@Override
	public String getMemSeqForMemNum() {
		String sql = "select S_MEMBER_NUM.NEXTVAL AS MEMBERNUM FROM dual";
		String memNum = null;
		setQueryCacheable(false);
		List<Map<String,?>> list = getDao().queryNativeSQL(sql, null);
		if(list != null && !list.isEmpty()&& list.get(0) != null && !list.get(0).isEmpty()
				&& list.get(0).get("MEMBERNUM") != null){
			memNum = String.valueOf(list.get(0)
					.get("MEMBERNUM"));
		}else{
			System.err.println("================================异常==========================================");
			memNum = String.valueOf( new Date().getTime());
		}
		return memNum;
	}

	@Override
	public void updateMemberStatus(String strIds, IModuleRequest req ,Long status,String changeResion) {
		
		String[] strs = strIds.split(",");

		CcsUserProfile user = (CcsUserProfile) req.getUserProfile();
		for (int i = 0; i < strs.length; i++) {
			if ("on".equals(strs[i])) {

			} else {
				TMember tMember = findById(Long.valueOf(strs[i]));
				tMember.setStatus(status);
				tMember.setChangeStatusResion(changeResion);
				tMember.setUpdatedBy(user.getId());
				tMember.setUpdatedDate(new Date());
				super.createOrUpdate(tMember);
				
				TMemberLog tMemberLog = new TMemberLog();
				tMemberLog.setChangedBy(tMember.getUpdatedBy());
				tMemberLog.setChangedDate(new Date());
				tMemberLog.setMeberId(tMember.getId());
				tMemberLog.setMemberMobile(tMember.getMobile());
				tMemberLog.setMemberDeleted(tMember.getIsDeleted().toString());
				tMemberLog.setMemberStatus(tMember.getStatus().toString());
				tMemberLog.setRegistCinemaId(tMember.getRegistCinemaId());
				tMemberLog.setIsContactable(tMember.getContactable().toString());
				tMemberLog.setBirthday(tMember.getBirthday());
				tMemberLogService.createOrUpdate(tMemberLog);
			}
		}
		
	}
	
	@Override
	public String checkMobile(String mobileNo) {
		String sql = "select MEMBER_NO from T_MEMBER mem where mem.MOBILE = :mobileNo and mem.ISDELETE = 0";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobileNo", mobileNo);
		getDao().setCacheable(false);
		List<Map<String,?>> list = getDao().queryNativeSQL(sql, params);
		if (list != null && !list.isEmpty()&& list.get(0) != null && !list.get(0).isEmpty()
				&& list.get(0).get("MEMBER_NO") != null) {
			return list.get(0).get("MEMBER_NO").toString();
		}else{
			return null;
		}
	}

}
