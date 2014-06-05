package com.wanda.member.activity.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ActivityPointRuleRowMapper implements RowMapper<ActivityPointRule> {

	@Override
	public ActivityPointRule mapRow(ResultSet rs, int rownum)
			throws SQLException {
		ActivityPointRule rule = new ActivityPointRule();
		rule.setAdditionCode(rs.getInt("ADDITION_CODE"));
		rule.setAdditionPercent(rs.getInt("ADDITION_PERCENT"));
		rule.setEndDtime(rs.getDate("END_DTIME"));
		rule.setExtPointCriteriaId(rs.getInt("EXT_POINT_CRITERIA_ID"));
		rule.setExtPointRuleId(rs.getInt("EXT_POINT_RULE_ID"));
		rule.setRuleCode(rs.getString("RULE_CODE"));
		rule.setRuleName(rs.getString("RULE_NAME"));
		rule.setStartDtime(rs.getDate("START_DTIME"));
		rule.setCriteriaCode(rs.getString("CRITERIA_CODE"));
		rule.setCriteriaName(rs.getString("CRITERIA_NAME"));
		rule.setCriteriaScheme(rs.getString("CRITERIA_SCHEME"));
		return rule;
	}

}
