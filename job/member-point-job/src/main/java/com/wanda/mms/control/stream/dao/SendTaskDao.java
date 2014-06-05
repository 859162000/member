package com.wanda.mms.control.stream.dao;



import java.util.List;

import com.wanda.mms.control.stream.vo.SendTask;


/**
 *  
 * 任务DAO
 * @author wangshuai 
 * @date 2013-04-15
 */
public interface SendTaskDao {
	
	/**
	 * 批量插入任务对列
	 * 
	 * @return list
	 */
	public String addSendTask(List<SendTask> stlist);
	/**
	 * 插入任务对列
	 * @param stlist
	 * @param SendTime
	 * @param msgId
	 * @return String
	 */
	public String addSendTask(List<SendTask> stlist,String sendTime,Long msgId);
	
	/***
	 *  查询有多少符合当前发送要求的发送任务
	 * @param sendtypeid
	 * @param msgId
	 * @return list
	 */
	public List<SendTask> fandTargetandaddTaskSendTask(Long sendtypeid,Long msgId,String SendTime);
	/**
	 * 批量更新 发送消息任务功能。
	 * @param stlist
	 * @param sendtime
	 * @return  String
	 */
	public String updateListSendTask(List<SendTask> stlist,String datetime); 
	/**
	 * 更新 发送消息任务功能。
	 * @param sendtask
	 * @param sendtime
	 * @return  int
	 */
	public int updateListSendTask(SendTask sendtask,String datetime);
	/**
	 * 得到同一信息发送内容得任务
	 * @param msgid
	 * @return  List<SendTask>
	 */
	public List<SendTask> fandMsgCode(Long msgid);
	/**
	 * 查询有多少符合要求消息发送者
	 *  
	 * @param msgId
	 * @param retrytimes
	 * @return List<SendTask>
	 */
	public List<SendTask> fandAllTaskSend(Long msgId,int retrytimes);
	
	/**
	 * 给领导发送短信
	 *  
	 * @param msgId
	 * @param retrytimes
	 * @return List<SendTask>
	 */
	public List<SendTask> fandTaskSendByPriority(Long msgId,int priority);
	
	/**
	 * 插入任务对列RTX
	 * @param stlist
	 * @param SendTime
	 * @param msgId
	 * @return String
	 */
	public String addSendTaskRtx(List<SendTask> stlist,String sendTime,Long msgId);
	
	/**
	 * 查询有多少符合要求的RTX消息发送者
	 *  
	 * @param msgId
	 * @param retrytimes
	 * @return List<SendTask>
	 */
	public List<SendTask> fandAllTaskSendRtx(Long msgId,int retrytimes);
	
	/**
	 * 批量更新 发送消息任务功能。
	 * @param stlist
	 * @param sendtime
	 * @return  String
	 */
	public String updateListSendTaskRtx(List<SendTask> stlist,String datetime); 
	
	
	
}
