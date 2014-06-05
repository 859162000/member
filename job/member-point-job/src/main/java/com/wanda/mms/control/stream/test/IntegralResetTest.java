package com.wanda.mms.control.stream.test;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;

import com.wanda.mms.control.stream.db.SENDDBConnection;
import com.wanda.mms.control.stream.service.IntegralInitialization;


public class IntegralResetTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		SENDDBConnection db=SENDDBConnection.getInstance();	
//	    Connection conn = null;
//		conn=db.getConnection();
//		String date = "20141231";
//		IntegralReset ir = new IntegralReset();
//		
//		String ss = ir.IntegralResetDispose(conn, date);
//		System.out.println(ss);
		
//		IntegralInitialization ii = new IntegralInitialization();
//		String user ="admin";
//		long a =262;
//		int flag=0;
//		long b =100;
//		flag = ii.addMemberPointByID(conn, a, user,b);
//		System.out.println("flag="+flag+"");
		Date date = new Date();
		Calendar cal=Calendar.getInstance();
		//cal.setTime(date);//today;
		int x=-2;//or x=-3;
		cal.add(Calendar.DAY_OF_MONTH,x);
		
		String today=new java.text.SimpleDateFormat("yyyyMMdd").format(cal.getTime());
		System.out.println(today);
		
	}
	

}
