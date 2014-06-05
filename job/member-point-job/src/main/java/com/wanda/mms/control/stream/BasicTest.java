package com.wanda.mms.control.stream;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wanda.mms.control.stream.dao.impl.TmpDaoImpl;
import com.wanda.mms.dao.MyBatisDAO;
/**
 * 计算基础积分
 * @author lining
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/application*.xml")

public class BasicTest {
	@Resource
	MyBatisDAO myBatisDAO = null;
	Basic basic = new Basic();
	@Test
	public void testAgainCalculate() {
		fail("Not yet implemented");
	}

//	@Test
	public void testCalculatePoint_001() {
		Connection con = myBatisDAO.getConnection();
		Connection conDw = myBatisDAO.getDwConnection();
		try {
			con.setAutoCommit(true);
			conDw.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		basic.mbr = con;
		basic.mbrdw = conDw;
		
		String date = "2013-09-19";
		String cinema="830";
		basic.againCalculate(cinema, date);
		basic.prepareActivePointTransData(cinema, date);
		basic.calculatePoint(cinema, date);
	}
	@Test
	public void testCalculatePoint() {
		Connection con = myBatisDAO.getConnection();
		Connection conDw = myBatisDAO.getDwConnection();
		try {
			con.setAutoCommit(true);
			conDw.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		basic.mbr = con;
		basic.mbrdw = conDw;
		
		String date = "2013-08-31";
		String cinema="381";
		basic.againCalculate(cinema, date);
		TmpDaoImpl tm = new TmpDaoImpl();
		tm.tmp(basic.mbr, cinema);
		basic.prepareActivePointTransData(cinema, date);
		basic.calculatePoint(cinema, date);
	}
	
		/**
		 * member_key='6337055' 手机号：13819730717
			观影日期：2013-09-19
			票：35 45 45
			影城内码：830
		 */
}
