package com.wanda.member.basic.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class BasicPointTransRowMapper implements RowMapper<BasicPointTrans> {

	@Override
	public BasicPointTrans mapRow(ResultSet rs, int rownum)
			throws SQLException {
		BasicPointTrans trans = new BasicPointTrans();
		trans.setInnerCode(rs.getString("INNER_CODE"));
		trans.setMemberId(rs.getString("MEMBER_KEY"));
		trans.setAmount(rs.getBigDecimal("AMOUNT"));
		trans.setOrderCode(rs.getString("ORDER_CODE"));
		trans.setItemCode(rs.getString("ITEM_CODE"));
		trans.setBizDate(rs.getString("BIZ_DATE"));
		return trans;
	}

}
