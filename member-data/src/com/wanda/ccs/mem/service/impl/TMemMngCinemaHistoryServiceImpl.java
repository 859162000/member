package com.wanda.ccs.mem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.mem.dao.ITMemMngCinemaHistoryDao;
import com.wanda.ccs.mem.service.TMemMngCinemaHistoryService;
import com.wanda.ccs.model.TMemMngcinemaHistory;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TMemMngCinemaHistoryServiceImpl extends BaseCrudServiceImpl<TMemMngcinemaHistory>
		implements TMemMngCinemaHistoryService {

	@Autowired
	private ITMemMngCinemaHistoryDao dao;

	@Override
	public IBaseDao<TMemMngcinemaHistory> getDao() {
		return dao;
	}
}
