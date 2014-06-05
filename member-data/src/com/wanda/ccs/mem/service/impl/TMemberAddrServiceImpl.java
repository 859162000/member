package com.wanda.ccs.mem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wanda.ccs.mem.dao.ITMemberAddrDao;
import com.wanda.ccs.mem.service.TMemberAddrService;
import com.wanda.ccs.model.TMemberAddr;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TMemberAddrServiceImpl extends BaseCrudServiceImpl<TMemberAddr>
		implements TMemberAddrService {

	@Autowired
	private ITMemberAddrDao dao;

	@Override
	public IBaseDao<TMemberAddr> getDao() {
		return dao;
	}
}
