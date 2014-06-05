package com.wanda.member.basic.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MbrLevelRowMapper implements RowMapper<MbrLevel> {

	@Override
	public MbrLevel mapRow(ResultSet rs, int rownum)
			throws SQLException {
		MbrLevel level = new MbrLevel();
		level.setMemberId(rs.getString("MEMBER_ID"));
		level.setLevelPointTotal(rs.getInt("LEVEL_POINT_TOTAL"));
		level.setTicketCount(rs.getInt("TICKET_COUNT"));
		level.setMemLevel(rs.getInt("MEM_LEVEL"));
		return level;
	}

}
