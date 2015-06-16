/**  
* @Title: MessageApproveVo.java
* @Package com.wanda.ccs.member.segment.vo
* @Description: 短信发送审批实体
* @author 许雷
* @date 2015年5月21日 下午2:54:08
* @version V1.0  
*/
package com.wanda.ccs.member.segment.vo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @ClassName: MessageApproveVo
 * @Description: (审批表)
 * @author 许雷
 * @date 2015年5月21日 下午2:54:08
 *
 */
public class MessageApproveVo implements Serializable{
	/**
	* @Fields serialVersionUID : (序列化)
	*/
	private static final long serialVersionUID = -3920256215085773701L;
	
	private Long approveId;
	
	private Long segmMessageId;
	
	private Timestamp createDate;
	
	private Timestamp approveDate;
	
	private Long status;
	
	private String approver;

	private String approveSugg;
	
	private String version;
	
	public Long getApproveId() {
		return approveId;
	}
	
	public void setApproveId(Long approveId) {
		this.approveId = approveId;
	}


	public Long getSegmMessageId() {
		return segmMessageId;
	}

	public void setSegmMessageId(Long segmMessageId) {
		this.segmMessageId = segmMessageId;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Timestamp approveDate) {
		this.approveDate = approveDate;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public String getApproveSugg() {
		return approveSugg;
	}

	public void setApproveSugg(String approveSugg) {
		this.approveSugg = approveSugg;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
