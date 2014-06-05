package com.wanda.member.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

public class DateUtilTest {

	@Test
	public void testGetLastDayOfYear() {
		SimpleDateFormat fromat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = DateUtil.getLastDayOfYear(2014);
		Assert.assertEquals("2014-12-31 23:59:59", fromat.format(date));
	}
	
	public void testGetCurrentYear(){
		Assert.assertEquals(2013, DateUtil.getCurrentYear());
	}
	public void testGetFirstYear(){
		Assert.assertEquals("2013-01-01", DateUtil.getFirstDayofYear(2013));
	}
}
