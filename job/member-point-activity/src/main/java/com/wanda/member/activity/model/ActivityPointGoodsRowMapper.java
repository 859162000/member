package com.wanda.member.activity.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ActivityPointGoodsRowMapper implements RowMapper<ActivityPointGoods> {

	@Override
	public ActivityPointGoods mapRow(ResultSet rs, int rownum)
			throws SQLException {
		ActivityPointGoods goods = new ActivityPointGoods();
		goods.setInnerCode(rs.getString("INNER_CODE"));
		goods.setMemberKey(rs.getString("MEMBER_KEY"));
		goods.setSaleAmount(rs.getBigDecimal("SALE_AMOUNT"));
		goods.setCsOrderCode(rs.getString("CS_ORDER_CODE"));
		goods.setItemCode(rs.getString("ITEM_CODE"));
		return goods;
	}

}
