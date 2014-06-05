package com.wanda.ccs.schedule.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.basemgt.service.TCinemaService;
import com.wanda.ccs.model.TCinema;
import com.wanda.ccs.model.TScheduleGuideB;
import com.wanda.ccs.schedule.dao.ITScheduleGuideBDao;
import com.wanda.ccs.schedule.service.TScheduleGuideBService;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.DateUtil;
import com.xcesys.extras.util.CommonUtil;

@Service
public class TScheduleGuideBServiceImpl extends
		BaseCrudServiceImpl<TScheduleGuideB> implements TScheduleGuideBService {

	private static final String NAME_YMD = "YMD";
	private static final String NAME_AREA = "AREA";
	private static final String NAME_CINEMAID = "CINEMAID";
	private static final String NAME_FILMID = "FILMID";
	private static final String NAME_HALLS = "HALLS";
	private static final String NAME_ROUNDS = "ROUNDS";
	private static final String NAME_GUIDEBID = "GUIDEBID";
	private static final String NAME_GUIDEEXACT = "GUIDEEXACT";
	private static final String NAME_CINEMANAME = "CINEMANAME";
	private static final String NAME_HALLCOUNT = "HALLCOUNT";
	private static final String NAME_FILMNAME = "FILMNAME";
	private static final String NAME_ALARM = "ALARM";

	@Autowired
	private ITScheduleGuideBDao dao = null;

	@Autowired
	private TCinemaService cinemaService;

	@Override
	public PageResult<TScheduleGuideB> findByCriteria(
			QueryCriteria<String, Object> criteria) {
		String fromClause = " select c from TScheduleGuideB c";
		String[] whereBodies = new String[] { "c.filmId = :filmId",
				"c.cinemaId=:cinemaId", "c.scheduleGuideHId=:scheduleGuideHId" };
		String joinClause = "";
		PageResult<TScheduleGuideB> result = getDao().queryQueryCriteria(
				fromClause, joinClause, null, whereBodies, criteria);
		return result;
	}

	@Override
	public IBaseDao<TScheduleGuideB> getDao() {
		return dao;
	}

	@Override
	public List<TScheduleGuideB> findByCinemaAndDate(Long cinemaId, Date date) {
		TCinema cinema = cinemaService.getById(cinemaId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("date", date);
		params.put("approved", Boolean.TRUE);
		params.put("suitRegion", cinema.getArea());
		StringBuffer hql = new StringBuffer();
		hql.append(" from TScheduleGuideB c where ")
				.append(" c.tScheduleGuideH.suitStartDate<=:date ")
				.append(" and c.tScheduleGuideH.suitEndDate>=:date ")
				.append(" and c.tScheduleGuideH.approved>=:approved ")
				.append(" and (c.tScheduleGuideH.suitRegion = :suitRegion or c.tScheduleGuideH.suitRegion is null) ")
				.append(" and c.tScheduleGuideH.ver = ( ")
				.append(" 	select max(d.ver) from TScheduleGuideH d where ")
				.append(" 	d.issue = c.tScheduleGuideH.issue  ")
				.append(" and d.approved >=:approved )  ")
				.append(" 	order by c.tScheduleGuideH.approvedTime desc  ");
		
		setQueryCacheable(true);
		List<TScheduleGuideB> list1 = super.queryHql(hql.toString(), params);
		
		//精确型
		List<TScheduleGuideB> exatlist = new ArrayList<TScheduleGuideB>();
		Map<Long,TScheduleGuideB> exactMap = new HashMap<Long, TScheduleGuideB>();
		//描述型
		List<TScheduleGuideB> desplist = new ArrayList<TScheduleGuideB>();
		Long despHId = null;
		for (TScheduleGuideB o : list1) {
			if(o.getFilmId()==null){
				if(despHId==null){
					despHId = o.gettScheduleGuideH().getId();
				}
				if(despHId == o.gettScheduleGuideH().getId()){
					desplist.add(o);
				}
			}else{
				if(!exactMap.containsKey(o.getFilmId())){
					exactMap.put(o.getFilmId(), o);
					exatlist.add(o);
				}
			}
		}
		
		desplist.addAll(exatlist);
		return desplist;
		
	}

	@Override
	public Map<Long, Integer> findFilmIdAndCount(Long infoHId) {
		if (infoHId == null || infoHId.compareTo(0L) == 0)
			return null;

		Map<Long, Integer> map = new HashMap<Long, Integer>();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("infoHId", infoHId);
		String hql = "select c.filmId,count(c.filmId) from TScheduleGuideB c where c.tScheduleGuideH.id = :infoHId and c.filmId != null group by c.filmId";
		List<?> filmIdCount = super.queryHql(hql, params);

		for (int i = 0; i < filmIdCount.size(); i++) {
			Object[] idct = (Object[]) filmIdCount.get(i);
			map.put((Long) idct[0], NumberUtils.toInt("" + idct[1]));
		}

		return map;
	}

	@SuppressWarnings("all")
	@Override
	public PageResult findAlarmInformations(QueryCriteria criteria) {

		StringBuffer sql = new StringBuffer();
		sql.append("select ph.ymd,c.area,")
				.append("ph.cinema_Id as cinemaid,")
				.append("pb.film_Id as filmid ,")
				.append("count(distinct pb.hall_Id) as halls,")
				.append("count(*) as rounds,")
				.append("gb.seqid as guidebid,")
				.append("gb.exact as guideexact,")
				.append("c.short_Name as cinemaname,")
				.append("c.hall_Count as hallcount,")
				.append("f.film_Name as filmname  ")
				.append("from T_Schedule_Plan_B pb ,")
				.append("T_Schedule_Plan_H ph ,")
				.append("T_Cinema c ,")
				.append("T_Schedule_Guide_B gb , ")
				.append("T_Schedule_Guide_H gh,")
				.append("T_Film f ")
				.append("where pb.schedule_plan_h_id = ph.seqid ")
				.append("and ph.cinema_Id = c.seqid ")
				.append("and pb.film_Id = gb.film_Id ")
				.append("and pb.film_Id = f.seqid ")
				.append("and gb.schedule_guide_h_id = gh.seqid ")
				.append("and (ph.ymd between gh.suit_Start_Date and gh.suit_End_Date)  ")
				.append("and gh.approved = '1'  ")
				.append("and (c.area = gh.suit_Region or gh.suit_Region is null) ");
		/**
		 * 区域、影城、影片查询条件
		 */
		Object area = criteria.get("area");
		Object cinema = criteria.get("cinema");
		Object film = criteria.get("film");
		if (area != null && !"".equals(area)) {
			sql.append("and c.area = '" + area + "' ");
		}
		if (cinema != null && !"".equals(cinema)) {
			sql.append("and c.short_Name like '" + cinema + "%' ");
		}
		if (film != null && !"".equals(film)) {
			sql.append("and f.film_Name like '" + film + "%' ");
		}
		sql.append("group by ph.ymd,c.area,ph.cinema_Id,pb.film_Id ,gb.seqid,gh.issue,gb.exact,c.short_Name,c.hall_Count ,f.film_Name order by ");
		/**
		 * 排序方式
		 */
		if (criteria.getSort() != null && criteria.getSort().length() > 0) {
			sql.append(criteria.getSort()).append(" ");
		} else {
			sql.append("ph.ymd desc,c.area,ph.cinema_Id,gh.issue desc");
		}

		setQueryCacheable(false);
		String countsql = "select count(*) from (" + sql.toString() + ")";
		PageResult pr = getDao().queryNativeSQL(sql.toString(), countsql, null, criteria.getStartIndex(), criteria.getPageSize());
		List alarmList = pr.getContents();
		for (int i = 0; i < alarmList.size(); i++) {
			Map map = (Map)alarmList.get(i);
			String alarm = getAlarmInformation(map);
			if (alarm != null && alarm.length() > 0) {
				map.put(NAME_ALARM, alarm);
			}
		}
		return pr;
	}

	@SuppressWarnings("rawtypes")
	private String getAlarmInformation(Map map) {
		String alertInfo = "符合指导信息";

		String exact = "" + map.get(NAME_GUIDEEXACT);
		String cinemahalls = "" + map.get(NAME_HALLCOUNT);
		String halls = "" + map.get(NAME_HALLS);
		String rounds = "" + map.get(NAME_ROUNDS);
		Map<String, String> exactMap = CommonUtil
				.splitScheduleGuideExact(exact);
		if (exactMap == null) {
			return null;
		}
		String info = exactMap.get(cinemahalls);
		int comp = 0;
		if (StringUtils.isNotBlank(info)) {
			if (info.endsWith("场")) {
				comp = CommonUtil.compareWithRange(rounds,
						info.substring(0, info.length() - 1));
				if (comp == -1) {
					alertInfo = "未达最低场次数";
				} else if(comp == 1){
					alertInfo = "超过最高场次数";
				}
			} else if (info.endsWith("厅")) {
				comp = CommonUtil.compareWithRange(halls,
						info.substring(0, info.length() - 1));
				if (comp == -1) {
					alertInfo = "未达最低影厅数";
				} else if(comp == 1){
					alertInfo = "超过最高影厅数";
				}
			} 
		}

		return alertInfo;
	}

	@Override
	@SuppressWarnings("all")
	public Map<Long, Map<String, Integer>> findAlarmMonthStatistics(
			QueryCriteria criteria) {
		Map<Long, Map<String, Integer>> result = new LinkedHashMap<Long, Map<String, Integer>>();

		criteria.setSort("c.area,ph.cinema_Id,ph.ymd desc,gh.issue desc ");
		List contents = findAlarmInformations(criteria).getContents();
		for (Object obj : contents) {
			Map<String, Object> map = (HashMap<String, Object>) obj;
			String alarm = "" +map.get(NAME_ALARM);
			if(!alarm.equals("符合指导信息")){
				String ymd = DateUtil.formatDate((Date) map.get(NAME_YMD),
						DateUtil.PATTERN_DATE_YYYYMMDD);
				String ym = ymd.substring(0, 6);
				Long cinemaId = NumberUtils.toLong(String.valueOf(map
						.get(NAME_CINEMAID)));
			
				Map<String, Integer> ymcMap = result.get(cinemaId);
				if (ymcMap == null) {
					ymcMap = new LinkedHashMap<String, Integer>();
					ymcMap.put(ym, 1);
				} else {
					int c = ymcMap.get(ym) == null ? 0 : ymcMap.get(ym);
					ymcMap.put(ym, ++c);
				}
				result.put(cinemaId, ymcMap);
			}

		}
		return result;
	}

}
