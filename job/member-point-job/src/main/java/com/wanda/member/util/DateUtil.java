package com.wanda.member.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static final SimpleDateFormat fromat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static Date getLastDayOfYear(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();
		StringBuffer endStr = new StringBuffer().
				append(new SimpleDateFormat("yyyy-MM-dd").format(currYearLast))
				.append(" 23:59:59");  
		try {
			return fromat.parse(endStr.toString());
		} catch (ParseException e) {			
			e.printStackTrace();
			return currYearLast;
		}
	}
	
	public static String getDate(Date date) {

		return fromat.format(date);
	}

	public static int getCurrentYear() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		return year;
	}

	public static Date getFirstDayofYear(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return currYearFirst;
	}
	/**
	 * yyyy-MM
	 * @param month
	 * @return
	 */
	public static String getFirstDaySpecMonth(String month){
		Date dMonth = null;
		try {
			dMonth = fromat.parse(month);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, dMonth.getYear());
		cal.set(Calendar.MONTH, dMonth.getMonth());
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
		return new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
	}
}
