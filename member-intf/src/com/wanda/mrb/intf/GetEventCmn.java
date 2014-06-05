/**
 * 
 */
package com.wanda.mrb.intf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wanda.mrb.intf.member.vo.TContent;
import com.wanda.mrb.intf.utils.Utils;

/**
 * @author xuesi
 *
 */
public class GetEventCmn {
	private String chnlCode = "";
	private String upgradeFilm = "''";
	private String highlevelFilm = "''";
	private String birthFilm = "''";
	private String willUpgrade = "''";
	
	/**
	 * @param birthday 生日：判断今天是否是会员生日
	 * @param level 级别：用来判断会员是否是高级会员
	 * @param setTime 级别升降时间：判断是否是升级后第一次消费
	 * @param levelPointOffset 距离升级的积分差距
	 * @param ticketOffset 距离升级的购票差距
	 * @param memberSeqId
	 * @param conn
	 * @param triggerSystem 调用系统：pos或web
	 * @return 获取会员识别接口中的营销话术
	 */
	public TContent getCheckEvenContent(String birthday,String level,String setTime,String levelPointOffset,
			String ticketOffset,long memberSeqId,Connection conn,String triggerSystem){
		TContent content = new TContent();
		List<TContent> contentList = new ArrayList<TContent>();
		String lastTicketShowTime = "";//2399-12-30 20:20:20
		int pointOff = Integer.parseInt(levelPointOffset);
		int ticketOff = Integer.parseInt(ticketOffset);
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean upGrade = false;
		String hasCard = "2";
		String memberFilm = "member-film";
		try {
			ps = conn.prepareStatement(SQLConstDef.QUERY_LAST_TICKET_SHOW_TIME);
			ps.setLong(1, memberSeqId);
			rs = ps.executeQuery();
			if (rs != null && rs.next()) {
				lastTicketShowTime = rs.getString("LAST_TICKET_SHOW_TIME");
			}
			//判断是否满足：升级后首次观影
			if (setTime != null && !"".equals(setTime) && 
					lastTicketShowTime != null && !"".equals(lastTicketShowTime) 
					&& Utils.parserDateSS(setTime).after(Utils.parserDateSS(lastTicketShowTime))) {
				upGrade = true;
			}
			ps = conn.prepareStatement(SQLConstDef.SELECT_MEMBER_CARD_BY_MEMBERID);
			ps.setLong(1, memberSeqId);
			rs = ps.executeQuery();
			if (rs != null && rs.next()) {
				hasCard = "1";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//会员生日到现场观影，只弹POS外屏就可以了，现场再发送短信意义不大
		SimpleDateFormat df = new SimpleDateFormat("MM-dd");
		String nowTime = df.format(new Date());
		if (birthday != null && nowTime.equals(birthday.substring(5))) {//今天是会员的生日
			birthFilm = "birth-film";
		}  
//		if (Integer.parseInt(level) > 3 ) {//高级会员
//			highlevelFilm = "highlevel-film";
//		} 
		if (upGrade) {//升级后首次观影
			upgradeFilm = "upgrade-film";
		}
		if (pointOff <= 100 || ticketOff <= 2) {
			willUpgrade = "will-upgrade";
		}
		if ("WEB".equals(triggerSystem)&&(pointOff <= 100 || ticketOff <= 2)) {//WEB会员即将升级
			//WEB接口,网站不用话术
//			try {
//				ps = conn.prepareStatement(SQLConstDef.QUERY_EVENT_OFFER_CONTENT);
//				ps.setString(1, "WEB");
//				ps.setString(2, "WEB");
//				ps.setString(3, highlevelFilm);
//				ps.setString(4, upgradeFilm);				
//				ps.setString(5, "WEB");
//				ps.setString(6, "WEB");
//				ps.setString(7, ""+(Integer.parseInt(level)+1));
//				ps.setString(8, willUpgrade);
//				ps.setString(9, birthFilm);
//				rs = ps.executeQuery();
//				if (rs != null && rs.next()) {
//					String contentStr = rs.getString("CONTENT");
//					String webUpgradeContent =  getOfferContent(contentStr,levelPointOffset,ticketOffset);
//					content.setWebIntfContent(webUpgradeContent);
//					content.setPriority(rs.getString("PRIORITY"));
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			content.setWebIntfContent(composeWebCheckXML(content));
		} else {//POS
			//POS内屏
			try {
				ps = conn.prepareStatement(SQLConstDef.QUERY_EVENT_OFFER_CONTENT);
				ps.setString(1, "POS");
				ps.setString(2, "POS-INNER");
				ps.setString(3, highlevelFilm);
				ps.setString(4, upgradeFilm);				
				ps.setString(5, "POS");
				ps.setString(6, "POS-INNER");
				ps.setString(7, level);
				ps.setString(8, willUpgrade);
				ps.setString(9, birthFilm);
				ps.setString(10, "POS");
				ps.setString(11, "POS-INNER");
				ps.setString(12, hasCard);
				ps.setString(13, memberFilm);
				rs = ps.executeQuery();
				if (rs != null && rs.next()) {
					TContent contentPos = new TContent();
					String mainTitle = rs.getString("MAIN_TITLE");
					String eventId = rs.getString("EVENT_ID");
					if ("2".equals(eventId)) {
						mainTitle = mainTitle.replace("${targetlevel}", level);
					} else if ("10".equals(eventId)) {
						PreparedStatement psInner = conn.prepareStatement(SQLConstDef.QUERY_NAME_GENDER_FROM_MEMBER);
						psInner.setLong(1, memberSeqId);
						ResultSet rsInner = psInner.executeQuery();
						if (rsInner != null && rsInner.next()) {
							mainTitle = mainTitle.replace("${name}", rsInner.getString("NAME")==null? "":rsInner.getString("NAME"));
							String gender = rsInner.getString("GENDER");
							if ("F".equals(gender)) {
								mainTitle = mainTitle.replace("${gender}","女士");
							} else if ("M".equals(gender)) {
								mainTitle = mainTitle.replace("${gender}","先生");
							} else {
								mainTitle = mainTitle.replace("${gender}","");
							}
						}
					}
					contentPos.setChnlCode(rs.getString("CHNL_CODE"));
					contentPos.setEventCode(rs.getString("EVENT_CODE"));
					contentPos.setPriority(rs.getString("PRIORITY"));
					contentPos.setPosInnerTitle(mainTitle);
					contentPos.setPosInnerSubtitle(rs.getString("SUB_TITLE"));
					contentPos.setPosInnerSubject(rs.getString("SUBJECT"));
					contentList.add(contentPos);
				} 
//				else {
//					if (hasCard) {//有卡
//						ps = conn.prepareStatement(SQLConstDef.QUERY_EVENT_MEMBER_FILM);
//						ps.setString(1, "POS");
//						ps.setString(2, "POS-INNER");
//						ps.setString(3, "1");
//						rs = ps.executeQuery();
//						if(rs != null && rs.next()) {
//							TContent contentPos = new TContent();
//							contentPos.setChnlCode(rs.getString("CHNL_CODE"));
//							contentPos.setEventCode(rs.getString("EVENT_CODE"));
//							contentPos.setPriority(rs.getString("PRIORITY"));
//							contentPos.setPosInnerTitle(rs.getString("MAIN_TITLE"));
//							contentPos.setPosInnerSubtitle(rs.getString("SUB_TITLE"));
//							contentPos.setPosInnerSubject(rs.getString("SUBJECT"));
//							contentList.add(contentPos);
//						}
//					} else {//无卡
//						ps = conn.prepareStatement(SQLConstDef.QUERY_EVENT_MEMBER_FILM);
//						ps.setString(1, "POS");
//						ps.setString(2, "POS-INNER");
//						ps.setString(3, "2");
//						rs = ps.executeQuery();
//						if(rs != null && rs.next()) {
//							TContent contentPos = new TContent();
//							contentPos.setChnlCode(rs.getString("CHNL_CODE"));
//							contentPos.setEventCode(rs.getString("EVENT_CODE"));
//							contentPos.setPriority(rs.getString("PRIORITY"));
//							contentPos.setPosInnerTitle(rs.getString("MAIN_TITLE"));
//							contentPos.setPosInnerSubtitle(rs.getString("SUB_TITLE"));
//							contentPos.setPosInnerSubject(rs.getString("SUBJECT"));
//							contentList.add(contentPos);
//						}
//					}
//				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			//POS外屏，图片名称，图片参数
			try {
				ps = conn.prepareStatement(SQLConstDef.QUERY_EVENT_OFFER_CONTENT);
				ps.setString(1, "POS");
				ps.setString(2, "POS-OUTTER");
				ps.setString(3, highlevelFilm);
				ps.setString(4, upgradeFilm);				
				ps.setString(5, "POS");
				ps.setString(6, "POS-OUTTER");
				ps.setString(7, level);
				ps.setString(8, willUpgrade);
				ps.setString(9, birthFilm);
				ps.setString(10, "POS");
				ps.setString(11, "POS-OUTTER");
				ps.setString(12, hasCard);
				ps.setString(13, memberFilm);
				rs = ps.executeQuery();
				if (rs != null && rs.next()) {
					TContent contentPos = new TContent();
					String mainTitle = rs.getString("MAIN_TITLE");
					String eventId = rs.getString("EVENT_ID");
					if ("2".equals(eventId)) {
						mainTitle = mainTitle.replace("${targetlevel}", level);
					} else if ("1".equals(eventId)) {
						contentPos.setImg(rs.getString("IMG"));
					} else if ("10".equals(eventId)) {
						PreparedStatement psInner = conn.prepareStatement(SQLConstDef.QUERY_NAME_GENDER_FROM_MEMBER);
						psInner.setLong(1, memberSeqId);
						ResultSet rsInner = psInner.executeQuery();
						if (rsInner != null && rsInner.next()) {
							mainTitle = mainTitle.replace("${name}", rsInner.getString("NAME")==null? "":rsInner.getString("NAME"));
							String gender = rsInner.getString("GENDER");
							if ("F".equals(gender)) {
								mainTitle = mainTitle.replace("${gender}","女士");
							} else if ("M".equals(gender)) {
								mainTitle = mainTitle.replace("${gender}","先生");
							} else {
								mainTitle = mainTitle.replace("${gender}","");
							}
						}
					}
					contentPos.setChnlCode(rs.getString("CHNL_CODE"));
					contentPos.setEventCode(rs.getString("EVENT_CODE"));
					contentPos.setPriority(rs.getString("PRIORITY"));
					contentPos.setPosOutterTitle(mainTitle);
					contentPos.setPosOutterSubtitle(rs.getString("SUB_TITLE"));
					contentPos.setPosOutterSubject(rs.getString("SUBJECT"));
					contentList.add(contentPos);
				} 
//				else {
//					if (hasCard) {//有卡
//						ps = conn.prepareStatement(SQLConstDef.QUERY_EVENT_MEMBER_FILM);
//						ps.setString(1, "POS");
//						ps.setString(2, "POS-OUTTER");
//						ps.setString(3, "1");
//						rs = ps.executeQuery();
//						if(rs != null && rs.next()) {
//							TContent contentPos = new TContent();
//							contentPos.setChnlCode(rs.getString("CHNL_CODE"));
//							contentPos.setEventCode(rs.getString("EVENT_CODE"));
//							contentPos.setPriority(rs.getString("PRIORITY"));
//							contentPos.setPosInnerTitle(rs.getString("MAIN_TITLE"));
//							contentPos.setPosInnerSubtitle(rs.getString("SUB_TITLE"));
//							contentPos.setPosInnerSubject(rs.getString("SUBJECT"));
//							contentList.add(contentPos);
//						}
//					} else {//无卡
//						ps = conn.prepareStatement(SQLConstDef.QUERY_EVENT_MEMBER_FILM);
//						ps.setString(1, "POS");
//						ps.setString(2, "POS-OUTTER");
//						ps.setString(3, "2");
//						rs = ps.executeQuery();
//						if(rs != null && rs.next()) {
//							TContent contentPos = new TContent();
//							contentPos.setChnlCode(rs.getString("CHNL_CODE"));
//							contentPos.setEventCode(rs.getString("EVENT_CODE"));
//							contentPos.setPriority(rs.getString("PRIORITY"));
//							contentPos.setPosInnerTitle(rs.getString("MAIN_TITLE"));
//							contentPos.setPosInnerSubtitle(rs.getString("SUB_TITLE"));
//							contentPos.setPosInnerSubject(rs.getString("SUBJECT"));
//							contentList.add(contentPos);
//						}
//					}
//				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			//POS短信
			try {
				ps = conn.prepareStatement(SQLConstDef.QUERY_EVENT_OFFER_CONTENT);
				ps.setString(1, "POS");
				ps.setString(2, "SMS");
				ps.setString(3, highlevelFilm);
				ps.setString(4, upgradeFilm);				
				ps.setString(5, "POS");
				ps.setString(6, "SMS");
				ps.setString(7, level);
				ps.setString(8, willUpgrade);
				ps.setString(9, "");
				ps.setString(10, "POS");
				ps.setString(11, "POS-INNER");
				ps.setString(12, hasCard);
				ps.setString(13, memberFilm);
				rs = ps.executeQuery();
				if (rs != null && rs.next()) {
					String contentStr = rs.getString("CONTENT");
					String eventId = rs.getString("EVENT_ID");
					if ("2".equals(eventId)) {
						contentStr = contentStr.replace("$", level);
					}
					content.setPosMsgContent(contentStr);
					content.setChnlCode(rs.getString("CHNL_CODE"));
					content.setEventCode(rs.getString("EVENT_CODE"));
					content.setPriority(rs.getString("PRIORITY"));
//					contentList.add(content);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			content.setPosIntfCount(composePosCheckXML(contentList));
		}
		return content;
	}
	
	/**
	 * @param conn
	 * @param triggerSystem
	 * @return 获取注册时的营销话术
	 */
	public TContent getRegEvenContent(Connection conn,String triggerSystem){
		TContent content = new TContent();
		PreparedStatement ps = null;
		ResultSet rs = null;
		if ("WEB".equals(triggerSystem)) {
			//WEB短信
			try {
				ps = conn.prepareStatement(SQLConstDef.QUERY_EVENT_REG_OFFER_CONTENT);
				ps.setString(1, "WEB");
				ps.setString(2, "SMS");
				rs = ps.executeQuery();
				if (rs != null && rs.next()) {
					String contentStr = rs.getString("CONTENT");
					content.setWebMsgContent(contentStr);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			//POS短信
			try {
				ps = conn.prepareStatement(SQLConstDef.QUERY_EVENT_REG_OFFER_CONTENT);
				ps.setString(1, "POS");
				ps.setString(2, "SMS");
				rs = ps.executeQuery();
				if (rs != null && rs.next()) {
					String contentStr = rs.getString("CONTENT");
					content.setPosMsgContent(contentStr);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			//POS外屏
			try {
				ps = conn.prepareStatement(SQLConstDef.QUERY_EVENT_REG_OFFER_CONTENT);
				ps.setString(1, "POS");
				ps.setString(2, "POS-OUTTER");
				rs = ps.executeQuery();
				if (rs != null && rs.next()) {
					content.setPriority(rs.getString("PRIORITY"));
					content.setImg(rs.getString("IMG"));
					content.setPosOutterTitle(rs.getString("MAIN_TITLE"));
					content.setPosOutterSubtitle(rs.getString("SUB_TITLE"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			//POS内屏
			try {
				ps = conn.prepareStatement(SQLConstDef.QUERY_EVENT_REG_OFFER_CONTENT);
				ps.setString(1, "POS");
				ps.setString(2, "POS-INNER");
				rs = ps.executeQuery();
				if (rs != null && rs.next()) {
					content.setPriority(rs.getString("PRIORITY"));
					content.setPosInnerTitle(rs.getString("MAIN_TITLE"));
					content.setPosInnerSubtitle(rs.getString("SUB_TITLE"));
					content.setPosInnerSubject(rs.getString("SUBJECT"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return content;
	}
	
	public TContent getRedeemEvenContent(Connection conn){
		TContent content = new TContent();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(SQLConstDef.QUERY_EVENT_REDEEM_OFFER_CONTENT);
			rs = ps.executeQuery();
			if (rs != null && rs.next()) {
				String contentStr = rs.getString("CONTENT");
				content.setRedeemContent(contentStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}
	
	/**
	 * @param offer
	 * @param pointOffset
	 * @param ticketOffset
	 * @return 网站调用会员识别时，如果会员即将升级，返回的营销话术处理结果
	 */
	public String getOfferContent(String offer,String pointOffset,String ticketOffset){
		//积分差 >0,票差>0: xx分或xx张影票		
		//积分差 >0,票差=0: xx分
		//积分差 =0,票差>0: xx张影票
		int pointOff = Integer.parseInt(pointOffset);
		int ticketOff = Integer.parseInt(ticketOffset);
		String content = "";
		String temp = "";
		if (pointOff > 0 && ticketOff > 0) {
			temp = pointOffset +"分或"+ ticketOffset +"张影票";
		} else if (pointOff > 0 && ticketOff == 0) {
			temp = pointOffset +"分";
		} else {
			temp = ticketOffset +"张影票";
		}
		content = offer.replace("${upinfo}", temp);
		return content;
	}
	
	//POS注册话术
	public String composePosRegXML(TContent content){
		StringBuffer buf = new StringBuffer();
		buf.append("<ROOT>");
			buf.append("<SALESTALK>");
				buf.append("<NAME>").append("POS-REGIST").append("</NAME>");
				buf.append("<CHANEL>").append("POS-OUTTER").append("</CHANEL>");
				buf.append("<PRIORITY>").append(content.getPriority()).append("</PRIORITY>");
				buf.append("<CONTENT>");
					buf.append("<IMGNAME>").append(content.getImg()).append("</IMGNAME>");
					buf.append("<TITLE>").append(content.getPosOutterTitle()).append("</TITLE>");
					buf.append("<SUBTITLE>").append(content.getPosOutterSubtitle()).append("</SUBTITLE>");
					buf.append("<SUBJECT>").append("</SUBJECT>");
				buf.append("</CONTENT>");
			buf.append("</SALESTALK>");
			buf.append("<SALESTALK>");
			buf.append("<NAME>").append("POS-REGIST").append("</NAME>");
			buf.append("<CHANEL>").append("POS-INNER").append("</CHANEL>");
			buf.append("<PRIORITY>").append(content.getPriority()).append("</PRIORITY>");
			buf.append("<CONTENT>");
				buf.append("<TITLE>").append(content.getPosInnerTitle()).append("</TITLE>");
				buf.append("<SUBTITLE>").append(content.getPosInnerSubtitle()).append("</SUBTITLE>");
				buf.append("<SUBJECT>");
				if (content.getPosInnerSubject() != null && content.getPosInnerSubject().contains(";")) {
					String[] msgArray = content.getPosInnerSubject().split(";");
					for (int j = 0; j < msgArray.length; j++) {
						buf.append("<MSG>").append(msgArray[j]).append("</MSG>");
					}
				} else {
					buf.append("<MSG>").append(content.getPosInnerSubject()).append("</MSG>");
				}
				buf.append("</SUBJECT>");
			buf.append("</CONTENT>");
		buf.append("</SALESTALK>");
		buf.append("</ROOT>");
		return buf.toString();
	}
	
	//网站注册话术--暂时不用
	public String composeWebRegXML(TContent content){
		StringBuffer buf = new StringBuffer();
		buf.append("<ROOT>");
			buf.append("<SALESTALK>");
				buf.append("<NAME>").append("WEB-REGIST").append("</NAME>");
				buf.append("<CHANEL>").append("WEB").append("</CHANEL>");
				buf.append("<PRIORITY>").append(content.getPriority()).append("</PRIORITY>");
				buf.append("<CONTENT>").append(content.getWebIntfContent()).append("</CONTENT>");
			buf.append("</SALESTALK>");
		buf.append("</ROOT>");
		return buf.toString();
	}
	//POS会员识别话术
	public String composePosCheckXML(List<TContent> contentList){
		TContent content = new TContent();
		StringBuffer buf = new StringBuffer();
		buf.append("<ROOT>");
		for (int i = 0; i < contentList.size(); i++) {
			content = contentList.get(i);
			buf.append("<SALESTALK>");
			buf.append("<NAME>").append(content.getEventCode().toUpperCase()).append("</NAME>");
			buf.append("<CHANEL>").append(content.getChnlCode()).append("</CHANEL>");
			buf.append("<PRIORITY>").append(content.getPriority()).append("</PRIORITY>");
			if ("POS-INNER".equals(content.getChnlCode())) {
				buf.append("<CONTENT>");
					buf.append("<TITLE>").append(content.getPosInnerTitle()).append("</TITLE>");
					buf.append("<SUBTITLE>").append(content.getPosInnerSubtitle()).append("</SUBTITLE>");
					buf.append("<SUBJECT>");
					if (content.getPosInnerSubject() != null && content.getPosInnerSubject().contains(";")) {
						String[] msgArray = content.getPosInnerSubject().split(";");
						for (int j = 0; j < msgArray.length; j++) {
							buf.append("<MSG>").append(msgArray[j]).append("</MSG>");
						}
					} else {
						buf.append("<MSG>").append(content.getPosInnerSubject()).append("</MSG>");
					}
					buf.append("</SUBJECT>");
				buf.append("</CONTENT>");
			} else {
				buf.append("<CONTENT>");
					buf.append("<IMGNAME>").append(content.getImg()).append("</IMGNAME>");
					buf.append("<TITLE>").append(content.getPosOutterTitle()).append("</TITLE>");
					buf.append("<SUBTITLE>").append(content.getPosOutterSubtitle()).append("</SUBTITLE>");
					buf.append("<SUBJECT>");
					if (content.getPosOutterSubject() != null && content.getPosOutterSubject().contains(";")) {
						String[] msgArray = content.getPosOutterSubject().split(";");
						for (int j = 0; j < msgArray.length; j++) {
							buf.append("<MSG>").append(msgArray[j]).append("</MSG>");
						}
					} else {
						buf.append("<MSG>").append(content.getPosOutterSubject()).append("</MSG>");
					}
					buf.append("</SUBJECT>");
				buf.append("</CONTENT>");
			}
			buf.append("</SALESTALK>");
		}
		buf.append("</ROOT>");
		return buf.toString();
	}
	//WEB会员识别话术
	public String composeWebCheckXML(TContent content){
		StringBuffer buf = new StringBuffer();
		buf.append("<ROOT>");
			buf.append("<SALESTALK>");
				buf.append("<NAME>").append("WILL-UPGRADE").append("</NAME>");
				buf.append("<CHANEL>").append("WEB").append("</CHANEL>");
				buf.append("<PRIORITY>").append(content.getPriority()).append("</PRIORITY>");
				buf.append("<CONTENT>").append(content.getWebIntfContent()).append("</CONTENT>");
			buf.append("</SALESTALK>");
		buf.append("</ROOT>");
		return buf.toString();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String nowTime = df.format(new Date());
		System.out.println(nowTime);
	}

}
