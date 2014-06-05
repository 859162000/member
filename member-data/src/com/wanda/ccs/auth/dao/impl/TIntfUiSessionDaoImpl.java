package com.wanda.ccs.auth.dao.impl;

import org.springframework.stereotype.Repository;

import com.wanda.ccs.auth.dao.ITIntfUiSessionDao;
import com.wanda.ccs.model.TIntfUiSession;
import com.xcesys.extras.core.dao.impl.BaseDaoImpl;

/**
 * POS用户会话
 */
@Repository
public class TIntfUiSessionDaoImpl extends BaseDaoImpl<TIntfUiSession>
		implements ITIntfUiSessionDao {
}