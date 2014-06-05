package com.wanda.ccs.schedule.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.basemgt.service.TCinemaService;
import com.wanda.ccs.model.TCinema;
import com.wanda.ccs.model.TScheduleParam;
import com.wanda.ccs.schedule.dao.ITScheduleParamDao;
import com.wanda.ccs.schedule.service.TScheduleParamService;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.StringUtil;

@Service
public class TScheduleParamServiceImpl extends
		BaseCrudServiceImpl<TScheduleParam> implements TScheduleParamService {

	@Autowired
	private ITScheduleParamDao dao = null;

	@Autowired
	private TCinemaService cinemaService;

	@Override
	public IBaseDao<TScheduleParam> getDao() {
		return dao;
	}

	@Override
	public boolean checkExisted(String region, Long cityId, Long cinemaId,
			String hallType, Long filmId) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("region", region);
		params.put("cityId", cityId);
		params.put("cinemaId", cinemaId);
		params.put("hallType", hallType);
		params.put("filmId", filmId);

		StringBuffer hql = new StringBuffer();
		hql.append("from TScheduleParam c where c.filmId = :filmId ");
		hql.append(StringUtil.isNullOrBlank(region) ? " and c.region IS NULL " : " and c.region = :region ");
		hql.append(StringUtil.isNullOrBlank(hallType) ? " and c.hallType IS NULL " : " and c.hallType = :hallType ");
		hql.append(cityId == null ? " and c.cityId IS NULL " : " and c.cityId = :cityId ");
		hql.append(cinemaId == null ? " and c.cinemaId IS NULL " : " and c.cinemaId = :cinemaId ");
		
		int count = super.queryCountHql(hql.toString(), params, null);
		return count > 0;
	}

	private List<TScheduleParam> findByCinemaAndFilm(Long cinemaId,
			String hallType, Long[] filmIds) {

		TCinema cinema = cinemaService.findById(cinemaId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("filmIds", filmIds);
		params.put("cinemaId", cinema.getId());

		StringBuilder hql = new StringBuilder(
				"select c from TScheduleParam c where (c.cinemaId=:cinemaId ");

		// 城市
		Long cityId = cinema.getCityId();
		if (cityId != null) {
			hql.append(" or (c.cinemaId is null and c.cityId = :cityId) ");
			params.put("cityId", cityId);
		}

		// 区域
		String area = cinema.getArea();
		if (!StringUtil.isNullOrBlank(area)) {
			hql.append(" or (c.cinemaId is null and c.cityId is null and c.region =:region) ");
			params.put("region", area);
		}
		hql.append(" or c.region is null) and c.filmId in (:filmIds) ");

		// 影厅类型
		if (!StringUtil.isNullOrBlank(hallType)) {
			hql.append(" and (c.hallType=:hallType or c.hallType is null) ");
			params.put("hallType", hallType);
		}
		super.setQueryCacheable(true);
		return super.queryHql(hql.toString(), params);
	}

	@Override
	public PageResult<TScheduleParam> findByCriteria(
			QueryCriteria<String, Object> criteria) {
		String fromClause = "select c from TScheduleParam c left join fetch c.cinema cc left join fetch c.film f left join c.city ct ";
		String[] whereBodies = new String[] { "c.filmId = :filmId",
				"UPPER(f.filmName) like UPPER(:filmName)",
				"c.region = :region or c.region is null", "c.cityId = :cityId",
				"f.premiereDate <= :endDate", "f.endDate >= :startDate",
				"c.cinemaId = :cinemaId" };
		String joinClause = "";
		String orderClause = null;
		if (StringUtil.isNullOrBlank(criteria.getSort())) {
			orderClause = "c.region, ct.name, cc.shortName";
		}
		PageResult<TScheduleParam> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, criteria);
		return result;
	}

	@Override
	public Map<Long, TScheduleParam> getParamsMap(Long cinemaId,
			String hallType, Long[] filmIds) {
		List<TScheduleParam> list = findByCinemaAndFilm(cinemaId, hallType,
				filmIds);
		Map<Long, TScheduleParam> map = new HashMap<Long, TScheduleParam>();
		if (list != null && !list.isEmpty()) {
			for (TScheduleParam p : list) {
				Long filmId = p.getFilmId();
				TScheduleParam param = map.get(filmId);

				if (param == null) {
					map.put(filmId, p);
					continue;
				}
				if (!StringUtil.isNullOrBlank(p.getHallType())) {
					// 首先是设定在影厅类型的参数。
					map.put(filmId, p);
				} else if (p.getCinemaId() != null
						&& StringUtil.isNullOrBlank(param.getHallType())) {
					// 首先是设定在影院上的参数。
					map.put(filmId, p);
				} else if (p.getCityId() != null && param.getCinemaId() == null
						&& StringUtil.isNullOrBlank(param.getHallType())) {
					// 影院上未设定则查找城市上设定的参数。
					map.put(filmId, p);
				} else if (!StringUtil.isNullOrBlank(p.getRegion())
						&& param.getCinemaId() == null
						&& param.getCityId() == null
						&& StringUtil.isNullOrBlank(param.getHallType())) {
					map.put(filmId, p);
				}
			}
		}
		return map;
	}

	@Override
	public Map<Long, Map<String, TScheduleParam>> getParamsMap(Long cinemaId,
			Long[] filmIds) {
		List<TScheduleParam> list = findByCinemaAndFilm(cinemaId, null, filmIds);
		Map<Long, Map<String, TScheduleParam>> resultMap = new HashMap<Long, Map<String, TScheduleParam>>();
		if (list != null && !list.isEmpty()) {
			for (TScheduleParam p : list) {
				Long filmId = p.getFilmId();

				Map<String, TScheduleParam> tpMap = resultMap.get(filmId);
				if (tpMap == null) {
					tpMap = new HashMap<String, TScheduleParam>();
					tpMap.put(p.getHallType(), p);
					resultMap.put(filmId, tpMap);
					continue;
				}

				TScheduleParam param = tpMap.get(p.getHallType());
				if(param == null){
					tpMap.put(p.getHallType(), p);
					continue;
				}
				if (p.getCinemaId() != null) {
					// 首先是设定在影院上的参数。
					tpMap.put(p.getHallType(), p);
				} else if (p.getCityId() != null && param.getCinemaId() == null) {
					// 影院上未设定则查找城市上设定的参数。
					tpMap.put(p.getHallType(), p);
				} else if (!StringUtil.isNullOrBlank(p.getRegion())
						&& param.getCinemaId() == null
						&& param.getCityId() == null) {
					tpMap.put(p.getHallType(), p);
				}
			}
		}
		return resultMap;
	}
}
