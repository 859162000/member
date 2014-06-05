package com.wanda.ccs.schedule.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.model.TSchedulePlanD;
import com.wanda.ccs.schedule.dao.ITSchedulePlanDDao;
import com.wanda.ccs.schedule.service.TSchedulePlanDService;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TSchedulePlanDServiceImpl extends
		BaseCrudServiceImpl<TSchedulePlanD> implements TSchedulePlanDService {

	@Autowired
	private ITSchedulePlanDDao dao = null;

	@Override
	public IBaseDao<TSchedulePlanD> getDao() {
		return dao;
	}

}
