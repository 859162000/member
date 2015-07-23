/**  
 * @Title: MessageSendJob.java
 * @Package com.wanda.ccs.member.segment
 * @Description: 短信发送工具类
 * @author 许雷
 * @date 2015年6月1日 上午10:16:25
 * @version V1.0  
 */
package com.wanda.ccs.member.segment;

import java.sql.Connection;

import com.hp.smsplatform.server.ServiceClient;
import com.hp.smsplatform.server.ServiceUrl;
import com.hp.smsplatform.server.rabbitmq.WriteToQueue;
import com.hp.smsplatform.server.webservice.bean.Settle;
import com.hp.smsplatform.server.webservice.bean.SettleResult;

/**
 * @ClassName: SendMsgUtil
 * @Description: 短信发送工具类
 * @author 许雷
 * @date 2015年6月3日 上午9:36:15
 *
 */
public class SendMsgUtil {

	/**
	 * @param msgSvcIp
	 *            短信代理地址
	 * @param msgChannelId
	 *            通道号
	 * @param mobileNo
	 *            手机号
	 * @param systemId
	 *            系统编号
	 * @param msgContent
	 *            短信内容
	 * @throws Exception
	 */
	public static int sendSegmMsg( String msgSvcIp,
			String msgChannelId, String mobileNo, String systemId,
			String msgContent) throws Exception {
		ServiceUrl.setMqUrl(msgSvcIp);
		ServiceUrl.setWebServiceUrl("http://" + msgSvcIp
				+ "/SmsPlatform/messagePlatform/smsService?wsdl");
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
			// 发送短信
			WriteToQueue wq = new WriteToQueue();
			wq.sendBulkSmsToQueue(msgChannelId, mobileNo, "001", settleId,
					msgContent, 0);
			if (wq.isSendSeccess) {
				System.out.println("发送成功");
			}
			return wq.sendSuccessCount;
		} catch (Exception e) {
			throw e;
		}
	}
}
