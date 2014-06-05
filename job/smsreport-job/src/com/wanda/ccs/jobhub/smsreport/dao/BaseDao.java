package com.wanda.ccs.jobhub.smsreport.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;

public abstract class BaseDao {
	
	@InstanceIn(path = "/dataSource")
	protected DataSource dataSource;
	
	@InstanceIn(path = "/dataSourceSMS")
	protected DataSource dataSourceStatus;
	
	@InstanceIn(path = "/dataSourceOds")
	protected DataSource dataSourceSMS;
	
	@InstanceIn(path = "/transactionManager")
	protected DataSourceTransactionManager transactionManager;
	
	protected ExtJdbcTemplate extJdbcTemplate = null;
	
	protected ExtJdbcTemplate extJdbcTemplateStatus = null;
	
	protected ExtJdbcTemplate extJdbcTemplateSMS = null;

	public ExtJdbcTemplate getJdbcTemplate() {
		if (this.extJdbcTemplate == null) {
			this.extJdbcTemplate = new ExtJdbcTemplate(dataSource);
		}
		return this.extJdbcTemplate;
	}
	
	public ExtJdbcTemplate getJdbcTemplateStatus() {
		if (this.extJdbcTemplateStatus == null) {
			this.extJdbcTemplateStatus = new ExtJdbcTemplate(dataSourceStatus);
		}
		return this.extJdbcTemplateStatus;
	}
	
	public ExtJdbcTemplate getJdbcTemplateSMS() {
		if (this.extJdbcTemplateSMS == null) {
			this.extJdbcTemplateSMS = new ExtJdbcTemplate(dataSourceSMS);
		}
		return this.extJdbcTemplateSMS;
	}
	
	/**
	 * @throws SQLException 
	 * 根据type类型设置参数个数
	 * 
	 * @param @param type
	 * @param @param date    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	protected void setParam(int type, String date, PreparedStatement pst) throws SQLException {
		switch (type) {
			case 1:	//会员统计
			case 2:	//票房统计
				pst.setString(1,date);
				pst.setString(2,date);
				pst.setString(3,date);
				pst.setString(4,date);
				break;
			case 3:	//卖品统计
			case 4: //可兑换积分兑换
			case 5: //可兑换积分增加
			case 6: //可兑换积分余额
				pst.setString(1,date);
				break;
			case 7:	//会员月票房累计，以后分线上，线下
				date = date.substring(0,7)+"-01";
				pst.setString(1,date);
				pst.setString(2,date);
				pst.setString(3,date);
				pst.setString(4,date);
				break;
			case 8: //会员年票房累计，以后分线上，线下
				date = date.substring(0,4)+"-01-01";
				pst.setString(1,date);
				pst.setString(2,date);
				pst.setString(3,date);
				pst.setString(4,date);
				break;
		default:
			break;
		}
	}
	
}
