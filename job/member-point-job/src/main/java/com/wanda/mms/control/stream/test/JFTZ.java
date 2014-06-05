package com.wanda.mms.control.stream.test;

import java.sql.Connection;
import java.util.Date;

import com.wanda.mms.control.stream.PointAdjust;
import com.wanda.mms.control.stream.dao.PointHistroyDao;
import com.wanda.mms.control.stream.dao.impl.PointHistroyDaoImpl;
import com.wanda.mms.control.stream.db.SENDDBConnection;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.PointHistroy;

public class JFTZ {
	
	public static void main(String[] args) {
		Date da = new Date();
		String date = DateUtil.getDateStrss(da);//时间可能是 时分秒
		String yy = (String) date.subSequence(0, 4);
		int intyy = Integer.valueOf(yy);
		intyy = intyy +1;
		String yyyy = String.valueOf(intyy);
		String te = "-12-31 23:59:59";
		String time =yyyy+te; 
		String dt = yy+te;
		PointHistroyDao dao = new PointHistroyDaoImpl();
		SENDDBConnection db=SENDDBConnection.getInstance();	
	    Connection conn = null;
		conn=db.getConnection();
		
		PointAdjust pa = new PointAdjust();//积分调整
		PointHistroy ph = new PointHistroy();
		ph.setSetTime(date);
		ph.setMemberid(3092698);
		ph.setLevel_Point(100);
		ph.setTicket_Count(2);
		ph.setExchange_Point(100);
		ph.setExchange_Point_Expire_Time(time);
		ph.setPoint_Type("4");
		ph.setPoint_Sys("3");
		ph.setPoint_Trans_Type("1");
		
		String ss = pa.adjust(conn,ph);
		System.out.println(ss);
	}

}
