package com.wanda.ccs.mem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.mem.dao.ITMemFavContactsDao;
import com.wanda.ccs.mem.service.TMemberFavContactsService;
import com.wanda.ccs.model.TMemFavContact;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TMemFavContactsServiceImpl extends BaseCrudServiceImpl<TMemFavContact>
		implements TMemberFavContactsService {

	@Autowired
	private ITMemFavContactsDao dao;

	@Override
	public IBaseDao<TMemFavContact> getDao() {
		return dao;
	}
}
