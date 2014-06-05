package com.wanda.mrb.intf.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatTools {

	
	public static String setNullEmpey(String value ){
		if(value==null){
			return "";
		}
		return value;
	}
	
	/**
	 * 查找标签中为NULL，替换成空
	 * @param xml
	 * @return
	 */
	public static String setTagEmpey(String xml){
		Pattern p = Pattern.compile("<[a-zA-Z_]+[1-9]?[^><]*>null</[a-zA-Z_]+[1-9]?>");  
	    Matcher m  = p.matcher(xml);  
	    while(m.find()){
	    	xml=xml.replace(m.group(), m.group().replace("null", ""));
	    }
	    return xml;
	}
	
	
	public static String formatDateSS(String str) {
		if(str==null || str.equals("")){
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = formatter.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	  return formatter.format(d);
	}
	
	public static Date formatDates(String str) {
		if(str==null || str.equals("")){
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = formatter.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	  return d;
	}
	public static String formatDate(String str) {
		if(str==null || str.equals("")){
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = formatter.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	  return formatter.format(d);
	}
	
	public static String formatTimeStamp(String str){
		if(str==null || str.equals("")){
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh24:mi:ss");
		Date d = null;
		try {
			d = formatter.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	  return formatter.format(d);
	}
	
	public static String formatTimeStamp(){
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh24:mi:ss");
		String nowTime = formatter.format(now);
	    return nowTime;
	}
	
	public static String getNowTime(){
		Date now = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = dateFormat.format(now);
		return nowTime;
	}
	
	public static String bizDate(String time){
		if(time==null||"".equals(time)){
			return "";
		}
		String myday="";
		String bday="";
		long x=0;
		try {
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		SimpleDateFormat fomtday=new SimpleDateFormat("yyyy-MM-dd");  
	     Date date4;
		 date4 = fomtday.parse(time);
		 myday=fomtday.format(date4);
	     Calendar calendar = Calendar.getInstance();
		 calendar.setTime(date4);
		 calendar.add(Calendar.DAY_OF_MONTH, -1);
		 calendar.getTime();
		 bday=fomtday.format(calendar.getTime());
	     calendar.add(Calendar.DAY_OF_MONTH, 2);
		 calendar.getTime();
	     String aday=fomtday.format(calendar.getTime());

	     Date date1=formatter.parse(bday+" 06:00:00");
	     Date date2=formatter.parse(time);
	     Date date3=formatter.parse(aday+" 06:00:00");
	     long l=date2.getTime()-date1.getTime();
	     x=date3.getTime()-date2.getTime();
		} catch (ParseException e) {
			return time;
		}
	     if(x>86400000){
	    	 return bday;
	     }
	     return myday;
	}

	
	public static void main(String args[]){
//		String xml = "<ccsresp icode='C3001' serialnum='3000001' retcode='0' retmsg=''><CINEMA_NAME>null</CINEMA_NAME>";
//		setTagEmpey(xml);
		
//		String time = "2014-12-31.23.59.59.0";
//		String time1 = formatDate(time);
//		String time2 = formatTimeStamp(time);
		
//		System.out.println(time1);
		System.out.println(getNowTime());
		
	}
	
	
	
}
