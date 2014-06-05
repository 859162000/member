package com.wanda.mms.control.stream.test;

import java.sql.Connection;
import java.util.Date;

import com.wanda.mms.control.stream.dao.PointHistroyDao;
import com.wanda.mms.control.stream.dao.impl.PointHistroyDaoImpl;
import com.wanda.mms.control.stream.db.SENDDBConnection;
import com.wanda.mms.control.stream.service.PointsDeduction;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.MemberPoint;
import com.wanda.mms.control.stream.vo.PointHistroy;

public class Jfkj {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PointHistroy ph = new PointHistroy();
		MemberPoint mp = new MemberPoint();
		
		long 	memberid =81; 
		long exchange_point =-100; 
		long org_Point_Balance=2000;
		String point_sys = "3"; 
		String point_trans_code = "0000"; 
		String point_trans_code_pos = "1111"; 
		String create_by = "admin";
		Date da = new Date();
		String date = DateUtil.getDateStrss(da); 
		String yy = (String) date.subSequence(0, 4);
		int intyy = Integer.valueOf(yy);
		intyy = intyy +1;
		String yyyy = String.valueOf(intyy);
		String te = "-12-31 23:59:59";
		String time =yyyy+te; 
		 
		ph.setCreate_By("admina");
		 
		
		PointHistroyDao dao = new PointHistroyDaoImpl();
		SENDDBConnection db=SENDDBConnection.getInstance();	
	    Connection conn = null;
		conn=db.getConnection();
		
	//	String ss = dao.addPointHistroy(conn, ph);
		PointsDeduction pd = new PointsDeduction();
	//	String ss = pd.deductionPoints(conn, memberid, exchange_point,org_Point_Balance ,point_sys, point_trans_code, point_trans_code_pos, create_by);
	//	System.out.println("ss = "+ss);

	}

}
