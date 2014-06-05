package com.wanda.ccs.jobhub.smsreport.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 待发送信息列表
 * 
 * @author zhurui
 * @date 2014年1月7日 下午3:38:25
 */
public class SendSms implements Serializable {

	private static final long serialVersionUID = 1L;
	/* 待发送用户列表 */
	private Map<String, List<TargetSmsRelation>> sendTargetList = new HashMap<String, List<TargetSmsRelation>>();
	/* 待发送短信列表 */
	private Map<String, SMSData> sendSmsList = new HashMap<String, SMSData>();

	public Map<String, List<TargetSmsRelation>> getSendTargetList() {
		return sendTargetList;
	}

	public void setSendTargetList(
			Map<String, List<TargetSmsRelation>> sendTargetList) {
		this.sendTargetList = sendTargetList;
	}

	public Map<String, SMSData> getSendSmsList() {
		return sendSmsList;
	}

	public void setSendSmsList(Map<String, SMSData> sendSmsList) {
		this.sendSmsList = sendSmsList;
	}

	/**
	 * 判断影城内码对应的待发送短信息数据
	 * 
	 * @param innerCode
	 * @return
	 */
	public boolean isSmsExsit(String innerCode) {
		return getSendSmsList().containsKey(innerCode);
	}

	/**
	 * 获取影城内码对应的待发送用户列表
	 * 
	 * @param innerCode
	 * @return
	 */
	public List<TargetSmsRelation> getTargetListByInnerCode(String innerCode) {
		if (getSendTargetList().containsKey(innerCode) == false) {
			getSendTargetList().put(innerCode, new ArrayList<TargetSmsRelation>());
		}

		return getSendTargetList().get(innerCode);
	}

}
