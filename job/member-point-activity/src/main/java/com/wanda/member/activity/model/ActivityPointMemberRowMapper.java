package com.wanda.member.activity.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ActivityPointMemberRowMapper implements RowMapper<ActivityPointMember> {

	@Override
	public ActivityPointMember mapRow(ResultSet rs, int rownum)
			throws SQLException {
		ActivityPointMember member = new ActivityPointMember();
		member.setMemberKey(rs.getString("MEMBER_KEY"));
		return member;
	}

}
