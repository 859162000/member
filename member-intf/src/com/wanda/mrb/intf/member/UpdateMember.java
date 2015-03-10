package com.wanda.mrb.intf.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.w3c.dom.Element;

import com.wanda.mrb.intf.ConstDef;
import com.wanda.mrb.intf.SQLConstDef;
import com.wanda.mrb.intf.ServiceBase;
import com.wanda.mrb.intf.member.vo.TMember;
import com.wanda.mrb.intf.utils.ResultQuery;
import com.wanda.mrb.intf.utils.SqlHelp;
import com.wanda.mrb.intf.utils.Utils;

public class UpdateMember extends ServiceBase {
	public UpdateMember() {
		super();
		super.intfCode = ConstDef.CONST_INTFCODE_M_UPDATEMEMBER;
		this.timeOutFlag = true;
	}

	String cinemaCode = "";
	long favCinemaCodeId;
	TMember member = new TMember();
	boolean updateMobile;
	boolean updateBirth;
	String mobile = "";
	String birth = "";
	int birthUpdateTime;
	long memberId;

	@Override
	protected void bizPerform() throws Exception {
		Connection conn = getDBConnection();
		PreparedStatement ps = null;
		
		//修改t_member_info之前，判断输入的会员编码和手机号是否对应到同一个会员		
		if(!"".equals(member.favCinema)){
			//根据影城编码查出影城id
			ResultQuery rsq=SqlHelp.query(conn, SQLConstDef.MEMBER_SELECT_CINEMAID_BY_CODE, member.favCinema);
			ResultSet rs=rsq.getResultSet();
			if(rs !=null && rs.next()){
				favCinemaCodeId=rs.getLong("seqid"); // 获取主键
			}else{
				throwsBizException("M020003", "影城编码错误！");
			}
			rsq.free();
		}
		//记录修改日志
		//查出会员的生日，和输入的生日做对比，如果不同的话，为修改生日，考虑为空的情况
		//根据会员的会员编号查手机号，如果手机号不同，则修改手机号
		ResultQuery rsq=SqlHelp.query(conn, SQLConstDef.SELECT_MOBILE_BIRTH_MEMBER, member.memberNo);
		ResultSet rs=rsq.getResultSet();
		while (rs.next()) {
			mobile = rs.getString("mobile");
			birth = rs.getString("birthday");
			memberId = rs.getLong("member_id");
		}
		rsq.free();//释放对象
		if(!"".equals(member.birthday)&&!member.birthday.equals(birth)){
			updateBirth = true;
		}
		if (!member.mobile.equals(mobile)) {
			updateMobile = true;
		}
		if (updateBirth) {
			rsq=SqlHelp.query(conn, SQLConstDef.SELECT_BIRTH_UPDATE_TIME, member.memberNo);
			rs=rsq.getResultSet();
			while (rs.next()) {
				birthUpdateTime = rs.getInt("birthday_update_time");
			}
			rsq.free();//释放对象
			if (birthUpdateTime >= 1) {
				throwsBizException("M020005", "生日最多修改1次！");
			} else {//给生日修改次数计数器+1
				SqlHelp.operate(conn, SQLConstDef.ADD_BIRTH_UPDATE_TIME,member.memberNo);
			}
		}
		if (updateBirth || updateMobile) {
			SqlHelp.operate(conn, SQLConstDef.INSERT_MEMBER_LOG,
					""+memberId,member.mobile,member.operator,member.operator,member.operator,member.birthday);
		}
		//由于网站提出新需求，可以修改手机号，所以注释掉下面这段代码
		rsq=SqlHelp.query(conn, SQLConstDef.MEMBER_CHECK_MEMBER_NO_BY_MOBILE, member.mobile);
		rs=rsq.getResultSet();
		if(rs !=null && rs.next()){
			if(!member.memberNo.equals(rs.getString("MEMBER_NO"))){
				throwsBizException("M120001", "手机号和会员编码不匹配！");
			} else {
				//如果喜欢的影城不为空
				if (!"".equals(member.favCinema)) {
					try {
						ps = conn.prepareStatement(SQLConstDef.MEMBER_REGISTER_UPDATE);
						ps.setString(1, member.name);//
						ps.setString(2, "".equals(member.gender)?"O":member.gender);
						ps.setDate(3, "".equals(member.birthday)?null:java.sql.Date.valueOf(member.birthday));//
						ps.setLong(4, 1);// 会员状态，注册是设置成有效 1有效 0无效
						ps.setString(5, member.operator);
						ps.setString(6, member.email);
						ps.setString(7, member.tel);
						ps.setLong(8, favCinemaCodeId);
						ps.setString(9, member.sourceType);
						ps.setString(10, member.mobile);
						ps.setString(11, member.arrivalType);
						ps.setString(12, member.oftenChannel);
						ps.setString(13, member.memberNo);
						ps.execute();
					} catch (Exception e) {
						conn.rollback();
						throw e;
					}
				} else {
					try {
						ps = conn.prepareStatement(SQLConstDef.MEMBER_REGISTER_UPDATE_NOFAVCINEMA);
						ps.setString(1, member.name);//
						ps.setString(2, "".equals(member.gender)?"O":member.gender);
						ps.setDate(3, "".equals(member.birthday)?null:java.sql.Date.valueOf(member.birthday));//
						ps.setLong(4, 1);// 会员状态，注册是设置成有效 1有效 0无效
						ps.setString(5, member.operator);
						ps.setString(6, member.email);
						ps.setString(7, member.tel);
						ps.setString(8, member.sourceType);
						ps.setString(9, member.mobile);
						ps.setString(10, member.arrivalType);
						ps.setString(11, member.oftenChannel);
						ps.setString(12, member.memberNo);
					
						ps.execute();
					} catch (Exception e) {
						conn.rollback();
						throw e;
					}
				}
			}
		}else{
			//如果喜欢的影城不为空
			if (!"".equals(member.favCinema)) {
				try {
					ps = conn.prepareStatement(SQLConstDef.MEMBER_REGISTER_UPDATE);
					ps.setString(1, member.name);//
					ps.setString(2, "".equals(member.gender)?"O":member.gender);
					ps.setDate(3, "".equals(member.birthday)?null:java.sql.Date.valueOf(member.birthday));//
					ps.setLong(4, 1);// 会员状态，注册是设置成有效 1有效 0无效
					ps.setString(5, member.operator);
					ps.setString(6, member.email);
					ps.setString(7, member.tel);
					ps.setLong(8, favCinemaCodeId);
					ps.setString(9, member.sourceType);
					ps.setString(10, member.mobile);
					ps.setString(11, member.arrivalType);
					ps.setString(12, member.oftenChannel);
					ps.setString(13, member.memberNo);
					ps.execute();
				} catch (Exception e) {
					conn.rollback();
					throw e;
				}
			} else {
				try {
					ps = conn.prepareStatement(SQLConstDef.MEMBER_REGISTER_UPDATE_NOFAVCINEMA);
					ps.setString(1, member.name);//
					ps.setString(2, "".equals(member.gender)?"O":member.gender);
					ps.setDate(3, "".equals(member.birthday)?null:java.sql.Date.valueOf(member.birthday));//
					ps.setLong(4, 1);// 会员状态，注册是设置成有效 1有效 0无效
					ps.setString(5, member.operator);
					ps.setString(6, member.email);
					ps.setString(7, member.tel);
					ps.setString(8, member.sourceType);
					ps.setString(9, member.mobile);
					ps.setString(10, member.arrivalType);
					ps.setString(11, member.oftenChannel);
					ps.setString(12, member.memberNo);
					ps.execute();
				} catch (Exception e) {
					conn.rollback();
					throw e;
				}
			}
		}
		rsq.free();
		
		//修改t_member_info
		SqlHelp.operate(conn, SQLConstDef.MEMBER_INFO_UPDATE, ""+member.education,""+member.occupation,""+member.income,"".equals(member.maritalStatus)?"O":member.maritalStatus,
				""+member.childNumber,""+member.fqCinemaDist,""+member.fqCinemaTime,"".equals(member.mobileOptin)?"O":member.mobileOptin,
				""+member.identityType,member.idCardNo,member.idCardHashNo,
				member.operator,member.otherNo,member.memberNo);
		//修改t_member_addr
		SqlHelp.operate(conn, SQLConstDef.MEMBER_ADDR_UPDATE,member.zipCode,member.address1,
				"1",(member.provinceID==null)?"":""+member.provinceID,(member.cityID==null)? "":""+member.cityID,
				member.operator,member.memberNo);
		//修改T_MEM_FAV_FILMTYPE
		if (!"".equals(member.favFilmType)) {
			SqlHelp.operate(conn, SQLConstDef.MEMBER_FAV_FILMTYPE_UPDATE,member.favFilmType,
					member.operator,member.memberNo);
		}
		//修改T_MEM_FAV_CONTACT
		if(!"".equals(member.contactMeans))
		{
			SqlHelp.operate(conn, SQLConstDef.MEMBER_FAV_CONTACT_UPDATE,member.contactMeans,member.operator,member.memberNo);
		}
	}

	@Override
	protected void parseXMLParam(Element root) throws Exception {
		try {
			try {
				//常用购票渠道
				member.oftenChannel = getChildValueByName(root,"OFTEN_CHANNEL", 15);
				// 到达影城方式 交通方式
				member.arrivalType = getChildValueByName(root,"ARRIVAL_TYPE", 2);
			} catch (Exception e) {
				System.out.println("OFTEN_CHANNEL,ARRIVAL_TYPE is not writed");
			}
//		 
			// 会员编码
			member.memberNo = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_MEMBERNO, 64);
			member.name = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_NAME, 50);
			member.mobile = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_MOBILE, 11);
			member.tel = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_TEL, 30);
			member.idCardNo = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_IDCARD_NO, 18);
			member.idCardHashNo = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_IDCARD_HASHNO, 32);
			String identityTypeStr = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_IDENTITY_TYPE, 8);
			if (!"".equals(identityTypeStr)) {
				member.identityType = Integer.parseInt(identityTypeStr);
			}
			member.email = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_EMAIL, 50);
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
			if(!"".equals(member.birthday)){
				if(!Utils.validDate(member.birthday)){
					throwsBizException("M020004", "生日格式应为：YYYY-MM-DD！");
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
			member.operator = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_OPERATOR, 50);
			member.operatorName = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_OPERATORNAME, 50);
			member.otherNo = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_OTHERNO, 50);
			member.favCinema = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_FAV_CINEMA, 50);
			// 招募渠道
			member.registChnID = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_SOURCEFOR, 11);
			// 招募渠道扩展
			member.registChnExtID = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_SOURCEFOR_EXT, 11);
			// 会员来源
			member.sourceType = getChildValueByName(root,
					ConstDef.CONST_INTFCODE_M_REGISTER_DTSID, 11);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	protected String composeXMLBody() {
		return "0";
	}

}