/**  
 * @Title: MessageSendJob.java
 * @Package com.wanda.ccs.member.segment
 * @Description: 短信定时发送的任务
 * @author 许雷
 * @date 2015年6月1日 上午10:16:25
 * @version V1.0  
 */
package com.wanda.ccs.member.segment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wanda.ccs.member.segment.vo.SegmentMessageVo;

/**
 * @ClassName: MessageSendJob
 * @Description: 短信定时发送的任务
 * @author 许雷
 * @date 2015年6月1日 上午10:16:25
 *
 */
public class SendJobThread extends Thread implements MessageSendConf{

	private static Log log = LogFactory.getLog(SendJobThread.class);

	private SegmentMessageVo messageSendVo;

	private BlockingQueue<String> moibleQue;
	
	private Connection conn;
	
	private String content = "";

	public SendJobThread(SegmentMessageVo messageSendVo, String content,
			BlockingQueue<String> moibleQue, Connection conn) {
		this.messageSendVo = messageSendVo;
		this.content = content;
		this.moibleQue = moibleQue;
		this.conn = conn;
	}

	/*
	 * (非 Javadoc) <p>Title: run</p> <p>Description: </p>-
	 * 
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		// 获取短信平台代理地址和通道号
		try {
			if (messageSendVo == null || moibleQue == null
					|| moibleQue.size() == 0) {
				log.warn("An Exception here is messageSend or moibleList object is null, so can't send!");
			} else {
				String moible = moibleQue.poll();
				while (moible != null) {
					for (int i = 0; i < 3; i++) {// 失败尝试三次
						String j = MSGSendUtil.sendSMSBySysC(moible,content);
						System.out.println();
						if ("S001".equals(j)) {
							insertSendMobileLog(moible, "Y");
							int count = MessageSendJob.count.getAndIncrement();
							log.info("Send Message : 第" + count + "条， 已发送！" );
							break;
						} else {
							if (i == 2) {
								insertSendMobileLog(moible, "N");
							}
						}
					}
					moible = moibleQue.poll();
				}
			}
		} catch (Exception e) {
			log.warn("An Exception here is " + e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	
	public void insertSendMobileLog(String moible, String status) {
		PreparedStatement mobileLog = null;
		try {
			mobileLog = conn.prepareStatement(INSERT_T_SEND_MOBILE_LOG);
			Timestamp sendTime = new Timestamp(System.currentTimeMillis());
			mobileLog.setLong(1, messageSendVo.getSegmMessageId());
			mobileLog.setLong(2, Integer.parseInt(messageSendVo.getSegmentId()));
			mobileLog.setString(3, moible);
			mobileLog.setTimestamp(4, sendTime);
			mobileLog.setString(5, status);
			mobileLog.setString(6, messageSendVo.getUpdateBy());
			mobileLog.setTimestamp(7, sendTime);
			mobileLog.setString(8, messageSendVo.getUpdateBy());
			mobileLog.setTimestamp(9, sendTime);
			mobileLog.setString(10, null);
			mobileLog.execute();
			mobileLog.close();
			log.info("==========INSERT_T_SEND_MOBILE_LOG========已插入" + moible );
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
