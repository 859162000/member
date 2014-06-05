package com.wanda.ccs.jobhub.member.dao;

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
		Object[] param = getParams(date, sql);
		
		BuddyReader sqlReader = new BuddyXmlReader(this.getClass(), "MemberDNASql.xml");
		getJdbcTemplateOds().update(sqlReader.get(sql.toString()), param);
	}
	
	private Object[] getParams(String date, DnaSql type) throws Exception {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
		String startDate, endDate;
		Object[] param = new Object[] {};
		
		switch(type) {
		case CLEAN_ALL_DNA:
		case ACX_HUANGNIU:
		case ACX_BEHAVIOR_INDEX:
		case ACX_BEHAVIOR_STAND:
		case ACX_BEHAVIOR_STAND_UPDATE:
		case ACX_BEHAVIOR_DISTANCE:
			param = new Object[] {};
			break;
		case ACX_BEHAVIOR_FILM_TKT:
			cal.setTime(format.parse(date));
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			endDate = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
			startDate = date+"01";
			param = new Object[] { startDate, endDate };
			break;
		case ACX_BEHAVIOR_BASE:
			cal.setTime(format.parse(date));
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			endDate = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
			startDate = date+"01";
			param = new Object[] { startDate, startDate, endDate };
			break;
		case ACX_BEHAVIOR_SEGMENT:
			cal.setTime(format.parse(date));
			cal.add(Calendar.MONTH, 1);
			date = new SimpleDateFormat("yyyyMM").format(cal.getTime());
			param = new Object[] { date };
			break;
		}
		
		return param;
	}
	
	/**
	 * DNA计算sql
	 */
	public static enum DnaSql {
		CLEAN_ALL_DNA,
		ACX_HUANGNIU,
		ACX_BEHAVIOR_FILM_TKT,
		ACX_BEHAVIOR_BASE,
		ACX_BEHAVIOR_INDEX,
		ACX_BEHAVIOR_STAND,
		ACX_BEHAVIOR_STAND_UPDATE,
		ACX_BEHAVIOR_DISTANCE,
		ACX_BEHAVIOR_SEGMENT;
	}

}
