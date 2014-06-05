package com.wanda.ccs.jobhub.smsreport.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.wanda.ccs.jobhub.smsreport.SmsSqlCont;
import com.wanda.ccs.jobhub.smsreport.util.BuddyReader;
import com.wanda.ccs.jobhub.smsreport.util.BuddyXmlReader;
import com.wanda.ccs.jobhub.smsreport.util.FieldUtil;
import com.wanda.ccs.jobhub.smsreport.vo.SMSData;
import com.wanda.ccs.jobhub.smsreport.vo.Target;

public class MemberLineSmsDao extends BaseDao {
	
	public final static Logger logger = Logger.getLogger(MemberLineSmsDao.class);
	
	/**
	 * 检查JOB表发送状态
	 * 
	 * @param yyyyMMdd
	 * @return
	 */
	public boolean checkJobStatus(int yyyyMMdd, String param) {
		String sql = SmsSqlCont.CHECK_JOB_STATUS.replace("{1}", param);
		int count = getJdbcTemplateStatus().queryForInt(sql, new Object[] { yyyyMMdd });
		if(count > 0) {
			return true;
		}
		
		return false;
	}
	
	public boolean updateJobStatus(int yyyyMMdd, int status) {
		String sql = SmsSqlCont.UPDATE_JOB_STATUS.replace("{1}", "FLAG_SMS=?");
		int count = getJdbcTemplateStatus().update(sql, new Object[] { status, yyyyMMdd });
		if(count > 0) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 查询院线短信统计数据
	 * 
	 * @return
	 */
	public SMSData queryLineSMS(final String date) {
		BuddyReader sqlReader = new BuddyXmlReader(this.getClass(), "LineMemberSql.xml");
		
		final SMSData sms = new SMSData();
		// 查询院线统计数据
		//String[] sqlList = {LINE.MEMBER_SQL,LINE.TICKET_SQL,LINE.GOODS_SQL,LINE.EXCHANGE_POINT_SQL,LINE.EXCHANGE_POINT_NEW_SQL,LINE.EXCHANGE_POINT_BALANCE_SQL,LINE.TICKET_MONTH_SQL,LINE.TICKET_YEAR_SQL};
		String[] sqlList = {sqlReader.get("MEMBER_SQL"),sqlReader.get("TICKET_SQL"),sqlReader.get("GOODS_SQL"),sqlReader.get("EXCHANGE_POINT_SQL"),sqlReader.get("EXCHANGE_POINT_NEW_SQL"),sqlReader.get("EXCHANGE_POINT_BALANCE_SQL"),sqlReader.get("TICKET_MONTH_SQL"),sqlReader.get("TICKET_YEAR_SQL")};
		for(int i=0,len=sqlList.length;i<len;i++) {
			System.out.println("开始执行-->"+sqlList[i]);
			final int type = i+1;
			
			getJdbcTemplateSMS().execute(sqlList[i], new PreparedStatementCallback<Object>() {
				
				@Override
				public Object doInPreparedStatement(PreparedStatement ps)
						throws SQLException, DataAccessException {
					
					setParam(type, date, ps);
					
					ps.execute(); //执行
					ResultSet rs = ps.getResultSet();
					if(rs.next()) {
						//sms.setCinemaInnerCode(rs.getString("cinema_inner_code"));
						ResultSetMetaData rsmd=rs.getMetaData();
						for(int i=1,len=rsmd.getColumnCount();i<=len;i++) {
							String field = rsmd.getColumnName(i);
							try {
								FieldUtil.setValue(sms, field, rs.getString(field));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								//e.printStackTrace();
							}
						}
					}
					
					return null;
				}
				
			});
		}
		
		return sms;
	}
	
	/**
	 * 查询院线短信统计校验数据
	 * 
	 * @return
	 */
	public SMSData queryLineSMSValidateData(final String date) {
		BuddyReader sqlReader = new BuddyXmlReader(this.getClass(), "LineMemberSql.xml");
		
		final SMSData sms = new SMSData();
		// 查询院线统计数据
		//String[] sqlList = {LINE.MEMBER_SQL,LINE.TICKET_SQL,LINE.GOODS_SQL,LINE.EXCHANGE_POINT_SQL,LINE.EXCHANGE_POINT_NEW_SQL,LINE.EXCHANGE_POINT_BALANCE_SQL,LINE.TICKET_MONTH_SQL,LINE.TICKET_YEAR_SQL};
		String[] sqlList = {sqlReader.get("MEMBER_SQL"),sqlReader.get("TICKET_SQL"),sqlReader.get("GOODS_SQL"),sqlReader.get("EXCHANGE_POINT_SQL"),sqlReader.get("EXCHANGE_POINT_NEW_SQL"),sqlReader.get("EXCHANGE_POINT_BALANCE_SQL"),sqlReader.get("TICKET_MONTH_SQL"),sqlReader.get("TICKET_YEAR_SQL")};
		for(int i=0,len=sqlList.length;i<len;i++) {
			System.out.println("开始执行-->"+sqlList[i]);
			final int type = i+1;
			
			getJdbcTemplate().execute(sqlList[i], new PreparedStatementCallback<Object>() {
				
				@Override
				public Object doInPreparedStatement(PreparedStatement ps)
						throws SQLException, DataAccessException {
					
					setParam(type, date, ps);
					
					ps.execute(); //执行
					ResultSet rs = ps.getResultSet();
					if(rs.next()) {
						//sms.setCinemaInnerCode(rs.getString("cinema_inner_code"));
						ResultSetMetaData rsmd=rs.getMetaData();
						for(int i=1,len=rsmd.getColumnCount();i<=len;i++) {
							String field = rsmd.getColumnName(i);
							try {
								FieldUtil.setValue(sms, field, rs.getString(field));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								//e.printStackTrace();
							}
						}
					}
					
					return null;
				}
				
			});
		}
		
		return sms;
	}
	
	/**
	 * 院线保存短信用户关系
	 * 
	 * @param smsId
	 * @param list
	 */
	public void insertTargetSmsRelationForLine(final Long smsId, final List<Target> list) {
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName("insertTargetSmsRelationForLine");
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			getJdbcTemplate().batchUpdate(SmsSqlCont.INSERT_TARGET_SMS_RELATION, new BatchPreparedStatementSetter() {
				
				@Override
				public int getBatchSize() {
					return list.size();
				}
				
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Target target = list.get(i);
					ps.setLong(1, target.getId());
					ps.setLong(2, smsId);
					ps.setInt(3, 1);	//待发送
					ps.setString(4, target.getMobile());
					ps.setInt(5, 1);	//发送类型
					ps.setInt(6, 3);	//优先级
				}
				
			});
		}catch(Exception ex) {
			transactionManager.rollback(status);
		}
		transactionManager.commit(status);
		
	}

}
