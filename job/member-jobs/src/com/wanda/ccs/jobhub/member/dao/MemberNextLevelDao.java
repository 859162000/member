package com.wanda.ccs.jobhub.member.dao;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.wanda.ccs.jobhub.member.utils.BuddyReader;
import com.wanda.ccs.jobhub.member.utils.BuddyXmlReader;

public class MemberNextLevelDao {
	
	public final static Logger logger = Logger.getLogger(MemberNextLevelDao.class);
	
	@InstanceIn(path = "/dataSource")
	private DataSource dataSource;
	
	private ExtJdbcTemplate extJdbcTemplate = null;
	
	private ExtJdbcTemplate getJdbcTemplate()  {
		if(this.extJdbcTemplate == null) {
			this.extJdbcTemplate = new ExtJdbcTemplate(dataSource);
		}
		return this.extJdbcTemplate;
	}
	
	/**
	 * 清除会员升级统计临时数据
	 */
	public void clearMemberNextLevel() {
		BuddyReader sqlReader = new BuddyXmlReader(this.getClass(), "MemberNextLevelSql.xml");
		String sql = sqlReader.get("CLEAN_MEMBER_LEVEL");
		
		getJdbcTemplate().update(sql);
	}
	
	/**
	 * 统计会员下一级别升级所需条件
	 * 
	 * @param yyyy
	 */
	public void calculateMemberNextLevel(String yyyy) {
		BuddyReader sqlReader = new BuddyXmlReader(this.getClass(), "MemberNextLevelSql.xml");
		String sql = sqlReader.get("CALCULATE_MEMBER_NEXT_LEVEL");
		String next = String.valueOf(Integer.parseInt(yyyy)+1);
		
		getJdbcTemplate().update(sql, new Object[] { yyyy, next });
	}
	
	/**
	 * 根据统计数据更新t_member_levle表中的字段
	 */
	public void updateMemberNextLevel() {
		BuddyReader sqlReader = new BuddyXmlReader(this.getClass(), "MemberNextLevelSql.xml");
		String sql = sqlReader.get("SYNC_MEMBER_NEXT_LEVEL");
		
		getJdbcTemplate().update(sql);
	}
	
	public int existTempDate() {
		BuddyReader sqlReader = new BuddyXmlReader(this.getClass(), "MemberNextLevelSql.xml");
		String sql = sqlReader.get("EXIST_TEMP_DATE"); 
		
		return getJdbcTemplate().queryForInt(sql);
	}

}
