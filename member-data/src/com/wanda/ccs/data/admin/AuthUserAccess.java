package com.wanda.ccs.data.admin;



import java.sql.Timestamp;

import com.icebean.core.adb.ADB;

public class AuthUserAccess extends ADB {
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
	private Timestamp requestTime;
	private Timestamp createTime;
	private String userId;
	private String userName;
	private String userLevel;
	private String userRegion;
	private String userCinema;
	private String posType;
	private String opGroup;
	private String opStation;
	
	public AuthUserAccess(){
		
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public Long getProcessTime() {
		return processTime;
	}
	public void setProcessTime(Long processTime) {
		this.processTime = processTime;
	}
	public String getRequestIp() {
		return requestIp;
	}
	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}
	public String getMarkup() {
		return markup;
	}
	public void setMarkup(String markup) {
		this.markup = markup;
	}
	public String getRequestUri() {
		return requestUri;
	}
	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}
	public String getControlPath() {
		return controlPath;
	}
	public void setControlPath(String controlPath) {
		this.controlPath = controlPath;
	}
	public String getRequestPath() {
		return requestPath;
	}
	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}
	public String getExecute() {
		return execute;
	}
	public void setExecute(String execute) {
		this.execute = execute;
	}
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public String getRetMethod() {
		return retMethod;
	}
	public void setRetMethod(String retMethod) {
		this.retMethod = retMethod;
	}
	public String getRetUrl() {
		return retUrl;
	}
	public void setRetUrl(String retUrl) {
		this.retUrl = retUrl;
	}
	public String getLogMessage() {
		return logMessage;
	}
	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}
	public String getRequestParams() {
		return requestParams;
	}
	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception = exception;
	}
	public String getExceptionDetail() {
		return exceptionDetail;
	}
	public void setExceptionDetail(String exceptionDetail) {
		this.exceptionDetail = exceptionDetail;
	}
	public String getCookies() {
		return cookies;
	}
	public void setCookies(String cookies) {
		this.cookies = cookies;
	}
	public Timestamp getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Timestamp requestTime) {
		this.requestTime = requestTime;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}
	public String getUserRegion() {
		return userRegion;
	}
	public void setUserRegion(String userRegion) {
		this.userRegion = userRegion;
	}
	public String getUserCinema() {
		return userCinema;
	}
	public void setUserCinema(String userCinema) {
		this.userCinema = userCinema;
	}
	public String getPosType() {
		return posType;
	}
	public void setPosType(String posType) {
		this.posType = posType;
	}
	public String getOpGroup() {
		return opGroup;
	}
	public void setOpGroup(String opGroup) {
		this.opGroup = opGroup;
	}
	public String getOpStation() {
		return opStation;
	}
	public void setOpStation(String opStation) {
		this.opStation = opStation;
	}
	
	
	
}
