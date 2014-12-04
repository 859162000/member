package com.wanda.mrb.intf.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import org.w3c.dom.Element;

import com.wanda.mrb.intf.ConstDef;
import com.wanda.mrb.intf.SQLConstDef;
import com.wanda.mrb.intf.ServiceBase;
import com.wanda.mrb.intf.utils.SendMsgUtil;
import com.wanda.mrb.intf.utils.SmsConfigFactory;
import com.wanda.mrb.intf.utils.SqlHelp;

public class SendCheckCode extends ServiceBase {
	private String mobile;
	String checkCode = "";
	String systemCode = "";
	public SendCheckCode() {
		super();
		super.intfCode = ConstDef.CONST_INTFCODE_M_SENDCHECKCODE;
	}

	@Override
	protected void bizPerform() throws Exception {
		Connection conn = getDBConnection();
		String cinemaInnerCode = "";
		// 获取短信平台代理地址和通道号
		String msgSvcIp = "";
		String msgChannelId = "";
		Map<String, String> msgConfigMap = SmsConfigFactory
				.getSmsConfigInstance(conn);
		msgSvcIp = msgConfigMap.get("MSG_MQ_IP");
		msgChannelId = msgConfigMap.get("MSG_CHANNEL_ID");
		String msgRedOpen = msgConfigMap.get("MSG_RED_OPEN");
		if (super.cinemaCode != null && !"99999999".equals(super.cinemaCode)) {
			try {
				PreparedStatement ps = conn
						.prepareStatement(SQLConstDef.MEMBER_SELECT_INNERCODE_BY_CODE);
				ps.setString(1, super.cinemaCode);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					cinemaInnerCode = rs.getString("inner_code");
				}
			} catch (Exception e) {
				conn.rollback();
				throw e;
			}
		} else {
			cinemaInnerCode = "002";
		}

		if ("1".equals(msgRedOpen)) {
			// 短信开放
			// 存入
			checkCode = random(6);// 短信发送内容
			SqlHelp.operate(conn, SQLConstDef.INSERT_CHECK_CODE, checkCode,
					mobile);
			try {
				// 调用发短信接口发短信
				SendMsgUtil.sendMsgCheckCode(conn, msgSvcIp, msgChannelId,
						mobile, cinemaInnerCode, "您的手机验证码为" + checkCode
								+ ",5分钟内有效");
			} catch (Exception e) {
				SendMsgUtil.sendMsgCheckCode(conn, msgSvcIp, msgChannelId,
						mobile, "002", "您的手机验证码为" + checkCode + ",5分钟内有效");
			}
		}
	}

	@Override
	protected void parseXMLParam(Element root) throws Exception {
		mobile = getChildValueByName(root, "MOBILE", 64);
		systemCode = getChildValueByName(root, "SYSTEM_CODE", 64);
	}

	@Override
	protected String composeXMLBody() {
		StringBuffer buf = new StringBuffer();
		buf.append(createXmlTag("CHECK_CODE", checkCode));
		return buf.toString();
	}

	public static String random(int n) {
		String tex = "";
		for (int i = 1; i <= n; i++) {
			tex += "" + (int) (Math.random() * 10);
		}
		return tex;
	}

}
