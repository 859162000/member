package com.wanda.ccs.film.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.film.dao.ITFilmSlotDao;
import com.wanda.ccs.film.service.TFilmSlotService;
import com.wanda.ccs.model.TFilmSlot;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.StringUtil;

@Service
public class TFilmSlotServiceImpl extends BaseCrudServiceImpl<TFilmSlot>
		implements TFilmSlotService {

	@Autowired
	private ITFilmSlotDao dao = null;

	@Override
	public IBaseDao<TFilmSlot> getDao() {
		return dao;
	}

	public List<TFilmSlot> getLowestPrice(Long filmId, String area,
			Long cityId, Date startDate, Date endDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TFilmSlot c where c.tFilm.id = :filmId ";
		params.put("filmId", filmId);

		if (startDate != null && endDate != null) {
			hql += " and c.startDate<=:endDate and c.endDate >= :startDate";
			params.put("startDate", startDate);
			params.put("endDate", endDate);
		}

		if (cityId != null) {
			hql += " and (c.cityId=:cityId or c.cityId is null) ";
			params.put("cityId", cityId);
		} else if (!StringUtil.isNullOrBlank(area)) {
			hql += " and (c.area = :area or c.area is null) ";
			params.put("area", area);
		}
		hql += " order by c.cityId, c.area, c.startDate ";
		return super.queryHql(hql, params);
	}

}
