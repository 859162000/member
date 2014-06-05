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
import org.springframework.transaction.annotation.Transactional;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.vo.MemberPointVo;
import com.wanda.ccs.jobhub.member.vo.PointHistoryVo;
import com.wanda.ccs.jobhub.member.vo.PointOperationType;


/**
 * 
 * 会员积分表操作
 * 
 * @author Charie Zhang
 * @since 2013-12-22
 */
public class MemberPointDao {
	
	private JdbcTemplate jdbcTemplate = null;
	
	private SimpleJdbcInsert jdbcInsert = null;
	
	@InstanceIn(path = "/dataSource")
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.jdbcInsert = new SimpleJdbcInsert(dataSource)
				.withTableName("T_MEMBER_POINT");
	}

	public void insert(MemberPointVo mpv) {
		mpv.setMemberPointId(jdbcTemplate.queryForLong("select S_T_MEMBER_POINT.NEXTVAL from DUAL"));
		SqlParameterSource parameters = new BeanPropertySqlParameterSource(mpv);
		jdbcInsert.execute(parameters);
	}
	
	
	/**
	 * 更新可兑换积分
	 * @param memberPointId
	 * @param exgPointBalance
	 * @param exgExpirePointBalance
	 * @param updateDate
	 */
	public void updateBatchExgPoint(List<MemberPointVo> points) {
		List<Object[]> batch = new ArrayList<Object[]>();
		for (MemberPointVo point : points) {
			Object[] values = new Object[] {
					point.getExgPointBalance(), 
					point.getUpdateDate(), 
					point.getMemberPointId()
			};
			
			batch.add(values);
		}
		
		jdbcTemplate.batchUpdate("update T_MEMBER_POINT set EXG_POINT_BALANCE=?, UPDATE_DATE=? where MEMBER_POINT_ID=?", 
				batch);
	}
	
	
	public MemberPointVo get(Long memberPointId) {
        String sql = "select * from T_MEMBER_POINT where MEMBER_POINT_ID=?";
        Object[] args = { memberPointId };
        return jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper<MemberPointVo>(MemberPointVo.class));
	}
	
	public MemberPointVo getBy(Long memberId) {
        String sql = "select * from T_MEMBER_POINT where MEMBER_ID=?";
        Object[] args = { memberId };
        return jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper<MemberPointVo>(MemberPointVo.class));
	}
	
	
	
	
}
