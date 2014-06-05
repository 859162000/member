package com.wanda.ccs.auth.service;

import com.wanda.ccs.model.TIntfUiSession;
import com.xcesys.extras.core.service.ICrudService;

/**
 * POS用户会话Service
 */
public interface TIntfUiSessionService extends ICrudService<TIntfUiSession> {
	public TIntfUiSession getBySessionId(String sessionId);
}
