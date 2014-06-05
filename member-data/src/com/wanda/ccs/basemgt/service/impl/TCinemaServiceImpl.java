package com.wanda.ccs.basemgt.service.impl;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.auth.service.TAuthOrgAssignService;
import com.wanda.ccs.basemgt.dao.ITCinemaDao;
import com.wanda.ccs.basemgt.service.TCinemaService;
import com.wanda.ccs.model.TAuthOrgAssign;
import com.wanda.ccs.model.TCinema;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.dao.util.QLUtil;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.StringUtil;

@Service
public class TCinemaServiceImpl extends BaseCrudServiceImpl<TCinema> implements
		TCinemaService {

	@Autowired
	private ITCinemaDao dao = null;
	@Autowired
	private TAuthOrgAssignService orgAssignSvc;

	@Override
	public IBaseDao<TCinema> getDao() {
		return dao;
	}

	@Override
	public PageResult<TCinema> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = " select c from TCinema c ";
		String[] whereBodies = new String[] {
				"c.shortName like :shortName", 
				"c.area = :area",
				"c.id = :cinemaId"};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.code,c.shortName";
		}
		PageResult<TCinema> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;
	}

	@Override
	public TCinema findByCode(String code) {
		if (StringUtil.isNullOrBlank(code))
			return null;

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", code);
		List<TCinema> list = super.queryHql(
				"from TCinema c where c.code=:code", params);
		if (list == null || list.size() == 0)
			return null;
		return list.get(0);
	}

	@Override
	public boolean checkExisted(String code) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TCinema c where ";
		if (!StringUtil.isNullOrBlank(code)) {
			hql += " c.code=:code";
			params.put("code", code);
		} else {
			return false;
		}
		int count = super.queryCountHql(hql, params, null);
		return count > 0;
	}

	@Override
	public boolean checkExistedByShortName(String shortName) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TCinema c where ";
		if (!StringUtil.isNullOrBlank(shortName)) {
			hql += " c.shortName=:shortName";
			params.put("shortName", shortName);
		} else {
			return false;
		}
		int count = super.queryCountHql(hql, params, null);
		return count > 0;
	}

	@Override
	public boolean checkExistedByOutName(String outName) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TCinema c where ";
		if (!StringUtil.isNullOrBlank(outName)) {
			hql += " c.outName=:outName";
			params.put("outName", outName);
		} else {
			return false;
		}
		int count = super.queryCountHql(hql, params, null);
		return count > 0;
	}

	@Override
	public List<TCinema> findUnDeletedCinemas(Long... city) {
		String fromClause = "from TCinema c where 1=1 ";

		if (city != null && city.length > 0) {
			if (city.length == 1) {
				fromClause += " and c.cityId = :city";
			} else {
				fromClause += " and c.cityId in (:city)";
			}
		}
		fromClause += " and  c.suppType not in ('01','02') and c.isOpen = 1 order by c.area";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("city", city);
		List<TCinema> lst = getDao().queryHql(fromClause, params);
		return lst;
	}

	@Override
	public List<TCinema> findCinemasOrderByName(String area) {
		Map<String, Object> params = new HashMap<String, Object>();
		String fromClause = " from TCinema c where c.suppType not in ('01','02') and c.isOpen = 1 ";
		if(area != null && !"".equals(area)){
			fromClause += " and c.area=:area";
			params.put("area", area);
		}
		fromClause += " order by c.innerName asc";
		setQueryCacheable(true);
		List<TCinema> lst = getDao().queryHql(fromClause, params);
		return lst;
	}

	@Override
	public Map<Long, TCinema> findUnDeletedCinemasMap() {

		String fromClause = " select c from TCinema c where c.suppType not in ('01','02') and c.isOpen = 1 order by c.area";
		List<TCinema> lst = getDao().queryHql(fromClause, null);
		Map<Long, TCinema> cinemasMap = new HashMap<Long, TCinema>();
		for (TCinema cinema : lst) {
			cinemasMap.put(cinema.getId(), cinema);
		}
		setQueryCacheable(true);
		return cinemasMap;
	}
	@Override
	public Map<Long, TCinema> findUnDeletedCinemasMapByReqgion(String region) {

		String fromClause = " select c from TCinema c where c.suppType not in ('01','02') and c.isOpen = 1 and c.area = :region order by c.area";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("region", region);
		List<TCinema> lst = getDao().queryHql(fromClause, params);
		Map<Long, TCinema> cinemasMap = new HashMap<Long, TCinema>();
		for (TCinema cinema : lst) {
			cinemasMap.put(cinema.getId(), cinema);
		}
		setQueryCacheable(true);
		return cinemasMap;
	}

	@Override
	public List<TCinema> findCinemasByArea(String area) {
		String hql = " select c from TCinema c where c.suppType not in ('01','02') and c.isOpen = 1 ";
		Map<String, Object> params = new HashMap<String, Object>();

		if (!StringUtil.isNullOrBlank(area)) {
			hql += " and c.area=:area";
			params.put("area", area);
		}
		setQueryCacheable(true);
		return getDao().queryHql(hql, params);
	}

	@Override
	public Map<Long, String> getCinamasByArea(String area) {
		Map<Long, String> map = new Hashtable<Long, String>();
		String hql = " select c from TCinema c where c.suppType not in ('01','02') and c.isOpen = 1 ";
		Map<String, Object> params = new HashMap<String, Object>();

		if (!StringUtil.isNullOrBlank(area)) {
			hql += " and c.area=:area";
			params.put("area", area);
		}
		setQueryCacheable(true);
		List<TCinema> list = getDao().queryHql(hql, params);
		for (TCinema c : list) {
			map.put(c.getId(), c.getShortName());
		}
		return map;
	}

	/** 获取所有未删除的影院列表(价格策略的不可用规则中使用) */
	@Override
	public Map<String, String> findUnDeletedCinema() {
		String fromClause = " select c from TCinema c  order by c.area";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deleted", Boolean.FALSE);
		List<TCinema> lst = getDao().queryHql(fromClause, params);
		Map<String, String> cinemasMap = new HashMap<String, String>();
		for (TCinema cinema : lst) {
			cinemasMap.put(cinema.getId().toString(), cinema.getShortName());
		}
		return cinemasMap;
	}

	@Override
	public boolean checkExistedByInnerName(String innerName) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TCinema c where ";
		if (!StringUtil.isNullOrBlank(innerName)) {
			hql += " c.innerName=:innerName";
			params.put("innerName", innerName);
		} else {
			return false;
		}
		int count = super.queryCountHql(hql, params, null);
		return count > 0;
	}

	@Override
	public String findMaxInnerCode() {
		String maxInnerCode = null;
		String hql = "select max(cast(c.innerCode as int)) from TCinema c";
		Object uniqueResult = getUniversalDao().uniqueResult(hql, null);
		if(uniqueResult != null){
			maxInnerCode = String.valueOf(uniqueResult);
		}
		return maxInnerCode;
	}

	@Override
	public Map<String, Map<String, String>> getAllCinemas() {
		Map<String, Map<String, String>> maps = new LinkedHashMap<String, Map<String, String>>();

		List<TCinema> defs = (List<TCinema>) super.queryHql("from TCinema c",
				null);

		if (defs != null && !defs.isEmpty()) {
			for (TCinema def : defs) {
				String regionCode = def.getArea();
				Map<String, String> map = maps.get(regionCode);
				if (map == null) {
					map = new LinkedHashMap<String, String>();
					maps.put(regionCode, map);
				}
				map.put(def.getId()+"", def.getShortName());
			}
		}
		return maps;
	}
	
	@Override
	public List<TCinema> findByLikeCodeOrName(String like) {
		if (!StringUtil.isNullOrBlank(like)) {
			like = like.toLowerCase();
		}
		String hql = "from TCinema where (upper(pinCode) like upper(:like) or upper(outName) like upper(:like)) and suppType not in ('01','02') and isOpen = 1 order by outName";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("like", "%" + like + "%");
		PageResult<TCinema> result = super.queryHql(hql,
				QLUtil.generateCountQL(hql, null), params, 0, 100);
		return result.getContents();
	}
	@Override
	public List<TCinema> getCinamasByRegion(String region) {
		String hql = " select c from TCinema c where c.suppType not in ('01','02') and c.isOpen = 1 ";
		Map<String, Object> params = new HashMap<String, Object>();

		if (!StringUtil.isNullOrBlank(region)) {
			hql += " and c.area=:area";
			params.put("area", region);
		}
		setQueryCacheable(true);
		List<TCinema> list = getDao().queryHql(hql, params);
		for (TCinema c : list) {
			TAuthOrgAssign orgAssign = orgAssignSvc.getOrgAssignByCinema(c.getId());
			if(orgAssign!= null){
				c.setHasChild(true);
				c.setPkCorp(orgAssign.getPkCorp());
			}
		}
		return list;
	}

	@Override
	public Map<Long, String> findAllCinemas() {
		String fromClause = " select c from TCinema c order by c.area";
		List<TCinema> lst = getDao().queryHql(fromClause, null);
		Map<Long, String> cinemasMap = new HashMap<Long, String>();
		for (TCinema cinema : lst) {
			cinemasMap.put(cinema.getId(), cinema.getShortName());
		}
		return cinemasMap;
	}
	@Override
	public List< Map<String, ?>> getMonitorLoadFile(QueryCriteria<String, Object> query){
		
		Map<String, Object> params = new HashMap<String, Object>();
//		"select c.code,c.inner_name,m.DAY,m.fsize from t_cinema c,(select INNER_CODE,DAY ,count(DAY) fsize from (select distinct INNER_CODE,DAY,FILE_NAME,MONTH from T_MONITOR_UPLOAD_FILE)  where MONTH=:month group by INNER_CODE,DAY) m where c.INNER_CODE=m.INNER_CODE";
		String sql= "select c.code,c.inner_name,c.inner_code,m.DAY,m.fsize from t_cinema c,(select INNER_CODE,DAY ,count(DAY) fsize from (select distinct INNER_CODE,DAY,FILE_NAME,MONTH from T_MONITOR_UPLOAD_FILE)  where 1=1 ";
		if(query.get("month") != null && !query.get("month").toString().equals("")){
			params.put("month", query.get("month").toString());
			sql += " and MONTH=:month ";
		}
		sql += " group by INNER_CODE,DAY) m where c.INNER_CODE=m.INNER_CODE ";
		if(query.get("cinemaName") != null && !query.get("cinemaName").toString().equals("")){
			params.put("cinemaName", "%"+query.get("cinemaName").toString()+"%");
			sql += " and c.inner_name like :cinemaName ";
		}
		return dao.queryNativeSQL(sql, params);
	}
	
	@Override
	public List< Map<String, ?>> getLoadFileByDayAndCinemaCode(String day,String innerCode){
		if(day == null || day.equals("") || innerCode == null || innerCode.equals(""))
			return null;
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("day", day);
		params.put("innerCode", innerCode);
		String sql="select * from T_MONITOR_UPLOAD_FILE where day= :day and inner_code= :innerCode order by file_name";
		return dao.queryNativeSQL(sql, params);
	}
	
	@Override
	public List< Map<String, ?>> getCinemaDaysGroup(String moth){
		if(StringUtil.isNullOrBlank(moth))
			return null;
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("moth", moth);
		

		String sql= "select dg.days,dg.cinemacode,c.inner_name,dg.right_flag,dg.right_ticket_flag,dg.right_goods_flag ,dg.descs from T_POS_DAY_GROUP dg,T_cinema c where dg.cinemacode=c.code and to_char(to_date(dg.months,'yyyy-mm-dd'),'yyyymm')= :moth order by dg.days";
		return dao.queryNativeSQL(sql, params);
	}
	
	@Override
	public List< Map<String, ?>> getAllOnlineCinema(){
		String sql="select c.code,c.inner_name from T_INTF_CINEMA_URL iu ,t_CINEMA c where iu.CINEMAID=c.SEQID";
		return dao.queryNativeSQL(sql, null);
	}

	@Override
	public int updateCodeIntfClientInfo(String oldcode, String newcode) {
		String hql="update TIntfClientinfo set cinemacode = :newcode where cinemacode = :oldcode";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("newcode", newcode);
		params.put("oldcode", oldcode);
		return dao.updateHql(hql, params);
	}

}
