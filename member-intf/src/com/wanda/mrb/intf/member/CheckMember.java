package com.wanda.mrb.intf.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import org.w3c.dom.Element;

import com.wanda.mrb.intf.ConstDef;
import com.wanda.mrb.intf.GetEventCmn;
import com.wanda.mrb.intf.SQLConstDef;
import com.wanda.mrb.intf.ServiceBase;
import com.wanda.mrb.intf.member.vo.TContent;
import com.wanda.mrb.intf.member.vo.TMember;
import com.wanda.mrb.intf.utils.SmsConfigFactory;

public class CheckMember extends ServiceBase {
	public CheckMember() {
		super();
		super.intfCode = ConstDef.CONST_INTFCODE_M_CHECKMEMBER;
	}

	TMember member = new TMember();
	long memberSeqId = 0;
	String memberStatus = "";
	String triggerSystem = "";
	String cinemaInnerCode = "";//发短信是传的影城内码
	TContent content = new TContent();
	GetEventCmn gec = new GetEventCmn();

	@Override
	protected void bizPerform() throws Exception {
		Connection conn = getDBConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		if ((member.memberNo != null) && !("".equals(member.memberNo))) {
			System.err.print("t_member");
			ps = conn.prepareStatement(SQLConstDef.MEMBER_CHECK_BY_MEMBERNO);
			ps.setString(1, member.memberNo);
		} else if ((member.mobile != null) && !("".equals(member.mobile))) {
			// 验证手机号
			//checkMobileNO(conn, member.mobile);
			System.err.print("t_member");
			ps = conn.prepareStatement(SQLConstDef.MEMBER_CHECK_BY_MOBILE);
			ps.setString(1, member.mobile);
		} else if ((member.cardNumber != null)
				&& !("".equals(member.cardNumber))) {
			if ("0".equals(member.cardType)) {
				// 会员卡
				System.err.print("T_MACK_DADDY_CARD,T_MEMBER");
				ps = conn
						.prepareStatement(SQLConstDef.MEMBER_CHECK_BY_CARDNUMBER);
				ps.setString(1, member.cardNumber);
			} else if ("1".equals(member.cardType)) {
				System.err.print("T_MEM_CARD_REL,T_MEM_CARD_INFO,T_MEMBER");
				ps = conn
						.prepareStatement(SQLConstDef.MEMBER_CHECK_BY_REL_CARDNUMBER);
				ps.setString(1, member.cardNumber);
			}
		}
		rs = ps.executeQuery();
		if (rs != null && rs.next()) {
			memberSeqId = rs.getLong("MEMBER_ID"); // 获取主键
			memberStatus = rs.getString("STATUS");
			if ("1".equals(memberStatus)) {
				// 获取到了memberid之后，可以根据memberid获取到会员信息
				// t_member基本信息
				
				System.err.print("T_MEMBER,T_CINEMA");
				ps = conn.prepareStatement(SQLConstDef.MEMBER_CHECK_BY_MEMBERID);
				ps.setLong(1, memberSeqId);
				rs = ps.executeQuery();
				if (rs != null && rs.next()) {
					member.memberNo = rs.getString("MEMBER_NO");
					member.name = rs.getString("NAME");
					/*member.name = member.name.replace("&", "&amp;");
					member.name = member.name.replace("<", "&lt;");
					member.name = member.name.replace(">", "&gt;");
					member.name = member.name.replace("&apos;", "&apos;");
					member.name = member.name.replace("\"", "&quot;");*/
					member.mobile = rs.getString("MOBILE");
					member.email = rs.getString("EMAIL");
					member.birthday = rs.getString("BIRTHDAY");
					member.gender = rs.getString("GENDER");
					member.registCinemaCode = rs.getString("CODE");
					member.operator = rs.getString("REGIST_OP_NO");
					member.operatorName = rs.getString("REGIST_OP_NAME");
					member.dtsId = rs.getString("SOURCE_TYPE");
					member.registChnID = rs.getString("REGIST_CHNID");
					member.counterName = rs.getString("SHORT_NAME");
					member.tel = rs.getString("PHONE");
					member.registDate = rs.getString("REGIST_DATE");
					// 常驻影城
					long cinemaID = rs.getLong("FAV_CINEMA_ID");
					ps = conn
							.prepareStatement(SQLConstDef.MEMBER_SELECT_CINEMACODE_BY_ID);
					ps.setLong(1, cinemaID);
					rs = ps.executeQuery();
					while (rs != null && rs.next()) {
						member.favCinema = rs.getString("code");
						member.favCinemaName = rs.getString("short_name");
					}
				}

				// t_member_info信息
				
				 System.err.print("T_MEMBER_INFO"); 
				ps = conn.prepareStatement(SQLConstDef.MEMBER_INFO_BY_MEMBERID);
				ps.setLong(1, memberSeqId);
				rs = ps.executeQuery();
				if (rs != null && rs.next()) {
					member.idCardNo = rs.getString("IDCARD_NO");
					member.idCardHashNo = rs.getString("IDCARD_HASHNO");
					member.identityType = rs.getInt("IDCARD_TYPE");
					member.maritalStatus = rs.getString("MARRY_STATUS");
					member.childNumber = rs.getInt("CHILD_NUMBER");
					member.education = rs.getInt("EDUCATION");
					member.occupation = rs.getInt("OCCUPATION");
					member.income = rs.getInt("INCOME");
					member.fqCinemaDist = rs.getInt("FQ_CINEMA_DIST");
					member.fqCinemaTime = rs.getInt("FQ_CINEMA_TIME");
					member.mobileOptin = rs.getString("MOBILE_OPTIN");
					member.otherNo = rs.getString("QQ");
					long manCinemaID = rs.getLong("MANAGE_CINEMA_ID");
					ps = conn.prepareStatement(SQLConstDef.MEMBER_SELECT_CINEMACODE_BY_ID);
					ps.setLong(1, manCinemaID);
					rs = ps.executeQuery();
					while (rs != null && rs.next()) {
						member.manCinema = rs.getString("code");
						member.manCinemaName = rs.getString("short_name");
					}
				}

				// t_member_addr信息
				 System.err.print("T_MEMBER_ADDR"); 
				ps = conn.prepareStatement(SQLConstDef.MEMBER_ADDR_BY_MEMBERID);
				ps.setLong(1, memberSeqId);
				rs = ps.executeQuery();
				if (rs != null && rs.next()) {
					member.address1 = rs.getString("ADDRESS1");
					String provinceId = rs.getString("PROVINCE_ID");
					if (!"".equals(provinceId) && provinceId != null) {
						member.provinceID = Long.parseLong(provinceId);
					}
					String cityId = rs.getString("CITY_ID");
					if (!"".equals(cityId) && cityId != null) {
						member.cityID = Long.parseLong(cityId);
					}
					member.zipCode = rs.getString("ZIPCODE");
				}

				// T_MEM_FAV_CONTACT信息
				 System.err.print("T_MEM_FAV_CONTACT"); 
				ps = conn.prepareStatement(SQLConstDef.MEMBER_CONTACT_BY_MEMBERID);
				ps.setLong(1, memberSeqId);
				rs = ps.executeQuery();
				if (rs != null && rs.next()) {
					member.contactMeans = rs.getString("CONTACT_MEANS");
				}

				// T_MEMBER_LEVEL信息
				System.err.print("T_MEMBER_LEVEL"); 
				ps = conn.prepareStatement(SQLConstDef.MEMBER_LEVEL_BY_MEMBERID);
				ps.setLong(1, memberSeqId);
				rs = ps.executeQuery();
				if (rs != null && rs.next()) {
					member.levelCode = rs.getString("MEM_LEVEL");
					member.expireDate = rs.getString("EXPIRE_DATE");
					member.orgLevel = rs.getString("ORG_LEVEL");
					member.setTime = rs.getString("SET_TIME");
					member.targetLevel = rs.getString("TARGET_LEVEL");
					member.levelPointOffset = rs.getString("LEVEL_POINT_OFFSET");
					member.ticketOffset = rs.getString("TICKET_OFFSET");
				}

				// 会员卡
				System.err.print("T_MACK_DADDY_CARD");
				ps = conn.prepareStatement(SQLConstDef.MEMBER_CARD_BY_MEMBERID);
				ps.setLong(1, memberSeqId);
				rs = ps.executeQuery();
				if (rs != null && rs.next()) {
					member.cardNumber = rs.getString("CARD_NUMBER");
				}
				
				//T_MEMBER_POINT
				System.err.print("T_MEMBER_POINT");
				ps = conn.prepareStatement(SQLConstDef.SELECT_MEMBER_POINT);
				ps.setLong(1, memberSeqId);
				rs = ps.executeQuery();
				if (rs != null && rs.next()) {
					member.levelPointTotal = rs.getInt("LEVEL_POINT_TOTAL");
					member.activityPoint = rs.getInt("ACTIVITY_POINT");
					member.exgPointBalance = rs.getInt("EXG_POINT_BALANCE");
					member.exgExpirePointBal = rs.getInt("EXG_EXPIRE_POINT_BALANCE");
				}
				
				//获取短信平台代理地址和通道号
				String msgSvcIp = "";
				String msgChannelId = "";
				String msgCheckOpen = "";
				String msgOpen = "";
//				try {
//					ps = conn.prepareStatement(SQLConstDef.SELECT_MSG_SVC_INFO);
//					ps.setString(1, "MSG_MQ_IP");
//					rs = ps.executeQuery();
//					while (rs.next()) {
//						msgSvcIp = rs.getString("parameter_value");
//					}
//					
//					ps = conn.prepareStatement(SQLConstDef.SELECT_MSG_SVC_INFO);
//					ps.setString(1, "MSG_CHANNEL_ID");
//					rs = ps.executeQuery();
//					while (rs.next()) {
//						msgChannelId = rs.getString("parameter_value");
//					}
//					
//					ps = conn.prepareStatement(SQLConstDef.SELECT_MSG_SVC_INFO);
//					ps.setString(1, "MSG_CHECK_OPEN");
//					rs = ps.executeQuery();
//					while (rs.next()) {
//						msgCheckOpen = rs.getString("parameter_value");
//					}
//					
//					ps = conn.prepareStatement(SQLConstDef.SELECT_MSG_SVC_INFO);
//					ps.setString(1, "MSG_OPEN");
//					rs = ps.executeQuery();
//					while (rs.next()) {
//						msgOpen = rs.getString("parameter_value");
//					} 
//					
//					if ("0".equals(msgOpen)) {
//						cinemaInnerCode = "001";
//					} else {
//						ps = conn.prepareStatement(SQLConstDef.SELECT_MSG_CINEMA_INNERCODE);
//						ps.setLong(1, memberSeqId);
//						rs = ps.executeQuery();
//						while (rs.next()) {
//							cinemaInnerCode = rs.getString("inner_code");
//						}
//					}
//				} catch (Exception e) {
//					conn.rollback();
//					throw e;
//				}
				
				Map<String,String> msgConfigMap = SmsConfigFactory.getSmsConfigInstance(conn);
				msgSvcIp = msgConfigMap.get("MSG_MQ_IP");
				msgChannelId = msgConfigMap.get("MSG_CHANNEL_ID");
				msgCheckOpen = msgConfigMap.get("MSG_CHECK_OPEN");
				msgOpen = msgConfigMap.get("MSG_OPEN");
				if ("0".equals(msgOpen)) {
					cinemaInnerCode = "001";
				} else {
					ps = conn.prepareStatement(SQLConstDef.SELECT_MSG_CINEMA_INNERCODE);
					ps.setLong(1, memberSeqId);
					rs = ps.executeQuery();
					while (rs.next()) {
						cinemaInnerCode = rs.getString("inner_code");
					}
				}
				
				//POS发送短信
				if(super.cinemaCode != null && !"99999999".equals(super.cinemaCode)){
					//POS发送短信
					triggerSystem = "POS";
					content = gec.getCheckEvenContent(member.birthday, member.levelCode, member.setTime, 
							member.levelPointOffset, member.ticketOffset, memberSeqId, conn, triggerSystem);
					member.talk = content.getPosIntfCount();
//					ResultQuery rsq=SqlHelp.query(conn, SQLConstDef.MEMBER_SELECT_INNERCODE_BY_CODE, super.cinemaCode);
//					rs=rsq.getResultSet();
//					while (rs.next()) {
//						cinemaInnerCode = rs.getString("INNER_CODE");
//					}
//					SendCheckCodeMsg.sendMsgCheckCode(member.mobile,cinemaInnerCode,content.getPosMsgContent());
//					SMSControl smsSendObj = new SMSControl();
//					smsSendObj.smssend(conn, member.mobile, content.getPosMsgContent());
					System.err.print(content.getPosMsgContent());
					String posMsgContent = content.getPosMsgContent();
//					if ("1".equals(msgCheckOpen) && !"".equals(posMsgContent) && posMsgContent != null) {
//						SendMsgUtil.sendMsgCheckCode(conn, msgSvcIp, msgChannelId, member.mobile, cinemaInnerCode, content.getPosMsgContent());
//					}
//					member.talk = gec.getCheckEvenContent(member.birthday, member.levelCode, member.setTime, 
//							member.levelPointOffset, member.ticketOffset, memberSeqId, conn, triggerSystem).getPosIntfCount();
				} else{
					triggerSystem = "WEB";
					member.talk = gec.getCheckEvenContent(member.birthday, member.levelCode, member.setTime, 
							member.levelPointOffset, member.ticketOffset, memberSeqId, conn, triggerSystem).getWebIntfContent();
				}
			} else {
				throwsBizException("M030004", "该会员已禁用！");
			}
		} else {
			throwsBizException("M030002", "该会员不存在！");
		}
	}

	@Override
	protected void parseXMLParam(Element root) throws Exception {
		// 会员编码
		member.memberNo = getChildValueByName(root,
				ConstDef.CONST_INTFCODE_M_REGISTER_MEMBERNO, 64);

		// 会员手机号
		member.mobile = getChildValueByName(root,
				ConstDef.CONST_INTFCODE_M_REGISTER_MOBILE, 11);

		// 会员卡号
		member.cardNumber = getChildValueByName(root,
				ConstDef.CONST_INTFCODE_M_REGISTER_CARD_NUMBER, 30);

		// 会员卡类型 0：会员卡 1：会员关联卡
		String cardTypeStr = getChildValueByName(root,
				ConstDef.CONST_INTFCODE_M_REGISTER_CARD_TYPE, 2);
		if ((member.cardNumber != null) && !("".equals(member.cardNumber))) {
			validateParamNotEmpty(cardTypeStr, "卡类型");
			if (!"0".equals(cardTypeStr) && !"1".equals(cardTypeStr)) {
				throwsBizException("M030001", "卡类型参数错误");
			}
			member.cardType = cardTypeStr;
		}
		// 是否展现营销话术0:不需要话术 1：需要返回话术
		String istalkStr = getChildValueByName(root,
				ConstDef.CONST_INTFCODE_M_REGISTER_ISTALK, 8);
		if ("".equals(istalkStr)) {
			throwsBizException("M030002", "营销话术参数不能为空");
		} else {
			member.istalk = Integer.parseInt(istalkStr);
		}
	}

	@Override
	protected String composeXMLBody() {
		StringBuffer buf = new StringBuffer();
		buf.append(createXmlTag("MEMBER_NO", member.memberNo));
		buf.append(createXmlTag("NAME", member.name));
		buf.append(createXmlTag("MOBILE", member.mobile));
		buf.append(createXmlTag("TEL", member.tel));
		buf.append(createXmlTag("ID_CARD_NO", member.idCardNo));
		buf.append(createXmlTag("ID_CARD_HASH_NO", member.idCardHashNo));
		buf.append(createXmlTag("IDENTITY_TYPE", "" + member.identityType));
		buf.append(createXmlTag("EMAIL", member.email));
		buf.append(createXmlTag("ADDRESS", member.address1));
		buf.append(createXmlTag("PROVINCE_ID", "" + member.provinceID));
		buf.append(createXmlTag("CITY_ID", "" + member.cityID));
		buf.append(createXmlTag("ZIP", member.zipCode));
		buf.append(createXmlTag("CONTACT_MEANS", member.contactMeans));
		buf.append(createXmlTag("BIRTHDAY", member.birthday));
		buf.append(createXmlTag("GENDER", member.gender));
		buf.append(createXmlTag("MARITAL_STATUS", member.maritalStatus));
		buf.append(createXmlTag("CHILD_NUMBER", "" + member.childNumber));
		buf.append(createXmlTag("EDUCATION", "" + member.education));
		buf.append(createXmlTag("OCCUPATION", "" + member.occupation));
		buf.append(createXmlTag("INCOME", "" + member.income));
		buf.append(createXmlTag("FILM_TYPES", member.favFilmType));
		buf.append(createXmlTag("FQ_CINEMA_DIST", "" + member.fqCinemaDist));
		buf.append(createXmlTag("FQ_CINEMA_TIME", "" + member.fqCinemaTime));
		buf.append(createXmlTag("MOBILE_OPTIN", member.mobileOptin));
		buf.append(createXmlTag("COUNTER", member.counter));
		buf.append(createXmlTag("COUNTER_NAME", member.counterName));
		buf.append(createXmlTag("OPERATOR", member.operator));
		buf.append(createXmlTag("DTSID", member.dtsId));// 会员来源
		buf.append(createXmlTag("SOURCE_FOR", member.registChnID));// 招募渠道
		buf.append(createXmlTag("OTHER_NO", member.otherNo));
		buf.append(createXmlTag("SALES_TALK_XML",member.talk));
		buf.append(createXmlTag("LEVEL_POINT_TOTAL", ""
				+ member.levelPointTotal));
		buf.append(createXmlTag("ACTIVITY_POINT", "" + member.activityPoint));
		buf.append(createXmlTag("EXG_POINT_BALANCE", ""
				+ member.exgPointBalance));
		buf.append(createXmlTag("EXG_EXPIRE_POINT_BALANCE", ""
				+ member.exgExpirePointBal));
		buf.append(createXmlTag("LEVEL_CODE", member.levelCode));
		buf.append(createXmlTag("EXPIRE_DATE", member.expireDate));
		buf.append(createXmlTag("ORG_LEVEL", member.orgLevel));
		buf.append(createXmlTag("SET_TIME", member.setTime));
		buf.append(createXmlTag("TARGET_LEVEL", member.targetLevel));
		buf.append(createXmlTag("LEVEL_POINT_OFFSET", member.levelPointOffset));
		buf.append(createXmlTag("MEMBER_CARD_NO", member.cardNumber));// 会员卡号
		buf.append(createXmlTag("FAV_CINEMA", member.favCinema));
		buf.append(createXmlTag("FAV_CINEMA_NAME", member.favCinemaName));
		buf.append(createXmlTag("MAN_CINEMA", member.manCinema));
		buf.append(createXmlTag("MAN_CINEMA_NAME", member.manCinemaName));
		buf.append(createXmlTag("REG_TIME", member.registDate));
		buf.append(createXmlTag("OPERATOR_NAME", member.operatorName));
		return buf.toString();
	}

	public void checkMemberCardMobile(Connection conn, String mobile) {

	}
}
