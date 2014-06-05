package com.wanda.ccs.mem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wanda.ccs.mem.dao.ITMemberInfoDao;
import com.wanda.ccs.mem.service.TMemberInfoService;
import com.wanda.ccs.model.TMemberInfo;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TMemberInfoServiceImpl extends BaseCrudServiceImpl<TMemberInfo>
		implements TMemberInfoService {

	@Autowired
	private ITMemberInfoDao dao;

	@Override
	public IBaseDao<TMemberInfo> getDao() {
		return dao;
	}
}
