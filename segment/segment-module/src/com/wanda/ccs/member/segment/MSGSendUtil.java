package com.wanda.ccs.member.segment;

public class MSGSendUtil {

	/**
	 * 集团发送短信 组织发送短信内容
	 */
	public static String sendSMSBySysC(String phoneNum, String sendContent) {
//		String sPxy = "http://10.199.90.26:8080/voucherSMS/sendSMSMsg.do"; //测试用数据
//		String sUrl = "http://msg.wanda.cn:8080/ReceiveServer.svc";
//		String sSys = "YXZBXT001";
		String sPxy = SMSConfig.smsProxyUrl;
		String sUrl = SMSConfig.smsUrl;
		String sSys = SMSConfig.smsFromsys;
		String sTitle = "万达院线会员服务";
		String result = SendSMSHttpUtils.sendPost(sPxy, ConstDef.NAMED_SMS_URL
				+ sUrl + ConstDef.NAMED_SMS_FROMSYS + sSys
				+ ConstDef.NAMED_SMS_TARGET + phoneNum
				+ ConstDef.NAMED_SMS_TITLE + "【" + sTitle + "】"
				+ ConstDef.NAMED_SMS_MSCONTENT + sendContent);
		return result;
	}

	public static void main(String[] args) {
		sendSMSBySysC("13089603422", "亲爱的用户");
	}
}
