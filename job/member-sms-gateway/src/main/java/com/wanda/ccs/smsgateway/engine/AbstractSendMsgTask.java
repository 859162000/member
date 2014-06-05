package com.wanda.ccs.smsgateway.engine;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.hp.smsplatform.server.rabbitmq.WriteToQueue;
import com.wanda.ccs.smsgateway.console.GateWayApp;
import com.wanda.ccs.smsgateway.constant.DelimiterConst;
import com.wanda.ccs.smsgateway.constant.MoMsgConst;
import com.wanda.ccs.smsgateway.engine.dao.EngineLogDAO;
import com.wanda.ccs.smsgateway.engine.dao.SendLogDAO;
import com.wanda.ccs.smsgateway.engine.model.SendLog;
import com.wanda.ccs.smsgateway.exception.TaskCheckException;


public abstract class AbstractSendMsgTask implements ITask,InitializingBean {
	public static final int TASK_COMPLETED = 1;
	public static final int TASK_FAILED = -1;
	protected Configuration configurationUtil;
	
	protected Long logId;
	protected String EngineName;
	protected String systemId = "001";
	protected String channelId = "00001";
	protected String settleId;
	//"13904080136|||MA#YZM#123123|||linkid|||platform|||ServiceUp|||SpNumber";
	private String originMsg;
	private String noPrefixMsg;
	private Long sendLogId;
	
	public void afterPropertiesSet() throws Exception {
		//设置商家结算编码，根据配置文件初始化默认值，在task实现类里可以修改
		this.setSettleId(configurationUtil.getString("send.settleid"));
		//设置系统编码
		this.setSystemId(configurationUtil.getString("send.systemid"));
		//设置通道编码
		this.setChannelId(configurationUtil.getString("send.channelid"));
	}
	
	/**
	 * 子类需要实现该方法，完成业务处理
	 * 返回值：task是否执行成功
	 */
	protected abstract boolean taskRun() throws Exception;
	
	/**
	 * 子类实现该方法，提供日志记录对象
	 * @return
	 */
	protected abstract Log getLogger();
	
	public void run(){
		if (this.getLogger().isDebugEnabled()){
			this.getLogger().debug("[日志] "+this.getTaskName()+"开始执行[ThreadId:"+Thread.currentThread().getId()+"] ...");
		}
		try {
			//检测规定时间内，某个号码的下行短信数量
			this.checkSendLimit(this.getMobileNo());
			//调用子类业务处理
			boolean runStatus = this.taskRun();
			if(runStatus){
				//发送成功 修改发送短信日志状态
				this.getSendLogDAOImpl().updateAddQueueSuccess(this.getSendLogId());
			}else{
				//发送失败 修改发送短信状态
				this.getSendLogDAOImpl().updateAddQueueFailed(this.getSendLogId());
			}
			//更新引擎任务状态为完成
			this.getEngineLogDAOImpl().updateTaskState(this.getLogId(), this.getTaskName(), TASK_COMPLETED);
		} catch(TaskCheckException tce){
			//更新引擎任务状态为失败
			this.getEngineLogDAOImpl().updateTaskState(this.getLogId(), this.getTaskName(), TASK_FAILED);
			//写引擎任务错误日志
			this.getEngineLogDAOImpl().insertTaskError(this.getLogId(), tce.getMessage());
		}catch (Exception e) {
			this.getLogger().error("[错误] "+this.getTaskName()+"[ThreadId:"+Thread.currentThread().getId()+"]执行出现错误 ...", e);
			this.writeTaskErrorLog(e);
		}
		if (this.getLogger().isDebugEnabled()){
			this.getLogger().debug("[日志] "+this.getTaskName()+"执行完成[ThreadId:"+Thread.currentThread().getId()+"] ...");
		}
	}
	/**
	 * 检测同一号码指定时间内下行短信数量是否超过限制
	 * @param mobileNo
	 * @throws TaskCheckException
	 */
	public void checkSendLimit(String mobileNo) throws TaskCheckException{
		//获取规定时间内，某个号码的下行短信数量
		String second = configurationUtil.getString("send.second");
		int limit = Integer.valueOf(configurationUtil.getString("send.limit"));
		int count = this.getSendLogDAOImpl().querySendedCount(mobileNo, second);
		if(count > limit){
			StringBuffer errorMsg = new StringBuffer();
			errorMsg.append("用户号码：");
			errorMsg.append(mobileNo);
			errorMsg.append(" 在");
			errorMsg.append(second);
			errorMsg.append("秒内已经发送下行短信");
			errorMsg.append(count);
			errorMsg.append("条，已经超过系统限制的上限值");
			errorMsg.append(limit);
			errorMsg.append("条。该条信息不加入下行队列。");
			throw new TaskCheckException(errorMsg.toString());
		}
	}
	
	/**
	 * 向下行短信队列添加记录，返回添加结果
	 * @param sendLog
	 * @return
	 */
	protected boolean sendSms(String sendMsg){
		WriteToQueue wq=new WriteToQueue();
		wq.sendBulkSmsToQueue(
				this.getChannelId(), 
				this.getMobileNo(), 
				this.getSystemId(), 
				this.getSettleId(),
				sendMsg, 
				this.getLinkId(), 
				this.getPlatform(), 
				this.getServiceUp(), 
				this.getSpNumber(), 
				0
				);
		return wq.isSendSeccess;
	}
	
	/**
	 * 向发送历史表写记录
	 * @param sendMsg
	 * @return
	 */
	protected Long writeSendLog(String sendMsg){
		SendLog sendLog = new SendLog(this.getLogId(), this.getEngineName());
		sendLog.setTaskName(this.getTaskName());
		sendLog.setMobileNo(this.getMobileNo());
		sendLog.setSendMsg(sendMsg);
		sendLog.setLinkId(this.getLinkId());
		sendLog.setPlatform(this.getPlatform());
		sendLog.setServiceUp(this.getServiceUp());
		sendLog.setSpNumber(this.getSpNumber());
		sendLog.setSystemId(this.getSystemId());
		sendLog.setSettleId(this.getSettleId());
		sendLog.setChannelId(this.getChannelId());
		//写入发送短信日志
		this.getSendLogDAOImpl().insertSendLog(sendLog);
		this.sendLogId = sendLog.getSendId();
		return this.sendLogId;
	}
	
	/**
	 * 写异常历史
	 * @param e
	 */
	protected void writeTaskErrorLog(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		//更新引擎任务状态为失败
		this.getEngineLogDAOImpl().updateTaskState(this.getLogId(), this.getTaskName(), TASK_FAILED);
		//写引擎任务错误日志
		this.getEngineLogDAOImpl().insertTaskError(this.getLogId(), sw.toString());
	}
	
	/**
	 * 解析originMsg获取mobileNo
	 * @return
	 */
	public String getMobileNo(){
		String[] moArr = StringUtils.split(this.getOriginMsg(), DelimiterConst.MO_SMS_DELIMITER);
		Assert.isTrue(moArr.length > MoMsgConst.MOBILE_NO_INDEX, "解析上行队列内容错误，上行队列内容为："+this.getOriginMsg());
		return moArr[MoMsgConst.MOBILE_NO_INDEX];
	}
	
	/**
	 * 解析originMsg获取用户发送的原始短信内容，包含指令信息
	 * @return
	 */
	public String getOriginUserMsg(){
		String[] moArr = StringUtils.split(this.getOriginMsg(), DelimiterConst.MO_SMS_DELIMITER);
		Assert.isTrue(moArr.length > MoMsgConst.MSG_CONTENT_INDEX, "解析上行队列内容错误，上行队列内容为："+this.getOriginMsg());
		return moArr[MoMsgConst.MSG_CONTENT_INDEX];
	}
	
	/**
	 * 解析originMsg获取linkId
	 * @return
	 */
	public String getLinkId(){
		String[] moArr = StringUtils.split(this.getOriginMsg(), DelimiterConst.MO_SMS_DELIMITER);
		Assert.isTrue(moArr.length > MoMsgConst.LINK_ID_INDEX, "解析上行队列内容错误，上行队列内容为："+this.getOriginMsg());
		return moArr[MoMsgConst.LINK_ID_INDEX];
	}
	
	/**
	 * 解析originMsg获取platform
	 * @return
	 */
	public String getPlatform(){
		String[] moArr = StringUtils.split(this.getOriginMsg(), DelimiterConst.MO_SMS_DELIMITER);
		Assert.isTrue(moArr.length > MoMsgConst.PLAT_FORM_INDEX, "解析上行队列内容错误，上行队列内容为："+this.getOriginMsg());
		return moArr[MoMsgConst.PLAT_FORM_INDEX];
	}
	
	/**
	 * 解析originMsg获取serviceup
	 * @return
	 */
	public String getServiceUp(){
		String[] moArr = StringUtils.split(this.getOriginMsg(), DelimiterConst.MO_SMS_DELIMITER);
		Assert.isTrue(moArr.length > MoMsgConst.SERVICE_UP_INDEX, "解析上行队列内容错误，上行队列内容为："+this.getOriginMsg());
		return moArr[MoMsgConst.SERVICE_UP_INDEX];
	}
	
	/**
	 * 解析originMsg获取spnumber
	 * @return
	 */
	public String getSpNumber(){
		String[] moArr = StringUtils.split(this.getOriginMsg(), DelimiterConst.MO_SMS_DELIMITER);
		Assert.isTrue(moArr.length > MoMsgConst.SP_NUMBER_INDEX, "解析上行队列内容错误，上行队列内容为："+this.getOriginMsg());
		return moArr[MoMsgConst.SP_NUMBER_INDEX];
	}

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public String getEngineName() {
		return EngineName;
	}

	public void setEngineName(String engineName) {
		EngineName = engineName;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getOriginMsg() {
		return originMsg;
	}

	public void setOriginMsg(String originMsg) {
		this.originMsg = originMsg;
	}

	public String getNoPrefixMsg() {
		return noPrefixMsg;
	}

	public void setNoPrefixMsg(String noPrefixMsg) {
		this.noPrefixMsg = noPrefixMsg;
	}

	public Configuration getConfigurationUtil() {
		return configurationUtil;
	}

	public void setConfigurationUtil(Configuration configurationUtil) {
		this.configurationUtil = configurationUtil;
	}

	public SendLogDAO getSendLogDAOImpl() {
		return GateWayApp.getBean("sendLogDAO");
	}

	public EngineLogDAO getEngineLogDAOImpl() {
		return GateWayApp.getBean("engineLogDAO");
	}


	public String getSettleId() {
		return settleId;
	}

	public void setSettleId(String settleId) {
		this.settleId = settleId;
	}

	public Long getSendLogId() {
		return sendLogId;
	}
}
