package com.wanda.mrb.intf.utils;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	/**
	 * format timestamp to YYYY-MM-DD
	 */
	public static String formatDate(Timestamp timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(timestamp);
	}

	/**
	 * format timestamp to HH:mm
	 */
	public static String formatHourMinute(Timestamp timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(timestamp);
	}

	public static String format(Timestamp timestamp, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(timestamp);
	}

	/**
	 * format double to XXXXX.YY
	 */
	public static String formatBalance(double balance) {
		DecimalFormat df = new DecimalFormat("###.##");
		return df.format(balance);
	}

	public static void main(String[] argv) {
		System.out.println(formatDate(new Timestamp(new java.util.Date()
				.getTime())));
		System.out.println(formatHourMinute(new Timestamp(new java.util.Date()
				.getTime())));
		System.out.println(formatBalance(123456.8));
		System.out.println("a=" + validDate("2011-12-01"));
		System.out.println("b=" + validDate("201112-30"));
		
		try {
			System.out.println(DateBeforeMonth("2013-02-16","2013-06-15",90));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Date parserDate(String str) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.parse(str);
	}
	
	public static Date parserDateSS(String str) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(str);
	}
	
	/*
	 * test if theDate's format is YYYY-MM-DD
	 */
	public static boolean validDate(String theDate) {
		try {
			java.sql.Date.valueOf(theDate);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static Date parseDataDD(String str) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.parse(str);
	}

	public static String DateBeforeMonth(String startDate, String endDate,
			int subMonth) throws Exception {

		Date date1 = new SimpleDateFormat("yyyy-mm-dd").parse(endDate);
		Date date2 = new SimpleDateFormat("yyyy-mm-dd").parse(startDate);
		long day = (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000) > 0 ? (date1
				.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000)
				: (date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000);
		System.out.println("相差的日期: " + day);
		if (day>90) {
			System.out.println("相差的日期大于90: " + day);
//			Calendar cal = Calendar.getInstance();
//			cal.setTime(date1);
//			cal.add(Calendar.DAY_OF_YEAR, subMonth);
//			date2 = cal.getTime();
//			Date date3 = new SimpleDateFormat("yyyy-mm-dd").parse(date2.toString());
			System.out.println(date1.getTime());
			date1.setTime(date1.getTime()-(24 * 60 * 60 * 1000));
			SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
			startDate=formate.format(date1);
			System.out.println("当前时间："+endDate);
			System.out.println("3个月前的时间："+startDate);
		} else {
			System.out.println("相差的日期小于90: " + day);
		}
		return startDate;
	}
}
