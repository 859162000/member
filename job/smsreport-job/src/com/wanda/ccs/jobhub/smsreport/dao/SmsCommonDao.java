package com.wanda.ccs.jobhub.smsreport.dao;

import java.io.IOException;
import java.rmi.RemoteException;
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
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.wanda.ccs.jobhub.smsreport.CallBack;
import com.wanda.ccs.jobhub.smsreport.SmsSqlCont;
import com.wanda.ccs.jobhub.smsreport.util.FieldUtil;
import com.wanda.ccs.jobhub.smsreport.util.FreeMarkerUtil;
import com.wanda.ccs.jobhub.smsreport.util.PropertiesUtil;
import com.wanda.ccs.jobhub.smsreport.util.VoucherHttpRequestUtil;
import com.wanda.ccs.jobhub.smsreport.vo.SMSData;
import com.wanda.ccs.jobhub.smsreport.vo.SendSms;
import com.wanda.ccs.jobhub.smsreport.vo.Target;
import com.wanda.ccs.jobhub.smsreport.vo.TargetSmsRelation;

import freemarker.template.TemplateException;

/**
 * 公共查询类
 * 
 * @author zhurui
 * @date 2014年1月12日 下午7:36:48
 */
public class SmsCommonDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(SmsCommonDao.class);
	
	/**
	 * 获取SMS序列id
	 * 
	 * @return
	 */
	public Long querySmsId() {
		return getJdbcTemplate().queryForLong(SmsSqlCont.SEQ_SMS_ID);
	}
	
	/**
	 * 查询短信数量
	 * 
	 * @param yyyyMMdd
	 * @param isCinema
	 * @return
	 */
	public int querySmsCount(int yyyyMMdd, boolean isCinema) {
		String sql = SmsSqlCont.QUERY_SMS_COUNT.replace("{1}", isCinema?"is not null":"is null");
		return getJdbcTemplate().queryForInt(sql, new Object[] {yyyyMMdd});
	}
	
	/**
	 * 查询发送人员数量
	 * 
	 * @param yyyyMMdd
	 * @param isCinema
	 * @return
	 */
	public int querySmsTargetCount(int yyyyMMdd, boolean isCinema) {
		String sql = SmsSqlCont.QUERY_WAIT_SEND_SMS_SQL_COUNT.replace("{1}", isCinema?"is not null":"is null");
		return getJdbcTemplate().queryForInt(sql, new Object[] {yyyyMMdd});
	}
	
	/**
	 * 获取用户信息
	 * 
	 * @param isSystemUser
	 * @param isCinema
	 * @return
	 */
	public Map<String, List<Target>> queryTargetList(String status, boolean isCinema) {
		String sql =  SmsSqlCont.QUERY_SEND_USER+" and t.issystemuser="+status+" and "+(isCinema?"t.cinema_inner_code is not null":"t.cinema_inner_code is null");
		logger.debug(sql);
		Map<String, List<Target>> map = getJdbcTemplate().query(sql, new ResultSetExtractor<Map<String, List<Target>>>() {

			@Override
			public Map<String, List<Target>> extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				Map<String, List<Target>> map = new HashMap<String, List<Target>>();
				List<Target> list;
				Target target;
				
				while(rs.next()) {
					target = new Target();
					target.setId(rs.getLong("seqid"));
					target.setName(rs.getString("name")); 
					target.setDuty(rs.getString("duty"));
					target.setMobile(rs.getString("mobile")); 
					target.setIsSystemUser(rs.getInt("issystemuser"));
					target.setCinemaInnerCode(rs.getString("cinema_inner_code"));
					if(map.containsKey(rs.getString("cinema_inner_code"))) {
						list = map.get(rs.getString("cinema_inner_code"));
					} else {
						list = new ArrayList<Target>();
						map.put(rs.getString("cinema_inner_code"), list);
					}
					list.add(target);
					target = null;
					list = null;
				}
				
				return map;
			}
		});
		
		return map;
	}
	
	/**
	 * 查询短信数据
	 * 
	 * @param yyyyMMdd
	 * @param isCinema		查询影城或院线数据
	 * @return
	 */
	public Map<String, SMSData> querySms(int yyyyMMdd, boolean isCinema) {
		String sql = SmsSqlCont.QUERY_SMS.replace("{1}", isCinema?" is not null ":" is null ");
		Map<String, SMSData> map = getJdbcTemplate().query(sql, new Object[] {yyyyMMdd}, new ResultSetExtractor<Map<String, SMSData>>() {
			
			@Override
			public Map<String, SMSData> extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				
				Map<String, SMSData> map = new HashMap<String, SMSData>();
				SMSData sms = null;
				while(rs.next()) {
					sms = new SMSData();
					ResultSetMetaData rsmd=rs.getMetaData();
					for(int i=1,len=rsmd.getColumnCount();i<=len;i++) {
						String field = rsmd.getColumnName(i);
						logger.debug(field);
						try {
							FieldUtil.setValue(sms, field, rs.getString(field));
						} catch (Exception e) {
							//e.printStackTrace();
						}
					}
					map.put(rs.getString("cinema_inner_code"), sms);
					sms = null;
				}
				
				return map;
			}
		});
		
		return map;
	}
	
	/**
	 * 更新发送状态
	 * 
	 * @param list
	 */
	public void updateSendStatus(final List<TargetSmsRelation> list, final int status) {
		getJdbcTemplate().batchUpdate(SmsSqlCont.UPDATE_TARGET_SMS_RELATION, new BatchPreparedStatementSetter() {
			@Override
			public int getBatchSize() {
				return list.size();
			}
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				TargetSmsRelation relation = list.get(i);
			    ps.setInt(1, status);
				ps.setLong(2, relation.getId());
			}
		});
	}
	
	/**
	 * 更新校验状态
	 * 
	 * @param list
	 */
	public void updateSMSValidStatus(final List<SMSData> list) {
		this.insertMethod("updateSMSValidStatus", new CallBack() {
			
			@Override
			public void execute() {
				getJdbcTemplate().batchUpdate(SmsSqlCont.UPDATE_SMS_VALID_STATUS, new BatchPreparedStatementSetter() {
					@Override
					public int getBatchSize() {
						return list.size();
					}
					
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						SMSData sms = list.get(i);
						ps.setString(1, sms.getValid());
						ps.setString(2, sms.getFailDesc());
						ps.setLong(3, Long.parseLong(sms.getSeqid()));
					}
				});
			}
		});
	}
	
	
	public boolean queryValidStatus(int yyyyMMdd, boolean isCinema) {
		String sql = SmsSqlCont.QUERY_SMS_VALID_FAIL_COUNT + (isCinema?" is not null " : " is null ");
		int count = getJdbcTemplate().queryForInt(sql, new Object[] {yyyyMMdd});
		
		return count > 0 ? false : true;
	}
	
	/**
	 * 更新短信信息
	 * 
	 * @param yyyyMMdd
	 * @param list
	 */
	public void updateSMS(final int yyyyMMdd, final List<SMSData> list) {
		getJdbcTemplate().batchUpdate(SmsSqlCont.UPDATE_SMS, new BatchPreparedStatementSetter() {
			
			@Override
			public int getBatchSize() {
				return list.size();
			}
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				SMSData sms = list.get(i);
				
				int index = setPreparedStatement(sms, 1, ps);
				
				ps.setTimestamp(index, sms.getCreateTime());
				ps.setLong(++index, Long.parseLong(sms.getSeqid()));
			}
		});
	}
	
	public void insertMethod(String name, CallBack callBack) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName(name);
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			callBack.execute();
			transactionManager.commit(status);
		}catch(Exception ex) {
			ex.printStackTrace();
			transactionManager.rollback(status);
		}
	}
	
	/**
	 * 保存短信信息
	 * 
	 * @param yyyyMMdd
	 * @param list
	 */
	public void insertSMS(final int yyyyMMdd, final List<SMSData> list) {
		
		insertMethod("insertSMS", new CallBack() {
			
			@Override
			public void execute() {
				getJdbcTemplate().batchUpdate(SmsSqlCont.INSERT_SMS, new BatchPreparedStatementSetter() {
					
					@Override
					public int getBatchSize() {
						return list.size();
					}
					
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						SMSData sms = list.get(i);
						ps.setInt(1, yyyyMMdd);
						ps.setString(2, sms.getCinemaInnerCode());
						
						setPreparedStatement(sms, 3, ps);
					}
				});
			}
		});
	}
	
	/**
	 * 保存短信信息
	 * 
	 * @param yyyyMMdd
	 * @param list
	 */
	public void insertSMSForSeqId(final int yyyyMMdd, final List<SMSData> list) {
		
		this.insertMethod("insertSMSForSeqId", new CallBack() {
			
			@Override
			public void execute() {
				getJdbcTemplate().batchUpdate(SmsSqlCont.INSERT_SMS_SEQID, new BatchPreparedStatementSetter() {
					
					@Override
					public int getBatchSize() {
						return list.size();
					}
					
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						SMSData sms = list.get(i);
						
						ps.setLong(1, Long.parseLong(sms.getSeqid()));
						ps.setInt(2, yyyyMMdd);
						ps.setString(3, sms.getCinemaInnerCode());
						
						setPreparedStatement(sms, 4, ps);
					}
				});
			}
		});
	}
	
	public boolean sendSMS(SendSms sendSms) throws TemplateException, IOException {
		boolean hasError = false;
		// 发送手机号列表
		StringBuilder target = new StringBuilder();
		// 发送短信内容
		String smsContent = "";
		// 待发送消息列表
		Map<String, SMSData> smsList = sendSms.getSendSmsList();
		// 发送目标
		Map<String, List<TargetSmsRelation>> targetSmsRelation = sendSms.getSendTargetList();
		// 待更新状态表
		List<TargetSmsRelation> updateStatusList = new ArrayList<TargetSmsRelation>();
		for(Map.Entry<String, SMSData> entry : smsList.entrySet()) {
			String innerCode = entry.getKey();
			SMSData sms = entry.getValue();
			
			Map<String, Object> rootMap = sms.getRootMap();
			FreeMarkerUtil util = FreeMarkerUtil.getInstance();
			// 待发消息
			smsContent = util.genFileString("template.ftl", rootMap);
			
			List<TargetSmsRelation> list = targetSmsRelation.get(innerCode);
			for(int i=1,len=list.size();i<=len;i++) {
				target.append(list.get(i-1).getMobile()).append(",");
				updateStatusList.add(list.get(i-1));
				if(i % 100 == 0) {	//每一百个号发送一次
					hasError = sendSmsToTarget(target, smsContent, updateStatusList);
				}
			}
			
			if(target.length() > 0) {	//将剩余的短信发送出去
				hasError = sendSmsToTarget(target, smsContent, updateStatusList);
			}
		}
		
		return hasError;
	}
	
	/**
	 * 发送短信状态
	 * 
	 * @param target
	 * @param smsContent
	 * @param updateStatusList
	 * @throws RemoteException
	 */
	private boolean sendSmsToTarget(StringBuilder target, String smsContent, List<TargetSmsRelation> updateStatusList) throws RemoteException {
		boolean hasError = false;
		target.deleteCharAt(target.length() - 1);
		
		System.out.println("发送目标手机号"+target);
		System.out.println("发送内容"+smsContent);
		
		//String status = SetSMSInfo(Contact.FROM_SYS, target.toString(), smsContent);// 调用短信功能
		//String smsUrl = req.getParameter("smsUrl");
		//String fromSys = req.getParameter("FromSys");
		//String phone = req.getParameter("Target");
		//String content = req.getParameter("MSContent");
		//String title = req.getParameter("title");
		//String status = "OK";
		
		String status = VoucherHttpRequestUtil.sendPost(PropertiesUtil.getValueByKey("sendUrl"), "smsUrl="+PropertiesUtil.getValueByKey("smsUrl")+"&FromSys="+PropertiesUtil.getValueByKey("FromSys")+"&Target="+target+"&MSContent="+smsContent+"&title=会员手机报");
		System.out.println("======================"+status+"=======================");
		if("OK".equals(status)) { // 发送成功
			updateSendStatus(updateStatusList, 2);
		} else {
			hasError = true;
			updateSendStatus(updateStatusList, 3);
		}
		
		updateStatusList.clear();
		target.delete(0, target.length());
		
		return hasError;
	}
	
	/**
	 * 调用短信发送接口
	 * 
	 * @param fromSys
	 * @param target
	 * @param smsContent
	 * @return
	 * @throws RemoteException 
	 */
	public String SetSMSInfo(String fromSys, String target, String smsContent) throws RemoteException {
		String result = "";
		com.wanda.ccs2.wcf.ReceiveServerStub stub = new com.wanda.ccs2.wcf.ReceiveServerStub();
		com.wanda.ccs2.wcf.service.SetSMSInfo setSMSInfo58 = new com.wanda.ccs2.wcf.service.SetSMSInfo();
		setSMSInfo58.setFromSys(fromSys);
		setSMSInfo58.setTarget(target);
		setSMSInfo58.setMsContent(smsContent);
		com.wanda.ccs2.wcf.service.SetSMSInfoResponse r = stub.setSMSInfo(setSMSInfo58);
		logger.debug("SMS,Target=" + target + "|content=" + smsContent);
		result = r.getSetSMSInfoResult();

		return result;
	}
	
	/**
	 * 查询影城待发送的短信数据和用户列表
	 * 
	 * @param sql
	 * @param yyyyMMdd
	 * @param param
	 * @param obj
	 */
	public SendSms querySendTarget(String sql, int yyyyMMdd, Object[] param) {
		List<Object> list = new ArrayList<Object>();
		list.add(yyyyMMdd);
		
		StringBuilder statusSql = new StringBuilder();
		if(param != null && param.length > 0) {
			statusSql.append(" and s.status in (");
			for(int i=0,len=param.length;i<len;i++) {
				statusSql.append("?,");
				list.add(param[i]);
			}
			statusSql.deleteCharAt(statusSql.length() - 1).append(")");
		}
		
		sql = sql.replace("{1}", statusSql.toString());
		logger.debug(sql);
		SendSms sendSms = getJdbcTemplate().query(sql, list.toArray(), new ResultSetExtractor<SendSms>() {
			@Override
			public SendSms extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				SendSms sendSms = new SendSms();
				String innerCode;
				while(rs.next()) {
					innerCode = rs.getString("CINEMA_INNER_CODE");
					if(sendSms.isSmsExsit(innerCode) == false) {	//待发送短信不存在
						sendSms.getSendSmsList().put(innerCode, getSMS(rs));
					}
					sendSms.getTargetListByInnerCode(innerCode).add(new TargetSmsRelation(rs.getLong("seqid"), rs.getLong("targetid"), rs.getLong("msgid"), rs.getString("mobile")));
					innerCode = null;
				}
				return sendSms;
			}
		});
		
		return sendSms;
	}
	
	private int setPreparedStatement(SMSData sms, int startIndex, PreparedStatement ps) throws SQLException {
		
		/* 新增会员数 */
		ps.setDouble(startIndex, String2Double(sms.getNewMember()));//new_member
		/* 线下新增会员数 */
		ps.setDouble(++startIndex, String2Double(sms.getNotNetMember()));//not_net_member
		/* 线上新增会员数 */
		ps.setDouble(++startIndex, String2Double(sms.getNetMember()));//net_member
		/* 会员总数 */
		ps.setDouble(++startIndex, String2Double(sms.getNewMemberSum()));//new_member_sum
		/* 线下新增会员总数 */
		ps.setDouble(++startIndex, String2Double(sms.getNotNetMemberSum()));//not_net_member_sum
		/* 线上新增会员总数 */
		ps.setDouble(++startIndex, String2Double(sms.getNetMemberSum()));//net_member_sum
		/* 会员昨日线下票房 */
		ps.setDouble(++startIndex, String2Double(sms.getNotNetTicketSum()));//not_net_ticket_sum
		/* 会员昨日线上票房 */
		ps.setDouble(++startIndex, String2Double(sms.getNetTicketSum()));//net_ticket_sum
		/* 线下会员卖品 */
		ps.setDouble(++startIndex, String2Double(sms.getNotNetGoodsSum()));//not_net_goods_sum
		/* 可兑换积分兑换 */
		ps.setDouble(++startIndex, String2Double(sms.getExchangePoint()));//exchange_point
		/* 可兑换积分新增 */
		ps.setDouble(++startIndex, String2Double(sms.getExchangePointNew()));//exchange_point_new
		/* 可兑换积分余额 */
		ps.setDouble(++startIndex, String2Double(sms.getExchangePointBalance()));//exchange_point_balance
		/* 线下月会员票房 */
		ps.setDouble(++startIndex, String2Double(sms.getNotNetTicketMonthSum()));//not_net_ticket_month_sum
		/* 线上月会员票房 */
		ps.setDouble(++startIndex, String2Double(sms.getNetTicketMonthSum()));//net_ticket_month_sum
		/* 会员本年线下票房 */
		ps.setDouble(++startIndex, String2Double(sms.getNotNetTicketYearSum()));//not_net_ticket_year_sum
		/* 会员本年线上票房 */
		ps.setDouble(++startIndex, String2Double(sms.getNetTicketYearSum()));//net_ticket_year_sum
		
		return ++startIndex;
	}
	
	/**
	 * 获取待发送短信数据
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private SMSData getSMS(ResultSet rs) throws SQLException {
		SMSData sms = new SMSData(
				rs.getString("DATAYMD"),
				rs.getString("CINEMA_INNER_CODE"),
				rs.getString("CINEMA_INNER_NAME"),
				rs.getString("NEW_MEMBER"),
				rs.getString("NOT_NET_MEMBER"),
				rs.getString("NET_MEMBER"),
				rs.getString("NEW_MEMBER_SUM"),
				rs.getString("NOT_NET_MEMBER_SUM"),
				rs.getString("NET_MEMBER_SUM"),
				rs.getString("NOT_NET_TICKET_SUM"),
				rs.getString("NET_TICKET_SUM"),
				rs.getString("NOT_NET_GOODS_SUM"),
				rs.getString("EXCHANGE_POINT"),
				rs.getString("EXCHANGE_POINT_NEW"),
				rs.getString("EXCHANGE_POINT_BALANCE"),
				rs.getString("NOT_NET_TICKET_MONTH_SUM"),
				rs.getString("NET_TICKET_MONTH_SUM"),
				rs.getString("NOT_NET_TICKET_YEAR_SUM"),
				rs.getString("NET_TICKET_YEAR_SUM"));
		
		sms.setSeqid(rs.getString("SEQID"));
		return sms;
	}
	
	private double String2Double(String value) {
		if(value != null && !"".equals(value)) {
			return Double.parseDouble(value);
		}
		
		return 0;
	}
	
	public Map<String, Integer> queryStatus(int yyyyMMdd) {
		final Map<String, Integer> flag = new HashMap<String, Integer>();
		
		getJdbcTemplateStatus().query(SmsSqlCont.QUERY_JOB_STATUS, new Object[] {yyyyMMdd}, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				flag.put("FLAG_SMS", rs.getInt("FLAG_SMS"));
				flag.put("FLAG_CINEMA_SMS", rs.getInt("FLAG_CINEMA_SMS"));
			}
		});
		
		return flag;
	}
	
}
