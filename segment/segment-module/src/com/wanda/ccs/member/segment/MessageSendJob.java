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
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

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
public class MessageSendJob extends TimerTask {

	private static Log log = LogFactory.getLog(MessageSendJob.class);

	private SegmentMessageVo messageSendVo;

	private List<String> moibleList;

	private Connection conn;

	/**
	* @Fields SELECT_MSG_SVC_INFO
	*/
	private static final String SELECT_MSG_SVC_INFO = "select mc.parameter_value from t_member_config mc where mc.parameter_name = ?";

	/**
	 * @Fields UPDATE_APPROVE_STATUS : 修改SEGM_MESSAGE审批状态的SQL
	 */
	private final String UPDATE_SEND_STATUS = "update SEGM_MESSAGE set SEND_STATUS=? where SEGM_MESSAGE_ID=?";

	public MessageSendJob(SegmentMessageVo messageSendVo,
			List<String> moibleList, Connection conn) {
		this.messageSendVo = messageSendVo;
		this.moibleList = moibleList;
		this.conn = conn;
	}

	/*
	 * (非 Javadoc) <p>Title: run</p> <p>Description: </p>
	 * 
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		// 获取短信平台代理地址和通道号
		String msgSvcIp = "";
		String msgChannelId = "";
		String systemId = "001";
		PreparedStatement upstatusPs = null;
		int sendSuccessCount = 0;
		try {
			Map<String, String> msgConfigMap = SmsConfigFactory
					.getSmsConfigInstance(conn);
			try {
				PreparedStatement ps = conn
						.prepareStatement(SELECT_MSG_SVC_INFO);
				upstatusPs = conn.prepareStatement(UPDATE_SEND_STATUS);
				ps.setString(1, "MSG_OPEN");
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					String isMsgOpen = rs.getString("parameter_value");
					if ("0".equals(isMsgOpen)) {
						systemId = "001";
					}
				}
			} catch (Exception e) {
				log.warn("AN Exception here is ",e);
				e.printStackTrace();
			}
			msgSvcIp = msgConfigMap.get("MSG_MQ_IP");
			msgChannelId = msgConfigMap.get("MSG_CHANNEL_ID");
			if (messageSendVo == null || moibleList == null
					|| moibleList.size() == 0) {
				log.warn("An Exception here is messageSend or moibleList object is null, so can't send!");
			} else {
				String content = messageSendVo.getContent();
				for (String moible : moibleList) {
					for (int i = 0; i < 3; i++) {// 失败尝试三次
						int j = SendMsgUtil.sendSegmMsg(conn, msgSvcIp,
								msgChannelId, moible, systemId, content);
						if (j != 0) {
							sendSuccessCount += 1;
							break;
						}
					}
				}
			}
			conn.prepareStatement("DROP TABLE T_MOIBLE_"+messageSendVo.getSegmentId()).execute();//删除对应客群的电话号表
			log.info("MESSAGE HAS BEEN SEND SEND CALCOUNT IS "
					+ sendSuccessCount);
		} catch (Exception e) {
			log.warn("An Exception here is " + e.getMessage(), e);
			e.printStackTrace();
		} finally {
			try {
				upstatusPs.setString(1, "" + sendSuccessCount);
				upstatusPs.setString(2, "" + messageSendVo.getSegmMessageId());
				upstatusPs.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public SegmentMessageVo getMessageSendVo() {
		return messageSendVo;
	}

	public void setMessageSendVo(SegmentMessageVo messageSendVo) {
		this.messageSendVo = messageSendVo;
	}

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
