package com.wanda.mrb.intf.member;

import java.sql.Connection;
import java.sql.ResultSet;

import org.w3c.dom.Element;

import com.wanda.mrb.intf.ConstDef;
import com.wanda.mrb.intf.SQLConstDef;
import com.wanda.mrb.intf.ServiceBase;
import com.wanda.mrb.intf.utils.ResultQuery;
import com.wanda.mrb.intf.utils.SqlHelp;

public class PassCheckCode extends ServiceBase{
	private String mobile;
	private String checkCode;
	private String msgOpen;
	private String cardNumber;
	private String cardType;
	public PassCheckCode(){
		super();
		super.intfCode=ConstDef.CONST_INTFCODE_M_PASSCHECKCODE;
	}
	@Override
	protected void bizPerform() throws Exception {
		Connection conn = getDBConnection();
		ResultQuery rsq = null;
		ResultSet rs = null;
		//通过维数据判断发送短信开关是否开放
//		ResultQuery rsq = SqlHelp.query(conn,
//				SQLConstDef.SELECT_MSG_OPEN_FROM_DIM, "开放短信验证");
//		ResultSet rs = rsq.getResultSet();
//		if (rs != null && rs.next()) {
//			msgOpen = rs.getString("name");
//		}
//		rsq.free();
//		
//		if ("开放".equals(msgOpen)) {
			//短信开放，1.可以进行手机和验证码的校验；2.可以进行手机号和卡号的验证
			if (!"".equals(mobile)&&!"".equals(checkCode)) {
				//进行手机和验证码的校验
				rsq=SqlHelp.query(conn, SQLConstDef.SELECT_CHECK_CODE,checkCode,mobile);
				rs=rsq.getResultSet();
				if(rs !=null && rs.next()){
				}else{
					throwsBizException("M140001", "验证码无效");
				}
				rsq.free();
			} else if(!"".equals(mobile)&&!"".equals(cardNumber)&&!"".equals(cardType)){
				//进行手机号和卡号的验证   1.会员卡  2.关联会员卡
				if ("0".equals(cardType)) {
					//会员卡
					rsq=SqlHelp.query(conn, SQLConstDef.SELECT_MEMBER_CARD_BY_MOBILE,mobile);
					rs=rsq.getResultSet();
					if(rs !=null && rs.next()){
						String cardNum = rs.getString("CARD_NUMBER");
						if (!cardNumber.equals(cardNum)) {
							throwsBizException("M140002", "手机号和会员卡卡号不匹配！");
						}
					}else{
						throwsBizException("M140002", "手机号和会员卡卡号不匹配！");
					}
					rsq.free();
				} else if ("1".equals(cardType)) {
					//关联会员卡
					rsq=SqlHelp.query(conn, SQLConstDef.SELECT_REL_MEMBER_CARD_BY_MOBILE,mobile);
					rs=rsq.getResultSet();
					if(rs !=null && rs.next()){
						String cardNum = rs.getString("CARD_NUMBER");
						if (!cardNumber.equals(cardNum)) {
							throwsBizException("M140003", "手机号和关联会员卡卡号不匹配！");
						}
					}else{
						throwsBizException("M140003", "手机号和关联会员卡卡号不匹配！");
					}
					rsq.free();
				} else{
					throwsBizException("M140004", "卡类型参数错误！");
				}
			} else {
				throwsBizException("M140005", "输入参数错误！");
			}
//		} 
//		else {
//			//短信未开放，只能进行通过手机和卡号，卡类型进行验证
//			if(!"".equals(mobile)&&!"".equals(cardNumber)&&!"".equals(cardType)){
//				//进行手机号和卡号的验证   1.会员卡  2.关联会员卡
//				if ("0".equals(cardType)) {
//					//会员卡
//					rsq=SqlHelp.query(conn, SQLConstDef.SELECT_MEMBER_CARD_BY_MOBILE,mobile);
//					rs=rsq.getResultSet();
//					if(rs !=null && rs.next()){
//						String cardNum = rs.getString("CARD_NUMBER");
//						if (!cardNumber.equals(cardNum)) {
//							throwsBizException("M140002", "手机号和会员卡卡号不匹配！");
//						}
//					}else{
//						throwsBizException("M140002", "手机号和会员卡卡号不匹配！");
//					}
//					rsq.free();
//				} else if ("1".equals(cardType)) {
//					//关联会员卡
//					rsq=SqlHelp.query(conn, SQLConstDef.SELECT_REL_MEMBER_CARD_BY_MOBILE,mobile);
//					rs=rsq.getResultSet();
//					if(rs !=null && rs.next()){
//						String cardNum = rs.getString("CARD_NUMBER");
//						if (!cardNumber.equals(cardNum)) {
//							throwsBizException("M140003", "手机号和关联会员卡卡号不匹配！");
//						}
//					}else{
//						throwsBizException("M140003", "手机号和关联会员卡卡号不匹配！");
//					}
//					rsq.free();
//				} else{
//					throwsBizException("M140004", "卡类型参数错误！");
//				}
//			} else {
//				throwsBizException("M140005", "输入参数错误！");
//			}
//		}
	}

	@Override
	protected void parseXMLParam(Element root) throws Exception {
		mobile = getChildValueByName(root,"MOBILE", 12);
		checkCode = getChildValueByName(root,"CHECK_CODE", 12);
		cardNumber = getChildValueByName(root,"CARD_NUMBER", 50);
		cardType = getChildValueByName(root,"CARD_TYPE", 12);//0:会员卡  1:关联会员卡
	}

	@Override
	protected String composeXMLBody() {
		// TODO Auto-generated method stub
		return null;
	}

}
