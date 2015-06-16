/**  
* @Title: MsgIpConfigBean.java
* @Package com.wanda.ccs.member.segment
* @Description: TODO(用一句话描述该文件做什么)
* @author 许雷
* @date 2015年5月22日 下午3:12:57
* @version V1.0  
*/
package com.wanda.ccs.member.segment;

/**
 * @ClassName: MsgIpConfigBean
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 许雷
 * @date 2015年5月22日 下午3:12:57
 *
 */
public class MsgIpConfigBean {
	private String msgMqIp;
	private String msgMqPort;
	private String msgSvcIp;
	private String msgSvcPort;
	private String msgChannelId;
	public String getMsgMqIp() {
		return msgMqIp;
	}
	public void setMsgMqIp(String msgMqIp) {
		this.msgMqIp = msgMqIp;
	}
	public String getMsgMqPort() {
		return msgMqPort;
	}
	public void setMsgMqPort(String msgMqPort) {
		this.msgMqPort = msgMqPort;
	}
	public String getMsgSvcIp() {
		return msgSvcIp;
	}
	public void setMsgSvcIp(String msgSvcIp) {
		this.msgSvcIp = msgSvcIp;
	}
	public String getMsgSvcPort() {
		return msgSvcPort;
	}
	public void setMsgSvcPort(String msgSvcPort) {
		this.msgSvcPort = msgSvcPort;
	}
	public String getMsgChannelId() {
		return msgChannelId;
	}
	public void setMsgChannelId(String msgChannelId) {
		this.msgChannelId = msgChannelId;
	}
}
