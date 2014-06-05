package com.wanda.mms.control.stream.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	/**
	 * 按yyyy-MM-dd 格式得到年月日  
	 * @param date
	 * @return
	 */
	public static String getDateStrymd(Date date){
		String str=null;//20-8月-81
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		str=sdf.format(date);
		return str;
	}
	/**
	 * 按yyyyMMdd 格式得到年月日  
	 * @param date
	 * @return
	 */
	public static String getDateStr(Date date){
		String str=null;//20-8月-81
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		str=sdf.format(date);
		return str;
	}
	/**
	 * 按yyyy-MM-dd HH:mm:ss 格式得到年-月-日  时：分：秒
	 * @param date
	 * @return
	 */
	public static String getDateStrss(Date date){
		String str=null;//20-8月-81
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		str=sdf.format(date);
		return str;
	}
	/**
	 * yyyy-MM-dd HH:mm:ss 转换成日期类型  
	 * @param date
	 * @return
	 */
	public static Date getStringForDate(String str){
		Date date=null;
		DateFormat dateFormat = DateFormat.getDateInstance();
		try {
			date=dateFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * 按yyyy年MM月dd日  格式得到年-月-日   
	 * @param date
	 * @return
	 */
	public static String getDateStrCn(Date date){
		String str=null;//20-8月-81
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 
		str=sdf.format(date);
		return str;
	}
	//把时间转换成Long以便对比大小
	public static Long Hour(Date time){
		SimpleDateFormat st=new SimpleDateFormat("yyyyMMddHHmmss");
		return Long.parseLong(st.format(time));
		}
	//
	public static Date StringToDate(String s){
		Date time=new Date();
		SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			time=sd.parse(s);
			}catch (ParseException e) {
				System.out.println("输入的日期格式有误！");
				}		
			return time;
			}
	
	 
 
	/**
	 * 存入数据库datetime类型
	 * String s = "2012-02-02 12:12:12";
	 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	 * Date s_date =(Date)sdf.parse(s);
	 */
	
	
}
