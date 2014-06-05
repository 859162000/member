package com.wanda.ccs.basemgt.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.basemgt.dao.ITHolidayDao;
import com.wanda.ccs.basemgt.service.THolidayService;
import com.wanda.ccs.model.THolidays;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class THolidayServiceImpl extends BaseCrudServiceImpl<THolidays>
		implements THolidayService {

	@Autowired
	private ITHolidayDao dao = null;

	@Override
	public IBaseDao<THolidays> getDao() {
		return dao;
	}

	@Override
	public THolidays findByDate(Date date) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("date", date);
		List<THolidays> list = getDao()
				.queryHql(
						"select t from THolidays t where t.isdelete = 0 and t.startdate <= :date and t.enddate >= :date",
						params);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}
}
