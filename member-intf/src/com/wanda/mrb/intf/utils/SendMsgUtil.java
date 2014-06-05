/**
 * 
 */
package com.wanda.mrb.intf.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hp.smsplatform.server.ServiceClient;
import com.hp.smsplatform.server.ServiceUrl;
import com.hp.smsplatform.server.rabbitmq.WriteToQueue;
import com.hp.smsplatform.server.webservice.bean.Settle;
import com.hp.smsplatform.server.webservice.bean.SettleResult;
import com.wanda.mrb.intf.SQLConstDef;
import com.wanda.mrb.intf.bean.MsgIpConfigBean;

/**
 * @author xuesi
 *
 */
public class SendMsgUtil {
	/**
	 * @param msgSvcIp	短信代理地址
	 * @param msgChannelId	通道号
	 * @param mobileNo	手机号
	 * @param systemId	系统编号
	 * @param msgContent	短信内容
	 */
	public static void sendMsgCheckCode(Connection conn, String msgSvcIp,String msgChannelId,String mobileNo,String systemId,String msgContent){
		try {
			PreparedStatement ps = conn.prepareStatement(SQLConstDef.SELECT_MSG_SVC_INFO);
			ps.setString(1, "MSG_OPEN");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String isMsgOpen = rs.getString("parameter_value");
				if ("0".equals(isMsgOpen)) {
					systemId = "001";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ServiceUrl.setMqUrl(msgSvcIp);
		ServiceUrl.setWebServiceUrl("http://"+msgSvcIp+"/SmsPlatform/messagePlatform/smsService?wsdl");
		SettleResult sr;
		String settleId = "";
		try {
			sr = ServiceClient.getSettleInfo(systemId);
			if ("00".equals(sr.getStatus())) {
				for (Settle stl : sr.getSettles()) {
					settleId = stl.getSettleId();
				}
			} else {
				System.out.println("获取商户信息失败，错误码：" + sr.getStatus());
			}
			//发送短信
			WriteToQueue wq=new WriteToQueue();
			wq.sendBulkSmsToQueue(
					msgChannelId,
					mobileNo, 
					"001", 
					settleId, 
					msgContent, 
					0
					);
			if(wq.isSendSeccess){
				System.out.println("发送成功");
			}
			System.out.println(wq.sendSuccessCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static MsgIpConfigBean getMsgIpConfig(Connection conn){
		MsgIpConfigBean msgIpMap = new MsgIpConfigBean();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SQLConstDef.SELECT_MSG_SVC_INFO);
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
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
