/**
 * 
 */
package com.wanda.ccs.campaign;

import com.hp.smsplatform.server.ServiceClient;
import com.hp.smsplatform.server.ServiceUrl;
import com.hp.smsplatform.server.rabbitmq.WriteToQueue;
import com.hp.smsplatform.server.webservice.bean.Settle;
import com.hp.smsplatform.server.webservice.bean.SettleResult;

/**
 * @author xuesi
 * 发送单条的测试短信，用来验证短信内容是否会被屏蔽
 */
public class SendTestMsg {
	/**
	 * @param msgSvcIp:短信平台ip地址
	 * @param msgChannelId:发送短信用的通道号
	 * *******************ip地址和通道号从T_MEMBER_CONFIG中取，parameter分别对应MSG_MQ_IP和MSG_CHANNEL_ID
	 * @param mobileNo:手机号
	 * @param systemId:系统编号。影城发送短信时，传手机号的注册影城内码。院线发送短信时，传：001
	 * @param msgContent:短信内容
	 */
	public static void sendMsgCheckCode(String msgSvcIp,String msgChannelId,String mobileNo,String systemId,String msgContent){
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		sendMsgCheckCode("10.199.90.46","00001","18611626991","001","尊敬的会员，您好！");
	}

}
