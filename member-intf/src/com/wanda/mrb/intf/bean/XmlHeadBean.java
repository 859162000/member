package com.wanda.mrb.intf.bean;

import com.wanda.mrb.intf.ReCodeDef;


public class XmlHeadBean {
	private String intfVer = "";
	private String intfCode = "";
	private String clientType = "";
	private String serialNum = "";
	private String username = "";
	private String passwd = "";
	private String retCode = "";  
	private String retMsg = "";
	private String ipAddress = "";
	
	public void setRetInfo(String retCode, String retMsg) {
		this.retCode = retCode;
		this.retMsg = retMsg;
	}
	
	public boolean isRetCodeSuccess() {
		if (ReCodeDef.CONST_RESPCODE_SUCCESS.equals(retCode))
			return true;
		else
			return false;
	}
	public String getIntfVer() {
		return intfVer;
	}
	public void setIntfVer(String intfVer) {
		this.intfVer = intfVer;
	}
	public String getIntfCode() {
		return intfCode;
	}
	public void setIntfCode(String intfCode) {
		this.intfCode = intfCode;
	}
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public String getRetMsg() {
		return retMsg;
	}
	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append( "intfVer="+intfVer);
		buf.append("|intfCode="+intfCode);
		buf.append("|clientType="+clientType);
		buf.append("|ipAddress="+ipAddress);
		buf.append("|serialNum="+serialNum);
		buf.append("|username="+username);
		buf.append("|passwd="+passwd);
//		buf.append("|beginTime="+beginTime);
		buf.append("|retCode="+retCode);
		buf.append("|bizParams{");
//			for (Iterator<String> itr = bizParams.keySet().iterator(); itr.hasNext();) {
//				String key = (String)itr.next();
//				buf.append(key+":");
//				buf.append((String)bizParams.get(key)+";");
//			}
		buf.append("}");
		
		return buf.toString();
	}
}
