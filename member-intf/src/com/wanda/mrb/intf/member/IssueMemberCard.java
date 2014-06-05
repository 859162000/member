/**
 * 
 */
package com.wanda.mrb.intf.member;

import java.sql.Connection;
import java.sql.ResultSet;

import org.w3c.dom.Element;

import com.wanda.mrb.intf.ConstDef;
import com.wanda.mrb.intf.SQLConstDef;
import com.wanda.mrb.intf.ServiceBase;
import com.wanda.mrb.intf.member.vo.TMember;
import com.wanda.mrb.intf.utils.ResultQuery;
import com.wanda.mrb.intf.utils.SqlHelp;

/**
 * @author xuesi
 * 
 */
public class IssueMemberCard extends ServiceBase {
	public IssueMemberCard() {
		super();
		super.intfCode = ConstDef.CONST_INTFCODE_M_ISSUEMEMBERCARD;
	}

	TMember member = new TMember();
	String serviceType = "";// 服务类型 1.发卡 2.补卡3.卡升级
	String serviceDesc = "";// 补卡原因描述
	private int memberId;
	private long cardId;
	private long cinemaId;
	private long oldCardId;

	@Override
	protected void bizPerform() throws Exception {
		Connection conn = getDBConnection();
		memberId = this.checkMember(conn,member.memberNo);

		// 判断该卡是否已经被发放。状态为已预制的卡可以发卡，其余状态的都不可以发卡
		ResultQuery rsq = SqlHelp.query(conn,
				SQLConstDef.QUERY_CARD_STATUS_BY_CARD, member.cardNumber);
		ResultSet rs = rsq.getResultSet();
		if (rs != null && rs.next()) {
			String cardStatus = rs.getString("STATUS");
			if (!"I".equals(cardStatus)) {
				throwsBizException("M120001", "卡状态不符合发放条件！");
			}
		} else {
			throwsBizException("M120005", "卡不存在！");
		}
		rsq.free();

		// 判断该会员卡是否已经发放给了其他会员
		rsq = SqlHelp.query(conn, SQLConstDef.QUERY_CARD_MEMBERID_BY_CARDNUMBER, member.cardNumber);
		rs = rsq.getResultSet();
		if (rs != null && rs.next()) {
			throwsBizException("M120002", "该会员卡已经发放给其他会员！");
		}
		rsq.free();
		
		//根据卡号查出卡的MACK_DADDY_CARD_ID
		rsq = SqlHelp.query(conn, SQLConstDef.ISSUE_CARD_ID, member.cardNumber);
		rs = rsq.getResultSet();
		if (rs != null && rs.next()) {
			cardId = rs.getLong("MACK_DADDY_CARD_ID");
		}
		rsq.free();
		
		//根据影城编码查到影城ID
		rsq = SqlHelp.query(conn, SQLConstDef.MEMBER_SELECT_CINEMAID_BY_CODE, member.registCinemaCode);
		rs = rsq.getResultSet();
		if (rs != null && rs.next()) {
			cinemaId = rs.getLong("seqid");
		}
		rsq.free();
		
		if("1".equals(serviceType)){//发卡
			// 判断每个会员只能有一个万人迷卡
			rsq = SqlHelp.query(conn, SQLConstDef.QUERY_CARD_NUM_BY_MEMBERID, ""
					+ memberId);
			rs = rsq.getResultSet();
			if (rs != null && rs.next()) {
				int cardNum = rs.getInt("NUM");
				if (cardNum > 0) {
					throwsBizException("M120003", "每个会员只能绑定一个万人迷卡！");
				}
			}
			rsq.free();
			
			//插入卡记录
			SqlHelp.operate(conn,SQLConstDef.ISSUE_CARD,String.valueOf(memberId),member.cardNumber);
			//插入服务记录
			SqlHelp.operate(conn,SQLConstDef.ISSUE_CARD_SVC,
					String.valueOf(cardId),
					String.valueOf(cinemaId),
					String.valueOf(member.operator),
					String.valueOf(member.operatorName),
					"发放",
					serviceDesc,
					String.valueOf(memberId));
		}else if("2".equals(serviceType)||"3".equals(serviceType)){//补卡，卡升级
			//查出老卡号			
			rsq = SqlHelp.query(conn, SQLConstDef.ISSUE_OLD_CARD_ID, ""
					+ memberId);
			rs = rsq.getResultSet();
			if (rs != null && rs.next()) {
				oldCardId = rs.getLong("MACK_DADDY_CARD_ID");
			}
			rsq.free();
			//清掉老卡
			SqlHelp.operate(conn,SQLConstDef.ISSUE_CARD_DEL_OLD,String.valueOf(oldCardId));
			//插入新卡
			SqlHelp.operate(conn,SQLConstDef.ISSUE_CARD,String.valueOf(memberId),member.cardNumber);
			//记录卡服务
			SqlHelp.operate(conn,SQLConstDef.ISSUE_CARD_MAKE_UP,
					String.valueOf(oldCardId),
					String.valueOf(cinemaId),
					String.valueOf(member.operator),
					String.valueOf(member.operatorName),
					"补卡",
					serviceDesc,
					String.valueOf(cardId),
					String.valueOf(memberId));
		}else{
			throwsBizException("M120004", "发卡类型错误！");
		}
	}

	@Override
	protected void parseXMLParam(Element root) throws Exception {
		member.memberNo = getChildValueByName(root, "MEMBER_NO", 64);
		member.cardNumber = getChildValueByName(root, "CARD_NO", 64);
		member.registCinemaCode = getChildValueByName(root, "CINEMA_CODE", 8);
		member.operator = getChildValueByName(root, "OPERATOR", 50);
		member.operatorName = getChildValueByName(root, "OPERATOR_NAME", 50);
		serviceType = getChildValueByName(root, "TYPE", 1);
		serviceDesc = getChildValueByName(root, "DESC", 500);
	}

	@Override
	protected String composeXMLBody() {
		return null;
	}

}
