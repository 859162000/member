package com.wanda.ccs.auth.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.auth.dao.ITAuthRptSessionDao;
import com.wanda.ccs.auth.service.TAuthRptSessionService;
import com.wanda.ccs.model.TAuthRptSession;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TAuthRptSessionServiceImpl extends
		BaseCrudServiceImpl<TAuthRptSession> implements TAuthRptSessionService {

	@Autowired
	private ITAuthRptSessionDao dao = null;

	@Override
	public IBaseDao<TAuthRptSession> getDao() {
		return dao;
	}

	@Override
	public void createSessionArray(TAuthRptSession[] sessionArray) {
		for(TAuthRptSession session : sessionArray){
			dao.create(session);
		}
	}

	@Override
	public TAuthRptSession getRptSessionBySessionKey(String sessionKey) {
		if(sessionKey == null || sessionKey.equals(""))
			return null;
		String hql = "select c from TAuthRptSession c where c.sessionKey = :sessionKey";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sessionKey", sessionKey);
		List<TAuthRptSession> list = dao.queryHql(hql, params);
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
}
