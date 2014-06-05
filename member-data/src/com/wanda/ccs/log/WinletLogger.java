package com.wanda.ccs.log;

import java.sql.Timestamp;
import java.text.DecimalFormat;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Category;
import org.apache.log4j.Priority;

import com.aggrepoint.adk.ILogger;
import com.aggrepoint.adk.IModule;
import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.aggrepoint.adk.IServerContext;
import com.aggrepoint.adk.IUserProfile;
import com.aggrepoint.adk.IWinletConst;
import com.aggrepoint.adk.WebBaseException;
import com.aggrepoint.adk.data.BaseModuleDef;
import com.aggrepoint.adk.data.LogParam;
import com.aggrepoint.adk.data.LoggerDef;
import com.aggrepoint.adk.data.RetCode;
import com.aggrepoint.adk.data.WinletActionDef;
import com.aggrepoint.adk.data.WinletStateDef;
import com.icebean.core.adb.db.DbAdapter;
import com.icebean.core.common.Log4jIniter;
import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.auth.UserLevel;
import com.wanda.ccs.data.admin.AuthUserAccess;

/**
 * 简单的Winlet日志
 * 
 * @author YJM
 */
public class WinletLogger implements ILogger, IWinletConst {
	protected static DecimalFormat reqFormat = new DecimalFormat(
			"000000000000000");
	protected static DecimalFormat timeFormat = new DecimalFormat("0000000");
	protected static DecimalFormat codeFormat = new DecimalFormat("00000");
	protected String m_strServerName;
	protected Category m_contextLog;
	public static final String[] SYSTEM_PARAMS = new String[]{"null","_adk_i","_adk_f","_purl","_adk_a","_x","_a","_adk_v","_adk_vf","_adk_p","_pg"};

	/** 初始化 */
	public void init(LoggerDef def, IServerContext context)
			throws WebBaseException {
		m_strServerName = context.getServerName();
		m_contextLog = Log4jIniter.getCategory(context.getClass());
	}

	public void log(BaseModuleDef moduleDef, IModule module, RetCode code,
			IModuleRequest req, IModuleResponse resp, boolean skipExecution) {
//		AuthUserAccess authUserAccess = new AuthUserAccess();

//		authUserAccess.setServerName(m_strServerName);
//		authUserAccess.setRequestId(req.getHeader(REQUEST_HEADER_REQUEST_ID));
//		authUserAccess.setProcessTime(Long.valueOf(timeFormat.format(System
//				.currentTimeMillis() - req.getCreateTime())));
//		authUserAccess.setCreateTime(new Timestamp(System.currentTimeMillis()));
//		authUserAccess.setRequestTime(new Timestamp(req.getCreateTime()));
//		authUserAccess.setRetCode(codeFormat.format(resp.getRetCode()));
//		addUser(authUserAccess, req);
//		authUserAccess.setRequestIp(req.getHeader(REQUEST_HEADER_USER_IP));
//		authUserAccess.setMarkup(req.getHeader(REQUEST_HEADER_MARKUP_TYPE));
//		authUserAccess.setRequestUri(req.getHeader(REQUEST_HEADER_REQUEST_URI));
//		authUserAccess.setControlPath(req.getControlPath());
//		authUserAccess.setRequestPath(req.getRequestPath());
//		addMethodAndPresent(authUserAccess, moduleDef, module, code, resp,
//				skipExecution);
//		authUserAccess.setLogMessage(resp.getLogMessage());
//		addParams(authUserAccess, moduleDef, req);
//		authUserAccess.setException(resp.getRetThrow() == null ? "" : resp
//				.getRetThrow().toString().substring(0, 500));
//		addCookies(authUserAccess, req);

		Category log = Log4jIniter.getCategory(
				module.getImplementationObjectClass(), getClass());

		StringBuffer sb = new StringBuffer();
		sb.append("| ");
		sb.append(m_strServerName);

		sb.append(" | ");
		String reqId = req.getHeader(REQUEST_HEADER_REQUEST_ID);
		try {
			sb.append(reqFormat.format(Long.parseLong(reqId)));
		} catch (Exception e) {
			sb.append(reqId);
		}

		sb.append(" | ");
		sb.append(timeFormat.format(System.currentTimeMillis()
				- req.getCreateTime()));

		sb.append(" | ");
		sb.append(codeFormat.format(resp.getRetCode()));

		sb.append(" | ");
		sb.append(req.getRemoteAddr());

		addUser(sb, req);

		sb.append(" | ");
		sb.append(req.getHeader(REQUEST_HEADER_USER_IP));

		sb.append(" | ");
		sb.append(req.getHeader(REQUEST_HEADER_MARKUP_TYPE));

		sb.append(" | ");
		sb.append(req.getHeader(REQUEST_HEADER_REQUEST_URI));

		sb.append(" | ");
		sb.append(req.getControlPath());

		sb.append(req.getRequestPath());
		addMethodAndPresent(sb, moduleDef, module, code, resp, skipExecution);

		sb.append(" | ");
		sb.append(resp.getLogMessage() == null ? "" : resp.getLogMessage());

		addParams(sb, moduleDef, req);

		sb.append(" | ");
		Throwable t = resp.getRetThrow();
		sb.append(t == null ? "" : t.toString());

		addCookies(sb, req);
		
		addParams(sb, req);
		log.info(sb.toString());
		if (t instanceof Exception)
			t.printStackTrace();
//		authUserAccess.setExceptionDetail(t == null ? "" : t.getStackTrace()
//				.toString());
		try {
//			DbAdapter adapter = new DbAdapter(req.getDBConn());
//			adapter.createOrUpdate(authUserAccess);
//			//创建完日志后，提交 关闭数据库连接
//			adapter.commit();
//			adapter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void contextLog(Priority priority, IModuleRequest req,
			String message, Throwable exp) {
		StringBuffer sb = new StringBuffer();
		sb.append(message);
		sb.append(" | ");
		sb.append(m_strServerName);
		sb.append(" | ");
		String reqId = "0";
		try {
			sb.append(reqFormat.format(Long.parseLong(reqId)));
		} catch (Exception e) {
			sb.append(reqId);
		}
		sb.append(" | ");
		sb.append(timeFormat.format(System.currentTimeMillis()
				- req.getCreateTime()));
		sb.append(" | ");
		sb.append(req.getRemoteAddr());
		addUser(sb, req);
		sb.append(" | ");
		sb.append(req.getHeader(REQUEST_HEADER_USER_IP));
		sb.append(" | ");
		sb.append(req.getHeader(REQUEST_HEADER_MARKUP_TYPE));
		sb.append(" | ");
		sb.append(req.getHeader(REQUEST_HEADER_REQUEST_URI));
		sb.append(" | ");
		sb.append(req.getControlPath());
		sb.append(req.getRequestPath());
		sb.append(" | ");
		addCookies(sb, req);

		if (exp != null)
			m_contextLog.log(priority, sb.toString(), exp);
		else
			m_contextLog.log(priority, sb.toString());
	}

	protected void addUser(AuthUserAccess authUserAccess, IModuleRequest req) {
		IUserProfile u = req.getUserProfile();
		authUserAccess.setUserId(u.getProperty(IUserProfile.PROPERTY_ID));

		if (u instanceof CcsUserProfile) {
			CcsUserProfile user = (CcsUserProfile) u;
			authUserAccess.setUserName(user.getName());

			if (user.getLevel() == UserLevel.GROUP) {
				authUserAccess.setUserLevel("G");
			} else if (user.getLevel() == UserLevel.REGION) {
				authUserAccess.setUserLevel("R");
				authUserAccess.setUserRegion(user.getRegionName());
			} else if (user.getLevel() == UserLevel.CINEMA) {
				authUserAccess.setUserLevel("C");
				authUserAccess.setUserCinema(user.getCinemaName());
				authUserAccess.setUserRegion(user.getRegionName());
			}
			authUserAccess.setPosType(user.getPosType());
			authUserAccess.setOpGroup(user.getOpGroup());
			authUserAccess.setOpStation(user.getOpStation());
		}
	}

	protected void addMethodAndPresent(AuthUserAccess authUserAccess,
			BaseModuleDef moduleDef, IModule module, RetCode code,
			IModuleResponse resp, boolean skipExecution) {
		StringBuffer execute = new StringBuffer();
		execute.append(module.getImplementationObjectClass().getName());
		if (moduleDef instanceof WinletStateDef
				|| moduleDef instanceof WinletActionDef) {
			execute.append(".");
			execute.append(moduleDef.m_strMethod == null
					|| moduleDef.m_strMethod.equals("") ? moduleDef.m_strPath
					: moduleDef.m_strMethod);
		}
		execute.append(skipExecution ? "(S)" : "(X)");
		authUserAccess.setExecute(execute.toString());

		if (code != null) {
			authUserAccess.setRetMethod(code.m_pMethod.getKey());
			switch (code.m_pMethod) {
			case FORWARD:
			case REDIRECT:
			case INCLUDE:
			case PASS:
				authUserAccess.setRetUrl(code.m_strUrl);
				break;
			case FORWARD_CUSTOMIZED:
			case REDIRECT_CUSTOMIZED:
			case INCLUDE_CUSTOMIZED:
				authUserAccess.setRetUrl(resp.getRetUrl());
				break;
			}
		}
	}

	protected void addParams(AuthUserAccess authUserAccess,
			BaseModuleDef moduleDef, IModuleRequest req) {
		StringBuffer params = new StringBuffer();
		LogParam[] logParams = moduleDef.getLogParams();
		if (!req.isMultipart() && logParams != null)
			for (int i = 0; i < logParams.length; i++) {
				if (i != 0)
					params.append(", ");
				params.append(logParams[i].m_strName);
				params.append("=");
				params.append(req.getParameter(logParams[i].m_strName, ""));
			}
		authUserAccess.setRequestParams(params.toString());
	}

	protected void addCookies(AuthUserAccess authUserAccess, IModuleRequest req) {
		StringBuffer cookie = new StringBuffer();
		Cookie[] ck = ((HttpServletRequest) req.getRequestObject())
				.getCookies();
		if (ck != null)
			for (int i = 0; i < ck.length; i++) {
				cookie.append(ck[i].getName());
				cookie.append("=");
				cookie.append(ck[i].getValue());
				cookie.append(";");
			}
		else
			cookie.append(" ");

		authUserAccess.setCookies(cookie.toString().length() > 200 ? cookie
				.toString().substring(0, 200) : cookie.toString());

	}

	// ==========================================================================================================
	protected void addUser(StringBuffer sb, IModuleRequest req) {
		String user = "";
		String userLevel = "";
		String regionName = "";
		String cinemaName = "";
		try {
			IUserProfile userProfile = req.getUserProfile();
			if (userProfile.isAnonymous()){
				user = "(anonymouse)";
			}else if (userProfile instanceof CcsUserProfile) {
				CcsUserProfile up = (CcsUserProfile) userProfile;
				user = up.getId();
				userLevel = up.getLevelName();
				regionName = up.getRegionName();
				cinemaName = up.getCinemaName();
			}
				
		} catch (Exception ee) {
		}
		sb.append(" | ");
		sb.append(user);
		sb.append(" | ");
		sb.append(userLevel);
		sb.append(" | ");
		sb.append(regionName);
		sb.append(" | ");
		sb.append(cinemaName);
	}

	protected void addMethodAndPresent(StringBuffer sb,
			BaseModuleDef moduleDef, IModule module, RetCode code,
			IModuleResponse resp, boolean skipExecution) {
		sb.append(" | ");
		sb.append(module.getImplementationObjectClass().getName());
		if (moduleDef instanceof WinletStateDef
				|| moduleDef instanceof WinletActionDef) {
			sb.append(".");
			sb.append(moduleDef.m_strMethod == null
					|| moduleDef.m_strMethod.equals("") ? moduleDef.m_strPath
					: moduleDef.m_strMethod);
		}
		sb.append(skipExecution ? "(S)" : "(X)");

		sb.append(" | ");
		if (code != null) {
			sb.append(code.m_pMethod.getKey());
			sb.append(" ");
			switch (code.m_pMethod) {
			case FORWARD:
			case REDIRECT:
			case INCLUDE:
			case PASS:
				sb.append(code.m_strUrl);
				break;
			case FORWARD_CUSTOMIZED:
			case REDIRECT_CUSTOMIZED:
			case INCLUDE_CUSTOMIZED:
				sb.append(resp.getRetUrl());
				break;
			}
		} else
			sb.append(resp.getRetCode());
	}

	protected void addParams(StringBuffer sb, BaseModuleDef moduleDef,
			IModuleRequest req) {
		sb.append(" | ");

		LogParam[] logParams = moduleDef.getLogParams();
		if (!req.isMultipart() && logParams != null)
			for (int i = 0; i < logParams.length; i++) {
				if (i != 0)
					sb.append(", ");
				sb.append(logParams[i].m_strName);
				sb.append("=");
				sb.append(req.getParameter(logParams[i].m_strName, ""));
			}
	}

	protected void addCookies(StringBuffer sb, IModuleRequest req) {
		sb.append(" | ");
		Cookie[] ck = ((HttpServletRequest) req.getRequestObject())
				.getCookies();
		if (ck != null)
			for (int i = 0; i < ck.length; i++) {
				sb.append(ck[i].getName());
				sb.append("=");
				sb.append(ck[i].getValue());
				sb.append(";");
			}
		else
			sb.append(" ");

	}
	
	protected void addParams(StringBuffer sb,
			IModuleRequest req) {
		sb.append(" | ");

		String[] logParams = req.getParameterNames(SYSTEM_PARAMS);
		if (!req.isMultipart() && logParams != null)
			for (int i = 0; i < logParams.length; i++) {
				if(i != 0)
					sb.append(", ");
				sb.append(logParams[i]);
				sb.append("=");
				sb.append(req.getParameter(logParams[i], ""));
			}
	}
	
	
}
