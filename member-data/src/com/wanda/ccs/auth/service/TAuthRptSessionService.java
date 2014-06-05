package com.wanda.ccs.auth.service;

import com.wanda.ccs.model.TAuthRptSession;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 用户会话service
 */
public interface TAuthRptSessionService extends ICrudService<TAuthRptSession> {
	/**
	 * 保存session数组
	 * @param sessionArray
	 */
	public void createSessionArray(TAuthRptSession[] sessionArray);
	
	/**
	 * 通过sessionKey获取session
	 * @param sessionKey
	 * @return
	 */
	public TAuthRptSession getRptSessionBySessionKey(String sessionKey);
}
