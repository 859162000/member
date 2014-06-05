package com.wanda.ccs.auth.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.auth.dao.ITIntfUiSessionDao;
import com.wanda.ccs.auth.service.TIntfUiSessionService;
import com.wanda.ccs.model.TIntfUiSession;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.StringUtil;

@Service
public class TIntfUiSessionServiceImpl extends
		BaseCrudServiceImpl<TIntfUiSession> implements TIntfUiSessionService {
	@Autowired
	private ITIntfUiSessionDao dao = null;

	@Override
	public IBaseDao<TIntfUiSession> getDao() {
		return dao;
	}

	@Override
	public TIntfUiSession getBySessionId(String sessionId) {
		if (StringUtil.isNullOrBlank(sessionId))
			return null;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("sessionId", sessionId);
		List<TIntfUiSession> list = queryHql(
				"select c from TIntfUiSession c where c.sessionId = :sessionId",
				map);
		if (list == null || list.size() == 0)
			return null;
		return list.get(0);
	}
}
