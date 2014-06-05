package com.wanda.mms.control.stream.dao;

 

import java.util.List;

import com.wanda.mms.control.stream.vo.MsgToSend;

 

/**
 *  
 * 信息发送内容DAO
 * @author wangshuai 
 * @date 2013-04-15
 */
  
public interface MsgToSendDao {
	/***
	 * 添加信息发送内容
	 * @param mts
	 */
	public void addMsgToSend(MsgToSend mts);
	/***
	 * 得到信息发送内容的SEQ
	 * @return
	 */
	public Long findMsgSEQ();
	
	/***
	 * 跟据时间与信息类型 查询出当天同一类型有多少条
	 * @param date
	 * @param SendTypeId
	 */
	public List<MsgToSend> findMsgToSendByDate(Long date);
	
	/***
	 * 跟据时间与信息类型 查询出当天同一类型时间最晚的一条
	 * @param mts
	 * @param SendTypeId
	 */
	public MsgToSend findMsgByDate(Long date, Long SendTypeId);
	
	//一个更新状态
	public void upMsgToSend(MsgToSend mts);
	//一个查寻有多少当天状态没更新的短信
	public List<MsgToSend> findMsgToSendByType(Long date,String type);
	
	
	
	
	
	
	 
	
	
	

}
