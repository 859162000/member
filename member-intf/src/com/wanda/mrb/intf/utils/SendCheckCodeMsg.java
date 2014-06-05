/**
 * 
 */
package com.wanda.mrb.intf.utils;

import com.hp.smsplatform.server.ServiceClient;
import com.hp.smsplatform.server.ServiceUrl;
import com.hp.smsplatform.server.rabbitmq.WriteToQueue;
import com.hp.smsplatform.server.webservice.bean.Settle;
import com.hp.smsplatform.server.webservice.bean.SettleResult;

/**
 * @author xuesi
 *
 */
public class SendCheckCodeMsg {
	public static void sendMsgCheckCode(String mobileNo,String systemId,String checkCode){
		String msgIp = "";
		String webServicePort = "";
		
		ServiceUrl.setMqUrl("10.199.90.46");
		ServiceUrl
			.setWebServiceUrl("http://10.199.90.46/SmsPlatform/messagePlatform/smsService?wsdl");
		SettleResult sr;
		try {
			//网站的数据库还没有准备好，目前系统id先用"001"
			systemId = "001";
			//获取结算编号
			String settleId = "";
			sr = ServiceClient.getSettleInfo(systemId);
			if ("00".equals(sr.getStatus())) {
				for (Settle stl : sr.getSettles()) {
					settleId = stl.getSettleId();
					System.out.println("******"+settleId);
				}
			} else {
				System.out.println("获取商户信息失败，错误码：" + sr.getStatus());
			}
			//发送短信
			WriteToQueue wq=new WriteToQueue();
			wq.sendBulkSmsToQueue(
					"00001",//万达影迷汇的通道号，需要李超去申请
					mobileNo, 
					systemId, 
					"001", 
					checkCode, 
					0
					);
			if(wq.isSendSeccess){
				System.out.println("发送成功");
			}
			System.out.println("===============");
			System.out.println(wq.sendSuccessCount);
//			ReadFromQueue rfq = new ReadFromQueue();
//			while (true) {
//				System.out.println(rfq.deliveryMessage("BulkSmsQueue", 0));
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		sendMsgCheckCode("18601915759","001","您的验证码是：123321");
	}

}
