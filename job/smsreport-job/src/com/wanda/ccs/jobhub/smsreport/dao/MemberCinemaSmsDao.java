package com.wanda.ccs.jobhub.smsreport.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.wanda.ccs.jobhub.smsreport.SmsSqlCont;
import com.wanda.ccs.jobhub.smsreport.util.BuddyReader;
import com.wanda.ccs.jobhub.smsreport.util.BuddyXmlReader;
import com.wanda.ccs.jobhub.smsreport.util.FieldUtil;
import com.wanda.ccs.jobhub.smsreport.vo.SMSData;
import com.wanda.ccs.jobhub.smsreport.vo.Target;

public class MemberCinemaSmsDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(MemberCinemaSmsDao.class);
	
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
	
	/**
	 * 更新13库状态
	 * 
	 * @param yyyyMMdd
	 * @param status
	 * @return
	 */
	public boolean updateJobStatus(int yyyyMMdd, int status) {
		String sql = SmsSqlCont.UPDATE_JOB_STATUS.replace("{1}", "FLAG_CINEMA_SMS=?");
		int count = getJdbcTemplateStatus().update(sql, new Object[] { status, yyyyMMdd });
		if(count > 0) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 通过影城内码返回对应的影城统计空对象
	 * 
	 * @return
	 */
	public Map<String, SMSData> getSMSByInnerCode() {
		
		Map<String, SMSData> map = getJdbcTemplate().query(SmsSqlCont.QUERY_CINEMA_INNER_CODE, new ResultSetExtractor<Map<String, SMSData>>() {
			@Override
			public Map<String, SMSData> extractData(ResultSet rs)
					throws SQLException, DataAccessException {
				Map<String, SMSData> map = new HashMap<String, SMSData>();
				
				while(rs.next()) {
					map.put(rs.getString("INNER_CODE"), new SMSData(rs.getString("INNER_CODE"), rs.getString("INNER_NAME")));
				}
				
				return map;
			}
		});
		
		return map;
	}
	
	/**
	 * 查询影城短信统计数据
	 * 
	 * @return
	 */
	public Map<String, SMSData> queryCinemaSMS(final String date) {
		BuddyReader sqlReader = new BuddyXmlReader(this.getClass(), "CinemaMemberSql.xml");
		
		// 根据影城内码查询map
		final Map<String, SMSData> map = getSMSByInnerCode();
		// for循环查询所有统计，放入对应的影城中
		//String[] sqlList = {CINEMA.MEMBER_SQL,CINEMA.TICKET_SQL,CINEMA.GOODS_SQL,CINEMA.EXCHANGE_POINT_SQL,CINEMA.EXCHANGE_POINT_NEW_SQL,CINEMA.EXCHANGE_POINT_BALANCE_SQL,CINEMA.TICKET_MONTH_SQL,CINEMA.TICKET_YEAR_SQL};
		String[] sqlList = {sqlReader.get("MEMBER_SQL"),sqlReader.get("TICKET_SQL"),sqlReader.get("GOODS_SQL"),sqlReader.get("EXCHANGE_POINT_SQL"),sqlReader.get("EXCHANGE_POINT_NEW_SQL"),sqlReader.get("EXCHANGE_POINT_BALANCE_SQL"),sqlReader.get("TICKET_MONTH_SQL"),sqlReader.get("TICKET_YEAR_SQL")};
		for(int i=0,len=sqlList.length;i<len;i++) {
			System.out.println("开始执行-->"+sqlList[i]);
			final int type = i+1;
			getJdbcTemplateSMS().execute(sqlList[i], new PreparedStatementCallback<Object>() {

				@Override
				public Object doInPreparedStatement(PreparedStatement ps)
						throws SQLException, DataAccessException {
					setParam(type, date, ps);
					
					ps.execute();	//开始执行查询
					ResultSet rs = ps.getResultSet();
					while(rs.next()) {
						String innerCode = rs.getString("inner_code");
						SMSData sms = map.get(innerCode);
						if(sms != null) {
							//System.out.println("将数据放入对应的影城内码为"+innerCode);
							ResultSetMetaData rsmd=rs.getMetaData();
							for(int i=1,len=rsmd.getColumnCount();i<=len;i++) {
								String field = rsmd.getColumnName(i);
								//System.out.println(field);
								try {
									FieldUtil.setValue(sms, field, rs.getString(field));
								} catch (Exception e) {
									//e.printStackTrace();
								}
							}
						} else {
							System.out.println("影城内码ID"+innerCode+"不存在");
						}
					}
					
					return null;
				}
				
			});			
		}
		
		return map;
		// 查询所有用户，通过map将用户和影城进行关联
	}
	
	/**
	 * 查询影城校验数据
	 * 
	 * @param date
	 * @return
	 */
	public Map<String, SMSData> queryCinemaSMSValidateData(final String date) {
		BuddyReader sqlReader = new BuddyXmlReader(this.getClass(), "CinemaMemberSql.xml");
		
		// 根据影城内码查询map
		final Map<String, SMSData> map = getSMSByInnerCode();
		// for循环查询所有统计，放入对应的影城中
		//String[] sqlList = {CINEMA.MEMBER_SQL,CINEMA.TICKET_SQL,CINEMA.GOODS_SQL,CINEMA.EXCHANGE_POINT_SQL,CINEMA.EXCHANGE_POINT_NEW_SQL,CINEMA.EXCHANGE_POINT_BALANCE_SQL,CINEMA.TICKET_MONTH_SQL,CINEMA.TICKET_YEAR_SQL};
		String[] sqlList = {sqlReader.get("MEMBER_SQL"),sqlReader.get("TICKET_SQL"),sqlReader.get("GOODS_SQL"),sqlReader.get("EXCHANGE_POINT_SQL"),sqlReader.get("EXCHANGE_POINT_NEW_SQL"),sqlReader.get("EXCHANGE_POINT_BALANCE_SQL"),sqlReader.get("TICKET_MONTH_SQL"),sqlReader.get("TICKET_YEAR_SQL")};
		for(int i=0,len=sqlList.length;i<len;i++) {
			System.out.println("开始执行-->"+sqlList[i]);
			final int type = i+1;
			getJdbcTemplate().execute(sqlList[i], new PreparedStatementCallback<Object>() {

				@Override
				public Object doInPreparedStatement(PreparedStatement ps)
						throws SQLException, DataAccessException {
					setParam(type, date, ps);
					
					ps.execute();	//开始执行查询
					ResultSet rs = ps.getResultSet();
					while(rs.next()) {
						String innerCode = rs.getString("inner_code");
						SMSData sms = map.get(innerCode);
						if(sms != null) {
							System.out.println("将数据放入对应的影城内码为"+innerCode);
							ResultSetMetaData rsmd=rs.getMetaData();
							for(int i=1,len=rsmd.getColumnCount();i<=len;i++) {
								String field = rsmd.getColumnName(i);
								System.out.println(field);
								try {
									FieldUtil.setValue(sms, field, rs.getString(field));
								} catch (Exception e) {
									//e.printStackTrace();
								}
							}
						} else {
							System.out.println("影城内码ID"+innerCode+"不存在");
						}
					}
					
					return null;
				}
				
			});			
		}
		
		return map;
		// 查询所有用户，通过map将用户和影城进行关联
	}
	
	/**
	 * 影城保存短信用户关系
	 * 
	 * @param smsId
	 * @param list
	 */
	public void insertTargetSmsRelationForCinema(final Map<String, SMSData> smsMap, final Map<String, List<Target>> targetList) {
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		Object[] obj = null;
		List<Target> list = null;
		for(Map.Entry<String, SMSData> entry : smsMap.entrySet()) {
			list = targetList.get(entry.getKey());
			if(list != null && list.size() > 0) {	//判断该类会员短信是否需要发送给指定人员
				for(int i=0,len=list.size();i<len;i++) {
					obj = new Object[] {list.get(i).getId(), entry.getValue().getSeqid(), 1, list.get(i).getMobile(), 1, 3};
					batchArgs.add(obj);
				}
				obj = null; list = null;
			}
		}
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName("insertTargetSmsRelationForLine");
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			getJdbcTemplate().batchUpdate(SmsSqlCont.INSERT_TARGET_SMS_RELATION, batchArgs);
		}catch(Exception ex) {
			transactionManager.rollback(status);
		}
		transactionManager.commit(status);
	}
	
}
