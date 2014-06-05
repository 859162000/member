package com.wanda.ccs.jobhub.member.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.google.code.pathlet.quartz.DateUtils;
import com.wanda.ccs.jobhub.member.SystemException;
import com.wanda.ccs.jobhub.member.dao.MemberPointDao;
import com.wanda.ccs.jobhub.member.dao.PointHistoryDao;
import com.wanda.ccs.jobhub.member.service.PointOperationService;
import com.wanda.ccs.jobhub.member.utils.BuddyReader;
import com.wanda.ccs.jobhub.member.utils.BuddyXmlReader;
import com.wanda.ccs.jobhub.member.vo.MemberPointVo;
import com.wanda.ccs.jobhub.member.vo.PointAdjustType;
import com.wanda.ccs.jobhub.member.vo.PointHistoryVo;
import com.wanda.ccs.jobhub.member.vo.PointOperationSys;
import com.wanda.ccs.jobhub.member.vo.PointOperationType;



public class PointOperationServiceImpl implements PointOperationService {
	
	private static Log log = LogFactory.getLog(PointOperationServiceImpl.class);
	
	public final static int DEFAULT_QUERY_TIMEOUT = 60 * 60; //计算客群数量的超时时间单位秒
	
	public final static int BATCH_SIZE = 1000;
	
	private Timestamp nextExpireTime = new Timestamp(DateUtils.parseMediumDateTime("2014-12-31 23:59:59").getTime());
	
	private Timestamp thisExpireTime = new Timestamp(DateUtils.parseMediumDateTime("2013-12-31 23:59:59").getTime());
	
	@InstanceIn(path = "MemberPointDao")
	private MemberPointDao pointDao;
	
	@InstanceIn(path = "PointHistoryDao")
	private PointHistoryDao historyDao;
	
	@InstanceIn(path = "/transactionManager")
	private DataSourceTransactionManager transactionManager;
	
	private ExtJdbcTemplate extJdbcTemplate = null;
	
	private BuddyReader sqlReader;
	
	@InstanceIn(path = "/dataSource")
	public void setDataSource(DataSource dataSource) {
		this.extJdbcTemplate = new ExtJdbcTemplate(dataSource);
		this.sqlReader = new BuddyXmlReader(this.getClass(), "PointOperationSql.xml");
	}
	
	public void expire2013() {
		
		final List<PointHistoryVo> pointHistorys = new ArrayList<PointHistoryVo>(BATCH_SIZE);
		final List<MemberPointVo> memberPoints = new ArrayList<MemberPointVo>(BATCH_SIZE);
		
		extJdbcTemplate.setQueryTimeout(DEFAULT_QUERY_TIMEOUT);
		
		extJdbcTemplate.query(sqlReader.get("queryMemberHistory"), new ResultSetExtractor<Object>() {
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				try {
					long proceededMemberCount = 0;
					while(rs.next()) {
						long memberId = rs.getLong(1);     //PK of 	T_MEMBER
						String mobile =  rs.getString(2);  //mobile number of member
						long memberPointId = rs.getLong(3);
						long historyId = rs.getLong(4);    //PK of T_POINT_HISTORY
						long pointBalance = rs.getLong(5); //目前积分余额
						long toExpirePoint = rs.getLong(6); //今年底将过期积分
						long usedPoint = rs.getLong(7); //有效的兑换使用积分，应为负数

						
						addPointChanges(memberId, mobile, memberPointId, historyId, pointBalance, toExpirePoint, usedPoint, pointHistorys, memberPoints);
						if(memberPoints.size() >= BATCH_SIZE || pointHistorys.size() >= BATCH_SIZE) {
							commitPointChanges(pointHistorys, memberPoints);
							proceededMemberCount += memberPoints.size();
							log.info("Proceeded member count=" + proceededMemberCount);
							pointHistorys.clear();
							memberPoints.clear();
						}
						
					}
					
					if(memberPoints.size() > 0 || pointHistorys.size() > 0) {
						commitPointChanges(pointHistorys, memberPoints);
						proceededMemberCount += memberPoints.size();
						log.info("Proceeded member count=" + proceededMemberCount);
						pointHistorys.clear();
						memberPoints.clear();
					}
					
				} catch (Exception e) {
					//logHelper.error(e.getMessage(), e);
					throw new SQLException(e);
				}
				return null;
			}
		});
	}
	
	private void addPointChanges(long memberId, String mobile, long memberPointId, long historyId,
			long pointBalance,long toExpirePoint, long usedPoint, 
			List<PointHistoryVo> pointHistorys, List<MemberPointVo> memberPoints) {

//	 	System.out.println("~~~~~~~~ [memberId=" + memberId + 
//				", mobile=" + mobile + 
//				", historyId=" + historyId + 
//				", pointBalance=" + pointBalance + 
//				", toExpirePoint=" + toExpirePoint + 
//				", usedPoint=" + usedPoint + "]"
//				);
	 	
	 	long remainExpirePoint = toExpirePoint + usedPoint; //将扣减的积分数量（正数）
	 	
	 	if(remainExpirePoint > 0) {  //有需要扣减的积分
 			PointHistoryVo deduceHis = historyDao.newVo();
 			
 			deduceHis.setMemberId(memberId);
 			deduceHis.setMemberPointId(memberPointId);
 			deduceHis.setExchangePoint( - remainExpirePoint); //扣减积分
 			deduceHis.setExchangePointExpireTime(thisExpireTime);
 			deduceHis.setPointType(PointOperationType.EXPIRE.getCode());
 			deduceHis.setOrgPointBalance(pointBalance);
 			deduceHis.setPointBalance(pointBalance - remainExpirePoint);
 			//deduceHis.setAdjResion("负积分回零");
 			//deduceHis.setAdjReason(deduceHis.getAdjResion());
 			//deduceHis.setAdjReasonType(PointAdjustType.OFFSET_ZERO.getCode());
 			//private Long levelPoint = 0L;
 			//private Integer ticketCount = 0;
 			//private Long activityPoint = 0L;
 			//private String pointTransType;
 			//private String pointTransCode;
 			//private String pointTransCodeWeb;
 			//private String orderId;	
 			//private String productName;
 			//private String cinemaInnerCode;
 			pointHistorys.add(deduceHis);
	 		
	 		if((pointBalance - remainExpirePoint) < 0) {
	 			
	 			log.error("Error exchange point:[memberId=" + memberId + 
	 					", memberPointId=" + memberPointId + 
	 					", mobile=" + mobile + 
	 					", historyId=" + historyId + 
	 					", pointBalance=" + pointBalance + 
	 					", toExpirePoint=" + toExpirePoint + 
	 					", usedPoint=" + usedPoint + "]");
	 			//剩余的积分不够扣减，可能是应为之前的积分不正确导致。
	 			//处理方式直接积分调整到0
	 			long adjExgPoint = - (pointBalance - remainExpirePoint);
	 			
	 			PointHistoryVo zeroAdjustHis = historyDao.newVo();
	 			
	 			zeroAdjustHis.setMemberId(memberId);
	 			zeroAdjustHis.setMemberPointId(memberPointId);
	 			zeroAdjustHis.setExchangePoint(adjExgPoint);
	 			zeroAdjustHis.setExchangePointExpireTime(nextExpireTime);
	 			zeroAdjustHis.setPointType(PointOperationType.ADJUST.getCode());
	 			zeroAdjustHis.setAdjResion("负积分回零");
	 			zeroAdjustHis.setAdjReason(zeroAdjustHis.getAdjResion());
	 			zeroAdjustHis.setOrgPointBalance(pointBalance);
	 			zeroAdjustHis.setPointBalance(0L);
	 			zeroAdjustHis.setAdjReasonType(PointAdjustType.OFFSET_ZERO.getCode());
	 			zeroAdjustHis.setCreateDate(deduceHis.getCreateDate()); //保证时间一致
	 			zeroAdjustHis.setUpdateDate(deduceHis.getUpdateDate()); //保证时间一致
	 			//private Long levelPoint = 0L;
	 			//private Integer ticketCount = 0;
	 			//private Long activityPoint = 0L;
	 			//private String pointTransType;
	 			//private String pointTransCode;
	 			//private String pointTransCodeWeb;
	 			//private String orderId;	
	 			//private String productName;
	 			//private String cinemaInnerCode;
	 			
	 			pointHistorys.add(zeroAdjustHis);
	 			
	 			//积分时间设置为零
	 			MemberPointVo pointVo = new MemberPointVo();
	 			pointVo.setMemberPointId(memberPointId);
	 			pointVo.setExgPointBalance(0L);
	 			pointVo.setUpdateDate(deduceHis.getCreateDate());
	 			memberPoints.add(pointVo);
	 		}
	 		else {
	 			//正常扣减记录
	 			MemberPointVo pointVo = new MemberPointVo();
	 			pointVo.setMemberPointId(memberPointId);
	 			pointVo.setExgPointBalance(pointBalance - remainExpirePoint);
	 			pointVo.setUpdateDate(deduceHis.getCreateDate());
	 			memberPoints.add(pointVo);
	 		}
	 		
	 	}
		
	}

	/**
	 * Batch insert and update T_POINT_HISTORY and T_MEMBER_POINT
	 */
	private void commitPointChanges(List<PointHistoryVo> pointHistorys, List<MemberPointVo> memberPoints) {
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName("memberPointExpireOperations");
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = transactionManager.getTransaction(def);
		boolean hasError = false;
		try {
			historyDao.insertBatch(pointHistorys);
			pointDao.updateBatchExgPoint(memberPoints);
		}
		catch(Exception e) {
			hasError = true;
			transactionManager.rollback(status);
			throw new SystemException(e);
		}
		finally {
			if(hasError == false) {
				transactionManager.commit(status);
			}
		}
		

	}
	

}
