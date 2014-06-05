package com.wanda.ccs.model;

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.xcesys.extras.core.dao.model.AbstractEntity;

/**
 * TAuthUserAccess entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_AUTH_USER_ACCESS")
public class TAuthUserAccess extends AbstractEntity implements java.io.Serializable {
	private static final long serialVersionUID = -1147226755299188515L;
	
	private Long id;
	private String serverName;
	private String requestId;
	private Long processTime;
	private String requestIp;
	private String markup;
	private String requestUri;
	private String controlPath;
	private String requestPath;
	private String execute;
	private String retCode;
	private String retMethod;
	private String retUrl;
	private String logMessage;
	private String requestParams;
	private String exception;
	private String exceptionDetail;
	private String cookies;
	private Date requestTime;
	private Date createTime;
	private String userId;
	private String userName;
	private String userLevel;
	private String userRegion;
	private String userCinema;
	private String posType;
	private String opGroup;
	private String opStation;

	// Constructors

	/** default constructor */
	public TAuthUserAccess() {
	}


	/** full constructor */
	public TAuthUserAccess(String serverName,
			String requestId, Long processTime, String requestIp,
			String markup, String requestUri, String controlPath,
			String requestPath, String execute, String retCode,
			String retMethod, String retUrl, String logMessage,
			String requestParams, String exception, String exceptionDetail,
			String cookies, Date requestTime, Date createTime,
			String userId, String userName, String userLevel,
			String userRegion, String userCinema, String posType,
			String opGroup, String opStation) {
		this.serverName = serverName;
		this.requestId = requestId;
		this.processTime = processTime;
		this.requestIp = requestIp;
		this.markup = markup;
		this.requestUri = requestUri;
		this.controlPath = controlPath;
		this.requestPath = requestPath;
		this.execute = execute;
		this.retCode = retCode;
		this.retMethod = retMethod;
		this.retUrl = retUrl;
		this.logMessage = logMessage;
		this.requestParams = requestParams;
		this.exception = exception;
		this.exceptionDetail = exceptionDetail;
		this.cookies = cookies;
		this.requestTime = requestTime;
		this.createTime = createTime;
		this.userId = userId;
		this.userName = userName;
		this.userLevel = userLevel;
		this.userRegion = userRegion;
		this.userCinema = userCinema;
		this.posType = posType;
		this.opGroup = opGroup;
		this.opStation = opStation;
	}

	@SequenceGenerator(name = "generator", sequenceName = "S_T_AUTH_USER_ACCESS")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "AUTH_USER_ACCESS_ID", unique = true, nullable = false, precision = 38, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "SERVER_NAME", length = 100)
	public String getServerName() {
		return this.serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Column(name = "REQUEST_ID", length = 50)
	public String getRequestId() {
		return this.requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	@Column(name = "PROCESS_TIME", precision = 22, scale = 0)
	public Long getProcessTime() {
		return this.processTime;
	}

	public void setProcessTime(Long processTime) {
		this.processTime = processTime;
	}

	@Column(name = "REQUEST_IP", length = 20)
	public String getRequestIp() {
		return this.requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

	@Column(name = "MARKUP", length = 20)
	public String getMarkup() {
		return this.markup;
	}

	public void setMarkup(String markup) {
		this.markup = markup;
	}

	@Column(name = "REQUEST_URI", length = 500)
	public String getRequestUri() {
		return this.requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	@Column(name = "CONTROL_PATH", length = 500)
	public String getControlPath() {
		return this.controlPath;
	}

	public void setControlPath(String controlPath) {
		this.controlPath = controlPath;
	}

	@Column(name = "REQUEST_PATH", length = 500)
	public String getRequestPath() {
		return this.requestPath;
	}

	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}

	@Column(name = "EXECUTE", length = 100)
	public String getExecute() {
		return this.execute;
	}

	public void setExecute(String execute) {
		this.execute = execute;
	}

	@Column(name = "RET_CODE", length = 50)
	public String getRetCode() {
		return this.retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	@Column(name = "RET_METHOD", length = 20)
	public String getRetMethod() {
		return this.retMethod;
	}

	public void setRetMethod(String retMethod) {
		this.retMethod = retMethod;
	}

	@Column(name = "RET_URL")
	public String getRetUrl() {
		return this.retUrl;
	}

	public void setRetUrl(String retUrl) {
		this.retUrl = retUrl;
	}

	@Column(name = "LOG_MESSAGE", length = 500)
	public String getLogMessage() {
		return this.logMessage;
	}

	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}

	@Column(name = "REQUEST_PARAMS", length = 500)
	public String getRequestParams() {
		return this.requestParams;
	}

	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}

	@Column(name = "EXCEPTION", length = 500)
	public String getException() {
		return this.exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	@Column(name = "EXCEPTION_DETAIL", length = 2000)
	public String getExceptionDetail() {
		return this.exceptionDetail;
	}

	public void setExceptionDetail(String exceptionDetail) {
		this.exceptionDetail = exceptionDetail;
	}

	@Column(name = "COOKIES", length = 200)
	public String getCookies() {
		return this.cookies;
	}

	public void setCookies(String cookies) {
		this.cookies = cookies;
	}

	@Column(name = "REQUEST_TIME")
	public Date getRequestTime() {
		return this.requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "USER_ID", length = 50)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "USER_NAME", length = 20)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "USER_LEVEL", length = 1)
	public String getUserLevel() {
		return this.userLevel;
	}

	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	@Column(name = "USER_REGION", length = 20)
	public String getUserRegion() {
		return this.userRegion;
	}

	public void setUserRegion(String userRegion) {
		this.userRegion = userRegion;
	}

	@Column(name = "USER_CINEMA")
	public String getUserCinema() {
		return this.userCinema;
	}

	public void setUserCinema(String userCinema) {
		this.userCinema = userCinema;
	}

	@Column(name = "POS_TYPE", length = 20)
	public String getPosType() {
		return this.posType;
	}

	public void setPosType(String posType) {
		this.posType = posType;
	}

	@Column(name = "OP_GROUP", length = 50)
	public String getOpGroup() {
		return this.opGroup;
	}

	public void setOpGroup(String opGroup) {
		this.opGroup = opGroup;
	}

	@Column(name = "OP_STATION", length = 50)
	public String getOpStation() {
		return this.opStation;
	}

	public void setOpStation(String opStation) {
		this.opStation = opStation;
	}

}