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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.member.segment.service.SegmentMessageService;
import com.wanda.ccs.member.segment.vo.SegmentMessageVo;
import com.wanda.ccs.member.segment.vo.SendLogVo;

/**
 * @ClassName: MessageSendJob
 * @Description: 短信定时发送的任务
 * @author 许雷
 * @date 2015年6月1日 上午10:16:25
 *
 */
public class MessageSendJob extends TimerTask implements MessageSendConf{

	private static Log log = LogFactory.getLog(MessageSendJob.class);

	@InstanceIn(path = "SegmentMessageService")
	private SegmentMessageService segmentMessageService;
	
	private SegmentMessageVo messageSendVo;

	private BlockingQueue<String> moibleQue;

	private Connection conn;
	
	public static AtomicInteger count = new AtomicInteger(0);//标示成功的记录数 线程安全integer

	public MessageSendJob(SegmentMessageVo messageSendVo,
			BlockingQueue<String> moibleQue, Connection conn) {
		this.messageSendVo = messageSendVo;
		this.moibleQue = moibleQue;
		this.conn = conn;
	}

	/*
	 * (非 Javadoc) <p>Title: run</p> <p>Description: </p>
	 * 
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
//		// 获取短信平台代理地址和通道号
//		String msgSvcIp = "";
//		String msgChannelId = "";
//		String systemId = "001";
		PreparedStatement upstatusPs = null;
		ExecutorService executorService = Executors.newCachedThreadPool();
		try {
//			Map<String, String> msgConfigMap = SmsConfigFactory
//					.getSmsConfigInstance(conn);
//			try {
//				PreparedStatement ps = conn
//						.prepareStatement(SELECT_MSG_SVC_INFO);
				upstatusPs = conn.prepareStatement(UPDATE_SEND_STATUS);
//				ps.setString(1, "MSG_OPEN");
//				ResultSet rs = ps.executeQuery();
//				while (rs.next()) {
//					String isMsgOpen = rs.getString("parameter_value");
//					if ("0".equals(isMsgOpen)) {
//						systemId = "001";
//					}
//				}
//			} catch (Exception e) {
//				log.warn("AN Exception here is ",e);
//				e.printStackTrace();
//			}
//			msgSvcIp = msgConfigMap.get("MSG_MQ_IP");
//			msgChannelId = msgConfigMap.get("MSG_CHANNEL_ID");
//			
			
			Timestamp start = new Timestamp(System.currentTimeMillis());
			SendLogVo sendLogVo = new SendLogVo();
			sendLogVo.setStartTime(start);//短信发送开始时间
		    int treadCount = 20;//定义线程池线程总数  
		    new SMSConfig().initSMSConfig();
		    //调用短信接口发送短信 
		      for (int i = 0; i < treadCount; i++) {  
		    	  executorService.execute(new SendJobThread(messageSendVo,messageSendVo.getContent(),
		    				moibleQue));
		      }  
		      
		    Timestamp end = new Timestamp(System.currentTimeMillis());
			sendLogVo.setEndTime(end);//短信发送结束时间
			sendLogVo.setSendCount(MessageSendJob.count.longValue());//短信发送成功总数
			sendLogVo.setSegm_messageId(messageSendVo.getSegmMessageId());
			sendLogVo.setSendStatus("T");//标示发送成功
			conn.prepareStatement("DROP TABLE T_MOIBLE_"+messageSendVo.getSegmentId()).execute();//删除对应客群的电话号表
			log.info("MESSAGE HAS BEEN SEND SEND CALCOUNT IS "
					+ MessageSendJob.count.longValue());
			segmentMessageService.insertSendLog(sendLogVo);
		} catch (Exception e) {
			log.warn("An Exception here is " + e.getMessage(), e);
			e.printStackTrace();
		} 
		finally {//释放资源更新状态
			try {
				upstatusPs.setString(1, "" + MessageSendJob.count.longValue());
				upstatusPs.setString(2, "" + messageSendVo.getSegmMessageId());
				upstatusPs.execute();
				upstatusPs.close();//释放PreparedStatement Connection还在使用不需要释放
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				MessageSendJob.count.set(0);//标示成功记录数置0
			    executorService.shutdown(); //释放线程池
			}
		}
	}

	public SegmentMessageVo getMessageSendVo() {
		return messageSendVo;
	}

	public void setMessageSendVo(SegmentMessageVo messageSendVo) {
		this.messageSendVo = messageSendVo;
	}

	/**
	* @Title: getMsgIpConfig
	* @Description: 获取短信发送配置
	* @param @param conn
	* @param @return    设定文件
	* @return MsgIpConfigBean    返回类型
	* @throws
	*/
	public static MsgIpConfigBean getMsgIpConfig(Connection conn) {
		MsgIpConfigBean msgIpMap = new MsgIpConfigBean();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SELECT_MSG_SVC_INFO);
			ps.setString(1, "MSG_MQ_IP");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				msgIpMap.setMsgMqIp(rs.getString("parameter_value"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msgIpMap;
	}
}
