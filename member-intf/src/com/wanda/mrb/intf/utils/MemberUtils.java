/**
 * 
 */
package com.wanda.mrb.intf.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wanda.mrb.intf.ConstDef;
import com.wanda.mrb.intf.SQLConstDef;
import com.wanda.mrb.intf.exception.BusinessException;

/**
 * @author xuesi
 * 
 */
public class MemberUtils {

	public void checkEmail(Connection conn, String email)
			throws BusinessException {
		// 校验邮箱地址是否合法
		String regExp = "(^[\\S]{0,0}$)|(^[\\S]*@[\\S]*.[\\S]*$)";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(email);
		if (!m.find()) {
			throw new BusinessException(
					ConstDef.CONST_RESPCODE_EMAIL_NOT_RIGHT, "手机号不正确");
		}
	}

	public void checkMobile(Connection conn, String mobile)
			throws BusinessException, SQLException {
		// 校验手机号是否已经被注册
		PreparedStatement ps = null;
		ResultSet rs = null;
		ps = conn.prepareStatement(SQLConstDef.MEMBER_CHECK_MOBILE_NO);
		ps.setString(1, mobile);
		rs = ps.executeQuery();
		if (rs == null || !rs.next()) {
			throw new BusinessException(
					ConstDef.CONST_RESPCODE_EMAIL_NOT_RIGHT, "手机号不存在！");
		}
	}
	/**
	 * @param conn
	 * @param cinemaCode
	 *            按照会员编码生成规则生成会员编码 1位 ：C 2-4：注册影城内码 5-20：连续数字，0补空位
	 * @return
	 * @throws SQLException
	 */
	public String getMemberNO(Connection conn, String cinemaCode)
			throws SQLException {
		// 获取影城seqid，获取影城内码
		String memberNO = "";
		String maxNum = "";
		String cinemaId = "";
		String cinemaInnerCode = "";
		Integer num = 0;
		// 根据影城编码获取影城seqid和影城内码
		ResultQuery rsq = SqlHelp.query(conn,
				SQLConstDef.QUERY_CINEMA_SEQID_INNER_CODE, cinemaCode);
		ResultSet rs = rsq.getResultSet();
		while (rs != null && rs.next()) {
			cinemaId = rs.getString("seqid");
			cinemaInnerCode = rs.getString("inner_code");
		}
		rsq.free();// 释放对象
		// 获取到当前最大的会员编号
		rsq = SqlHelp.query(conn, SQLConstDef.QUERY_MAX_MEMBER_NO);
		rs = rsq.getResultSet();
		while (rs.next()) {
			maxNum = rs.getString("MEMBERNUM");
		}
		rsq.free();// 释放对象
//		if (maxNum != null && !"null".equals(maxNum)) {
//			num = Integer.parseInt(maxNum) + 1;
//
//		} else {
//			num = 1;
//		}
		// 拼编码
		memberNO = "C" + cinemaInnerCode + String.format("%016d", Integer.parseInt(maxNum));
		return memberNO;
	}

	/**
	 * @param conn
	 * @param cinemaCode
	 *            为会员在某一城市中随机分配一个影城作为注册影城
	 * @return
	 * @throws SQLException
	 */
	public String randomCinema(Connection conn, String cinemaCode)
			throws SQLException {
		int cinemaNum = 0;
		String randomCinema = "";
		
		//取到该城市的影城总数
		ResultQuery rsq = SqlHelp.query(conn,
				SQLConstDef.QUERY_CINEMA_NUM_FROM_CITY, cinemaCode);
		ResultSet rs = rsq.getResultSet();
		while (rs != null && rs.next()) {
			cinemaNum = rs.getInt("cinemanum");
		}
		rsq.free();// 释放对象

		//从该城市的全部影城中随机取出一家影城
		rsq = SqlHelp.query(conn, SQLConstDef.QUERY_CINEMA_FROM_CITY,
				cinemaCode);
		rs = rsq.getResultSet();
		Random rand = new Random();
		int randomCinemaNo = rand.nextInt(cinemaNum) + 1;
		while (randomCinemaNo > 0) {
			rs.next();
			randomCinemaNo--;
		}
		randomCinema = rs.getString("CODE");
		rsq.free();// 释放对象
		return randomCinema;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		 String regExp = "(^[\\d]{0,0}$)|(^[1][3-8]+\\d{9})";
//		 Pattern p = Pattern.compile(regExp);
//		 Matcher m = p.matcher("15966880002");
//		 if (!m.find()) {
//		 System.out.println("错误");
//		 } else {
//		 System.out.println("手机号正确");
//		 }
		// String intStr = "";
		// int a = Integer.parseInt(intStr);
//		Random rand = new Random();
//		int n = 6;
//		int a = 3;
//		while (true) {
//			a = rand.nextInt(n) + 1;
//			// if(a<0||a>6){
//			if (a == 6) {
//				System.out.println(a);
//			}
//		}
//		String regExp = "(^[\\d]{0,0}$)|(^[1][3-8]+\\d{9})";
//		Pattern p = Pattern.compile(regExp);
//		Matcher m = p.matcher("13812341234");
//		if (!m.find()) {
//			System.out.println("错误");
//		} else {
//			System.out.println("手机号正确");
//		}
//		 String a = "2574812875804374983";
//		 String b = a.toUpperCase();
//		 System.out.println(b);
//		String orderType = "G";
//		if("GA".indexOf(orderType) >= 0 ){
//			System.out.println("1");
//		}
		String a  = "今日观影双倍积分;今日免费获得观影套餐一份;今日免费获得2D影票一张;今日免费获得3D影票一张";
		String[] msgArray = a.split(";");
		for (int j = 0; j < msgArray.length; j++) {
			System.out.println("<MSG>"+msgArray[j]+"</MSG>");
		}
	}
}
