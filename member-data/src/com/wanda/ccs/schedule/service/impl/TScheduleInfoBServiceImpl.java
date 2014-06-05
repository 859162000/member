package com.wanda.ccs.schedule.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.auth.UserLevel;
import com.wanda.ccs.film.dao.ITFilmDao;
import com.wanda.ccs.model.ScheduleInfoVer;
import com.wanda.ccs.model.State;
import com.wanda.ccs.model.TScheduleInfoB;
import com.wanda.ccs.model.TScheduleInfoH;
import com.wanda.ccs.schedule.dao.ITScheduleInfoBDao;
import com.wanda.ccs.schedule.service.TScheduleInfoBService;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.dao.util.QLUtil;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.DateUtil;

@Service
public class TScheduleInfoBServiceImpl extends
		BaseCrudServiceImpl<TScheduleInfoB> implements TScheduleInfoBService {

	@Autowired
	private ITScheduleInfoBDao dao = null;
	@Autowired
	private ITFilmDao filmDao;

	@Override
	public List<TScheduleInfoB> findByCinemaAndDate(Long cinemaId, Date date) {
		String month = DateUtil.formatDate(date, "yyyyMM");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("month", month);
		params.put("cinemaId", cinemaId);
		params.put("today", DateUtil.set(date, 6, 0, 0, 0));
		params.put("tomorrow",
				DateUtil.set(DateUtil.addDays(date, 1), 6, 0, 0, 0));
		params.put("approved", Boolean.TRUE);
		String from = " from TScheduleInfoB c ";
		String[] whereBodies = new String[] {
				"c.cinemaId = :cinemaId",
				"(c.startDate<=:tomorrow or (c.startDate IS NULL and c.endDate IS NOT NULL))",
				"(c.endDate>=:today or (c.endDate IS NULL and c.startDate IS NOT NULL))",
				"c.tScheduleInfoH.month=:month",
				"c.tScheduleInfoH.approved=:approved",
				"c.tScheduleInfoH.ver = (select max(d.ver) from TScheduleInfoH d where d.month=:month and d.approved = :approved)" };
		String hql = QLUtil.generateQL(from, null, null, whereBodies, null,
				null, params);
		List<TScheduleInfoB> list = super.queryHql(hql, params);
		return list;
	}

	@Override
	public PageResult<TScheduleInfoB> findByCriteria(
			QueryCriteria<String, Object> criteria) {
		String fromClause = " select c from TScheduleInfoB c";
		String[] whereBodies = new String[] {};
		String joinClause = "";
		PageResult<TScheduleInfoB> result = getDao().queryQueryCriteria(
				fromClause, joinClause, null, whereBodies, criteria);
		return result;
	}

	@Override
	public List<TScheduleInfoB> findByHidAndFilmId(Long infoHId, Long filmId,
			CcsUserProfile user) {
		Map<String, Object> params = new HashMap<String, Object>();

		if(user!=null){
			int ordinal = user.getLevel().ordinal();
			if (ordinal == UserLevel.REGION.ordinal()) {
				params.put("area", user.getRegionCode());
			} else if (ordinal == UserLevel.CINEMA.ordinal()) {
				params.put("cinemaId", user.getCinemaId());
			}
		}
		params.put("infoHId", infoHId);
		params.put("filmId", filmId);

		String from = "select b from TScheduleInfoB b left join b.tCinema c";
		String[] whereBodies = new String[] { 
				"b.tScheduleInfoH.id=:infoHId",
				"b.filmId=:filmId", 
				"c.isOpen ='1'",
				"c.suppType not in ('01','02')",
				"b.cinemaId = :cinemaId",
				"c.area =:area"};

		String hql = QLUtil.generateQL(from, null, null, whereBodies,
				"b.tCinema.area", "asc", params);

		setQueryCacheable(false);
		return super.queryHql(hql, params);
	}

	@Override
	public List findDistinctFilmByInfoHId(Long infoHId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("infoHId", infoHId);
		String hql = "select distinct c.tFilm from TScheduleInfoB c where c.tScheduleInfoH.id = :infoHId";
		List films = super.queryHql(hql, params);
		return films;
	}

	@Override
	public List<TScheduleInfoB> findLastMonthLatelyApproved(TScheduleInfoH infoH) {

		Map<String, Object> params = new HashMap<String, Object>();
		String pattern = "yyyyMM";
		Date monthBegin = DateUtil.parse(infoH.getMonth(), pattern);
		String lastMonth = DateUtil.formatDate(
				DateUtil.addMonths(monthBegin, -1), pattern);
		params.put("lastMonth", lastMonth);
		params.put("status", State.APPROVED.getOrdinal());
		params.put("monthBegin", monthBegin);

		StringBuilder hql = new StringBuilder();

		hql.append("select new com.wanda.ccs.model.TScheduleInfoB")
				.append("(b.cinemaId, b.filmId, b.keyFilm,b.careFilm, ")
				.append(" b.copySource, b.startDate, b.endDate,b.onlineDate)")
				.append(" from TScheduleInfoB b ")
				.append(" where b.tScheduleInfoH.month = :lastMonth ")
				.append(" and b.tScheduleInfoH.status = :status ")
				.append(" and (b.endDate>=:monthBegin or (b.startDate IS NOT NULL and b.endDate IS NULL)) ")
				.append(" and b.tScheduleInfoH.ver = ")
				.append(" (select max(h.ver) from TScheduleInfoH h where h.month = :lastMonth and h.status = :status) ");

		List<TScheduleInfoB> list = super.queryHql(hql.toString(), params);
		for (TScheduleInfoB infoB : list) {
			infoB.settScheduleInfoH(infoH);
			infoB.settFilm(filmDao.findById(infoB.getFilmId()));
		}
		return list;
	}

	@Override
	public Map<String, String> findSuitFilmsBySuitDate(Date startDate,
			Date endDate, String... suitRegions) {

		String month = DateUtil.formatDate(startDate, "yyyyMM");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("month", month);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("approved", Boolean.TRUE);
		params.put("ver", ScheduleInfoVer.FIRST.getOrdinal());
		params.put("suitRegions", null);

		StringBuilder hql = new StringBuilder();
		hql.append(
				"select distinct c.filmId,c.tFilm.filmName from TScheduleInfoB c")
				.append(" where c.tScheduleInfoH.month = :month ")
				.append(" and c.tScheduleInfoH.approved = :approved ")
				.append(" and c.tScheduleInfoH.ver != (:ver) ")
				.append(" and (c.startDate <= :endDate or (c.startDate is null and c.endDate is not null)) ")
				.append(" and (c.endDate >= :startDate or (c.endDate is null and c.startDate is not null)) ");

		if (suitRegions != null && suitRegions.length > 0
				&& StringUtils.isNotBlank(StringUtils.join(suitRegions))) {
			params.put("suitRegions", suitRegions);
			hql.append(" and c.tCinema.area in (:suitRegions) ");
		}

		setQueryCacheable(true);
		List list = super.queryHql(hql.toString(), params);
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			map.put(String.valueOf((Long) o[0]), String.valueOf(o[1]));
		}
		return map;
	}

	@Override
	public IBaseDao<TScheduleInfoB> getDao() {
		return dao;
	}
}
