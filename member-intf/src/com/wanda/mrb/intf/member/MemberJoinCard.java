package com.wanda.mrb.intf.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.w3c.dom.Element;

import com.wanda.mms.control.stream.service.IntegralInitialization;
import com.wanda.mrb.intf.ConstDef;
import com.wanda.mrb.intf.SQLConstDef;
import com.wanda.mrb.intf.ServiceBase;
import com.wanda.mrb.intf.exception.BusinessException;
import com.wanda.mrb.intf.member.vo.TMember;
import com.wanda.mrb.intf.utils.FormatTools;
import com.wanda.mrb.intf.utils.MemberUtils;
import com.wanda.mrb.intf.utils.ResultQuery;
import com.wanda.mrb.intf.utils.SqlHelp;
import com.wanda.mrb.intf.utils.Utils;

public class MemberJoinCard extends ServiceBase {
	public MemberJoinCard(){
		super();
		super.intfCode = ConstDef.CONST_INTFCODE_M_MEMBERJOINCARD;
	}
	String regist = "";
	TMember member = new TMember();
	String favCinemaCode = "";//常驻影城
	long memberSeqId = 0;
	MemberUtils memberUtil = new MemberUtils();

	@Override
	protected void bizPerform() throws Exception {
		//卡绑定处理，卡帮到会员身上。0.不需要注册，直接绑定    1.需要注册，先注册后绑定
		Connection conn = getDBConnection();
		PreparedStatement ps = null;
		if ("1".equals(regist)) {
			//先使用新手机号注册
			try {
				// 验证手机号
				checkMobileNO(conn, member.newMobile);
				checkEmail(conn, member.email);
				
				//当注册影城为空，常驻影城不为空时，是来自网站的注册
				ps = conn.prepareStatement(SQLConstDef.MEMBER_REGISTER_MEMBERID);
				ResultSet rs = ps.executeQuery();
				if (rs != null && rs.next()) {
					memberSeqId = rs.getLong("NEXTVAL"); // 获取主键
				}
				//根据影城编码获得到影城id
				ps = conn.prepareStatement(SQLConstDef.MEMBER_SELECT_CINEMAID_BY_CODE);
				if (!"".equals(cinemaCode)) {
					// 获取会员编码
					member.memberNo = memberUtil.getMemberNO(conn, cinemaCode);
					ps.setString(1, cinemaCode);
				} else if (!"".equals(favCinemaCode)) {
					favCinemaCode = memberUtil.randomCinema(conn,favCinemaCode);
					// 获取会员编码
					member.memberNo = memberUtil.getMemberNO(conn, favCinemaCode);
					ps.setString(1, favCinemaCode);
				} else {
					throw new BusinessException("M010004", "网站常驻影城不能为空，POS注册影城不能为空！");
				}
				rs = ps.executeQuery();
				if (rs != null && rs.next()) {
					member.registCinemaId = rs.getLong("seqid"); // 获取主键
				}
				
				ps = conn.prepareStatement(SQLConstDef.MEMBER_REGISTER);
				ps.setLong(1, memberSeqId);
				ps.setString(2, member.memberNo);// 会员编码产生的规则
				ps.setString(3, member.newMobile);//
				ps.setString(4, member.name);//
				ps.setString(5, "".equals(member.gender)?"O":member.gender);//
				ps.setDate(6, "".equals(member.birthday)?null:java.sql.Date.valueOf(member.birthday));//
				ps.setLong(7, 1);// 注册时默认为新会员; 注册类型，需要接口中定义该字段 1:主动注册;2:自动转换;3:批量导入
				ps.setString(8, member.registChnID);//招募渠道
				ps.setString(9, member.operator);//
				ps.setString(10, member.operatorName);// 注册员工姓名
				ps.setLong(11, member.registCinemaId);// 
				ps.setLong(12, 1);// 会员状态，注册是设置成有效 1有效 0无效
				ps.setInt(13, 0);// 逻辑删除标识,默认:0 未删除;1删除;其他:非法
				ps.setString(14, member.operator);
				ps.setString(15, member.operator);
				ps.setString(16, member.email);
//				ps.setLong(17, member.iscontactable);
				ps.setString(17, member.tel);
				ps.setLong(18, member.registCinemaId);
				ps.setString(19, member.registDate);
				ps.setString(20, member.dtsId);//会员来源
				ps.execute();
			} catch (Exception e) {
				conn.rollback();
				throw e;
			}

			// 常用信息
			try {
				ps = conn.prepareStatement(SQLConstDef.MEMBER_REGISTER_INFO);
				ps.setLong(1, member.registCinemaId);
				ps.setInt(2, member.education);
				ps.setInt(3, member.occupation);
				ps.setInt(4, member.income);
				ps.setString(5, "".equals(member.maritalStatus)?"O":member.maritalStatus);
				ps.setInt(6, member.childNumber);
				ps.setLong(7, member.fqCinemaDist);
				ps.setLong(8, member.fqCinemaTime);
				ps.setString(9, "".equals(member.mobileOptin)?"O":member.mobileOptin);
				ps.setInt(10, member.identityType);
				ps.setString(11, member.idCardNo);
//				ps.setString(12, member.idCardHashNo);
				ps.setInt(12, 0);// 逻辑删除标识,默认:0 未删除;1删除;其他:非法
				ps.setString(13, member.operator);
				ps.setString(14, member.operator);
				ps.setLong(15, memberSeqId);
				ps.setString(16, member.otherNo);
				ps.execute();
			} catch (Exception e) {
				conn.rollback();
				throw e;
			}
			// 地址信息
			try {
				//先通过注册影城获取到省，市
				ps = conn.prepareStatement(SQLConstDef.MEMBER_REGISTER_PROVINCE_CITY);
				ps.setLong(1, member.registCinemaId);
				ResultSet rs = ps.executeQuery();
				while(rs!=null && rs.next()){
					member.provinceID = rs.getLong("PROVINCE");
					member.cityID = rs.getLong("CITY");
				}
				ps = conn.prepareStatement(SQLConstDef.MEMBER_REGISTER_ADDR);
				ps.setString(1, member.zipCode);
				ps.setString(2, member.address1);
				ps.setLong(3, member.provinceID);
				ps.setLong(4, member.cityID);
				ps.setInt(5, 0);// 逻辑删除标识,默认:0 未删除;1删除;其他:非法
				ps.setString(6, member.operator);
				ps.setString(7, member.operator);
				ps.setLong(8, memberSeqId);
				ps.execute();
			} catch (Exception e) {
				conn.rollback();
				throw e;
			}
			// 喜欢看的电影
			try {
				ps = conn
						.prepareStatement(SQLConstDef.MEMBER_REGISTER_FAV_FILMTYPE);
				if("".equals(member.favFilmType) || member.favFilmType == null){
					ps.setString(1, "1");
				}else {
					ps.setString(1, member.favFilmType);
				}
				ps.setInt(2, 0);// 逻辑删除标识,默认:0 未删除;1删除;其他:非法
				ps.setString(3, member.operator);
				ps.setString(4, member.operator);
				ps.setLong(5, memberSeqId);
				ps.execute();
			} catch (Exception e) {
				conn.rollback();
				throw e;
			}
			// 喜欢的联系方式
			try {
				ps = conn.prepareStatement(SQLConstDef.MEMBER_REGISTER_FAV_CONTACT);
				if("".equals(member.contactMeans) || member.contactMeans == null){
					ps.setString(1, "4");
				}else {
					ps.setString(1, member.contactMeans);
				}
				ps.setInt(2, 0);// 逻辑删除标识,默认:0 未删除;1删除;其他:非法
				ps.setString(3, member.operator);
				ps.setString(4, member.operator);
				ps.setLong(5, memberSeqId);
				ps.execute();
			} catch (Exception e) {
				conn.rollback();
				throw e;
			}
			// 处理积分
			// 处理会员级别
			IntegralInitialization initPoint = new IntegralInitialization();
			initPoint.addMemberPointByID(conn, memberSeqId, member.registOpNo, 0);
		}
		//上面的注册完成之后，进行绑定：1：只有新手机号，直接绑定； 2：新手机号和旧手机号都有，解绑再绑定
		if ("".equals(member.oldMobile)) {//旧手机号为空，直接绑定新手机号对应的会员
			//根据手机号查询到member_id
			ResultQuery rsq=SqlHelp.query(conn, SQLConstDef.MEMBER_CHECK_BY_MOBILE, member.newMobile);
			ResultSet rs=rsq.getResultSet();
			if(rs != null && rs.next()){
				member.memberID = rs.getLong("MEMBER_ID"); 
			}else{
				throwsBizException("M150002", "新手机号还未注册成为会员");
			}
			rsq.free();
			//保存卡信息
			//保存卡信息之前先判断卡信息中是否已经保存过该卡信息
			rsq=SqlHelp.query(conn, SQLConstDef.SELECT_CCS_CARD_INFO, member.cardNumber);
			rs=rsq.getResultSet();
			if(rs == null || !rs.next()){
				//8个参数：CARD_NUMBER,CARD_TYPE_CODE,CARD_TYPE_NAME,ISSUE_CINEMA,CARD_STATUS,EXPIRY_DATE,BALANCE,CARD_VALUE_TYPE
				SqlHelp.operate(conn, SQLConstDef.INSERT_CCS_CARD,
						member.cardNumber,
						member.cardTypeCode,
						member.cardTypeName,
						member.ccsCardCinema,
						member.ccsCardStatus,
						member.ccsCardExpireDate,
						member.ccsCardBalance,
						member.ccsCardValueType
						); 
			}
			rsq.free();
			//绑定关系
			rsq=SqlHelp.query(conn, SQLConstDef.SELECT_CCS_CARD_INFO_ID, member.cardNumber);
			rs=rsq.getResultSet();
			if(rs != null && rs.next()){
				member.ccsCardInfoId = rs.getLong("CARD_ID");
			}
			rsq.free();
			//4个参数：MEMBER_ID,CREATE_BY,UPDATE_BY,CARD_NUMBER
			SqlHelp.operate(conn, SQLConstDef.INSERT_CCS_CARD_REL,
					""+member.memberID,
					member.operator,
					member.operator,
					""+member.ccsCardInfoId
					);
		} else {//旧手机号和新手机号都不为空
			//首先判断旧手机号是不是会员，如果旧手机号也是会员，则进行解绑
			ResultQuery rsq=SqlHelp.query(conn, SQLConstDef.MEMBER_CHECK_BY_MOBILE, member.oldMobile);
			ResultSet rs=rsq.getResultSet();
			if(rs != null && rs.next()){
				member.memberID = rs.getLong("MEMBER_ID");
				//进行解绑   2个参数
				SqlHelp.operate(conn, SQLConstDef.DEL_CCS_CARD_OLD_REL,
						""+member.memberID,
						member.cardNumber
						);
			}
			rsq.free();
			//对新手机号注册的会员进行绑定
			//保存卡信息之前先判断卡信息中是否已经保存过该卡信息
			rsq=SqlHelp.query(conn, SQLConstDef.SELECT_CCS_CARD_INFO, member.cardNumber);
			rs=rsq.getResultSet();
			if(rs == null || !rs.next()){
				//7个参数：CARD_NUMBER,CARD_TYPE_CODE,CARD_TYPE_NAME,ISSUE_CINEMA,CARD_STATUS,EXPIRY_DATE,BALANCE
				SqlHelp.operate(conn, SQLConstDef.INSERT_CCS_CARD,
						member.cardNumber,
						member.cardTypeCode,
						member.cardTypeName,
						member.ccsCardCinema,
						member.ccsCardStatus,
						member.ccsCardExpireDate,
						member.ccsCardBalance,
						member.ccsCardValueType
						); 
			}
			rsq.free();
			//绑定关系
			rsq=SqlHelp.query(conn, SQLConstDef.SELECT_CCS_CARD_INFO_ID, member.cardNumber);
			rs=rsq.getResultSet();
			if(rs != null && rs.next()){
				member.ccsCardInfoId = rs.getLong("CARD_ID");
			}
			rsq.free();
			
			//根据手机号查询到member_id
			rsq=SqlHelp.query(conn, SQLConstDef.MEMBER_CHECK_BY_MOBILE, member.newMobile);
			rs=rsq.getResultSet();
			if(rs != null && rs.next()){
				member.memberID = rs.getLong("MEMBER_ID"); 
			}else{
				throwsBizException("M150002", "新手机号还未注册成为会员");
			}
			rsq.free();
			//4个参数：MEMBER_ID,CREATE_BY,UPDATE_BY,CARD_NUMBER
			SqlHelp.operate(conn, SQLConstDef.INSERT_CCS_CARD_REL,
					""+member.memberID,
					member.operator,
					member.operator,
					""+member.ccsCardInfoId
					);
		}
	}

	@Override
	protected void parseXMLParam(Element root) throws Exception {
		regist = getChildValueByName(root,"REGIST", 1);//0：不需要注册	1：需要注册
		member.newMobile = getChildValueByName(root,"NEW_MOBILE", 11);
		if ("".equals(member.newMobile)) {
			throwsBizException("M150001", "新手机号字段不能为空");
		}
		member.oldMobile = getChildValueByName(root,"OLD_MOBILE", 11);
		member.cardNumber =  getChildValueByName(root,"CARD_NUMBER", 50);
		member.ccsCardValueType = getChildValueByName(root,"CARD_VALUE_TYPE", 10);
		member.cardTypeCode = getChildValueByName(root,"CARD_TYPE_CODE", 50);
		member.cardTypeName = getChildValueByName(root,"CARD_TYPE_NAME", 1024);
		member.ccsCardCinema = getChildValueByName(root,"ISSUE_CINEMA", 20);
		member.ccsCardStatus = getChildValueByName(root,"CARD_STATUS", 1);
		member.ccsCardExpireDate = getChildValueByName(root,"EXPIRY_DATE", 20);
		member.ccsCardBalance = getChildValueByName(root,"BALANCE", 20);
		member.operator = getChildValueByName(root,"OPERATOR", 20);
		if ("1".equals(regist)) {
			try {
				// 姓名
				member.name = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_NAME, 50);
				// 手机号
				member.newMobile = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_MOBILE, 11);
				//固定电话
				member.tel = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_TEL, 30);
				//证件号码，掩码
				member.idCardNo = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_IDCARD_NO, 18);
				//证件号码，MD5码
//				member.idCardHashNo = getChildValueByName(root,
//						ConstDef.CONST_INTFCODE_M_REGISTER_IDCARD_HASHNO, 32);
				//证件类型
				String identityTypeStr = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_IDENTITY_TYPE, 8);
				if (!"".equals(identityTypeStr)) {
					member.identityType = Integer.parseInt(identityTypeStr);
				}
				//邮件地址
				member.email = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_EMAIL, 50);
				//地址
				member.address1 = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_ADDRESS, 150);

				String provinceIDStr = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_PROVINCEID, 30);
				if (!"".equals(provinceIDStr)) {
					member.provinceID = Long.parseLong(provinceIDStr);
				}
				String cityIDStr = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_CITYID, 30);
				if (!"".equals(cityIDStr)) {
					member.cityID = Long.parseLong(cityIDStr);
				}
				member.zipCode = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_ZIP, 10);
				member.contactMeans = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_CONTACTMEANS, 30);
				member.birthday = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_BIRTHDAY, 50);
				if (!"".equals(member.birthday)) {
					if(!Utils.validDate(member.birthday)){
						throwsBizException("M010005", "生日格式应为：YYYY-MM-DD！");
					}
				}
				member.gender = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_GENDER, 8);
				member.maritalStatus = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_MARITALSTATUS, 8);

				String childNumberStr = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_CHILDNUMBER, 8);
				if (!"".equals(childNumberStr)) {
					member.childNumber = Integer.parseInt(childNumberStr);
				}
				String educationStr = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_EDUCATION, 8);
				if (!"".equals(educationStr)) {
					member.education = Integer.parseInt(educationStr);
				}
				String occupationStr = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_OCCUPATION, 8);
				if (!"".equals(occupationStr)) {
					member.occupation = Integer.parseInt(occupationStr);
				}
				String incomeStr = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_INCOME, 20);
				if (!"".equals(incomeStr)) {
					member.income = Integer.parseInt(incomeStr);
				}
				member.favFilmType = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_FILMTYPES, 30);
				String fqCinemaDistStr = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_FQCINEMADIST, 8);
				if (!"".equals(fqCinemaDistStr)) {
					member.fqCinemaDist = Integer.parseInt(fqCinemaDistStr);
				}
				String fqCinemaTimeStr = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_FQCINEMATIME, 8);
				if (!"".equals(fqCinemaTimeStr)) {
					member.fqCinemaTime = Integer.parseInt(fqCinemaTimeStr);
				}
				member.mobileOptin = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_MOBILEOPTION, 50);
				cinemaCode = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_COUNTER, 10);// 注册影影城
				member.operatorName = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_OPERATORNAME, 50);
				// 招募渠道
				member.registChnID = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_SOURCEFOR, 11);
				// 会员来源
				member.dtsId = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_DTSID, 11);
				String istalkStr = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_ISTALK, 8);
				if (!"".equals(istalkStr)) {
					member.istalk = Integer.parseInt(istalkStr);
				}
				member.otherNo = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_OTHERNO, 50);
				favCinemaCode = getChildValueByName(root,
						ConstDef.CONST_INTFCODE_M_REGISTER_FAV_CINEMA, 10);
				String registDate = getChildValueByName(root,ConstDef.CONST_INTFCODE_M_REGISTER_DATE, 20);
				member.registDate = "".equals(registDate)?FormatTools.getNowTime():registDate;
			} catch (Exception e) {
				throw e;
			}
		}
	}

	@Override
	protected String composeXMLBody() {
		// TODO Auto-generated method stub
		return null;
	}

}
