package com.wanda.mms.control.stream.test;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import com.wanda.mms.control.stream.util.DateUtil;

public class Doub {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	//	java.text.DecimalFormat df = (java.text.DecimalFormat) java.text.NumberFormat.getInstance();
	//	df.applyPattern("");
		//double a1 = Double.valueOf(ss);
	
		//math.round();
	//	long a = Math.round(f);
		 
		//df.format(ss);
		
	//	double d1 = Double.valueOf(  msghead.getMonthTotal());
 	//	double d2 = Double.valueOf(  sumpoc);
 	//	double d3 = d1/d2;
//		double d4 = d3*100;
//		double f = 12.5;
//		String ss = String.valueOf(f);
//		BigDecimal d5 = new BigDecimal( ss  );
//		String s6 =d5.setScale(0,BigDecimal.ROUND_DOWN).toString();
//		System.out.println(s6);
//		long a = Long.valueOf(s6);
//		//System.out.println(df.format(f).toString());
//		Dimdef dd = new Dimdef();
//		T_DimdefDao tddao = new T_DimdefDaoImpl();
//		
//		SENDDBConnection db=SENDDBConnection.getInstance();	
//	    Connection conn = null;
//		conn=db.getConnection();
//		T_Dimdef td = tddao.fandT_DimdefByID(conn, Long.valueOf(dd.POINT), dd.POINT_QJGZ);
//		double de = Double.valueOf(td.getCode());
//		System.out.println(de);
		
	//	if(date1.before(date2)){
	        //date1比date2时间提前
	//}else{
	        //date2比date1时间提前
	//}
	//下面是示例：
	//判断当前时间是否在时间date2之前
	//时间格式 2012-12-31 16:16:34
//		String ss = " 2013-12-31 16:16:34";
//		Date tt = DateUtil.getStringForDate(ss);
//		Doub bb = new Doub();
//		boolean aa = bb.isDateBefore(tt);
//		System.out.println(aa);
		
		Calendar cal=Calendar.getInstance();
		int x=-1;//or x=-3;
		cal.add(Calendar.DAY_OF_MONTH,x);
		String today=new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		System.out.println(today);
		
		String[] sp =today.split("-");
		for (int i = 0; i < sp.length; i++) {
			System.out.println(sp[i]);
		}
		String ss =sp[1]+"月"+sp[2]+"日";
		//split
		System.out.println(ss);
		
	}
	
	public   boolean isDateBefore(Date date2){
       // Date date1 = new Date();//当前时间
        String ss = " 2014-01-01 00:00:00";
        Date date1 = DateUtil.getStringForDate(ss);
        return date1.before(date2);
}

}
