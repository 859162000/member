package com.wanda.ccs.auth;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.aggrepoint.adk.IUserEngine;
import com.aggrepoint.adk.IUserProfile;
import com.aggrepoint.adk.IWinletConst;

/**
 * 生成CCS用户身份
 * 
 * @author YJM
 */
public class CcsUserEngine implements IUserEngine, IWinletConst {
	public IUserProfile getUserProfile(IModuleRequest req, IModuleResponse resp)
			throws UnsupportedEncodingException {
		return new CcsUserProfile(
				((HttpServletRequest) req.getRequestObject())
						.getHeader(REQUEST_HEADER_USER_PROFILE));
	}
}
