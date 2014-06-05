package com.wanda.ccs.basemgt.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.wanda.ccs.basemgt.dao.ITHallDao;
import com.wanda.ccs.basemgt.service.TCinemaService;
import com.wanda.ccs.basemgt.service.THallService;
import com.wanda.ccs.model.TCinema;
import com.wanda.ccs.model.THall;
import com.wanda.ccs.model.UtilType;
import com.wanda.ccs.service.SpringCommonService;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class THallServiceImpl extends BaseCrudServiceImpl<THall> implements
		THallService {

	@Autowired
	private ITHallDao dao = null;

	@Autowired
	private TCinemaService cinemaService;

	@Override
	public IBaseDao<THall> getDao() {
		return dao;
	}

	@Override
	public PageResult<THall> findByCriteria(QueryCriteria<String, Object> query) {
		String fromClause = "from THall h left join fetch h.tCinema c";
		String[] whereBodies = new String[] { 
				"h.cinemaId = :cinemaId ",
				"UPPER(c.shortName) like UPPER(:cinemaName) ",
				"h.name = :name", 
				"h.deleted = :deleted",
				"c.area = :area"};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "h.name";
		}

		query.put("deleted", false);
		PageResult<THall> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;

	}

	@Override
	public boolean checkExisted(long cinemaId, String hallcode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cinemaId", cinemaId);
		params.put("name", hallcode);
		params.put("deleted", false);
		String hql = "from THall c where c.deleted =:deleted and c.cinemaId =:cinemaId and c.name =:name";
		int count = super.queryCountHql(hql, params, null);
		return count > 0;
	}

	@Override
	public Map<Long, String> findUnDeletedHalls() {
		Map<Long, String> map = new HashMap<Long, String>();

		String fromClause = " select c from THall c where c.deleted =:deleted";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deleted", Boolean.FALSE);
		List<THall> lst = getDao().queryHql(fromClause, params);

		for (THall gh : lst) {
			map.put(gh.getId(), gh.getName());
		}
		return map;
	}

	@Override
	public void createOrUpdate(THall newHall) {

		Assert.isTrue(newHall != null, "影厅对象为空，请重新保存");

		newHall.setIsDigital(Boolean.FALSE);
		newHall.setIsFilm(Boolean.FALSE);
		newHall.setIs3d(Boolean.FALSE);
		newHall.setIsImax(Boolean.FALSE);
		newHall.setIsReald(Boolean.FALSE);

		String types = Arrays.toString(newHall.getProjectTypes());
		List<String> val = null;
		/**
		 * T_SETTING数据格式为：
		 * PROJECTION_TYPE_DIGIT	01,02,06
		 * PROJECTION_TYPE_FILM		05,07
		 * PROJECTION_TYPE_IMAX		05,06
		 * PROJECTION_TYPE_3D		05,06,08,09,10
		 * PROJECTION_TYPE_REALD	09
		 */
		Map<String, List<String>> settingMap = SpringCommonService
				.getAllTSettings();

		for (String key : settingMap.keySet()) {
			val = settingMap.get(key);
			int idx = StringUtils.indexOfAny(types,
					val.toArray(new String[val.size()]));
			if (UtilType.HALL_DIGITAL.equals(key)) {
				if (idx != -1) {
					newHall.setIsDigital(Boolean.TRUE);
				}
			} else if (UtilType.HALL_FILM.equals(key)) {
				if (idx != -1) {
					newHall.setIsFilm(Boolean.TRUE);
				}
			} else if (UtilType.HALL_THREE_D.equals(key)) {
				if (idx != -1) {
					newHall.setIs3d(Boolean.TRUE);
				}
			} else if (UtilType.HALL_IMAX.equals(key)) {
				if (idx != -1) {
					newHall.setIsImax(Boolean.TRUE);
				}
			} else if (UtilType.HALL_REALD.equals(key)) {
				if (idx != -1) {
					newHall.setIsReald(Boolean.TRUE);
				}
			}
		}
		super.createOrUpdate(newHall);
		TCinema cinema = cinemaService.getById(newHall.getCinemaId());
		newHall.settCinema(cinema);
		cinema.gettHalls().add(newHall);
		updateCinema(cinema);
	}

	@Override
	public void delete(Collection<THall> list) {
		for (THall h : list) {
			delete(h);
			updateCinema(h.gettCinema());
		}
	}

	private void updateCinema(TCinema cinema) {
		Assert.isTrue(cinema != null, "没有指定影院，请您重新保存");

		long digitHallCount = 0L;
		long tridHallCount = 0L;
		long imaxHallCount = 0L;
		long seatCount = 0L;
		long hallCount = 0L;
		
		Set<THall> halls = cinema.gettHalls();
		for (THall h : halls) {
			if (h.getDeleted() != null && h.getDeleted()) {
				continue;
			}
			if (h.getIsDigital()) {
				digitHallCount++;
			}
			if (h.getIs3d() || h.getIsReald()) {
				tridHallCount++;
			}
			if (h.getIsImax()) {
				imaxHallCount++;
			}
			hallCount++;
			seatCount += h.getSeatCount() + h.getDisabledSeatCount();
		}
		cinema.setImaxHallCount(imaxHallCount);
		cinema.setTridHallCount(tridHallCount);
		cinema.setDigitHallCount(digitHallCount);
		cinema.setHallCount(hallCount);
		cinema.setSeatCount(seatCount);
		cinemaService.update(cinema);
	}

	@Override
	public List<String> findAllHallNo() {

		String fromClause = " select distinct c.name from THall c where c.deleted =:deleted order by cast(c.name as long)";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deleted", Boolean.FALSE);
		List<?> lst = getDao().queryHql(fromClause, params);
		List<String> hallNoList = new ArrayList<String>();
		for (Object o : lst) {
			hallNoList.add(o.toString());
		}
		return hallNoList;
	}

}
