package com.wanda.ccs.jobhub.member.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.wanda.ccs.jobhub.member.utils.BuddyReader;
import com.wanda.ccs.jobhub.member.utils.BuddyXmlReader;

public class MemberDNADao {
	
	public final static Logger logger = Logger.getLogger(MemberDNADao.class);
	
	@InstanceIn(path = "/dataSourceOds")
	private DataSource dataSourceOds;
	
	private ExtJdbcTemplate extJdbcTemplateOds = null;
	
	private ExtJdbcTemplate getJdbcTemplateOds()  {
		if(this.extJdbcTemplateOds == null) {
			this.extJdbcTemplateOds = new ExtJdbcTemplate(dataSourceOds);
		}
		return this.extJdbcTemplateOds;
	}
	
	public void cleanAllDNA() {
		BuddyReader sqlReader = new BuddyXmlReader(this.getClass(), "MemberDNASql.xml");
		String sql = sqlReader.get(DnaSql.CLEAN_ALL_DNA.toString());
		String[] sqls = sql.trim().split(";");
		getJdbcTemplateOds().batchUpdate(sqls);
	}
	
	public void calculateSql(final String date, final DnaSql sql) throws Exception {
		System.out.println("==================" + sql.getName() + "==================");
		Object[] param = getParams(date, sql);
		
		BuddyReader sqlReader = new BuddyXmlReader(this.getClass(), "MemberDNASql.xml");
		System.out.println("sql->"+sqlReader.get(sql.toString()));
		
		//getJdbcTemplateOds().update(sqlReader.get(sql.toString()), param);
	}
	
	private Object[] getParams(String date, DnaSql type) throws Exception {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		String startDate = "", endDate = "";
		Object[] param = new Object[] {};
		
		switch(type) {
		case CLEAN_ALL_DNA:
		case ACX_HUANGNIU:
		case ACX_BEHAVIOR_INDEX:
		case ACX_BEHAVIOR_STAND:
		case ACX_BEHAVIOR_STAND_UPDATE:
		case ACX_BEHAVIOR_DISTANCE:
			param = null;
			break;
		case ACX_BEHAVIOR_FILM_TKT:
			cal.setTime(format.parse(date));
			cal.add(Calendar.YEAR, -1);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			endDate = date+"-01";
			startDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			param = new Object[] { startDate, endDate };
			System.out.println(startDate + " " + endDate);
			break;
		case ACX_BEHAVIOR_BASE:
			cal.setTime(format.parse(date));
			cal.add(Calendar.YEAR, -1);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			endDate = date+"-01";
			startDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			
			cal.setTime(format.parse(date));
			cal.add(Calendar.MONTH, -1);
			String regDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			param = new Object[] { regDate, startDate, endDate };
			
			System.out.println(regDate + " " + startDate + " " + endDate);
			break;
		case ACX_BEHAVIOR_SEGMENT:
			cal.setTime(format.parse(date));
			date = new SimpleDateFormat("yyyyMM").format(cal.getTime());
			param = new Object[] { date };
			System.out.println(date);
			break;
		}
		
		return param;
	}
	
	/**
	 * DNA计算sql
	 */
	public static enum DnaSql {
		CLEAN_ALL_DNA("开始清除所有DNA统计数据"),
		ACX_HUANGNIU("开始统计黄牛"),
		ACX_BEHAVIOR_FILM_TKT("开始统计FILM_TKT表"),
		ACX_BEHAVIOR_BASE("开始统计BASE表"),
		ACX_BEHAVIOR_INDEX("开始统计INDEX表"),
		ACX_BEHAVIOR_STAND("开始统计STAND表"),
		ACX_BEHAVIOR_STAND_UPDATE("开始更新STAND表"),
		ACX_BEHAVIOR_DISTANCE("开始统计DISTANCE表"),
		ACX_BEHAVIOR_SEGMENT("开始统计SEGMENT表");
		
		private String name;
		
		private DnaSql(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}

}
