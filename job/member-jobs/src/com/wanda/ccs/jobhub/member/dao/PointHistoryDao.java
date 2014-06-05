package com.wanda.ccs.jobhub.member.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.vo.MemberPointVo;
import com.wanda.ccs.jobhub.member.vo.PointHistoryVo;
import com.wanda.ccs.jobhub.member.vo.PointOperationSys;
import com.wanda.ccs.jobhub.member.vo.PointOperationType;

/**
 * 
 * 会员积分历史表操作
 * 
 * @author Charie Zhang
 * @since 2013-12-22
 */
public class PointHistoryDao {
	
	private JdbcTemplate jdbcTemplate = null;
	
	private SimpleJdbcInsert jdbcInsert = null;
	
	private final static String INSERT_SQL = 		
			"INSERT INTO T_POINT_HISTORY (" +
			"POINT_HISTORY_ID, " +
			"MEMBER_ID, " +
			"SET_TIME, " +
			"LEVEL_POINT, " +
			"TICKET_COUNT, " +
			"ACTIVITY_POINT, " +
			"EXCHANGE_POINT, " +
			"EXCHANGE_POINT_EXPIRE_TIME, " +
			"POINT_TYPE, " +
			"POINT_SYS, " +
			"POINT_TRANS_TYPE, " +
			"POINT_TRANS_CODE, " +
			"POINT_TRANS_CODE_WEB, " +
			"ADJ_RESION, " +
			"ORG_POINT_BALANCE, " +
			"POINT_BALANCE, " +
			"IS_SYNC_BALANCE, " +
			"ISDELETE, " +
			"CREATE_BY, " +
			"CREATE_DATE, " +
			"UPDATE_BY, " +
			"UPDATE_DATE, " +
			"VERSION, " +
			"MEMBER_POINT_ID, " +
			"ADJ_REASON_TYPE, " +
			"ADJ_REASON, " +
			"ORDER_ID, " +
			"PRODUCT_NAME, " +
			"IS_SUCCEED, " +
			"CINEMA_INNER_CODE, " +
			"POINT_EXT_RULE_ID, " +
			"TRANS_TYPE, " +
			"T_TRANS_ORDER_ID, " +
			"T_TRANS_DETAIL_ID, " +
			"RECALCU_STATUS, " +
			"RECALCU_DATE, " +
			"IS_HISTORY" +
			") VALUES(" +
			"S_T_POINT_HISTORY.NEXTVAL, " +
			"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	@InstanceIn(path = "/dataSource")
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.jdbcInsert = new SimpleJdbcInsert(dataSource)
				.withTableName("T_POINT_HISTORY");
	}
	
	/**
	 * Create the history point with default properties value.
	 * @return
	 */
	public PointHistoryVo newVo() {
		PointHistoryVo vo = new PointHistoryVo();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		vo.setPointSys(PointOperationSys.MBR.getCode());
		vo.setIsSyncBalance(1L);
		vo.setIsdelete(false);
		vo.setIsSucceed(true);
		vo.setSetTime(now);
		vo.setRecalcuDate(now);
		vo.setRecalcuStatus("0");
		vo.setCreateDate(now);
		vo.setUpdateDate(now);
		vo.setVersion(1L);
		
		return vo;
	}

	public void insert(PointHistoryVo pointHistoryVo) {
		pointHistoryVo.setPointHistoryId(jdbcTemplate.queryForLong("select S_T_POINT_HISTORY.NEXTVAL from DUAL"));
		SqlParameterSource parameters = new BeanPropertySqlParameterSource(pointHistoryVo);
		jdbcInsert.execute(parameters);
	}
	
	public void insertBatch(List<PointHistoryVo> histories) {
		
		List<Object[]> batch = new ArrayList<Object[]>();
		for (PointHistoryVo history : histories) {
			Object[] values = new Object[] {
				history.getMemberId() ,//"MEMBER_ID, " +
				history.getSetTime() ,//"SET_TIME, " +
				history.getLevelPoint() ,//"LEVEL_POINT, " +
				history.getTicketCount() ,//"TICKET_COUNT, " +
				history.getActivityPoint() ,//"ACTIVITY_POINT, " +
				history.getExchangePoint() ,//"EXCHANGE_POINT, " +
				history.getExchangePointExpireTime() ,//"EXCHANGE_POINT_EXPIRE_TIME, " +
				history.getPointType() ,//"POINT_TYPE, " +
				history.getPointSys() ,//"POINT_SYS, " +
				history.getPointTransType() ,//"POINT_TRANS_TYPE, " +
				history.getPointTransCode() ,//"POINT_TRANS_CODE, " +
				history.getPointTransCodeWeb() ,//"POINT_TRANS_CODE_WEB, " +
				history.getAdjResion() ,//"ADJ_RESION, " +
				history.getOrgPointBalance() ,//"ORG_POINT_BALANCE, " +
				history.getPointBalance() ,//"POINT_BALANCE, " +
				history.getIsSyncBalance() ,//"IS_SYNC_BALANCE, " +
				history.getIsdelete() ,//"ISDELETE, " +
				history.getCreateBy() ,//"CREATE_BY, " +
				history.getCreateDate() ,//"CREATE_DATE, " +
				history.getUpdateBy() ,//"UPDATE_BY, " +
				history.getUpdateDate() ,//"UPDATE_DATE, " +
				history.getVersion() ,//"VERSION, " +
				history.getMemberPointId() ,//"MEMBER_POINT_ID, " +
				history.getAdjReasonType() ,//"ADJ_REASON_TYPE, " +
				history.getAdjReason() ,//"ADJ_REASON, " +
				history.getOrderId() ,//"ORDER_ID, " +
				history.getProductName() ,//"PRODUCT_NAME, " +
				history.getIsSucceed() ,//"IS_SUCCEED, " +
				history.getCinemaInnerCode() ,//"CINEMA_INNER_CODE, " +
				history.getPointExtRuleId() ,//"POINT_EXT_RULE_ID, " +
				history.getTransType() ,//"TRANS_TYPE, " +
				history.getTTransOrderId(),//"T_TRANS_ORDER_ID, " +
				history.getTTransDetailId(),//"T_TRANS_DETAIL_ID, " +
				history.getRecalcuStatus() ,//"RECALCU_STATUS, " +
				history.getRecalcuDate() ,//"RECALCU_DATE, " +
				history.getIsHistory() ,//"IS_HISTORY" +
			};
			
			batch.add(values);
		}
		
		jdbcTemplate.batchUpdate(INSERT_SQL, batch);

	}

	
	/*
	public void hasSucceedTransOrder(Long memberId, String orderId, String cinemaInnerCode) {
		String sql = "select count(POINT_HISTORY_ID) from T_POINT_HISTORY " +
				"where ORDER_ID=? and CINEMA_INNER_CODE=? and IS_SUCCEED=? and MEMBER_ID=?";
		Object[] args = {orderId, cinemaInnerCode, Boolean.TRUE, memberId};
		jdbcTemplate.queryForInt(sql, args);
	}
	
	public PointHistoryVo getBy(Long memberId) {
        String sql = "select * from T_POINT_HISTORY where MEMBER_ID=?";
        Object[] args = {memberId};
        return jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper<PointHistoryVo>(PointHistoryVo.class));
	}
	
	public PointHistoryVo get(Long pointHistoryId) {
        String sql = "select * from T_POINT_HISTORY where POINT_HISTORY_ID=?";
        Object[] args = {pointHistoryId};
        return jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper<PointHistoryVo>(PointHistoryVo.class));
	}
	*/
	
	
}
