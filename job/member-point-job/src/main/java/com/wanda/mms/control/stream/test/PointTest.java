package com.wanda.mms.control.stream.test;

import java.sql.Connection;
import java.util.Date;

import com.wanda.mms.control.stream.dao.PointHistroyDao;
import com.wanda.mms.control.stream.dao.impl.PointHistroyDaoImpl;
import com.wanda.mms.control.stream.db.SENDDBConnection;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.MemberPoint;
import com.wanda.mms.control.stream.vo.PointHistroy;

public class PointTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 
		PointHistroy ph = new PointHistroy();
		MemberPoint mp = new MemberPoint();
		
		ph.setMemberid(81);
		Date da = new Date();
		String date = DateUtil.getDateStrss(da);// 
		String yy = (String) date.subSequence(0, 4);
		int intyy = Integer.valueOf(yy);
		intyy = intyy +1;
		String yyyy = String.valueOf(intyy);
		String te = "-12-31 23:59:59";
		String time =yyyy+te; 
		ph.setSetTime(date);
		ph.setExchange_Point_Expire_Time(time);
		ph.setLevel_Point(2000);
		ph.setExg_expire_point_balance(2000);
		ph.setTicket_Count(20);
		ph.setActivity_Point(400);
		ph.setExchange_Point(2400);
		ph.setPoint_Type("1");
		ph.setPoint_Sys("3");
		ph.setPoint_Trans_Type("1");
		
		ph.setPoint_Trans_Code_Web("0");
		
		ph.setIsdelete(0);
		ph.setCreate_By("admina");
		ph.setVersion(1);
		
		PointHistroyDao dao = new PointHistroyDaoImpl();
		SENDDBConnection db=SENDDBConnection.getInstance();	
	    Connection conn = null;
		conn=db.getConnection();
		
		String ss = dao.addPointHistroy(conn, ph);
		System.out.println("ss = "+ss);

	}

}
